package sidev.lib.android.siframe.lifecycle.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.jetbrains.anko.support.v4.act
import sidev.lib.android.siframe._customizable._Config
import sidev.lib.android.siframe.arch.presenter.ArchPresenter
import sidev.lib.android.siframe.lifecycle.activity.Act
import sidev.lib.android.siframe.intfc.listener.OnViewCreatedListener
import sidev.lib.android.siframe.intfc.lifecycle.rootbase.ActFragBase
import sidev.lib.android.siframe.intfc.lifecycle.rootbase.ViewModelBase
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.base.LifecycleSideBase
import sidev.lib.android.siframe.arch.presenter.PresenterDependent
import sidev.lib.android.siframe.arch.view.AutoRestoreViewClient
import sidev.lib.android.siframe.arch.view.MviView
import sidev.lib.android.siframe.arch.view.MvvmView
import sidev.lib.android.siframe.intfc.lifecycle.InterruptableBase
import sidev.lib.android.siframe.intfc.lifecycle.LifecycleBase
import sidev.lib.android.siframe.intfc.lifecycle.rootbase.FragBase
import sidev.lib.android.siframe.tool.util.`fun`.loge
import sidev.lib.android.siframe.tool.util.`fun`.logew
import sidev.lib.universal.`fun`.*
import sidev.lib.universal.exception.IllegalStateExc

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

    override val _prop_ctx: Context
        get() = context!!
    final override var _prop_parentLifecycle: ActFragBase?= null
        private set
    final override var _prop_hierarchyOder: Int= 0
        private set

    val actSimple: Act?
        get() = activity as? Act ?: _prop_parentLifecycle as? Act
//    val actBarContentNavAct
//        get() = activity as SimpleAbsBarContentNavAct?
    override val styleId: Int
        get() = _Config.STYLE_APP
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

    //=========Obj Listener
    var onViewCreatedListener: ((View, Bundle?) -> Unit)?= null//OnViewCreatedListener?= null



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
        onViewCreatedListener?.invoke(view, savedInstanceState) //?.onViewCreated_(view, savedInstanceState)
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
            if(frag is Frag) frag.firstFragPageOnActivePosition= 0
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
        if(firstFragPageOnActivePosition.isNotNegative()){
            onActive(_prop_parentLifecycle?.layoutView, _prop_parentLifecycle, firstFragPageOnActivePosition)
            firstFragPageOnActivePosition= -1
        }
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
    }
// */

    override fun ___initSideBase() {}
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

    override fun <D> getIntentData(key: String, i: Intent?, default: D?): D {
        return if(i != null) super.getIntentData(key, i, default)
        else activity?.intent?.extras?.get(key) as D? ?: default as D
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