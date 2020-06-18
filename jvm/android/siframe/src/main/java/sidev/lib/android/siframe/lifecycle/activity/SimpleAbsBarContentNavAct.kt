package sidev.lib.android.siframe.lifecycle.activity

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView
import sidev.lib.android.siframe.customizable._init._Config
import sidev.lib.android.siframe.intfc.`fun`.InitActBarFun
import sidev.lib.android.siframe.tool.`var`._SIF_Constant
import sidev.lib.android.siframe.tool.util._ResUtil
import sidev.lib.android.siframe.tool.util._ViewUtil
import sidev.lib.android.siframe.tool.util.`fun`.loge
import sidev.lib.android.siframe.tool.util.`fun`.setChildPadding
import java.lang.Exception

/**
 * Kelas dalam framework yang digunakan sbg Activity dg:
 * - ActionBar (bar yang ada di atas Activity),
 * - Content (isi secara keseluruhan pada Activity),
 * - NavBar (bar yang ada di bawah Activity)
 */
abstract class SimpleAbsBarContentNavAct : SimpleAbsAct(), InitActBarFun{
/*
    override var isInherited: Boolean= false
    override fun _configInheritable() {
        super.isInherited= true
    }
 */

    override val styleId: Int
        get() = _Config.STYLE_NO_ACT_BAR //R.style.AppThemeNoActionBar
    override val layoutId: Int
        get() = _Config.LAYOUT_ACT_SIMPLE //R.layout.activity_simple
/*
    override val isViewInitFirst: Boolean
        get() = false
 */
    open val isContentLayoutInflatedFirst= true

    abstract val contentLayoutId: Int
    override val actBarId: Int= _Config.LAYOUT_COMP_ACT_BAR_DEFAULT //R.layout.component_action_bar_default
    open val isNavBarVisible= true
    open var menuId: Int?= null
        protected set

    open val actBarViewContainerId= _Config.ID_LL_BAR_ACT_CONTAINER //R.id.ll_bar_act_container
    open val contentViewContainerId= _Config.ID_VG_CONTENT_CONTAINER //R.id.ll_content_container
    open val navBarId= _Config.ID_LL_BAR_NAV_CONTAINER //R.id.bnv_bar_nav_container

    /**
     * Harus dimodifikasi secara internal (private) agar tidak terjadi inkonsistensi
     */
    protected open lateinit var contentViewContainer: ViewGroup
    open lateinit var actBarViewContainer: ViewGroup
        protected set
    protected open lateinit var navBar: BottomNavigationView

    abstract override fun _initActBar(actBarView: View)
    abstract fun _initNavBar(navBarView: BottomNavigationView)
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
        supportActionBar?.hide()

        initViewRoot()
        inflateAndFillViewStructure()

        __registerBackBtnView()
        registerBackBtnView(actBarViewContainer.findViewById(_Config.ID_IV_BACK)) //R.id.iv_back

        val actTitle= getIntentData(_SIF_Constant.EXTRA_TITLE) ?: this::class.java.simpleName
//        if(actTitle != null)
        setActBarTitle(actTitle)

        _initActBar(actBarViewContainer.getChildAt(0))
        if(isViewInitFirst && contentViewContainer.childCount > 0){
//            Log.e("BarContentNavAct", "__initViewFlow className= ${this::class.java.simpleName} isViewInitFirst==false = ${!isViewInitFirst}")
            __initView(rootView)
            _initView(contentViewContainer.getChildAt(0))
        }
        _initNavBar(navBar)
    }


    private fun initViewRoot(){
        contentViewContainer= findViewById(contentViewContainerId) //ll_content_container
        actBarViewContainer= findViewById(actBarViewContainerId) //ll_bar_act_container
        navBar= findViewById(navBarId) //bnv_bar_nav_container
    }

    private fun inflateAndFillViewStructure(){
        val actBar=
            if(actBarId != _Config.LAYOUT_COMP_ACT_BAR_DEFAULT)
                layoutInflater.inflate(actBarId, actBarViewContainer, false)
            else
                _ViewUtil.Template.actBar_Primary(this, _Config.TEMPLATE_VIEW_ACT_BAR_TYPE)
        actBarViewContainer.addView(actBar)

        if(isContentLayoutInflatedFirst && contentLayoutId > 0){
            val contentView= layoutInflater.inflate(contentLayoutId, contentViewContainer, false)
            contentViewContainer.addView(contentView)
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

    open fun setActBarTitle(title: String){
        if(::actBarViewContainer.isInitialized){
            try{
                actBarViewContainer.findViewById<TextView>(_Config.ID_TV_TITLE).text= title //R.id.tv_title
            } catch (e: Exception){
                _ViewUtil.Comp.setTvTitleTxt(actBarViewContainer, title)
            }
        }
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
    fun setActBarView(v: View): View{
        val prevView= actBarViewContainer.getChildAt(0)
        actBarViewContainer.removeView(prevView)
        actBarViewContainer.addView(v)
        loge("setActBarView()")
        return prevView
    }
/*
    fun <D> getIntentData(key: String, i: Intent?= null, default: D?= null): D {
        return if(i != null) i?.extras?.get(key) as D? ?: default as D
        else intent?.extras?.get(key) as D? ?: default as D
    }
 */
}

/*
abstract class SimpleAbsBarContentNavAct : AppCompatActivity(), BackBtnActView{
    override val actBackBtn: Activity
        get() = this
    override var backBtnViewList= ArrayList<View>()
    override var onBackPressedListenerList: ArrayList<OnBackPressedListener>
            = ArrayList()

    abstract val contentLayoutId: Int
    open val actBarId: Int= R.layout.component_action_bar_default
    open val isNavBarVisible= false
    open val menuId: Int?= null

    /**
     * Harus dimodifikasi secara internal (private) agar tidak terjadi inkonsistensi
     */
    lateinit var contentViewContainer: ViewGroup
        private set
    lateinit var actBarViewContainer: ViewGroup
        private set
    lateinit var navBar: BottomNavigationView
        private set

    abstract fun initActBar(actBarView: View)
    abstract fun initNavBar(navBarView: BottomNavigationView)
    abstract fun initView(contentView: View)
    @CallSuper
    protected open fun initView_int(contentView: View){}


    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppThemeNoActionBar)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple)

        initViewRoot()
        inflateAndFillViewStructure()

        registerBackBtnView_int()
        registerBackBtnView(actBarViewContainer.findViewById(R.id.iv_back))

        initView_int(contentViewContainer.getChildAt(0))

        initActBar(actBarViewContainer.getChildAt(0))
        initView(contentViewContainer.getChildAt(0))
        initNavBar(navBar)
    }

    private fun initViewRoot(){
        contentViewContainer= ll_content_container
        actBarViewContainer= ll_bar_act_container
        navBar= bnv_bar_nav_container
    }

    private fun inflateAndFillViewStructure(){
        val actBar= layoutInflater.inflate(actBarId, actBarViewContainer, false)
        val contentView= layoutInflater.inflate(contentLayoutId, contentViewContainer, false)

        actBarViewContainer.addView(actBar)
        contentViewContainer.addView(contentView)

        if(menuId != null)
            navBar.inflateMenu(menuId!!)
        else
            navBar.visibility= View.GONE
    }

    protected open fun setActBarTitle(title: String){
        if(::actBarViewContainer.isInitialized)
            actBarViewContainer.findViewById<TextView>(R.id.tv_title).text= title
    }

    fun <D> getIntentData(key: String, i: Intent?= null, default: D?= null): D {
        return if(i != null) i?.extras?.get(key) as D? ?: default as D
        else intent?.extras?.get(key) as D? ?: default as D
    }

    override fun onBackPressed() {
        if(!isBackPressedHandled())
            super.onBackPressed()
    }
}
 */