package sidev.lib.android.siframe.lifecycle.fragment

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
import sidev.lib.android.siframe.customizable._init._ConfigBase
import sidev.lib.android.siframe.lifecycle.activity.SimpleAbsAct
import sidev.lib.android.siframe.lifecycle.activity.SimpleAbsBarContentNavAct
import sidev.lib.android.siframe.intfc.listener.OnViewCreatedListener
import sidev.lib.android.siframe.intfc.view.SimpleAbsActFragView
import sidev.lib.android.siframe.presenter.Presenter
import sidev.lib.android.siframe.presenter.PresenterCallback

/**
 * Kelas dasar dalam framework yang digunakan sbg Fragment sbg pengganti dari Fragment
 */
abstract class SimpleAbsFrag : Fragment(), SimpleAbsActFragView, PresenterCallback {
    val actSimple
        get() = activity as SimpleAbsAct?
    val actBarContentNavAct
        get() = activity as SimpleAbsBarContentNavAct?
    override val styleId: Int
        get() = _ConfigBase.STYLE_APP
    override lateinit var layoutView: View
    override val lifecycleCtx: Context
        get() = ctx
    lateinit var ctx: Context
        private set
    private var isContextInit= false
    val layoutInflaterSimple
        get() = LayoutInflater.from(callbackCtx)

    override var presenter: Presenter?= null
    override var callbackCtx: Context?= context
        set(v) {
            field= v ?: context
        }

    /**
     * Dipakai untuk judul pada TabLayout yang dipasang pada ViewPager
     */
    open val fragTitle= "Judul"

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
        return inflater.inflate(layoutId, container, false)
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if(!isContextInit) callbackCtx= context!!
        layoutView= view
        initView_int(view)
        initView(view)

        Log.e("SingleBoundProAct", "this class (${this::class.java.simpleName}) layoutView.ll_btn_container.visibility= View.VISIBLE ===MULAI===")
        listener_onViewCreated?.onViewCreated_(view, savedInstanceState)
    }

    @CallSuper
    override fun onDetach() {
        super.onDetach()
        Log.e("SimpleAbsFrag", "Fragment ${this::class.java.simpleName} is detached!!!")
    }

    override fun initView_int(layoutView: View) {
        super.initView_int(layoutView)
        presenter= initPresenter()
    }

    override fun <D> getIntentData(key: String, i: Intent?, default: D?): D {
        return if(i != null) super.getIntentData(key, i, default)
        else activity?.intent?.extras?.get(key) as D? ?: default as D
    }


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

    override fun onPresenterSucc(reqCode: String, resCode: Int, data: Map<String, Any>?) {}
    override fun onPresenterFail(reqCode: String, resCode: Int, msg: String?, e: Exception?) {}
}