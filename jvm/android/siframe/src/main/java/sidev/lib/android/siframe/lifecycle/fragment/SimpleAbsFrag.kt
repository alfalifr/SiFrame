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
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import org.jetbrains.anko.support.v4.act
import sidev.lib.android.siframe.customizable._init._Config
import sidev.lib.android.siframe.lifecycle.activity.SimpleAbsAct
import sidev.lib.android.siframe.lifecycle.activity.SimpleAbsBarContentNavAct
import sidev.lib.android.siframe.intfc.listener.OnViewCreatedListener
import sidev.lib.android.siframe.intfc.lifecycle.rootbase.SimpleAbsActFragBase
import sidev.lib.android.siframe.presenter.Presenter
import sidev.lib.android.siframe.presenter.PresenterCallback

/**
 * Kelas dasar dalam framework yang digunakan sbg Fragment sbg pengganti dari Fragment
 */
abstract class SimpleAbsFrag : Fragment(),
    SimpleAbsActFragBase, PresenterCallback {
    val actSimple
        get() = activity as SimpleAbsAct?
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

    override var presenter: Presenter?= null
    override var callbackCtx: Context?= null
        set(v) {
            field= v ?: context
        }

    /**
     * Dipakai untuk judul pada TabLayout yang dipasang pada ViewPager
     */
    open val fragTitle= this::class.java.simpleName

    //=========Obj Listener
    var listener_onViewCreated: OnViewCreatedListener?= null

    fun inflateView(c: Context, container: ViewGroup?, savedInstanceState: Bundle?): View{
        val v= if(!::layoutView.isInitialized){
            callbackCtx= c
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
        Log.e("SimpleAbsFrag", "Fragment ${this::class.java.simpleName} onCreateView!!!")
        return inflater.inflate(layoutId, container, false)
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.e("SimpleAbsFrag", "Fragment ${this::class.java.simpleName} onViewCreated!!!")
        if(!isContextInit) callbackCtx= context!!
        ___initRootBase(act, view)
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

    override fun __initView(layoutView: View) {
        super.__initView(layoutView)
        presenter= initPresenter()
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
    fun onActive(parentView: View, pos: Int){}

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

    override fun onPresenterSucc(reqCode: String, resCode: Int, data: Map<String, Any>?) {}
    override fun onPresenterFail(reqCode: String, resCode: Int, msg: String?, e: Exception?) {}
}