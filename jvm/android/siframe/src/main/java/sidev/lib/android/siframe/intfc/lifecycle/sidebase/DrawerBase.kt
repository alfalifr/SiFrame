package sidev.lib.android.siframe.intfc.lifecycle.sidebase

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout
import sidev.lib.android.siframe.customizable._init._ColorRes
import sidev.lib.android.siframe.customizable._init._Config
import sidev.lib.android.siframe.intfc.`fun`.InitViewFun
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.base.ComplexLifecycleSideBase
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.base.LifecycleSideBase
import sidev.lib.android.siframe.tool.util._ResUtil
import sidev.lib.android.siframe.tool.util._ViewUtil
import sidev.lib.android.siframe.tool.util.`fun`.findViewByType
import sidev.lib.android.siframe.tool.util.`fun`.inflate
import sidev.lib.universal.`fun`.isNull
import sidev.lib.universal.`fun`.notNull

interface DrawerBase: ComplexLifecycleSideBase,
    InitViewFun {
    enum class Type{
        DRAWER_START, DRAWER_END
    }

    override val layoutId: Int
        get() = _Config.LAYOUT_DL

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
//              contentViewContainer!!.removeAllViews()
                contentViewContainer!!.addView(it)
                _initView(it)
            }
//        }
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
/*
        inflate(bottomDrawerLayoutId, contentViewContainer)
            .notNull { _initBottomDrawerView(it) }
        inflate(topDrawerLayoutId, contentViewContainer)
            .notNull { _initTopDrawerView(it) }
 */
    }

    fun setDrawerView(type: Type, v: View?){
        val drawer= when(type){
            Type.DRAWER_START -> startDrawerContainer
            Type.DRAWER_END -> endDrawerContainer
        }
        val isGone= v == null
        setDrawerGone(type, isGone)
        if(!isGone)
            drawer?.addView(v)
        else
            drawer?.removeAllViews()
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
}