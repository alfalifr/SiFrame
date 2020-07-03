package sidev.lib.android.siframe.lifecycle.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.CallSuper
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import sidev.lib.android.siframe.customizable._init._Config
import sidev.lib.universal.intfc.Inheritable
import sidev.lib.android.siframe.intfc.listener.OnBackPressedListener
import sidev.lib.android.siframe.intfc.lifecycle.rootbase.SimpleAbsActFragBase
import sidev.lib.android.siframe.intfc.lifecycle.rootbase.ViewModelBase
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.*
import sidev.lib.android.siframe.lifecycle.fragment.Frag
import sidev.lib.android.siframe.arch.presenter.Presenter
import sidev.lib.android.siframe.arch.presenter.PresenterDependent
import sidev.lib.android.siframe.arch.presenter.PresenterDependentCommon
import sidev.lib.android.siframe.arch.view.MviView
//import sidev.lib.android.siframe.presenter.Repository
//import sidev.lib.android.siframe.presenter.RepositoryCallback
import sidev.lib.android.siframe.tool.util._AppUtil
import sidev.lib.android.siframe.tool.`var`._SIF_Config
import sidev.lib.android.siframe.tool.util.`fun`.getRootView
import sidev.lib.android.siframe.tool.util.`fun`.loge

/**
 * Kelas dasar dalam framework yang digunakan sbg kelas Activity utama
 * sbg pengganti dari [AppCompatActivity].
 */
abstract class Act : AppCompatActivity(), Inheritable,
    SimpleAbsActFragBase,
    ViewModelBase, //Scr default, smua Activity pada framework ini menggunakan MVP dan MVVM.
                   //Knp kok gak bisa default MVP atau MVI?. Karena MVVM merupakan arsitektur bawaan
                   //framework Android
    PresenterDependentCommon<Presenter>, //Karena MVP maupun MVI sama-sama bergantung pada presenter.
//    MvpView,
//    MviView<State>,
//    PresenterCallback<Presenter>, //Ini memungkinkan Programmer untuk memilih arsitektur MVP. Repository adalah Presneter namun sudah lifecycle-aware.
    BackBtnBase {
    final override var isExpired: Boolean= false
        private set

    override var isInherited: Boolean= false
    override fun _configInheritable(){}

    override val lifecycleCtx: Context
        get() = this

    override val _prop_act: AppCompatActivity
        get() = this
    override val _sideBase_view: View
        get() = layoutView
    override val _sideBase_intent: Intent
        get() = intent
    override val _sideBase_ctx: Context
        get() = lifecycleCtx
    override val _sideBase_fm: FragmentManager
        get() = supportFragmentManager

    abstract override val layoutId: Int
/*
    override val actBackBtn: Activity
        get() = this
 */
    override var backBtnViewList= ArrayList<View>()
    override var onBackPressedListenerList: ArrayList<OnBackPressedListener>
        = ArrayList()

    override val styleId: Int
        get() = _Config.STYLE_APP
    override lateinit var layoutView: View
    open val isViewInitFirst= true

    override lateinit var _vmProvider: ViewModelProvider
    //    override val vmStoreOwner: ViewModelStoreOwner= this
//    override val lifecycleOwner: LifecycleOwner= this

    override var presenter: Presenter?= null
//    override var callbackCtx: Context?= this


    //    private val onDestroyListenerQueue= RunQueue<Any?, Unit_>()


    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        setStyle(this)
        super.onCreate(savedInstanceState)
        setContentView(layoutId)

//        lifecycle.addObserver(this)
//        Log.e("SimpleAbsAct", "onCreate isViewInitFirst= $isViewInitFirst name= ${this::class.java.simpleName}")
//        val v=  findViewById<View>(android.R.id.content).rootView
//        layoutView= v

        doWhenNotIherited {
            ___initRootBase(this, getRootView())
        }
        ___initSideBase()
/*
        if(isViewInitFirst){
            Log.e("SimpleAbsAct", "::layoutView.isInitialized ${::layoutView.isInitialized} name= ${this::class.java.simpleName}")
            __initView(v)
            _initView(v)
        }
 */
    }

    @CallSuper
    override fun onDestroy() {
        super.onDestroy()
        isExpired= true
        presenter= null
//        onDestroyListenerQueue.iterateRunQueue(null)
        Log.e("SimpleAbsAct", "Activity ${this::class.java.simpleName} is destroyed!!!")
    }

    private var frag: Fragment?= null
    private var isActBarAttached= false //agar attachActBarView() inisial dilakukan sekali
    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)
        frag= fragment
        if(this is MultipleActBarViewPagerBase<*>
            && isActBarViewFromFragment && !isActBarAttached){
            attachActBarView(0)
            attachActBarTitle(0)
            isActBarAttached= true
        }
    }
    override fun onResumeFragments() {
        super.onResumeFragments()
        if(this !is ViewPagerBase<*> //Karena frag.onActive() dilakukan oleh interface ViewPagerActBase
            && frag != null && frag is Frag){
            loge("onResumeFragments() onActive() caller")
            (frag as Frag).onActive(layoutView, this, 0) //pos di sini adalah untuk ViewPager.
        }
    }



    override fun ___initRootBase(vararg args: Any) {
        super<ViewModelBase>.___initRootBase(*args)
        presenter= if(this is MviView<*, *>) __initMviPresenter()
            else initPresenter()
        super<SimpleAbsActFragBase>.___initRootBase(*args)

        if(this is MviView<*, *>)
            restoreCurrentState()
    }

/*
    override fun ___initSideBase() {
        super<ViewModelBase>.___initSideBase()
        super<BackBtnBase>.___initSideBase()
    }
 */

    override fun <D> getIntentData(key: String, i: Intent?, default: D?): D {
        return if(i != null) super.getIntentData(key, i, default)
        else intent?.extras?.get(key) as D? ?: default as D
    }


    fun updateActBarCustomView(layoutId: Int){
        val actBarView= layoutInflater.inflate(layoutId, null, false)
        updateActBarCustomView(actBarView)
    }
    fun updateActBarCustomView(v: View){
//        if(supportActionBar().customView == null){
        val actBar= supportActionBar
        if(actBar != null){
//            val actBarView= layoutInflater.inflate(layoutId, null, false)
            actBar.customView= v
            actBar.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
            actBar.setDisplayShowCustomEnabled(true)
            actBar.setCustomView(v,
                ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT))

            (actBar.customView.parent as Toolbar).setContentInsetsAbsolute(0,0)
//            supportActionBar().hide()
//            supportActionBar()
        }
    }
    fun actBar(): View?{
        return supportActionBar?.customView
    }


    @CallSuper
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        onActResultListener?.onActResult(requestCode, resultCode, data)
    }

    var onActResultListener: OnActResultListener?= null
    interface OnActResultListener{
        fun onActResult(reqCode: Int, resCode: Int, data: Intent?)
    }
    fun setOnActResultListener(l: (reqCode: Int, resCode: Int, data: Intent?) -> Unit){
        onActResultListener= object : OnActResultListener {
            override fun onActResult(reqCode: Int, resCode: Int, data: Intent?) {
                l(reqCode, resCode, data)
            }
        }
    }

    @CallSuper
    override fun onBackPressed() {
        if(_SIF_Config.APP_VALID){
            if(!isBackPressedHandled())
                super.onBackPressed()
        } else
            _AppUtil.toHomeScreen(this)
    }


//    override fun onPresenterSucc(reqCode: String, resCode: Int, data: Map<String, Any>?) {}
//    override fun onPresenterFail(reqCode: String, resCode: Int, msg: String?, e: Exception?) {}
//    override fun render(state: State){}

    //    override fun onPresenterSucc(reqCode: String, resCode: Int, data: Map<String, Any>?) {}
//    override fun onPresenterFail(reqCode: String, resCode: Int, msg: String?, e: Exception?) {}

    /*
    private val onBackPressedListenerList= ArrayList<OnBackPressedListener>()
    interface OnBackPressedListener{
        fun onBackPressed_(): Boolean
    }
    fun addOnBackPressedListener(l: () -> Boolean){
        onBackPressedListenerList.add(object: OnBackPressedListener{
            override fun onBackPressed_(): Boolean {
                return l()
            }
        })
    }
    private fun iterateOnBackPressedListener(): Boolean{
        var bool= true
        for(l in onBackPressedListenerList)
            bool= bool && l.onBackPressed_()
        return bool
    }
 */
}