package sidev.lib.android.siframe.lifecycle.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import sidev.lib.`val`.SuppressLiteral
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
import sidev.lib.android.siframe.`val`._SIF_Config
import sidev.lib.android.siframe.`val`._SIF_Constant
import sidev.lib.android.siframe.intfc.listener.OnRequestPermissionsResultCallback
import sidev.lib.android.siframe.tool.util.`fun`.doOnce
//import sidev.lib.android.siframe.tool.util.`fun`.getExtra
import sidev.lib.android.std.tool.util.`fun`.getExtra
import sidev.lib.android.std.tool.util.`fun`.getRootView
import sidev.lib.android.std.tool.util.`fun`.loge
import sidev.lib.android.std.tool.util.`fun`.set
import sidev.lib.check.asNotNull
import sidev.lib.exception.IllegalStateExc
import kotlin.collections.ArrayList

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

    override val id: String
        get() = this::class.java.name
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

    /**
     * Isi pair Boolean `false` jika [OnRequestPermissionsResultCallback]
     * hanya dipanggil sekali lalu dihilangkan dari list.
     *
     */
    final override var onRequestPermissionResultCallbacks: Map<String, Pair<OnRequestPermissionsResultCallback, Boolean>>? = null
        private set

    override val styleId: Int
        get() = _styleId
    private var _styleId: Int = _SIF_Config.STYLE_APP
    final override lateinit var layoutView: View
    open val isViewInitFirst= true

    final override lateinit var _vmProvider: ViewModelProvider

    //    override val vmStoreOwner: ViewModelStoreOwner= this
//    override val lifecycleOwner: LifecycleOwner= this

    final override var presenter: ArchPresenter<*, *, *>?= null
    //override var onRequestPermissionResultCallback: ActivityCompat.OnRequestPermissionsResultCallback?= null

    //    final override var presenter: Presenter?= null
//    override var callbackCtx: Context?= this


    //    private val onDestroyListenerQueue= RunQueue<Any?, Unit_>()


    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        _initDataFromIntent(intent)
        _initData()

        loge("Activity ${this::class.simpleName} onCreate()")
//        isActivitySavedInstanceStateNull= savedInstanceState == null
        //initStyle(this)
        //Style hanya bisa diset sebelum `setContentView(layoutId)`.
        var styleId= styleId
        if(this is SingleFragActBase){
            __initFrag()
            fragment.asNotNull { frag: ActFragBase ->
                styleId = frag.styleId
            }
        }
        setTheme(styleId)

        super.onCreate(savedInstanceState)
        setContentView(layoutId)

//        lifecycle.addObserver(this)
//        Log.e("SimpleAbsAct", "onCreate isViewInitFirst= $isViewInitFirst name= ${this::class.java.simpleName}")
//        val v=  findViewById<View>(android.R.id.content).rootView
//        layoutView= v

//        doWhenNotIherited {
            ___initRootBase(this, getRootView(), false)
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

    override fun reinitView(){
/*
        val registerKey= this::class.java.name +"@" +this.hashCode() + _SIF_Constant.PROP_STACK
        doOnce(registerKey){
            registerActiveAct()
            _AppUtil.checkAppValidity(_prop_ctx)
        }
 */
        _initDataFromIntent(intent)
        _initData()
        __initViewFlow(layoutView)
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

    final override fun setStyle(styleId: Int) {
        setTheme(styleId)
        _styleId = styleId
    }

    /**
     * Same as [.startActivity] with no options
     * specified.
     *
     * @param intent The intent to start.
     *
     * @throws android.content.ActivityNotFoundException
     *
     * @see .startActivity
     * @see .startActivityForResult
     */
    override fun startActivity(intent: Intent?) {
        if(intent != null && intent.getExtra<Any?>(_SIF_Constant.CALLING_LIFECYCLE) == null)
            intent[_SIF_Constant.CALLING_LIFECYCLE]= this::class.java.name
        super.startActivity(intent)
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
        //super<ActFragBase>.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(onRequestPermissionResultCallbacks != null){
            val removedKey = mutableListOf<String>()
            for((key, c) in onRequestPermissionResultCallbacks!!){
                c.first.onRequestPermissionsResult(requestCode, permissions, grantResults)
                if(!c.second)
                    removedKey.add(key)
            }
            val mutC= onRequestPermissionResultCallbacks as MutableMap
            for(key in removedKey)
                mutC.remove(key)
        }
    }

    final override fun addOnRequestPermissionResultCallback(
        c: OnRequestPermissionsResultCallback,
        isPersistent: Boolean,
        replaceExisting: Boolean
    ): Boolean {
        if(onRequestPermissionResultCallbacks == null)
            onRequestPermissionResultCallbacks = mutableMapOf()
        return if(replaceExisting || !onRequestPermissionResultCallbacks!!.containsKey(c.tag)){
            (onRequestPermissionResultCallbacks as MutableMap)[c.tag] = c to isPersistent
            true
        } else false
    }

    final override fun addOnRequestPermissionResultCallback(
        key: String,
        isPersistent: Boolean,
        replaceExisting: Boolean,
        callback: (requestCode: Int, permissions: Array<out String>, grantResults: IntArray) -> Unit
    ): OnRequestPermissionsResultCallback? {
        if(onRequestPermissionResultCallbacks == null)
            onRequestPermissionResultCallbacks = mutableMapOf()
        val mutC= onRequestPermissionResultCallbacks as MutableMap
        @Suppress(SuppressLiteral.NAME_SHADOWING)
        val key = if(key.isBlank()) "_callback_${mutC.size}_" else key
        return if(replaceExisting || !onRequestPermissionResultCallbacks!!.containsKey(key)){
            val c= object: OnRequestPermissionsResultCallback {
                override val tag: String = key
                override fun onRequestPermissionsResult(
                    requestCode: Int,
                    permissions: Array<out String>,
                    grantResults: IntArray
                ) = callback.invoke(requestCode, permissions, grantResults)
            }
            mutC[c.tag] = c to isPersistent
            c
        } else null
    }

    final override fun removeOnRequestPermissionResultCallback(key: String): Boolean {
        if(onRequestPermissionResultCallbacks == null) return false
        val mutC= onRequestPermissionResultCallbacks as MutableMap
        val bool= mutC.remove(key) != null
        if(mutC.isEmpty())
            onRequestPermissionResultCallbacks = null
        return bool
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