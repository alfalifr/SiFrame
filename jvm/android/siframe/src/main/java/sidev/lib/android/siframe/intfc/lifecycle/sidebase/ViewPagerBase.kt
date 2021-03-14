package sidev.lib.android.siframe.intfc.lifecycle.sidebase

import android.app.Activity
import android.util.SparseArray
import android.view.View
import androidx.annotation.CallSuper
import androidx.core.view.get
import androidx.core.view.isNotEmpty
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import sidev.lib.android.siframe.`val`._SIF_Config
import sidev.lib.android.siframe.adapter.VpFragAdp
import sidev.lib.android.siframe.intfc.lifecycle.LifecycleBase
import sidev.lib.android.siframe.intfc.lifecycle.rootbase.ActFragBase
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.base.ComplexLifecycleSideBase
import sidev.lib.android.siframe.intfc.listener.OnPageFragActiveListener
import sidev.lib.android.siframe.lifecycle.activity.BarContentNavAct
import sidev.lib.android.siframe.lifecycle.fragment.Frag
import sidev.lib.android.std.tool.util.`fun`.getPosFrom
import sidev.lib.annotation.ChangeLog
import sidev.lib.check.asNotNull
import sidev.lib.check.trya
import sidev.lib.collection.toArrayList
import sidev.lib.exception.IllegalAccessExc
import java.lang.Exception

interface ViewPagerBase<F: Frag>: ComplexLifecycleSideBase {
    override val layoutId: Int
        get() = _SIF_Config.LAYOUT_VP

    override val _prop_view: View
    override val _prop_fm: FragmentManager

    var onPageFragActiveListener: SparseArray<OnPageFragActiveListener>

    val vp: ViewPager
        get()= _prop_view.findViewById(_SIF_Config.ID_VP)
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

    /** Penanda index yg merupakan [PAGE_MARK_START] yg akan diisikan ke [vpFragListMark]. */
    var vpFragListStartMark: Array<Int>
    var vpAdp: VpFragAdp
//    val vpFm: FragmentManager
//    val vpCtx: Context
    /** Index awal [vp]. */
    var pageStartInd: Int
    /** Index akhir [vp]. */
    var pageEndInd: Int

    var isVpTitleFragBased: Boolean
    var isVpBackOnBackPressed: Boolean

    companion object{
        val PAGE_MARK_START= 0
        val PAGE_MARK_CONT= 1
    }

    /**
     * Digunakan untuk menyimpan [ViewPager.OnPageChangeListener]
     * yg terhubung dg [BottomNavigationView].
     */
    var vpOnPageListenerToNavBar: ViewPager.OnPageChangeListener?

    /**
     * Listener sederhana untuk perpindahan halaman pada [vp].
     * Listener ini berbeda dg [ViewPager.OnPageChangeListener], yaitu dipanggil sebelum
     * [vp] berpindah halaman. Hasil return dari listener digunakan untuk
     * menentukan apakah [vp] berpindah halaman atau tidak.
     */
    var vpOnBeforePageJumpListener: ((oldPosition: Int, newPosition: Int) -> Boolean)? //= null


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

    /**
     * Opsional pada turunan yg memiliki tujuan yg berkaitan erat dg vp, seperti [MultipleActBarViewPagerBase].
     *
     * <10 Juli 2020> => Tidak jadi [CallSuper].
     */
//    @CallSuper
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
        vpAdp= VpFragAdp(_prop_fm)
        if(this is ActFragBase)
            vpAdp._prop_parentLifecycle= this
        vp.adapter= vpAdp
        setFragList(vpFragList)

        vp.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            private var currentPosition= 0
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {}
            override fun onPageSelected(position: Int) {
                trya{ vpFragList[currentPosition].currentState= LifecycleBase.State.PAUSED }

                attachActBarTitle(position)
                //Karena vpAdp.behavior == BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
                //vpFragList[position].resolveOnActive(_prop_view, this@ViewPagerBase, position)
                onPageFragActiveListener[position]?.onPageFragActive(_prop_view, position) //

                vpFragList[position].currentState= LifecycleBase.State.ACTIVE
                currentPosition= position
            }
        })
        if(vpFragList.isNotEmpty())
            attachActBarTitle(0)
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
//        loge("setFragListMark() vpFragList.size= ${vpFragList.size} markListInt.size= ${markListInt.size}")
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

            vpAdp.items= list.toArrayList() as ArrayList<Frag>
//            vpAdp= VpFragAdp(_prop_fm, *list)
//            vp.removeAllViews()

            if(isVpTitleFragBased && vpFragList.isNotEmpty())
                this.asNotNull { act: BarContentNavAct ->
                    try{ act.setActBarTitle(vpFragList.first().fragTitle) }
                    catch (e: Exception){}
                }
/*
            try {
                this.asNotNull { act: MultipleActBarViewPagerBase<*> ->
                    act.actBarViewList.clear()
                    act.attachActBarView(0)
                }
            } catch (e: IllegalAccessExc){}
 */
        } else{
            vp.adapter= null
        }

        setPageLimitInd(0, size)
        setFragListMark(initFragListMark())
        /*Karena vpAdp.behavior == BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        if(list != null) {
            if(vpFragList[0].currentState >= LifecycleBase.State.STARTED){
                vpFragList.firstOrNull()?.resolveOnActive(_prop_view, this, 0)
            } else
                vpFragList.firstOrNull()?.firstFragPageOnActivePosition= 0
        }
         */
    }

    fun getFragPos(frag: F): Int{
        return vpAdp.items?.indexOf(frag) ?: -1
    }

    fun attachActBarTitle(pos: Int){
        if(isVpTitleFragBased){
            when(this){
                is Activity -> this
                is Fragment -> this.activity
                else -> null
            }.asNotNull { act: BarContentNavAct ->
                try{ act.setActBarTitle(vpFragList[pos].fragTitle) }
                catch (e: Exception){
                    /* Ini ditujukan agar saat terjadi kesalahan saat setActBarTitle()
                    tidak menyebabkan error.
                     */
                }
            }
        }
    }

    /**
     * @return true jika berhasil dan sebaliknya.
     */
    fun registerOnPageFragActiveListener(frag: F, l: OnPageFragActiveListener): Boolean{
        val pos= vpAdp.items?.indexOf(frag) ?: -1
        return if(pos >= 0){
            onPageFragActiveListener.setValueAt(pos, l) //= l
            true
        } else false
    }
    fun registerOnPageFragActiveListener(frag: F, func: (vParent: View, pos: Int) -> Unit): Boolean{
        return registerOnPageFragActiveListener(frag, object : OnPageFragActiveListener {
            override var tag: Any?= null

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
     * Menghitung apakah perpindahan menuju halaman ke [index]
     * akan dilakukan. return `true` jika perpindahan dilakukan dan sebaliknya.
     */
    fun calculatePageJump(index: Int, checkForPageRule: Boolean= true): Boolean{
        val current= vp.currentItem

        // 1. Jika index sebelum dan sesudah sama, artinya gak pindah.
        if(current == index)
            return false

        // 2. Cek apakah jump sesuai aturan [vpFragListMark]
        if(checkForPageRule){
            val firstBool= if(index > current){ // Untuk case pageForth()
                if(vp.currentItem < (vp.adapter?.count ?: 0) -1
                        && (vp.currentItem < /*<=*/ pageEndInd || pageEndInd < 0)){
                    for(i in current +1 .. index)
                        if(vpFragListMark[i] == PAGE_MARK_START)
                            return false
                    true
                } else false
//            Log.e("pageForth", "vp.currentItem = ${vp.currentItem} vpFragListMark_int[vp.currentItem +1] = ${vpFragListMark[vp.currentItem]} bool = $bool")
//            Log.e("pageForth", "Gak masuk vp.currentItem = ${vp.currentItem} pageEndInd = $pageEndInd vp.childCount =${vp.childCount} bool = false")
            } else { // Untuk case pageBackward()
                if(vp.currentItem > 0
                        && vp.currentItem > /*>=*/ pageStartInd){
                    for(i in current downTo index +1)
                        if(vpFragListMark[i] == PAGE_MARK_START)
                            return false
                    true
                } else false
            }
            if(!firstBool)
                return false
        }

        // 3. Cek apakah [vpOnBeforePageJumpListener] menghasilkan `true`
        return (vpOnBeforePageJumpListener?.invoke(current, index) ?: true)
    }
    /**
     * Untuk menuju ke halaman [index] pada [vp].
     * return `true` jika perpindahan berhasil dilakukan.
     */
    fun jumpToPage(index: Int, checkForPageRule: Boolean= true): Boolean=
        calculatePageJump(index, checkForPageRule).also { if(it) vp.currentItem= index }

    /**
     * Halaman VP selanjutnya
     * @return true jika halaman VP belum terahir (berhasil pindah)
     */
    @ChangeLog("Rabu, 30 Sep 2020", "Logika pengecekan indek udah jadi satu pada `jumpToPage`.")
    fun pageForth(): Boolean{
        return jumpToPage(vp.currentItem +1)
/*
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
 */
    }
    @ChangeLog("Rabu, 30 Sep 2020", "Logika pengecekan indek udah jadi satu pada `jumpToPage`.")
    fun pageBackward(): Boolean{
        return jumpToPage(vp.currentItem -1)
/*
        return if(vp.currentItem > 0
            && vp.currentItem > /*>=*/ pageStartInd){
            if(vpFragListMark[vp.currentItem] == PAGE_MARK_START
/*
                && (vpFragListMark[vp.currentItem -1] == PAGE_MARK_CONT
                        || vpFragListMark[vp.currentItem -1] == PAGE_MARK_START)
 */
            ){
                false
            } else{
                vp.currentItem= vp.currentItem -1
                true
            }
        } else
            false
 */
    }
}