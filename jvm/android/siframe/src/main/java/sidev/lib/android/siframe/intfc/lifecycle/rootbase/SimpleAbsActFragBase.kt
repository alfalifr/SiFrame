package sidev.lib.android.siframe.intfc.lifecycle.rootbase

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import androidx.annotation.CallSuper
import sidev.lib.android.siframe.intfc.lifecycle.`fun`.initViewFun
import sidev.lib.android.siframe.intfc.lifecycle.rootbase.base.LifecycleRootBase
import sidev.lib.android.siframe.lifecycle.activity.SimpleAbsAct
import sidev.lib.android.siframe.tool.`var`._SIF_Constant
import sidev.lib.android.siframe.tool.manager.ActManager
import sidev.lib.android.siframe.tool.util._AppUtil
import sidev.lib.android.siframe.tool.util.`fun`.doOnce
import sidev.lib.universal.tool.util.ThreadUtil

/**
 * Interface dasar dari semua Activity dari Fragment yang ada pada framework ini.
 */
interface SimpleAbsActFragBase: LifecycleRootBase, initViewFun{
//    val layoutId: Int
//    val styleId: Int
    var layoutView: View
    val lifecycleCtx: Context

    /**
     * @param args berjumlah 1 yaitu:
     *      [0] => activity
     *      [1] => view
     */
    @CallSuper
    override fun ___initRootBase(vararg args: Any) {
        val act= args[0] as Activity
        layoutView= args[1] as View

        val registerKey= this::class.java.name +"@" +this.hashCode() +_SIF_Constant.PROP_STACK
        doOnce(registerKey){
            registerActiveAct()
            _AppUtil.checkAppValidity(lifecycleCtx)
        }
        __initViewFlow(layoutView)
        _initDataFromIntent(act.intent)
    }

    /**
     * Yg mengatur pemanggilan _initView() dan parameter yg dipass.
     */
    fun __initViewFlow(rootView: View){
        __initView(layoutView)
        _initView(layoutView)
    }

    @CallSuper
    fun _initDataFromIntent(intent: Intent){}

    fun _initView(){
        Log.e("SimpleAbsActFragView", "Act/Frag ini (${this::class.java.simpleName}) initView() di panggil")
        if(layoutView != null)
            _initView(layoutView)
        else
            Log.e("SimpleAbsActFragView", "Act/Frag ini (${this::class.java.simpleName} belum diinit)")
    }
    override fun _initView(layoutView: View)
    @CallSuper
    override fun __initView(layoutView: View){}

    fun setStyle(act: Activity){
        act.setTheme(styleId)
    }
    fun <D> getIntentData(key: String, i: Intent?= null, default: D?= null): D{
        return i?.extras?.get(key) as D? ?: default as D
    }

    private fun registerActiveAct(){
        try{
            if(this is SimpleAbsAct){
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