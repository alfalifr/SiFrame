package sidev.lib.android.siframe.lifecycle.activity

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView
import sidev.lib.android.siframe.customizable._init._ConfigBase
import sidev.lib.android.siframe.util._Constant

/**
 * Kelas dalam framework yang digunakan sbg Activity dg:
 * - ActionBar (bar yang ada di atas Activity),
 * - Content (isi secara keseluruhan pada Activity),
 * - NavBar (bar yang ada di bawah Activity)
 */
abstract class SimpleAbsBarContentNavAct : SimpleAbsAct(){
    override val styleId: Int
        get() = _ConfigBase.STYLE_NO_ACT_BAR //R.style.AppThemeNoActionBar
    override val layoutId: Int
        get() = _ConfigBase.LAYOUT_ACT_SIMPLE //R.layout.activity_simple

    override val isViewInitFirst: Boolean
        get() = false
    open val isContentLayoutInflatedFirst= true

    abstract val contentLayoutId: Int
    open val actBarId: Int= _ConfigBase.LAYOUT_COMP_ACT_BAR_DEFAULT //R.layout.component_action_bar_default
    open val isNavBarVisible= false
    open val menuId: Int?= null

    open val actBarViewContainerId= _ConfigBase.ID_LL_BAR_ACT_CONTAINER //R.id.ll_bar_act_container
    open val contentViewContainerId= _ConfigBase.ID_LL_CONTENT_CONTAINER //R.id.ll_content_container
    open val navBarId= _ConfigBase.ID_LL_BAR_NAV_CONTAINER //R.id.bnv_bar_nav_container

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
/*
    abstract fun initView(contentView: View)
    @CallSuper
    protected open fun initView_int(contentView: View){}
 */

    override fun onCreate(savedInstanceState: Bundle?) {
//        setTheme(R.style.AppThemeNoActionBar)
//        Log.e("SimpleAbsBarContentNavAct", "onCreate ===AWAL===== name: ${this::class.java.simpleName} ")
        super.onCreate(savedInstanceState)
//        Log.e("SimpleAbsBarContentNavAct", "onCreate ===AKHIR===== name: ${this::class.java.simpleName} ")
        supportActionBar?.hide()
//        setContentView(R.layout.activity_simple)

        initViewRoot()
        inflateAndFillViewStructure()

        registerBackBtnView_int()
        registerBackBtnView(actBarViewContainer.findViewById(_ConfigBase.ID_IV_BACK)) //R.id.iv_back

        val extraTitle= getIntentData<String?>(_Constant.EXTRA_TITLE)
        if(extraTitle != null)
            setActBarTitle(extraTitle)

        initActBar(actBarViewContainer.getChildAt(0))
        if(contentViewContainer.childCount > 0){
//            Log.e("SimpleAbsBarContentNavAct", "onCreate layoutView==null = ${layoutView==null}")
            initView_int(contentViewContainer.getChildAt(0))
            initView(contentViewContainer.getChildAt(0))
        }
        initNavBar(navBar)
    }

    private fun initViewRoot(){
        contentViewContainer= findViewById(contentViewContainerId) //ll_content_container
        actBarViewContainer= findViewById(actBarViewContainerId) //ll_bar_act_container
        navBar= findViewById(navBarId) //bnv_bar_nav_container
    }

    private fun inflateAndFillViewStructure(){
        val actBar= layoutInflater.inflate(actBarId, actBarViewContainer, false)
        actBarViewContainer.addView(actBar)

        if(isContentLayoutInflatedFirst && contentLayoutId > 0){
            val contentView= layoutInflater.inflate(contentLayoutId, contentViewContainer, false)
            contentViewContainer.addView(contentView)
        }

        if(menuId != null)
            navBar.inflateMenu(menuId!!)
        else
            navBar.visibility= View.GONE
    }

    open fun setActBarTitle(title: String){
        if(::actBarViewContainer.isInitialized)
            actBarViewContainer.findViewById<TextView>(_ConfigBase.ID_TV_TITLE).text= title //R.id.tv_title
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