package sidev.lib.android.siframe.lifecycle.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import org.jetbrains.anko.support.v4.act
import sidev.lib.android.siframe.customizable._init._Config
import sidev.lib.android.siframe.intfc.lifecycle.LifecycleViewBase
import sidev.lib.android.siframe.lifecycle.activity.Act
import sidev.lib.android.siframe.intfc.listener.OnViewCreatedListener
import sidev.lib.android.siframe.intfc.lifecycle.rootbase.SimpleAbsActFragBase
import sidev.lib.android.siframe.intfc.lifecycle.rootbase.ViewModelBase
import sidev.lib.android.siframe.intfc.lifecycle.rootbase.ViewModelFragBase
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.base.LifecycleSideBase
import sidev.lib.android.siframe.arch.viewmodel.FiewModel
import sidev.lib.android.siframe.arch.presenter.Presenter
import sidev.lib.android.siframe.arch.presenter.PresenterCallback
import sidev.lib.android.siframe.arch.presenter.PresenterDependent
import sidev.lib.android.siframe.arch.presenter.PresenterDependentCommon
import sidev.lib.android.siframe.arch.view.MviView
import sidev.lib.android.siframe.tool.util.`fun`.findViewByType
import sidev.lib.android.siframe.tool.util.`fun`.loge
import sidev.lib.universal.`fun`.asNotNullTo

//import sidev.lib.android.siframe.presenter.RepositoryCallback

/**
 * Kelas dasar dalam framework yang digunakan sbg Fragment sbg pengganti dari [Fragment].
 */
abstract class Frag : Fragment(),
    SimpleAbsActFragBase,
    ViewModelFragBase,
    PresenterDependentCommon<Presenter>, //Ini memungkinkan Programmer untuk memilih arsitektur MVP. Repository adalah Presneter namun sudah lifecycle-aware.
    LifecycleSideBase // Hanya sbg cetaka agar kelas ini dapat memanggil ___initSideBase()
    { //RepositoryCallback {

    final override var isExpired: Boolean= false
        private set

    val actSimple
        get() = activity as Act?
//    val actBarContentNavAct
//        get() = activity as SimpleAbsBarContentNavAct?
    override val styleId: Int
        get() = _Config.STYLE_APP
    override lateinit var layoutView: View
    override val lifecycleCtx: Context
        get() = context!!
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
    override lateinit var _vmProvider: ViewModelProvider
    //    override val vmStoreOwner: ViewModelStoreOwner= this
//    override val lifecycleOwner: LifecycleOwner= this

    override var presenter: Presenter?= null

    /**
     * Dipakai untuk judul pada TabLayout yang dipasang pada ViewPager
     */
    open val fragTitle= this::class.java.simpleName

    //=========Obj Listener
    var listener_onViewCreated: OnViewCreatedListener?= null



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
        loge("layoutId == _Config.LAYOUT_DL => ${layoutId == _Config.LAYOUT_DL}")
        return inflater.inflate(layoutId, container, false)
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.e("SimpleAbsFrag", "Fragment ${this::class.java.simpleName} onViewCreated!!!")
//        if(!isContextInit) callbackCtx= context!!
        val drawer= view.findViewByType<DrawerLayout>()
        val isDrawer= view is DrawerLayout

        loge("drawer == null => ${drawer == null} isDrawer= $isDrawer")

        ___initRootBase(act, view)
        ___initSideBase()
/*
        layoutView= view
        __initView(view)
        _initView(view)
 */
//        Log.e("SingleBoundProAct", "this class (${this::class.java.simpleName}) layoutView.ll_btn_container.visibility= View.VISIBLE ===MULAI===")
        listener_onViewCreated?.onViewCreated_(view, savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.e("SimpleAbsFrag", "Fragment ${this::class.java.simpleName} is attached to context!!!")
    }
    override fun onAttach(act: Activity) {
        super.onAttach(act)
        Log.e("SimpleAbsFrag", "Fragment ${this::class.java.simpleName} is attached to activity!!!")
    }

    @CallSuper
    override fun onDetach() {
        super.onDetach()
        Log.e("SimpleAbsFrag", "Fragment ${this::class.java.simpleName} is detached!!!")
    }

    override fun onDestroy() {
        super.onDestroy()
        isExpired= true
        presenter= null
    }

    override fun ___initRootBase(vararg args: Any) {
        super<ViewModelFragBase>.___initRootBase(*args)
        loge("presenter ___initRootBase SELESAI")
        super<SimpleAbsActFragBase>.___initRootBase(*args)
        loge("___initRootBase SELESAI")
    }
/*
    override fun ___initRootBase(vararg args: Any) {
        super<ViewModelFragBase>.___initRootBase(*args)
        presenter= if(this is MviView<*, *>) __initMviPresenter()
            else initPresenter()
        loge("presenter ___initRootBase SELESAI")
        super<SimpleAbsActFragBase>.___initRootBase(*args)

        loge("presenter ___initRootBase RESTORE MULAI")
        if(this is MviView<*, *>)
            restoreCurrentState()
        loge("presenter ___initRootBase RESTORE SELESAI")
    }
 */

    override fun ___initSideBase() {}
///*
    override fun __initView(layoutView: View) {
        super.__initView(layoutView)
    presenter= if(this is MviView<*, *>) __initMviPresenter()
    else initPresenter()

    loge("presenter ___initRootBase RESTORE MULAI")
    if(this is MviView<*, *>)
        restoreCurrentState()
    loge("presenter ___initRootBase RESTORE SELESAI")
    }
// */


    override fun <T : FiewModel> getViewModelFromAct(cls: Class<T>): T? {
        return activity.asNotNullTo { act: ViewModelBase -> act.getViewModel(cls) }
    }

    override fun <D> getIntentData(key: String, i: Intent?, default: D?): D {
        return if(i != null) super.getIntentData(key, i, default)
        else activity?.intent?.extras?.get(key) as D? ?: default as D
    }

    /**
     * Dipanggil saat fragment terlihat di screen.
     * Untuk case ViewPager, fungsi ini dipanggil setiap kali penggunamembuka halaman fragment ini.
     *
     * @param parentView merupakan view tempat fragment ini menempel.
     */
    open fun onActive(parentView: View, callingLifecycle: LifecycleViewBase?, pos: Int){}

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