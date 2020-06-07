package sidev.lib.android.siframe.intfc.lifecycle.sidebase

import android.util.Log
import android.view.View
import androidx.annotation.CallSuper
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import sidev.lib.android.siframe.adapter.ViewPagerFragAdp
import sidev.lib.android.siframe.customizable._init._ConfigBase
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.base.LifecycleSideBase
import sidev.lib.android.siframe.intfc.listener.OnPageFragActiveListener
import sidev.lib.android.siframe.lifecycle.fragment.SimpleAbsFrag

interface ViewPagerActBase<F: SimpleAbsFrag>: LifecycleSideBase {
    override val layoutId: Int
        get() = _ConfigBase.LAYOUT_VP

    var onPageFragActiveListener: HashMap<Int, OnPageFragActiveListener>

    val vp: ViewPager
        get()= _sideBase_view.findViewById(_ConfigBase.ID_VP)
    var vpFragList: Array<F>
//    val viewPagerActViewView: View
    /**
     * Untuk menandai titik henti fragment page
     * Contoh:
     *      Ada 6 fragment dg alur page yang dapat dibuka yaitu:
     *          0-2, 3, 4-5
     *          Penjelasan: Artinya dari page 1 bisa diteruskan hingga page 3, tidak bisa diteruskan ke page 4.
     *              Begitu juga page 4 gak bisa ke mana-mana
     * Mekanisme kerja di kelas ini:
     * @see vpFragListStartMark Array Mark -> 0, 3, 4
     * @see vpFragListMark jika dijabarkan -> * o o * * o
     *
     * @see vpFragListMark seharusnya private agar vp tidak kacau
     */
    var vpFragListMark: Array<Int>
    var vpFragListStartMark: Array<Int>
    var vpAdp: ViewPagerFragAdp
//    val vpFm: FragmentManager
//    val vpCtx: Context
    var pageStartInd: Int
    var pageEndInd: Int

    companion object{
        val PAGE_MARK_START= 0
        val PAGE_MARK_CONT= 1
    }


    override fun _initInheritorBase() {
        initVp()
    }

    /**
     * Sebaiknya ngedit @see pageStartInd dan @see pagEndInd dari sini
     * biar integritas @see vp terjamin
     */
    fun setPageLimitInd(startInd: Int= pageStartInd, endInd: Int= pageEndInd){
        var startInd= startInd
        var endInd= endInd

        if(startInd < 0)
            startInd= 0
        else if(startInd >= vp.adapter?.count ?: 0)
            startInd= (vp.adapter?.count ?: 0) -1

        if(endInd < 0)
            endInd= 0
        else if(endInd >= vp.childCount)
            endInd= (vp.adapter?.count ?: 0) -1

        if(startInd > endInd)
            endInd= startInd

        pageStartInd= startInd
        pageEndInd= endInd

        if(vp != null)
            if(vp.currentItem !in pageStartInd .. pageEndInd)
                vp.currentItem= pageStartInd
    }

    @CallSuper
    fun initVp(){
        val fragList= initFragList()
        setFragList(fragList)
        setFragListMark(initFragListMark())

//        vp= _sideBase_view.findViewById(_ConfigBase.ID_VP)

        vpAdp= ViewPagerFragAdp(_sideBase_fm, *vpFragList)
        vp.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {}

            override fun onPageSelected(position: Int) {
                onPageFragActiveListener[position]?.onPageFragActive(_sideBase_view, position) //
            }
        })
        Log.e("ADP_VP", "initVp() vpFragList.size= ${vpFragList.size}")
        vp.adapter= vpAdp
    }
    fun initFragList(): Array<F>?{
        return null
    }
    fun initFragListMark(): Array<Int>{
        return vpFragListStartMark ?:
            Array(vpFragList.size){
                if(it == 0) PAGE_MARK_START
                else PAGE_MARK_CONT
            }
    }

    /**
     * Sebaiknya nge-set @see vpFragListMark_int lewat method ini aja
     */
    fun setFragListMark(mark: Array<Int>){
        val markListInt= Array(vpFragList.size){ PAGE_MARK_CONT }
        markListInt[0]=
            PAGE_MARK_START
        val limit= if(mark.size <= vpFragList.size) mark.size
            else vpFragList.size
        for(i in 0 until limit)
            markListInt[mark[i]]=
                PAGE_MARK_START
        vpFragListMark= markListInt
/*
        val markList= ArrayList<Int>()
        for(i in vpFragList.indices)
            markList.add(
                try{
                    mark[i]
                }catch(e: Exception){
                    PAGE_MARK_CONT
                }
            )
        vpFragListMark_int= markList.toTypedArray()
 */
    }

    /**
     * Sebaiknya nge-set @see vpFragList lewat method ini aja
     * biar gak kacau terutama @see pageStartInd sama @see pageEndInd
     */
    fun setFragList(list: Array<F>?){
        var size= 0
        if(list != null){
            vpFragList= list
            size= vpFragList.size
            vpAdp= ViewPagerFragAdp(_sideBase_fm, *list)
        } else
            vp.adapter= null
        setPageLimitInd(0, size)
    }

    fun registerOnPageFragToActListener(frag: F, l: OnPageFragActiveListener){
        onPageFragActiveListener[vpAdp.items.indexOf(frag)]= l
    }
    fun registerOnPageFragToActListener(frag: F, func: (vParent: View, pos: Int) -> Unit){
        registerOnPageFragToActListener(frag, object :
            OnPageFragActiveListener {
            override fun onPageFragActive(vParent: View, pos: Int) {
                func(vParent, pos)
            }
        })
    }

    fun setupVPWithTab(tab: TabLayout){
        tab.setupWithViewPager(vp)
        for(i in 0 until vp.childCount)
            tab.getTabAt(i)?.text= vpFragList[i].fragTitle
    }

    /**
     * Halaman VP selanjutnya
     * @return true jika halaman VP belum terahir (berhasil pindah)
     */
    fun pageForth(): Boolean{
        return if(vp.currentItem < (vp.adapter?.count ?: 0) -1
            && (vp.currentItem <= pageEndInd || pageEndInd < 0)){
            val bool= if(vpFragListMark[vp.currentItem +1] == PAGE_MARK_CONT){
                vp.currentItem= vp.currentItem +1
                true
            } else false
            Log.e("pageForth", "vp.currentItem = ${vp.currentItem} vpFragListMark_int[vp.currentItem +1] = ${vpFragListMark[vp.currentItem]} bool = $bool")
            bool
        } else{
            Log.e("pageForth", "Gak masuk vp.currentItem = ${vp.currentItem} pageEndInd = $pageEndInd vp.childCount =${vp.childCount} bool = false")
            false
        }
    }
    fun pageBackward(): Boolean{
        return if(vp.currentItem > 0
            && vp.currentItem >= pageStartInd){
            if(vpFragListMark[vp.currentItem] == PAGE_MARK_START
                && vpFragListMark[vp.currentItem -1] == PAGE_MARK_CONT
            ){
                false
            } else{
                vp.currentItem= vp.currentItem -1
                true
            }
        } else
            false
    }
}