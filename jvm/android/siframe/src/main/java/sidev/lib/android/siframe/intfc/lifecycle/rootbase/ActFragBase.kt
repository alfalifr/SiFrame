package sidev.lib.android.siframe.intfc.lifecycle.rootbase

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import androidx.annotation.CallSuper
import androidx.annotation.IdRes
import androidx.annotation.StyleRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import sidev.lib.android.siframe.intfc.`fun`.InitViewFun
import sidev.lib.android.siframe.intfc.lifecycle.FragmentHostBase
import sidev.lib.android.siframe.intfc.lifecycle.rootbase.base.LifecycleRootBase
import sidev.lib.android.siframe.lifecycle.activity.Act
import sidev.lib.android.siframe.`val`._SIF_Constant
import sidev.lib.android.siframe.intfc.lifecycle.IdLifecyleOwner
import sidev.lib.android.siframe.intfc.prop.*
import sidev.lib.android.siframe.tool.manager.ActManager
import sidev.lib.android.siframe.tool.util._AppUtil
import sidev.lib.android.siframe.tool.util.`fun`.doOnce
import sidev.lib.android.std.tool.util.`fun`.loge
import sidev.lib.check.notNull
import sidev.lib.exception.IllegalInheritanceExc
import sidev.lib.jvm.tool.util.ThreadUtil

/**
 * Interface dasar dari semua Activity dari Fragment yang ada pada framework ini.
 */
interface ActFragBase: IdLifecyleOwner, LifecycleRootBase, FragmentHostBase,
    OnRequestPermissionsResultCallbackProp, //ArchView,
    InitViewFun, CtxProp, IntentProp, HierarchyOrderProp {
//    val layoutId: Int
//    val styleId: Int
    val layoutView: View
    override val _prop_ctx: Context
    override val _prop_fm: FragmentManager
    override val _prop_intent: Intent?
    //override var onRequestPermissionResultCallback: ActivityCompat.OnRequestPermissionsResultCallback?

    //    val lifecycleCtx: Context

    /**
     * @param args berjumlah 1 yaitu:
     *      [0] => activity
     *      [1] => view
     */
    @CallSuper
    override fun ___initRootBase(vararg args: Any) {
        //val act= args[0] as Activity
        if(this !is Activity && this !is Fragment) throw IllegalInheritanceExc(
            ActFragBase::class, this::class,
            "Kelas `ActFragBase` hanya boleh di-implement oleh `Activity` atau `Fragment`"
        )
        //layoutView= args[0] as View
        val continueInitData= args.isEmpty() || args[0].let { it is Boolean && it }

        val registerKey= this::class.java.name +"@" +this.hashCode() + _SIF_Constant.PROP_STACK
        doOnce(registerKey){
            registerActiveAct()
            _AppUtil.checkAppValidity(_prop_ctx)
        }
        if(continueInitData){
            _prop_intent.notNull { _initData(it) }
            //_initData()
        }
        __initViewFlow(layoutView)
    }

    /**
     * Yg mengatur pemanggilan _initView() dan parameter yg dipass.
     */
    fun __initViewFlow(rootView: View){
        __initView(layoutView)
        _initView(layoutView)
    }

    //@CallSuper
    fun _initData(intent: Intent){}
    //fun _initData(){}

    fun _initView(){
//        loge("SimpleAbsActFragView", "Act/Frag ini (${this::class.java.simpleName}) initView() di panggil")
        if(layoutView != null)
            _initView(layoutView)
        else
            loge("Act/Frag ini (${this::class.java.simpleName} belum diinit)")
    }
    override fun _initView(layoutView: View)
    @CallSuper
    override fun __initView(layoutView: View){}

    override fun reinitView(){
        /*
        val registerKey= this::class.java.name +"@" +this.hashCode() + _SIF_Constant.PROP_STACK
        doOnce(registerKey){
            registerActiveAct()
            _AppUtil.checkAppValidity(_prop_ctx)
        }
         */
        //_initDataFromIntent(act.intent)
        _prop_intent.notNull { _initData(it) }
        __initViewFlow(layoutView)
    }
/*
    fun initStyle(act: Activity){
        act.setTheme(styleId)
    }
 */
    //TODO 4 Feb 2021 -> Buat mekanisme setStyle untuk recreate view, trutama saat ada fragment attach
    fun setStyle(@StyleRes styleId: Int){
        _prop_ctx.setTheme(styleId)
    }
    fun <D> getIntentData(key: String, i: Intent?= null, default: D?= null): D {
        try{ return i?.extras?.get(key) as? D ?: default as D }
        catch (e: ClassCastException){
            throw IllegalArgumentException("Tidak ada nilai dg key: $key pada intent: $i dan default == null")
        }
    }

    fun <V: View> findViewById(@IdRes id: Int): V = layoutView.findViewById(id)

    private fun registerActiveAct(){
        try{
            if(this is Act){
                _SIF_Constant.REG_ACT_FUN_REGISTERER_NAME= ThreadUtil.getCurrentFunName()
                ActManager.process(ActManager.REGISTER_ACT_TO_STACK, this)
//                val app= application as BaseApp
//                app.currentAct= this
            }
/*
            /**
            * Fragment gak didaftarkan karena memang yg berada di stack hanya Activity.
            * Jika memang stack digunakan untuk blokir, maka yg yg diblokir adalah activity-nya.
            */
            else if(this is SimpleAbsFrag){
                _SIF_Constant.REG_FRAG_FUN_REGISTERER_NAME= ThreadUtil.getCurrentFunName()
                ActManager.process(ActManager.REGISTER_ACT_TO_STACK, this.activity!!)
//                val app= context!!.applicationContext as BaseApp
//                app.currentAct= this.actSimple
            }
 */
        } catch (castExc: ClassCastException){}
    }
}