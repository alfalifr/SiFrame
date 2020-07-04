package sidev.lib.android.siframe.intfc.lifecycle.sidebase

import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout
import sidev.lib.android.siframe.customizable._init._ColorRes
import sidev.lib.android.siframe.customizable._init._Config
import sidev.lib.android.siframe.tool.util._ResUtil
import sidev.lib.android.siframe.tool.util._ViewUtil
import sidev.lib.android.siframe.tool.util.`fun`.findViewByType

interface DrawerFragBase : DrawerBase{
    override val layoutId: Int
        get() = if(_prop_act !is DrawerActBase) super.layoutId
                else contentLayoutId

    override var rootDrawerLayout: DrawerLayout?
    override var contentViewContainer: ViewGroup?
    override var startDrawerContainer: ViewGroup?
    override var endDrawerContainer: ViewGroup?

    override fun __initDrawer(rootView: View) {
        if(_prop_act !is DrawerActBase){ //Jika activity bkn DrawerActBase, brarti Fragment ini boleh meng-inflate Drawer pada layar.

//        contentLayoutId= _sideBase_intent.getExtra(_SIF_Constant.DRAWER_START_LAYOUT_ID, contentLayoutId)!!
            contentViewContainer= rootView.findViewById(contentContainerId)
            startDrawerContainer= rootView.findViewById(startDrawerContainerId)
            endDrawerContainer= rootView.findViewById(endDrawerContainerId)

            val drawerWidthPercent= _ResUtil.getDimen(_prop_ctx!!, _Config.DIMEN_DRAWER_HORIZONTAL_WIDTH_PERCENT)
            val drawerWidth= _ViewUtil.getPercentOfScreenWidth(_prop_act!!, drawerWidthPercent)

            val lpStart= DrawerLayout.LayoutParams(drawerWidth, ViewGroup.LayoutParams.MATCH_PARENT)
            lpStart.gravity= Gravity.START

            val lpEnd= DrawerLayout.LayoutParams(drawerWidth, ViewGroup.LayoutParams.MATCH_PARENT)
            lpEnd.gravity= Gravity.END

            startDrawerContainer?.layoutParams= lpStart
            endDrawerContainer?.layoutParams= lpEnd

            _ViewUtil.setBgColor(startDrawerContainer!!, _ColorRes.COLOR_PRIMARY_DARK)
            _ViewUtil.setBgColor(endDrawerContainer!!, _ColorRes.COLOR_PRIMARY_DARK)

            rootDrawerLayout= rootView.findViewByType()!!
            __initView(rootView)
            _inflateStructure(rootView.context)
        }
    }
}