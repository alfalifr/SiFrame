package sidev.lib.android.siframe.lifecycle.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import sidev.lib.android.siframe._customizable._Config
import sidev.lib.universal.intfc.Inheritable
import sidev.lib.android.siframe.intfc.listener.OnBackPressedListener
import sidev.lib.android.siframe.intfc.lifecycle.rootbase.ActFragBase
import sidev.lib.android.siframe.intfc.lifecycle.rootbase.ViewModelBase
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.*
import sidev.lib.android.siframe.lifecycle.fragment.Frag
import sidev.lib.android.siframe.arch.presenter.Presenter
import sidev.lib.android.siframe.arch.presenter.PresenterDependentCommon
import sidev.lib.android.siframe.arch.view.AutoRestoreViewClient
import sidev.lib.android.siframe.arch.view.MviView
import sidev.lib.android.siframe.arch.view.MvvmView
import sidev.lib.android.siframe.intfc.lifecycle.InterruptableBase
import sidev.lib.android.siframe.intfc.lifecycle.LifecycleBase
import sidev.lib.android.siframe.tool.util._AppUtil
import sidev.lib.android.siframe.tool.`var`._SIF_Config
import sidev.lib.android.siframe.tool.`var`._SIF_Constant
import sidev.lib.android.siframe.tool.util.`fun`.getRootView
import sidev.lib.android.siframe.tool.util.`fun`.loge

/**
 * Kelas dasar dalam framework yang digunakan sbg kelas Activity utama
 * sbg pengganti dari [AppCompatActivity].
 * Scr default semua kelas turunan Activity pada framework ini
 * menggunakan arsitektur MVVM, namun tidak meng-extend interface [MvvmView].
 */
abstract class Act : AppCompatActivity(), Inheritable,
    ActFragBase,
    ViewModelBase, //Scr default, smua Activity pada framework ini menggunakan MVP dan MVVM.
                   //Knp kok gak bisa default MVP atau MVI?. Karena MVVM merupakan arsitektur bawaan
                   //framework Android
    PresenterDependentCommon<Presenter>, //Karena MVP maupun MVI sama-sama bergantung pada presenter.
//    MvpView,
//    MviView<State>,
//    PresenterCallback<Presenter>, //Ini memungkinkan Programmer untuk memilih arsitektur MVP. Repository adalah Presneter namun sudah lifecycle-aware.
    BackBtnBase {
    var isActivitySavedInstanceStateNull= true
        private set

    final override var currentState: LifecycleBase.State= super<ActFragBase>.currentState
        internal set

    final override var isExpired: Boolean= false
        private set
    final override var isBusy: Boolean= false
    final override var busyOfWhat: String= InterruptableBase.DEFAULT_BUSY_OF_WHAT

    override var isInherited: Boolean= false
    override fun _configInheritable(){}
/*
    override val lifecycleCtx: Context
        get() = this
    override val _prop_ctx: Context
        get() = this
    override val _sideBase_ctx: Context
        get() = lifecycleCtx
 */

    final override val _prop_act: AppCompatActivity
        get() = this
    final override val _prop_ctx: Context
        get()= this
    final override val _prop_intent: Intent
        get() = intent
    final override val _prop_fm: FragmentManager
        get() = supportFragmentManager
    override val _prop_view: View
        get() = layoutView

    abstract override val layoutId: Int

    /*
    override val actBackBtn: Activity
        get() = this
 */
    final override var isHandlingBackBtn: Boolean= false
    override var isBackBtnHandledGradually: Boolean= super.isBackBtnHandledGradually
    final override var backBtnViewList= ArrayList<View>()
    final override var onBackPressedListenerList: ArrayList<OnBackPressedListener>
        = ArrayList()
    final override var removedOnBackPressedListenerList: ArrayList<OnBackPressedListener>
        = ArrayList()

    override val styleId: Int
        get() = _Config.STYLE_APP
    final override lateinit var layoutView: View
    open val isViewInitFirst= true

    final override lateinit var _vmProvider: ViewModelProvider
    //    override val vmStoreOwner: ViewModelStoreOwner= this
//    override val lifecycleOwner: LifecycleOwner= this

    final override var presenter: Presenter?= null
//    override var callbackCtx: Context?= this


    //    private val onDestroyListenerQueue= RunQueue<Any?, Unit_>()


    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        isActivitySavedInstanceStateNull= savedInstanceState == null
        setStyle(this)
        super.onCreate(savedInstanceState)
        setContentView(layoutId)

        if(!isActivitySavedInstanceStateNull && this is SingleFragActBase){
            //Anggapannya tidak mungkin fragment null karena sudah diinit di awal.
            fragment= supportFragmentManager.findFragmentByTag(_SIF_Constant.Internal.TAG_SINGLE_FRAG_ACT)!!
        }

//        lifecycle.addObserver(this)
//        Log.e("SimpleAbsAct", "onCreate isViewInitFirst= $isViewInitFirst name= ${this::class.java.simpleName}")
//        val v=  findViewById<View>(android.R.id.content).rootView
//        layoutView= v

        doWhenNotIherited {
            ___initRootBase(this, getRootView())
        }
        ___initSideBase()

        currentState= LifecycleBase.State.CREATED
/*
        if(isViewInitFirst){
            Log.e("SimpleAbsAct", "::layoutView.isInitialized ${::layoutView.isInitialized} name= ${this::class.java.simpleName}")
            __initView(v)
            _initView(v)
        }
 */
    }

    override fun onStart() {
        super.onStart()
        currentState= LifecycleBase.State.STARTED
    }

    override fun onResume() {
        super.onResume()
        currentState= LifecycleBase.State.ACTIVE
    }

    override fun onPause() {
        super.onPause()
        currentState= LifecycleBase.State.PAUSED
    }

    @CallSuper
    override fun onDestroy() {
        if(this is AutoRestoreViewClient)
            extractAllViewContent()

        super.onDestroy()
        isExpired= true
        presenter= null
        currentState= LifecycleBase.State.DESTROYED

//        onDestroyListenerQueue.iterateRunQueue(null)
        loge("Activity ${this::class.java.simpleName} is destroyed!!!")
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
//            loge("onResumeFragments() onActive() caller")
            (frag as Frag).onActive(layoutView, this, 0) //pos di sini adalah untuk ViewPager.
        }
    }



    override fun ___initRootBase(vararg args: Any) {
        super<ViewModelBase>.___initRootBase(*args)
        presenter= if(this is MviView<*, *>) __initMviPresenter()
            else initPresenter()
        super<ActFragBase>.___initRootBase(*args)

        if(this is MviView<*, *>)
            restoreCurrentState(true)
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