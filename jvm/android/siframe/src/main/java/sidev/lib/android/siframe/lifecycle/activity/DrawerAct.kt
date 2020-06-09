package sidev.lib.android.siframe.lifecycle.activity

import android.view.View
import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.DrawerActBase

abstract class DrawerAct: SimpleAbsAct(), DrawerActBase{
    override lateinit var rootDrawerLayout: DrawerLayout
    override lateinit var contentViewContainer: ViewGroup
    override lateinit var startDrawerContainer: ViewGroup
    override lateinit var endDrawerContainer: ViewGroup

    override fun __initViewFlow(rootView: View) {
        __initDrawer(rootView)
    }
}

/*
abstract class DrawerAct: SimpleAbsAct(){
    override val layoutId: Int
        get() = _ConfigBase.LAYOUT_DL

    open val contentContainerId= _ConfigBase.ID_VG_CONTENT_CONTAINER
    open val startDrawerContainerId= _ConfigBase.ID_SL_START_DRAWER_CONTAINER
    open val endDrawerContainerId= _ConfigBase.ID_SL_END_DRAWER_CONTAINER
//    open val bottomDrawerContainerId= _ConfigBase.ID_SL_BOTTOM_DRAWER_CONTAINER
//    open val topDrawerContainerId= _ConfigBase.ID_SL_TOP_DRAWER_CONTAINER


    abstract val contentLayoutId: Int
    abstract val startDrawerLayoutId: Int
    abstract val endDrawerLayoutId: Int
//    abstract val bottomDrawerLayoutId: Int
//    abstract val topDrawerLayoutId: Int

    lateinit var rootDrawerLayout: DrawerLayout
        private set
    lateinit var contentViewContainer: ViewGroup
        private set
    lateinit var startDrawerContainer: ViewGroup
        private set
    lateinit var endDrawerContainer: ViewGroup
        private set
/*
    lateinit var bottomDrawerContainer: ViewGroup
        private set
    lateinit var topDrawerContainer: ViewGroup
        private set
 */

    abstract override fun _initView(layoutView: View)
    abstract fun _initStartDrawerView(startDrawerView: View)
    abstract fun _initEndDrawerView(endDrawerView: View)
//    abstract fun _initBottomDrawerView(bottomDrawerView: View)
//    abstract fun _initTopDrawerView(topDrawerView: View)


    override fun __initViewFlow(rootView: View) {
        __initView(rootView)
        contentViewContainer= findViewById(contentContainerId)
        startDrawerContainer= findViewById(startDrawerContainerId)
        endDrawerContainer= findViewById(endDrawerContainerId)

        _inflateStructure()
        rootDrawerLayout= layoutView.findViewByType()!!

        rootDrawerLayout.closeDrawer(startDrawerContainer)
        rootDrawerLayout.closeDrawer(endDrawerContainer)
    }

    fun _inflateStructure(){
        inflate(contentLayoutId, contentViewContainer)
            .notNull { _initView(it) }
        inflate(startDrawerLayoutId, contentViewContainer)
            .notNull { _initStartDrawerView(it) }
        inflate(endDrawerLayoutId, contentViewContainer)
            .notNull { _initEndDrawerView(it) }
/*
        inflate(bottomDrawerLayoutId, contentViewContainer)
            .notNull { _initBottomDrawerView(it) }
        inflate(topDrawerLayoutId, contentViewContainer)
            .notNull { _initTopDrawerView(it) }
 */
    }
}
 */