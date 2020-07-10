package sidev.lib.android.siframe.intfc.lifecycle.sidebase

import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import sidev.lib.android.siframe._customizable._ColorRes
import sidev.lib.android.siframe._customizable._Config
import sidev.lib.android.siframe.intfc.listener.OnBackPressedListener
import sidev.lib.android.siframe.intfc.prop.BackBtnBaseProp
import sidev.lib.android.siframe.tool.util._ResUtil
import sidev.lib.android.siframe.tool.util._ViewUtil
import sidev.lib.android.siframe.tool.util.`fun`.findViewByType
import sidev.lib.android.siframe.tool.util.`fun`.loge
import sidev.lib.universal.`fun`.asNotNull
import sidev.lib.universal.`fun`.notNull

interface DrawerFragBase : DrawerBase, BackBtnBaseProp, LifecycleObserver{
    override val layoutId: Int
        get() = if(_prop_act !is DrawerActBase) super.layoutId
                else contentLayoutId

    override var rootDrawerLayout: DrawerLayout?
    override var contentViewContainer: ViewGroup?
    override var startDrawerContainer: ViewGroup?
    override var endDrawerContainer: ViewGroup?

    override val _prop_backBtnBase: BackBtnBase?

    /**
     * Untuk mengakodomasi back-button event oleh user saat drawer dibuka
     * pada level fragment. Nilai dari [OnBackPressedListener] disimpan agar
     * saat [DrawerFragBase] di-destroy maka tidak akan terjadi NullPointerException.
     */
    val onBackBtnListener: OnBackPressedListener
        //() -> Boolean
/*
        get()= {
            if(rootDrawerLayout != null){
                val startDrawerIsOpen= isDrawerOpen(DrawerBase.Type.DRAWER_START)
                val endDrawerIsOpen= isDrawerOpen(DrawerBase.Type.DRAWER_END)

                if(startDrawerIsOpen)
                    slideDrawer(DrawerBase.Type.DRAWER_START, false)

                if(endDrawerIsOpen)
                    slideDrawer(DrawerBase.Type.DRAWER_END, false)

                startDrawerIsOpen || endDrawerIsOpen
            } else false
        }
 */

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

            if(this is LifecycleOwner){
                this.lifecycle.addObserver(this)
                _prop_backBtnBase?.addOnBackBtnListener(onBackBtnListener)
            } else
                loge("Kelas ini bkn merupakan \"LifecycleOwner\" sehingga tidak dapat ditambah \"onBackBtnListener\" untuk drawer.")
        } else{
            __initView(rootView)
            _prop_act.asNotNull { act: DrawerActBase ->
                rootDrawerLayout= act.rootDrawerLayout
                contentViewContainer= act.contentViewContainer //rootView.findViewById(contentContainerId)
                startDrawerContainer= act.startDrawerContainer //rootView.findViewById(startDrawerContainerId)
                endDrawerContainer= act.endDrawerContainer //rootView.findViewById(endDrawerContainerId)

                contentViewContainer?.getChildAt(0).notNull { _initView(it) }
                startDrawerContainer?.getChildAt(0).notNull { _initStartDrawerView(it) }
                endDrawerContainer?.getChildAt(0).notNull { _initEndDrawerView(it) }
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun __detachBackBtnListener(){
        _prop_backBtnBase?.removeOnBackBtnListener(onBackBtnListener)
    }
}