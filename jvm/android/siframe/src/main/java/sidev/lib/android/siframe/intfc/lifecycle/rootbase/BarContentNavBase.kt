package sidev.lib.android.siframe.intfc.lifecycle.rootbase

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import sidev.lib.android.siframe.customizable._init._Config
import sidev.lib.android.siframe.intfc.`fun`.InitActBarFun
import sidev.lib.android.siframe.intfc.lifecycle.rootbase.base.LifecycleRootBase
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.base.LifecycleSideBase
import sidev.lib.android.siframe.intfc.prop.ActProp
import sidev.lib.android.siframe.lifecycle.activity.Act
import sidev.lib.android.siframe.tool.`var`._SIF_Constant
import sidev.lib.android.siframe.tool.util._ViewUtil
import sidev.lib.android.siframe.tool.util.`fun`.inflate
import sidev.lib.android.siframe.tool.util.`fun`.loge
import sidev.lib.universal.`fun`.asNotNull
import sidev.lib.universal.`fun`.notNull
import java.lang.Exception

interface BarContentNavBase: SimpleAbsActFragBase, InitActBarFun, ActProp {

    override val styleId: Int
        get() = _Config.STYLE_NO_ACT_BAR //R.style.AppThemeNoActionBar
    override val layoutId: Int
        get() = _Config.LAYOUT_ACT_SIMPLE //R.layout.activity_simple
    /*
        override val isViewInitFirst: Boolean
            get() = false
     */
    val isContentLayoutInflatedFirst
        get()= true

    val contentLayoutId: Int
//    override val actBarId: Int= _Config.LAYOUT_COMP_ACT_BAR_DEFAULT //R.layout.component_action_bar_default
    val isNavBarVisible
        get()= true
    var menuId: Int?
//        get()= null
//        protected set

    val actBarViewContainerId
        get()= _Config.ID_LL_BAR_ACT_CONTAINER //R.id.ll_bar_act_container
    val contentViewContainerId
        get()= _Config.ID_VG_CONTENT_CONTAINER //R.id.ll_content_container
    val navBarId
        get()= _Config.ID_LL_BAR_NAV_CONTAINER //R.id.bnv_bar_nav_container

    /**
     * Harus dimodifikasi secara internal (private) agar tidak terjadi inkonsistensi
     */
    var contentViewContainer: ViewGroup
    var actBarViewContainer: ViewGroup
//        protected set
    var navBar: BottomNavigationView

    override fun _initActBar(actBarView: View)
    fun _initNavBar(navBarView: BottomNavigationView)

    /*
    abstract fun initView(contentView: View)
    @CallSuper
    protected open fun initView_int(contentView: View){}
 */
/*
    override fun onCreate(savedInstanceState: Bundle?) {
//        setTheme(R.style.AppThemeNoActionBar)
//        Log.e("SimpleAbsBarContentNavAct", "onCreate ===AWAL===== name: ${this::class.java.simpleName} ")
        super.onCreate(savedInstanceState)
//        Log.e("SimpleAbsBarContentNavAct", "onCreate ===AKHIR===== name: ${this::class.java.simpleName} ")
        doWhenNotIherited {
            ___initRootBase(this, getRootView())
        }
    }
 */

    override fun __initViewFlow(rootView: View) {
//        Log.e("BarContentNavAct", "__initViewFlow className= ${this::class.java.simpleName}")
        _prop_act.asNotNull { act: Act ->
            act.supportActionBar?.hide()

            initViewRoot()
            inflateAndFillViewStructure()

            act.__registerBackBtnView()
            act.registerBackBtnView(actBarViewContainer.findViewById(_Config.ID_IV_BACK)) //R.id.iv_back

            val actTitle= act.getIntentData(_SIF_Constant.EXTRA_TITLE) ?: this::class.java.simpleName
//        if(actTitle != null)
            setActBarTitle(actTitle)

            _initActBar(actBarViewContainer.getChildAt(0))
            if(act.isViewInitFirst && contentViewContainer.childCount > 0){
//            Log.e("BarContentNavAct", "__initViewFlow className= ${this::class.java.simpleName} isViewInitFirst==false = ${!isViewInitFirst}")
                act.__initView(rootView)
                act._initView(contentViewContainer.getChildAt(0))
            }
            _initNavBar(navBar)
        }
    }



    private fun initViewRoot(){
        _prop_act.notNull { act ->
            contentViewContainer= act.findViewById(contentViewContainerId) //ll_content_container
            actBarViewContainer= act.findViewById(actBarViewContainerId) //ll_bar_act_container
            navBar= act.findViewById(navBarId) //bnv_bar_nav_container
        }
    }

    private fun inflateAndFillViewStructure(){
        _prop_act.notNull { act ->
            val actBar=
                if(actBarId != _Config.LAYOUT_COMP_ACT_BAR_DEFAULT)
                    act.inflate(actBarId, actBarViewContainer, false)
                else
                    _ViewUtil.Template.actBar_Primary(act, _Config.TEMPLATE_VIEW_ACT_BAR_TYPE)
            actBarViewContainer.addView(actBar)

            if(isContentLayoutInflatedFirst && contentLayoutId > 0){
                val contentView= act.inflate(contentLayoutId, contentViewContainer, false)
                contentViewContainer.addView(contentView)
            }
        }
/*
        <18 Juni 2020> => untuk sementara offset top ditiadakan
        val topPadding= _ResUtil.getDimen(this, _Config.DIMEN_ACT_BAR_OFFSET)
        contentViewContainer.setChildPadding(top= topPadding.toInt())
 */

        if(menuId != null && isNavBarVisible){
            navBar.inflateMenu(menuId!!)
            navBar.visibility= View.VISIBLE
        } else
            navBar.visibility= View.GONE
    }

    fun setActBarTitle(title: String){
        loge("setActBarTitle() title= $title")
        try{
            actBarViewContainer.findViewById<TextView>(_Config.ID_TV_TITLE).text= title //R.id.tv_title
        } catch (e: Exception){
            _ViewUtil.Comp.setTvTitleTxt(actBarViewContainer, title)
        } catch (e: Exception){
            loge("actBarViewContainer belum diinit!")
        }
/*
        if(::actBarViewContainer.isInitialized){
        }
 */
    }

    fun setMenu(menuId: Int?){
        if(menuId != this.menuId){
            navBar.menu.clear()
            this.menuId= menuId
            if(menuId != null){
                navBar.inflateMenu(menuId)
                navBar.visibility= View.VISIBLE
            } else
                navBar.visibility= View.GONE
        }
    }

    /**
     * @return view actBar sebelumnya yg dilepaskan.
     */
    fun setActBarView(v: View): View {
        val prevView= actBarViewContainer.getChildAt(0)
        actBarViewContainer.removeView(prevView)
        actBarViewContainer.addView(v)
        loge("setActBarView()")
        return prevView
    }
}