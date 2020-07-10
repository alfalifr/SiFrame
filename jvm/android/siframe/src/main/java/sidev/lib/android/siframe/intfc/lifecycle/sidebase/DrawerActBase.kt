package sidev.lib.android.siframe.intfc.lifecycle.sidebase

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.LifecycleOwner
import sidev.lib.android.siframe._customizable._ColorRes
import sidev.lib.android.siframe._customizable._Config
import sidev.lib.android.siframe.tool.util._ResUtil
import sidev.lib.android.siframe.tool.util._ViewUtil
import sidev.lib.android.siframe.tool.util.`fun`.findViewByType
import sidev.lib.android.siframe.tool.util.`fun`.loge

interface DrawerActBase : DrawerBase{
    override val _prop_act: AppCompatActivity
    override val _prop_ctx: Context

    override var rootDrawerLayout: DrawerLayout
    override var contentViewContainer: ViewGroup
    override var startDrawerContainer: ViewGroup
    override var endDrawerContainer: ViewGroup

    override fun __initDrawer(rootView: View) {
//        contentLayoutId= _sideBase_intent.getExtra(_SIF_Constant.DRAWER_START_LAYOUT_ID, contentLayoutId)!!
        contentViewContainer= rootView.findViewById(contentContainerId)
        startDrawerContainer= rootView.findViewById(startDrawerContainerId)
        endDrawerContainer= rootView.findViewById(endDrawerContainerId)

        val drawerWidthPercent= _ResUtil.getDimen(_prop_ctx, _Config.DIMEN_DRAWER_HORIZONTAL_WIDTH_PERCENT)
        val drawerWidth= _ViewUtil.getPercentOfScreenWidth(_prop_act, drawerWidthPercent)

        val lpStart= DrawerLayout.LayoutParams(drawerWidth, ViewGroup.LayoutParams.MATCH_PARENT)
        lpStart.gravity= Gravity.START

        val lpEnd= DrawerLayout.LayoutParams(drawerWidth, ViewGroup.LayoutParams.MATCH_PARENT)
        lpEnd.gravity= Gravity.END

        startDrawerContainer.layoutParams= lpStart
        endDrawerContainer.layoutParams= lpEnd

        _ViewUtil.setBgColor(startDrawerContainer, _ColorRes.COLOR_PRIMARY_DARK)
        _ViewUtil.setBgColor(endDrawerContainer, _ColorRes.COLOR_PRIMARY_DARK)

        rootDrawerLayout= rootView.findViewByType()!!
        __initView(rootView)
        _inflateStructure(rootView.context)

        if(this is LifecycleOwner){
            this.lifecycle.addObserver(this)
            _prop_backBtnBase?.addOnBackBtnListener(DrawerBase.TAG_ON_BACK_BTN_LISTENER, onBackBtnListener)
        } else
            loge("Kelas ini bkn merupakan \"LifecycleOwner\" sehingga tidak dapat ditambah \"onBackBtnListener\" untuk drawer.")
    }
}