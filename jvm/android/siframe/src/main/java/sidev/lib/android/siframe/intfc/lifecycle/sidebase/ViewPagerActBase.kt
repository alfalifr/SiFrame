package sidev.lib.android.siframe.intfc.lifecycle.sidebase

import android.util.SparseArray
import android.view.View
import androidx.annotation.CallSuper
import androidx.core.view.get
import androidx.core.view.isNotEmpty
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import sidev.lib.android.siframe.adapter.VpFragAdp
import sidev.lib.android.siframe.customizable._init._Config
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.base.ComplexLifecycleSideBase
import sidev.lib.android.siframe.intfc.listener.OnPageFragActiveListener
import sidev.lib.android.siframe.lifecycle.activity.SimpleAbsBarContentNavAct
import sidev.lib.android.siframe.lifecycle.fragment.SimpleAbsFrag
import sidev.lib.android.siframe.tool.util.`fun`.getPosFrom
import sidev.lib.android.siframe.tool.util.`fun`.loge
import sidev.lib.universal.`fun`.asNotNull
import sidev.lib.universal.`fun`.classSimpleName
import java.lang.Exception

interface ViewPagerActBase<F: SimpleAbsFrag>: ComplexLifecycleSideBase {
    override val layoutId: Int
        get() = _Config.LAYOUT_VP

    var onPageFragActiveListener: SparseArray<OnPageFragActiveListener>

    val vp: ViewPager
        get()= _sideBase_view.findViewById(_Config.ID_VP)
//    var lateVp: ViewPager
/*
        get(){
            val parId= _sideBase_view.id
            val callerFun= ThreadUtil.getCurrentCallerFunName(-1)
            Log.e("ViewPagerActBase", "parId= $parId callerFun= $callerFun")
            return _sideBase_view.findViewById(_ConfigBase.ID_VP)
        }
 */
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
    var vpAdp: VpFragAdp
//    val vpFm: FragmentManager
//    val vpCtx: Context
    var pageStartInd: Int
    var pageEndInd: Int

    var isVpTitleFragBased: Boolean
    var isVpBackOnBackPressed: Boolean

    companion object{
        val PAGE_MARK_START= 0
        val PAGE_MARK_CONT= 1
    }

    var vpOnPageListenerToNavBar: ViewPager.OnPageChangeListener?


    override fun ___initSideBase() {
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
//        lateVp= vp
//        loge("initVp() lateVp= vp")
//        setFragListMark(initFragListMark())

//        vp= _sideBase_view.findViewById(_ConfigBase.ID_VP)

//        vpAdp= ViewPagerFragAdp(_sideBase_fm, *vpFragList)
/*
        loge("initVp() ")
        loge("initVp() isVpTitleFragBased= $isVpTitleFragBased this is SimpleAbsBarContentNavAct= ${this is SimpleAbsBarContentNavAct}")
        loge("initVp() vp != null = ${vp != null}")
 */

        setFragList(vpFragList)
        vp.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {}
            override fun onPageSelected(position: Int) {
                if(this@ViewPagerActBase !is MultipleActBarViewPagerActBase<*>){
                    //Karena fungsi untuk isVpTitleFragBased udah didefinisikan sendiri
                    // di kelas MultipleActBarViewPagerActBase
                    this@ViewPagerActBase.asNotNull { act: SimpleAbsBarContentNavAct ->
                        if(isVpTitleFragBased){
                            try{ act.setActBarTitle(vpFragList[position].fragTitle) }
                            catch (e: Exception){
                                /* Ini ditujukan agar saat terjadi kesalahan saat setActBarTitle()
                                tidak menyebabkan error.
                                 */
                            }
                        }
                    }
                }
                vpFragList[position].onActive(_sideBase_view, this@ViewPagerActBase, position)
                onPageFragActiveListener[position]?.onPageFragActive(_sideBase_view, position) //
            }
        })
        if(isVpTitleFragBased && vpFragList.isNotEmpty())
            this.asNotNull { act: SimpleAbsBarContentNavAct ->
                try{ act.setActBarTitle(vpFragList.first().fragTitle) }
                catch (e: Exception){
                    /* Ini ditujukan agar saat terjadi kesalahan saat setActBarTitle()
                    tidak menyebabkan error.
                     */
                }
            }
//        val fragList= initFragList()

//        Log.e("ADP_VP", "initVp() vpFragList.size= ${vpFragList.size}")
//        vp.adapter= vpAdp
    }
    fun initFragList(): Array<F>?{
        return vpFragList
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
        loge("setFragListMark() vpFragList.size= ${vpFragList.size} markListInt.size= ${markListInt.size}")
        if(markListInt.isNotEmpty()){
            markListInt[0]= PAGE_MARK_START
            val limit= if(mark.size <= vpFragList.size) mark.size
            else vpFragList.size
            for(i in 0 until limit){
                val markStart= mark[i]
                if(markStart < markListInt.size)
                    markListInt[markStart]= PAGE_MARK_START
            }
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
            vpAdp= VpFragAdp(_sideBase_fm, *list)
//            vp.removeAllViews()
            vp.adapter= vpAdp

            if(isVpTitleFragBased && vpFragList.isNotEmpty())
                this.asNotNull { act: SimpleAbsBarContentNavAct ->
                    try{ act.setActBarTitle(vpFragList.first().fragTitle) }
                    catch (e: Exception){}
                }
            this.asNotNull { act: MultipleActBarViewPagerActBase<*> ->
                act.actBarViewList.clear()
                act.attachActBarView(0)
            }
        } else{
            vp.adapter= null
        }

        setPageLimitInd(0, size)
        setFragListMark(initFragListMark())

        if(list != null)
            vpFragList.firstOrNull()?.onActive(_sideBase_view, this, 0)
    }

    fun getFragPos(frag: F): Int{
        return vpAdp.items.indexOf(frag)
    }

    /**
     * @return true jika berhasil dan sebaliknya.
     */
    fun registerOnPageFragToActListener(frag: F, l: OnPageFragActiveListener): Boolean{
        val pos= vpAdp.items.indexOf(frag)
        return if(pos >= 0){
            onPageFragActiveListener.setValueAt(pos, l) //= l
            true
        } else false
    }
    fun registerOnPageFragToActListener(frag: F, func: (vParent: View, pos: Int) -> Unit): Boolean{
        return registerOnPageFragToActListener(frag, object :
            OnPageFragActiveListener {
            override fun onPageFragActive(vParent: View, pos: Int) {
                func(vParent, pos)
            }
        })
    }

    fun setupVpWithTab(tab: TabLayout){
        tab.setupWithViewPager(vp)
        for(i in 0 until vp.childCount)
            tab.getTabAt(i)?.text= vpFragList[i].fragTitle
    }

    fun setupVpWithNavBar(bnv: BottomNavigationView){
        val menu= bnv.menu
        var isVpSliding= false
        bnv.setOnNavigationItemSelectedListener { menuItem ->
            val pos= menuItem.getPosFrom(menu)
            if(!isVpSliding){
                isVpSliding= true
                vp.currentItem= pos
                isVpSliding= false
            }
            true
        }
        if(vpOnPageListenerToNavBar != null)
            vp.removeOnPageChangeListener(vpOnPageListenerToNavBar!!)
        vpOnPageListenerToNavBar= object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(pos: Int, posOffset: Float, posOffsetPx: Int) {}
            override fun onPageSelected(position: Int) {
                if(!isVpSliding && menu.isNotEmpty()){
                    isVpSliding= true
                    bnv.selectedItemId= menu[position].itemId
                    isVpSliding= false
                }
            }
        }
        vp.addOnPageChangeListener(vpOnPageListenerToNavBar!!)
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
//            Log.e("pageForth", "vp.currentItem = ${vp.currentItem} vpFragListMark_int[vp.currentItem +1] = ${vpFragListMark[vp.currentItem]} bool = $bool")
            bool
        } else{
//            Log.e("pageForth", "Gak masuk vp.currentItem = ${vp.currentItem} pageEndInd = $pageEndInd vp.childCount =${vp.childCount} bool = false")
            false
        }
    }
    fun pageBackward(): Boolean{
        return if(vp.currentItem > 0
            && vp.currentItem >= pageStartInd){
            if(vpFragListMark[vp.currentItem] == PAGE_MARK_START
                && (vpFragListMark[vp.currentItem -1] == PAGE_MARK_CONT
                        || vpFragListMark[vp.currentItem -1] == PAGE_MARK_START)
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