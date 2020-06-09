package sidev.lib.android.siframe.intfc.lifecycle.sidebase

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout
import sidev.lib.android.siframe.customizable._init._ColorRes
import sidev.lib.android.siframe.customizable._init._ConfigBase
import sidev.lib.android.siframe.intfc.lifecycle.`fun`.initView
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.base.LifecycleSideBase
import sidev.lib.android.siframe.tool.util._ViewUtil
import sidev.lib.android.siframe.tool.util.`fun`.findViewByType
import sidev.lib.android.siframe.tool.util.`fun`.inflate
import sidev.lib.universal.`fun`.isNull
import sidev.lib.universal.`fun`.notNull

interface DrawerActBase: LifecycleSideBase, initView{
    override val layoutId: Int
        get() = _ConfigBase.LAYOUT_DL

    val contentContainerId
        get()= _ConfigBase.ID_VG_CONTENT_CONTAINER
    val startDrawerContainerId
        get()= _ConfigBase.ID_VG_START_DRAWER_CONTAINER
    val endDrawerContainerId
        get()= _ConfigBase.ID_VG_END_DRAWER_CONTAINER
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
        contentViewContainer= rootView.findViewById(contentContainerId)
        startDrawerContainer= rootView.findViewById(startDrawerContainerId)
        endDrawerContainer= rootView.findViewById(endDrawerContainerId)

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
                startDrawerContainer.visibility= View.GONE
                rootDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, startDrawerContainer)
            }
        c.inflate(endDrawerLayoutId, endDrawerContainer)
            .notNull {
                endDrawerContainer.visibility= View.VISIBLE
                endDrawerContainer.addView(it)
                rootDrawerLayout.closeDrawer(endDrawerContainer)
                _initEndDrawerView(it)
            }.isNull {
                endDrawerContainer.visibility= View.GONE
                rootDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, endDrawerContainer)
            }
/*
        inflate(bottomDrawerLayoutId, contentViewContainer)
            .notNull { _initBottomDrawerView(it) }
        inflate(topDrawerLayoutId, contentViewContainer)
            .notNull { _initTopDrawerView(it) }
 */
    }
}