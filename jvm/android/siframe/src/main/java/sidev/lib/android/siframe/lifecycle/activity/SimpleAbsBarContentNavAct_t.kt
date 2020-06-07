package sidev.lib.android.siframe.lifecycle.activity

/*
abstract class SimpleAbsBarContentNavAct_t : SimpleAbsAct(), BackBtnActView {
    override val styleId: Int
        get() = _ConfigBase.STYLE_NO_ACT_BAR //R.style.AppThemeNoActionBar
    override val layoutId: Int
        get() = contentLayoutId
    override val actBackBtn: Activity
        get() = this
    override var backBtnViewList= ArrayList<View>()
    override var onBackPressedListenerList: ArrayList<OnBackPressedListener>
            = ArrayList()

    abstract val contentLayoutId: Int
    open val actBarId: Int= _ConfigBase.LAYOUT_COMP_ACT_BAR_DEFAULT //R.layout.component_action_bar_default
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
/*
    abstract fun initView(contentView: View)
    @CallSuper
    protected open fun initView_int(contentView: View){}
 */


    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(styleId)
        super.onCreate(savedInstanceState)
        setContentView(_ConfigBase.LAYOUT_ACT_SIMPLE)

        initViewRoot()
        inflateAndFillViewStructure()

        registerBackBtnView_int()
        registerBackBtnView(actBarViewContainer.findViewById(_ConfigBase.ID_IV_BACK))

        initView_int(contentViewContainer.getChildAt(0))

        initActBar(actBarViewContainer.getChildAt(0))
        initView(contentViewContainer.getChildAt(0))
        initNavBar(navBar)
    }

    private fun initViewRoot(){
        contentViewContainer= findViewById(_ConfigBase.ID_LL_CONTENT_CONTAINER)
        actBarViewContainer= findViewById(_ConfigBase.ID_LL_BAR_ACT_CONTAINER)
        navBar= findViewById(_ConfigBase.ID_LL_BAR_NAV_CONTAINER)
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
            actBarViewContainer.findViewById<TextView>(_ConfigBase.ID_TV_TITLE).text= title
    }
/*
    fun <D> getIntentData(key: String, i: Intent?= null, default: D?= null): D {
        return if(i != null) i?.extras?.get(key) as D? ?: default as D
        else intent?.extras?.get(key) as D? ?: default as D
    }
 */

    override fun onBackPressed() {
        if(!isBackPressedHandled())
            super.onBackPressed()
    }
}
 */