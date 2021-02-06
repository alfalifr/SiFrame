package sidev.lib.android.siframe.lifecycle.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.jetbrains.anko.support.v4.act
import sidev.lib.`val`.SuppressLiteral
import sidev.lib.android.siframe.`val`._SIF_Config
import sidev.lib.android.siframe.arch.presenter.ArchPresenter
import sidev.lib.android.siframe.lifecycle.activity.Act
import sidev.lib.android.siframe.intfc.lifecycle.rootbase.ActFragBase
import sidev.lib.android.siframe.intfc.lifecycle.rootbase.ViewModelBase
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.base.LifecycleSideBase
import sidev.lib.android.siframe.arch.presenter.PresenterDependent
import sidev.lib.android.siframe.arch.view.AutoRestoreViewClient
import sidev.lib.android.siframe.arch.view.MviView
import sidev.lib.android.siframe.arch.view.MvvmView
import sidev.lib.android.siframe.intfc.lifecycle.InterruptableBase
import sidev.lib.android.siframe.intfc.lifecycle.LifecycleBase
import sidev.lib.android.siframe.intfc.lifecycle.LifecycleViewBase
import sidev.lib.android.siframe.intfc.lifecycle.rootbase.FragBase
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.ViewPagerBase
import sidev.lib.android.siframe.`val`._SIF_Constant
import sidev.lib.android.siframe.intfc.listener.OnRequestPermissionsResultCallback
import sidev.lib.android.std.tool.util.`fun`.loge
import sidev.lib.android.std.tool.util.`fun`.logew
import sidev.lib.check.asNotNull
import sidev.lib.check.asNotNullTo
import sidev.lib.exception.IllegalStateExc
import sidev.lib.jvm.tool.manager.ListenerManager
import sidev.lib.jvm.tool.manager.createSimpleListener
import sidev.lib.reflex.clazz

/**
 * Kelas dasar dalam framework yang digunakan sbg Fragment sbg pengganti dari [Fragment].
 * Scr default semua fragment pada framework ini menggunakan arsitektur MVVM,
 * namun tidak meng-extend interface [MvvmView].
 */
abstract class Frag : Fragment(),
    FragBase,
    ViewModelBase,
    PresenterDependent, //Ini memungkinkan Programmer untuk memilih arsitektur MVP. Repository adalah Presneter namun sudah lifecycle-aware.
    LifecycleSideBase // Hanya sbg cetaka agar kelas ini dapat memanggil ___initSideBase()
{ //RepositoryCallback {

    final override var currentState: LifecycleBase.State= super<FragBase>.currentState
        internal set
    internal var firstFragPageOnActivePosition: Int= -1
        set(v){
            field= v
            loge("clazz= ${this::class} firstFragPageOnActivePosition= $firstFragPageOnActivePosition")
        }

    final override var isExpired: Boolean= false
        private set
    final override var isBusy: Boolean= false
    final override var busyOfWhat: String= InterruptableBase.DEFAULT_BUSY_OF_WHAT

    override val _prop_fm: FragmentManager
        get() = childFragmentManager
    override val _prop_ctx: Context
        get() = context ?: throw NullPointerException("`context` masih blum di-init (null).")
    final override var _prop_parentLifecycle: ActFragBase?= null
        private set
    final override var _prop_hierarchyOder: Int= 0
        private set

    override val id: String
        get() = this::class.java.name

    val actSimple: Act?
        get() = activity as? Act ?: _prop_parentLifecycle as? Act
//    val actBarContentNavAct
//        get() = activity as SimpleAbsBarContentNavAct?
    override val styleId: Int
        get() = _styleId
    private var _styleId: Int = _SIF_Config.STYLE_APP
    final override lateinit var layoutView: View
/*
    override val lifecycleCtx: Context
        get() = context!!
 */
/*
    lateinit var ctx: Context
        private set
 */
    private var isContextInit= false
/*
    val layoutInflaterSimple
        get() = LayoutInflater.from(callbackCtx)
 */

    /*
    override var callbackCtx: Context?= null
        set(v) {
            field= v ?: context
        }
 */
    final override lateinit var _vmProvider: ViewModelProvider

    //    override val vmStoreOwner: ViewModelStoreOwner= this
//    override val lifecycleOwner: LifecycleOwner= this
    final override var presenter: ArchPresenter<*, *, *>?= null

    //    final override var presenter: Presenter?= null

    /**
     * Dipakai untuk judul pada TabLayout yang dipasang pada ViewPager
     */
    open val fragTitle= this::class.java.simpleName
    //override var onRequestPermissionResultCallback: ActivityCompat.OnRequestPermissionsResultCallback?= null
    /**
     * Isi pair Boolean `false` jika [OnRequestPermissionsResultCallback]
     * hanya dipanggil sekali lalu dihilangkan dari list.
     *
     */
    final override var onRequestPermissionResultCallbacks: Map<String, Pair<OnRequestPermissionsResultCallback, Boolean>>?= null
        private set

    //=========Obj Listener
//    var onViewCreatedListener: ((View, Bundle?) -> Unit)?= null//OnViewCreatedListener?= null
    val onViewCreatedListener: ListenerManager<Unit, Unit> by lazy { createSimpleListener() }
    fun addOnViewCreatedListener(tag: String= _SIF_Constant.Internal.TAG_DEFAULT_PREFIX,
                                 forceReplace: Boolean= true,
                                 func: (View, Bundle?) -> Unit): Boolean
            = onViewCreatedListener.addListener(tag, forceReplace){ func(it["view"] as View, it["bundle"] as? Bundle) }

    val onActiveListener: ListenerManager<Unit, Unit> by lazy { createSimpleListener() }
    fun addOnActiveListener(tag: String= _SIF_Constant.Internal.TAG_DEFAULT_PREFIX,
                            forceReplace: Boolean= true,
                            func: (view: View, parent: LifecycleViewBase?, pos: Int) -> Unit): Boolean
            = onActiveListener.addListener(tag, forceReplace){
        func(layoutView, it["parent"] as? LifecycleViewBase, it["pos"] as Int)
    }

    val onPostViewCreatedListener: ListenerManager<Unit, Unit> by lazy { createSimpleListener() }
    fun addOnPostViewCreatedListener(tag: String= _SIF_Constant.Internal.TAG_DEFAULT_PREFIX,
                                     forceReplace: Boolean= true,
                                     func: (view: View) -> Unit): Boolean
            = onPostViewCreatedListener.addListener(tag, forceReplace){ func(layoutView) }



    final override fun setStyle(styleId: Int) {
        _prop_ctx.setTheme(styleId)
        _styleId = styleId
    }

    fun inflateView(c: Context, container: ViewGroup?, savedInstanceState: Bundle?): View{
        val v= if(!::layoutView.isInitialized){
//            callbackCtx= c
            isContextInit= true
            val infl= LayoutInflater.from(c)
            val vInt= onCreateView(infl, container, savedInstanceState)!!
            onViewCreated(vInt, savedInstanceState)
            vInt
        } else layoutView
        return v
    }

    @CallSuper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        Log.e("SimpleAbsFrag", "Fragment ${this::class.java.simpleName} onCreateView!!!")
        return inflater.inflate(layoutId, container, false)
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loge("Fragment ${this::class.java.simpleName} onViewCreated!!!")
/*
        if(!isContextInit) callbackCtx= context!!
        val drawer= view.findViewByType<DrawerLayout>()
        val isDrawer= view is DrawerLayout

        loge("drawer == null => ${drawer == null} isDrawer= $isDrawer")
 */

        val act= try{ act }
        catch (e: Exception){ if(_prop_parentLifecycle is Activity) _prop_parentLifecycle else null } as? Activity
            ?: throw IllegalStateException("Fragment: \"${this::class.qualifiedName}\" tidak dapat di-init karena activity null. \nUntuk mengakomodasikan activity, pass nilai activity pada \"_prop_parentLifecycle\".")

        ___initRootBase(act, view)
        ___initSideBase()
/*
        layoutView= view
        __initView(view)
        _initView(view)
 */
//        Log.e("SingleBoundProAct", "this class (${this::class.java.simpleName}) layoutView.ll_btn_container.visibility= View.VISIBLE ===MULAI===")
        currentState= LifecycleBase.State.CREATED
//        onViewCreatedListener?.invoke(view, savedInstanceState) //?.onViewCreated_(view, savedInstanceState)
        onViewCreatedListener.iterateListeners(
            "view" to view,
            "bundle" to savedInstanceState
        )
/*
        if(this is ViewPagerBase<*>){
            if(vpFragList[0].currentState.no >= LifecycleBase.State.CREATED.no)
                vpFragList.firstOrNull()?.onActive(_prop_view, this, 0)
        }
 */
    }

    override fun onAttach(context: Context) {
        loge("Fragment ${this::class.simpleName} onAttach(ctx) context: ${context::class}")
        super.onAttach(context)
        loge("Fragment ${this::class.java.simpleName} is attached to context!!!")
    }
    override fun onAttach(act: Activity) {
        loge("Fragment ${this::class.simpleName} onAttach(act) activity: ${act::class}")
        super.onAttach(act)
        loge("Fragment ${this::class.java.simpleName} is attached to activity!!!")
    }
    @CallSuper
    override fun onAttachFragment(childFragment: Fragment) {
        loge("Fragment ${this::class.simpleName} onAttachFragment() childFrag: ${childFragment::class}")
        super.onAttachFragment(childFragment)
        childFragment.asNotNull { frag: FragBase ->
            frag.onLifecycleAttach(this)
//            if(frag is Frag) frag.firstFragPageOnActivePosition= 0
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        loge("Fragment ${this::class.simpleName} onCreate()")
        //TODO <29 Juli 2020> => Sementara ini msh peringatan pada log, ke depannya mungkin akan throw exception.
        if(_prop_parentLifecycle == null)
            logew("_prop_parentLifecycle == null, lifecycle dapat menjadi tidak stabil!!!")
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        loge("Fragment ${this::class.simpleName} onStart()")
        super.onStart()
        currentState= LifecycleBase.State.STARTED
    }

    override fun onResume() {
        loge("Fragment ${this::class.simpleName} onResume()")
        super.onResume()
        currentState= LifecycleBase.State.ACTIVE
        resolveOnActive(_prop_parentLifecycle?.layoutView, _prop_parentLifecycle, firstFragPageOnActivePosition)

        /*Karena vpAdp.behavior == BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        if(firstFragPageOnActivePosition.isNotNegative()){
            resolveOnActive(_prop_parentLifecycle?.layoutView, _prop_parentLifecycle, firstFragPageOnActivePosition)
            firstFragPageOnActivePosition= -1
        }
         */
    }

    override fun onPause() {
        loge("Fragment ${this::class.simpleName} onPause()")
        super.onPause()
        currentState= LifecycleBase.State.PAUSED
    }

    @CallSuper
    override fun onDetach() {
        loge("Fragment ${this::class.simpleName} onDetach()")
        super.onDetach()
        onLifecycleDetach()
        loge("Fragment ${this::class.java.simpleName} is detached!!!")
    }

    override fun onDestroy() {
        loge("Fragment ${this::class.simpleName} onDestroy()")
        if(this is AutoRestoreViewClient)
            extractAllViewContent()

        super.onDestroy()
        isExpired= true
        presenter= null
        currentState= LifecycleBase.State.DESTROYED

        loge("Fragment ${this::class.java.simpleName} is destroyed!!!")
    }
/*
    override fun ___initRootBase(vararg args: Any) {
        super<ViewModelBase>.___initRootBase(*args)
        loge("presenter ___initRootBase SELESAI")
        super<SimpleAbsActFragBase>.___initRootBase(*args)
        loge("___initRootBase SELESAI")
    }
 */
///*
    override fun ___initRootBase(vararg args: Any) {
        super<ViewModelBase>.___initRootBase(*args)

        presenter= if(this is MviView<*, *, *>) __initMviPresenter()
            else initPresenter()

        super<FragBase>.___initRootBase(*args)

        if(this is MviView<*, *, *>)
            restoreCurrentState(true)

        onPostViewCreatedListener.iterateListeners()
    }
// */

    override fun ___initSideBase() {}

    override fun reinitView(){
/*
        val registerKey= this::class.java.name +"@" +this.hashCode() + _SIF_Constant.PROP_STACK
        doOnce(registerKey){
            registerActiveAct()
            _AppUtil.checkAppValidity(_prop_ctx)
        }
 */
        _initDataFromIntent(act.intent)
        _initData()
        __initViewFlow(layoutView)
    }
/*
    override fun __initView(layoutView: View) {
        super.__initView(layoutView)
    presenter= if(this is MviView<*, *>) __initMviPresenter()
    else initPresenter()

    loge("presenter ___initRootBase RESTORE MULAI")
    if(this is MviView<*, *>)
        restoreCurrentState()
    loge("presenter ___initRootBase RESTORE SELESAI")
    }
 */

    /**
     * Fungsi ini mengembalikan instance [ViewModel] dari activity.
     * Jika activity == null, maka instance yg dikembalikan adalah instance [ViewModel]
     * dari fragment ini.
     */
    override fun <T : ViewModel> getViewModel(cls: Class<T>): T {
        return activity.asNotNullTo { act: ViewModelBase -> act.getViewModel(cls) }
            ?: _vmProvider[cls]
    }
/*
    override fun <T : FiewModel> getViewModelFromAct(cls: Class<T>): T? {
        return activity.asNotNullTo { act: ViewModelBase -> act.getViewModel(cls) }
    }
 */
//    @JvmOverloads
    override fun <D> getIntentData(key: String, i: Intent?, default: D?): D {
        try{
            return if(i != null) super.getIntentData(key, i, default)
            else activity?.intent?.extras?.get(key) as? D ?: default as D
        } catch (e: ClassCastException){
            throw IllegalArgumentException("Tidak ada nilai dg key: $key pada intent: $i dan default == null")
        }
    }
    @JvmOverloads
    fun <D> getIntentData(key: String, default: D?= null): D =
        getIntentData(key, activity?.intent, default)
/*
    override fun onActive(parentView: View?, callingLifecycle: LifecycleViewBase?, pos: Int) {
        super.onActive(parentView, callingLifecycle, pos)
        onActiveListener.iterateListeners(
            "parent" to callingLifecycle,
            "pos" to pos
        )
    }
 */
    @CallSuper
    open fun onPostActive(parentView: View?, callingLifecycle: LifecycleViewBase?, pos: Int) {}

    internal fun resolveOnActive(parentView: View?, callingLifecycle: LifecycleViewBase?, pos: Int) {
        loge("resolveOnActive() parent= $callingLifecycle pos= $pos")
        onActive(parentView, callingLifecycle, pos)
        onPostActive(parentView, callingLifecycle, pos)
        onActiveListener.iterateListeners(
            "parent" to callingLifecycle,
            "pos" to pos
        )
        if(this !is ViewPagerBase<*>)
            childFragmentManager.fragments.forEach {
                it.asNotNull { frag: Frag -> frag.resolveOnActive(layoutView, this, 0) }
                    ?: it.asNotNull { frag: FragBase -> frag.onActive(layoutView, this, 0) }
            }
    }

    /*
        override fun onActive(parentView: View?, callingLifecycle: LifecycleViewBase?, pos: Int){
            super.onActive(parentView, callingLifecycle, pos)
            loge("callingLifecycle= ${callingLifecycle?.classSimpleName()}")
            _prop_parentLifecycle= callingLifecycle.asNotNullTo { parent: SimpleActFragBase ->
    //            loge("parent= ${parent::class.java.simpleName} is SimpleActFragBase")
                parent
            }
        }
    // */

    override fun onLifecycleAttach(callingLifecycle: ActFragBase) {
        super.onLifecycleAttach(callingLifecycle)
        if(callingLifecycle == _prop_parentLifecycle) return

        loge("Fragment: ${this::class} is attached to lifecycle: ${callingLifecycle.clazz}")
        if(callingLifecycle._prop_hierarchyOder >= _prop_hierarchyOder){ //Berguna agar _prop_parentLifecycle yg di-attach bkn merupakan parent yg jauh scr hirarki.
                // Pengecekan berfungsi agar _prop_parentLifecycle yg di-attach tetap parent scr langsung pada hirarki.
            _prop_parentLifecycle= callingLifecycle
            _prop_hierarchyOder= callingLifecycle._prop_hierarchyOder +1
        } else
            throw IllegalStateExc(this::class, callingLifecycle::class,
                "_prop_hierarchyOder = ${callingLifecycle._prop_hierarchyOder}",
                "_prop_hierarchyOder >= $_prop_hierarchyOder",
                "onLifecycleAttach() -> panggil onLifecycleDetach() agar ${this::class} lepas dari lifecycle sebelumnya (${_prop_parentLifecycle?.clazz})")
    }
    override fun onLifecycleDetach(){
        super.onLifecycleDetach()
        _prop_parentLifecycle= null
        _prop_hierarchyOder= 0
    }

    /**
     * Receive the result from a previous call to
     * [.startActivityForResult].  This follows the
     * related Activity API as described there in
     * [Activity.onActivityResult].
     *
     * @param requestCode The integer request code originally supplied to
     * startActivityForResult(), allowing you to identify who this
     * result came from.
     * @param resultCode The integer result code returned by the child activity
     * through its setResult().
     * @param data An Intent, which can return result data to the caller
     * (various data can be attached to Intent "extras").
     */
    @CallSuper
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        onActResultListener?.onActResult(requestCode, resultCode, data)
    }

    var onActResultListener: Act.OnActResultListener?= null

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
        //super<AppCompatActivity>.onRequestPermissionsResult(requestCode, permissions, grantResults)
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

        /*
        fun actSimple(func: (actSimple: SimpleAbsAct) -> Unit){
            if(actSimple != null)
                func(actSimple!!)
        }
        fun actBarContentNavAct(func: (actBarContentNavAct: SimpleAbsBarContentNavAct) -> Unit){
            if(actBarContentNavAct != null)
                func(actBarContentNavAct!!)
        }
        fun activity(func: (activity: FragmentActivity) -> Unit){
            if(activity != null)
                func(activity!!)
        }
     */

//    override fun onPresenterSucc(reqCode: String, resCode: Int, data: Map<String, Any>?) {}
//    override fun onPresenterFail(reqCode: String, resCode: Int, msg: String?, e: Exception?) {}
}