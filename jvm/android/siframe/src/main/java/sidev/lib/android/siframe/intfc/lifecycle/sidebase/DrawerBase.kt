package sidev.lib.android.siframe.intfc.lifecycle.sidebase

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import sidev.lib.android.siframe._customizable._Config
import sidev.lib.android.siframe.intfc.`fun`.InitViewFun
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.base.ComplexLifecycleSideBase
import sidev.lib.android.siframe.intfc.listener.OnBackPressedListener
import sidev.lib.android.siframe.intfc.prop.BackBtnBaseProp
import sidev.lib.android.siframe.tool.util.`fun`.inflate
import sidev.lib.android.siframe.tool.util.`fun`.loge
import sidev.lib.universal.`fun`.isNull
import sidev.lib.universal.`fun`.notNull
import sidev.lib.universal.`fun`.notNullTo
import java.lang.IllegalArgumentException

interface DrawerBase: ComplexLifecycleSideBase,
    BackBtnBaseProp, LifecycleObserver,
    InitViewFun {
    companion object{
        /**
         * Flag agar layout drawer yg sudah ada tidak dirubah walau [Context.inflate]
         * akan menghasilkan error. Scr default, [DrawerBase] ini akan menghilangkan (tidak tampil)
         * layout drawer jika [Context.inflate] menghasilkan error saat meng-inflate id layout yg salah.
         *
         * Flag ini berguna saat programmer menggunakan fragment yg berupa [DrawerFragBase]
         * pada [SingleFragActBase] namun tidak ingin mengubah tampilan drawer yg sudah ada.
         */
        @JvmStatic
        val DRAWER_LAYOUT_SAME_AS_EXISTING= -10
        val TAG_ON_BACK_BTN_LISTENER= "DrawerBase"
    }
    enum class Type{
        DRAWER_START, DRAWER_END
    }

    override val layoutId: Int
        get() = _Config.LAYOUT_DL


    override val _prop_backBtnBase: BackBtnBase?
    /**
     * Untuk mengakodomasi back-button event oleh user saat drawer dibuka.
     * Tujuan [onBackBtnListener] bkn hanya pada level fragment, namun juga Activity.
     * Hal tersebut dikarenakan ada bbrp Activity yg tidak bisa mendeteksi jika
     * drawernya dibuka. Nilai dari [OnBackPressedListener] disimpan agar
     * saat [DrawerFragBase] di-destroy maka tidak akan terjadi NullPointerException.
     */
    val onBackBtnListener: () -> Boolean
        get()= {
            loge("onBackBtnListener DrawerBase dipanggil")
            if(rootDrawerLayout != null){
                val startDrawerIsOpen= isDrawerOpen(Type.DRAWER_START)
                val endDrawerIsOpen= isDrawerOpen(Type.DRAWER_END)

                if(startDrawerIsOpen)
                    slideDrawer(Type.DRAWER_START, false)

                if(endDrawerIsOpen)
                    slideDrawer(Type.DRAWER_END, false)

                startDrawerIsOpen || endDrawerIsOpen
            } else false
        }

    val contentContainerId
        get()= _Config.ID_VG_CONTENT_CONTAINER
    val startDrawerContainerId
        get()= _Config.ID_VG_START_DRAWER_CONTAINER
    val endDrawerContainerId
        get()= _Config.ID_VG_END_DRAWER_CONTAINER
//    open val bottomDrawerContainerId= _ConfigBase.ID_SL_BOTTOM_DRAWER_CONTAINER
//    open val topDrawerContainerId= _ConfigBase.ID_SL_TOP_DRAWER_CONTAINER

    val contentLayoutId: Int
    val startDrawerLayoutId: Int
    val endDrawerLayoutId: Int
//    abstract val bottomDrawerLayoutId: Int
//    abstract val topDrawerLayoutId: Int

    //<27 Juni 2020> => Nullable karena kemungkinan interface ini digunakan oleh fragment
    //                    di mana container ini merupakan container milik activity-nya
    //                    sehingga mungkin saja activity-nya bkn merupakan DrawerBase.
    val rootDrawerLayout: DrawerLayout?
    val contentViewContainer: ViewGroup?
    val startDrawerContainer: ViewGroup?
    val endDrawerContainer: ViewGroup?
/*
    lateinit var bottomDrawerContainer: ViewGroup
        private set
    lateinit var topDrawerContainer: ViewGroup
        private set
 */

    fun _initStartDrawerView(startDrawerView: View)
    fun _initEndDrawerView(endDrawerView: View)

//    abstract fun _initBottomDrawerView(bottomDrawerView: View)
//    abstract fun _initTopDrawerView(topDrawerView: View)

    //<27 Juni 2020> => Implementasi awal dipindahkan ke DrawerActBase
    // karena DrawerBase dapat digunakan oleh Fragment (DrawerFragBase).
    fun __initDrawer(rootView: View)

    fun _inflateStructure(c: Context){
        //Agar saat __initDrawer() dipanggil kedua kalinya contentViewContainer gak kosongan,
        // terutama bagi SingleFragAct.
        // **Gak jadi**
//        if(contentViewContainer?.childCount == 0){
        c.inflate(contentLayoutId, contentViewContainer)
            .notNull {
                contentViewContainer!!.removeAllViews()
                    //<6 Juli 2020> => Dihapus dulu agar view lama yg ketumpuk hilang
                    //  Karena gakda gunanya jika gak dihilangkan tapi gak tampil.
                contentViewContainer!!.addView(it)
                _initView(it)
            }
//        }

        if(startDrawerLayoutId != DRAWER_LAYOUT_SAME_AS_EXISTING){
            c.inflate(startDrawerLayoutId, startDrawerContainer)
                .notNull {
                    startDrawerContainer!!.visibility= View.VISIBLE
                    startDrawerContainer!!.removeAllViews()
                    startDrawerContainer!!.addView(it)
                    rootDrawerLayout!!.closeDrawer(startDrawerContainer!!)
                    _initStartDrawerView(it)
                }.isNull {
                    setDrawerGone(Type.DRAWER_START)
//                rootDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, startDrawerContainer)
                }
        }

        if(endDrawerLayoutId != DRAWER_LAYOUT_SAME_AS_EXISTING){
            c.inflate(endDrawerLayoutId, endDrawerContainer)
                .notNull {
                    endDrawerContainer!!.visibility= View.VISIBLE
                    endDrawerContainer!!.removeAllViews()
                    endDrawerContainer!!.addView(it)
                    rootDrawerLayout!!.closeDrawer(endDrawerContainer!!)
                    _initEndDrawerView(it)
                }.isNull {
                    setDrawerGone(Type.DRAWER_END)
//                rootDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, endDrawerContainer)
                }
        }
/*
        inflate(bottomDrawerLayoutId, contentViewContainer)
            .notNull { _initBottomDrawerView(it) }
        inflate(topDrawerLayoutId, contentViewContainer)
            .notNull { _initTopDrawerView(it) }
 */
    }

    /**
     * Fungsi ini mengganti isi view dari [startDrawerContainer] maupun [endDrawerContainer].
     * Fungsi ini memanggil [ViewGroup.removeAllViews] terlebih dahulu sebelum memanggil
     * [ViewGroup.addView], sehingga tidak dapat menumpuk view isi.
     */
    fun setDrawerView(type: Type, v: View?){
        when(type){
            Type.DRAWER_START -> startDrawerContainer
            Type.DRAWER_END -> endDrawerContainer
        }.notNull { drawer ->
            val isGone= v == null
            setDrawerGone(type, isGone)
            drawer.removeAllViews()
            if(!isGone)
                drawer.addView(v)
        }
    }

    fun slideDrawer(type: Type, toOpen: Boolean= true){
        when(type){
            Type.DRAWER_START -> startDrawerContainer
            Type.DRAWER_END -> endDrawerContainer
        }.notNull { drawer ->
            if(toOpen)
                rootDrawerLayout?.openDrawer(drawer)
            else
                rootDrawerLayout?.closeDrawer(drawer)
        }
    }

    /**
     * Digunakan untuk mengecek apakah drawer dg tipe [type] sedang dibuka atau tidak.
     *
     * @return true jika sedang dibuka.
     *   false jika sedang ditutup atau drawer dg tipe [type] bkn drawer (gravity == [Gravity.NO_GRAVITY]).
     */
    fun isDrawerOpen(type: Type): Boolean{
        return when(type){
            Type.DRAWER_START -> startDrawerContainer
            Type.DRAWER_END -> endDrawerContainer
        }.notNullTo { drawer ->
            try{
                rootDrawerLayout.notNullTo { it.isDrawerOpen(drawer) }
                ?: false
            } catch (e: IllegalArgumentException){ false }
        } ?: false
    }

    /**
     * Fungsi untuk menampilkan atau menghilangkan [startDrawerContainer] maupun [endDrawerContainer]
     * dari layar dengan mengubah nilai [View.setVisibility] dan [DrawerLayout.LayoutParams.gravity].
     *
     * Fungsi ini tidak menghilangkan isi view dari [startDrawerContainer] maupun [endDrawerContainer]
     * karena jika isi sudah dihilangkan, maka tidak dapat ditampilkan lagi.
     * Untuk menghilangkan semua isi pada [startDrawerContainer] maupun [endDrawerContainer],
     * gunakan fungsi [setDrawerView] dg paramater [v: View] = null.
     */
    private fun setDrawerGone(type: Type, gone: Boolean= true){
        when(type){
            Type.DRAWER_START -> startDrawerContainer
            Type.DRAWER_END -> endDrawerContainer
        }.notNull { drawer ->
            drawer.visibility= if(gone) View.GONE
            else View.VISIBLE

            val lp= drawer.layoutParams as DrawerLayout.LayoutParams //DrawerLayout.LayoutParams(drawerWidth, ViewGroup.LayoutParams.MATCH_PARENT)

            lp.gravity= if(gone) Gravity.NO_GRAVITY
            else when(type){
                Type.DRAWER_START -> Gravity.START
                Type.DRAWER_END -> Gravity.END
            }
//        lp.width= 0
        }
    }

    /**
     * @param owner hanya sbg pengaman jika fungsi ini dipanggil dari luar.
     *   Jika tidak diberi [owner], kemungkinan terjadi error.
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun __detachBackBtnListener(owner: LifecycleOwner){
        if(owner == this)
            _prop_backBtnBase.notNull { base ->
                base.removeOnBackBtnListenerByTag(TAG_ON_BACK_BTN_LISTENER)
                loge("backBtnBase dicopot")
            }
//            _prop_backBtnBase?.removeOnBackBtnListenerByTag(TAG_ON_BACK_BTN_LISTENER)
    }
}