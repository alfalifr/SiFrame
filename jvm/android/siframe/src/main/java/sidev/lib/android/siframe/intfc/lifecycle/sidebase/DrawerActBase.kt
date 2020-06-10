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
import sidev.lib.android.siframe.tool.util._ResUtil
import sidev.lib.android.siframe.tool.util._ViewUtil
import sidev.lib.android.siframe.tool.util.`fun`.findViewByType
import sidev.lib.android.siframe.tool.util.`fun`.inflate
import sidev.lib.universal.`fun`.isNull
import sidev.lib.universal.`fun`.notNull

interface DrawerActBase: ComplexLifecycleSideBase,
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

    var rootDrawerLayout: DrawerLayout
    var contentViewContainer: ViewGroup
    var startDrawerContainer: ViewGroup
    var endDrawerContainer: ViewGroup
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


    fun __initDrawer(rootView: View) {
//        contentLayoutId= _sideBase_intent.getExtra(_SIF_Constant.DRAWER_START_LAYOUT_ID, contentLayoutId)!!
        contentViewContainer= rootView.findViewById(contentContainerId)
        startDrawerContainer= rootView.findViewById(startDrawerContainerId)
        endDrawerContainer= rootView.findViewById(endDrawerContainerId)

        val drawerWidthPercent= _ResUtil.getDimen(_sideBase_ctx, _Config.DIMEN_DRAWER_HORIZONTAL_WIDTH_PERCENT)
        val drawerWidth= _ViewUtil.getPercentOfScreenWidth(_sideBase_act, drawerWidthPercent)

        val lpStart= DrawerLayout.LayoutParams(drawerWidth, ViewGroup.LayoutParams.MATCH_PARENT)
        lpStart.gravity= Gravity.START

        val lpEnd= DrawerLayout.LayoutParams(drawerWidth, ViewGroup.LayoutParams.MATCH_PARENT)
        lpEnd.gravity= Gravity.END

        startDrawerContainer.layoutParams= lpStart
        endDrawerContainer.layoutParams= lpEnd

        _ViewUtil.setBgColor(startDrawerContainer, _ColorRes.COLOR_PRIMARY_DARK)
        _ViewUtil.setBgColor(endDrawerContainer, _ColorRes.COLOR_PRIMARY_DARK)

        rootDrawerLayout= rootView.findViewByType()!!
        _inflateStructure(rootView.context)

        __initView(rootView)
    }

    fun _inflateStructure(c: Context){
        c.inflate(contentLayoutId, contentViewContainer)
            .notNull {
                contentViewContainer.addView(it)
                _initView(it)
            }
        c.inflate(startDrawerLayoutId, startDrawerContainer)
            .notNull {
                startDrawerContainer.visibility= View.VISIBLE
                startDrawerContainer.addView(it)
                rootDrawerLayout.closeDrawer(startDrawerContainer)
                _initStartDrawerView(it)
            }.isNull {
                setDrawerGone(startDrawerContainer)
//                rootDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, startDrawerContainer)
            }
        c.inflate(endDrawerLayoutId, endDrawerContainer)
            .notNull {
                endDrawerContainer.visibility= View.VISIBLE
                endDrawerContainer.addView(it)
                rootDrawerLayout.closeDrawer(endDrawerContainer)
                _initEndDrawerView(it)
            }.isNull {
                setDrawerGone(endDrawerContainer)
//                rootDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, endDrawerContainer)
            }
/*
        inflate(bottomDrawerLayoutId, contentViewContainer)
            .notNull { _initBottomDrawerView(it) }
        inflate(topDrawerLayoutId, contentViewContainer)
            .notNull { _initTopDrawerView(it) }
 */
    }

    fun slideDrawer(type: Type, toOpen: Boolean= true){
        val drawer= when(type){
            Type.DRAWER_START -> startDrawerContainer
            Type.DRAWER_END -> endDrawerContainer
        }
        if(toOpen)
            rootDrawerLayout.openDrawer(drawer)
        else
            rootDrawerLayout.closeDrawer(drawer)
    }

    private fun setDrawerGone(drawer: View){
        drawer.visibility= View.GONE
        val lp= drawer.layoutParams as DrawerLayout.LayoutParams //DrawerLayout.LayoutParams(drawerWidth, ViewGroup.LayoutParams.MATCH_PARENT)
        lp.gravity= Gravity.NO_GRAVITY
        lp.width= 0
    }
}