package sidev.lib.android.siframe.lifecycle.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import sidev.lib.android.siframe.arch.presenter.*
import sidev.lib.android.siframe.intfc.listener.OnBackPressedListener
import sidev.lib.android.siframe.intfc.lifecycle.rootbase.ActFragBase
import sidev.lib.android.siframe.intfc.lifecycle.rootbase.ViewModelBase
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.*
import sidev.lib.android.siframe.arch.view.AutoRestoreViewClient
import sidev.lib.android.siframe.arch.view.MviView
import sidev.lib.android.siframe.arch.view.MvvmView
import sidev.lib.android.siframe.intfc.lifecycle.InterruptableBase
import sidev.lib.android.siframe.intfc.lifecycle.LifecycleBase
import sidev.lib.android.siframe.intfc.lifecycle.rootbase.FragBase
import sidev.lib.android.siframe.tool.util._AppUtil
import sidev.lib.android.siframe._val._SIF_Config
import sidev.lib.android.std.tool.util.`fun`.getRootView
import sidev.lib.android.std.tool.util.`fun`.loge
import sidev.lib.check.asNotNull
import sidev.lib.exception.IllegalStateExc

/**
 * Kelas dasar dalam framework yang digunakan sbg kelas Activity utama
 * sbg pengganti dari [AppCompatActivity].
 * Scr default semua kelas turunan Activity pada framework ini
 * menggunakan arsitektur MVVM, namun tidak meng-extend interface [MvvmView].
 */
abstract class Act : AppCompatActivity(), //Inheritable,
    ActFragBase,
    ViewModelBase, //Scr default, smua Activity pada framework ini menggunakan MVP dan MVVM.
                   //Knp kok gak bisa default MVP atau MVI?. Karena MVVM merupakan arsitektur bawaan
                   //framework Android
//    PresenterDependentCommon<Presenter>, //Karena MVP maupun MVI sama-sama bergantung pada presenter.
    PresenterDependent,
//    MvpView,
//    MviView<State>,
//    PresenterCallback<Presenter>, //Ini memungkinkan Programmer untuk memilih arsitektur MVP. Repository adalah Presneter namun sudah lifecycle-aware.
    BackBtnBase {
//    var isActivitySavedInstanceStateNull= true
//        private set

    final override var currentState: LifecycleBase.State= super<ActFragBase>.currentState
        internal set

    final override var isExpired: Boolean= false
        private set
    final override var isBusy: Boolean= false
    final override var busyOfWhat: String= InterruptableBase.DEFAULT_BUSY_OF_WHAT

//    override var isInherited: Boolean= false
//    override fun _configInheritable(){}

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
        get() = _SIF_Config.STYLE_APP
    final override lateinit var layoutView: View
    open val isViewInitFirst= true

    final override lateinit var _vmProvider: ViewModelProvider

    //    override val vmStoreOwner: ViewModelStoreOwner= this
//    override val lifecycleOwner: LifecycleOwner= this

    final override var presenter: ArchPresenter<*, *, *>?= null
    override var onRequestPermissionResultCallback: ActivityCompat.OnRequestPermissionsResultCallback?= null

    //    final override var presenter: Presenter?= null
//    override var callbackCtx: Context?= this


    //    private val onDestroyListenerQueue= RunQueue<Any?, Unit_>()


    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        loge("Activity ${this::class.simpleName} onCreate()")
//        isActivitySavedInstanceStateNull= savedInstanceState == null
        setStyle(this)
        super.onCreate(savedInstanceState)
        setContentView(layoutId)

//        lifecycle.addObserver(this)
//        Log.e("SimpleAbsAct", "onCreate isViewInitFirst= $isViewInitFirst name= ${this::class.java.simpleName}")
//        val v=  findViewById<View>(android.R.id.content).rootView
//        layoutView= v

//        doWhenNotIherited {
            ___initRootBase(this, getRootView())
//        }
        ___initSideBase()

        currentState= LifecycleBase.State.CREATED
/*
        if(this is ViewPagerBase<*>){
            if(vpFragList[0].currentState.no >= LifecycleBase.State.CREATED.no)
                vpFragList.firstOrNull()?.onActive(_prop_view, this, 0)
        }
 */
/*
        if(isViewInitFirst){
            Log.e("SimpleAbsAct", "::layoutView.isInitialized ${::layoutView.isInitialized} name= ${this::class.java.simpleName}")
            __initView(v)
            _initView(v)
        }
 */
    }

    override fun onStart() {
        loge("Activity ${this::class.simpleName} onStart()")
        super.onStart()
        currentState= LifecycleBase.State.STARTED
    }

    override fun onResume() {
        loge("Activity ${this::class.simpleName} onResume()")
        super.onResume()
        currentState= LifecycleBase.State.ACTIVE
    }

    override fun onPause() {
        loge("Activity ${this::class.simpleName} onPause()")
        super.onPause()
        currentState= LifecycleBase.State.PAUSED
    }

    @CallSuper
    override fun onDestroy() {
        loge("Activity ${this::class.simpleName} onDestroy()")
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
        loge("Activity ${this::class.simpleName} onAttachFragment() fragment: ${fragment::class}")
        super.onAttachFragment(fragment)
        frag= fragment
        if(this is MultipleActBarViewPagerBase<*>
            && isActBarViewFromFragment && !isActBarAttached){
            attachActBarView(0)
            attachActBarTitle(0)
            isActBarAttached= true
        }
        frag.asNotNull { frag: FragBase ->
            try{ frag.onLifecycleAttach(this) }
            catch (e: IllegalStateExc){
                loge("${this::class} fragment.onLifecycleAttach() IllegalStateExc diabaikan.")
            }
        }
    }
    @CallSuper
    override fun onResumeFragments() {
        loge("Activity ${this::class.simpleName} onResumeFragments()")
        super.onResumeFragments()
//        frag.asNotNull { frag: Frag -> frag.resolveOnActive(layoutView, this, 0) }
/*
        if(this !is ViewPagerBase<*> //Karena frag.onActive() dilakukan oleh interface ViewPagerActBase
            && frag != null && frag is Frag){
//            loge("onResumeFragments() onActive() caller")
            (frag as Frag).onActive(layoutView, this, 0) //pos di sini adalah untuk ViewPager.
        }
 */
    }



    override fun ___initRootBase(vararg args: Any) {
        super<ViewModelBase>.___initRootBase(*args)
        presenter= if(this is MviView<*, *, *>) __initMviPresenter() //as MviPresenter<ViewState, ViewIntent>
            else initPresenter()
        super<ActFragBase>.___initRootBase(*args)

        if(this is MviView<*, *, *>)
            restoreCurrentState(true)
    }

/*
    override fun ___initSideBase() {
        super<ViewModelBase>.___initSideBase()
        super<BackBtnBase>.___initSideBase()
    }
 */

    override fun <D> getIntentData(key: String, i: Intent?, default: D?): D {
        try{
            return if(i != null) super.getIntentData(key, i, default)
            else intent?.extras?.get(key) as? D ?: default as D
        } catch (e: ClassCastException){
            throw IllegalArgumentException("Tidak ada nilai dg key: $key pada intent: $i dan default == null")
        }

    }
    @JvmOverloads
    fun <D> getIntentData(key: String, default: D?= null): D =
        getIntentData(key, intent, default)

    override fun <T : View> findViewById(id: Int): T = super<AppCompatActivity>.findViewById(id)

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

    /**
     * Callback for the result from requesting permissions. This method
     * is invoked for every call on [.requestPermissions].
     *
     *
     * **Note:** It is possible that the permissions request interaction
     * with the user is interrupted. In this case you will receive empty permissions
     * and results arrays which should be treated as a cancellation.
     *
     *
     * @param requestCode The request code passed in [.requestPermissions].
     * @param permissions The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     * which is either [android.content.pm.PackageManager.PERMISSION_GRANTED]
     * or [android.content.pm.PackageManager.PERMISSION_DENIED]. Never null.
     *
     * @see .requestPermissions
     */
    @CallSuper
    final override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super<AppCompatActivity>.onRequestPermissionsResult(requestCode, permissions, grantResults)
        super<ActFragBase>.onRequestPermissionsResult(requestCode, permissions, grantResults)
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