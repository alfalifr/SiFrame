package sidev.lib.android.siframe.tool.manager

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import sidev.lib.universal.exception.IllegalAccessExc
import sidev.lib.android.siframe.tool.ActLifecycleObs
import sidev.lib.android.siframe.tool.`var`._SIF_Constant
import sidev.lib.android.siframe.tool.util.`fun`.loge
import sidev.lib.universal.`fun`.className
import sidev.lib.universal.`fun`.copy
import sidev.lib.universal.tool.util.ThreadUtil
import java.lang.ClassCastException
import java.util.*

object ActManager {
    val REGISTER_ACT_TO_STACK= "register_act"
    val POP_STACK= "pop_stack"
    private val KEY_ACT_MANAGER= "_sif_internl_act_manager"

    private val actStack= Stack<Activity>()


    fun process(request: String, vararg params: Any){
        val callerFunName= ThreadUtil.getCurrentCallerFunName()
        loge("callerFunName= $callerFunName")

        if(callerFunName == _SIF_Constant.REG_ACT_FUN_REGISTERER_NAME
            || callerFunName == _SIF_Constant.REG_FRAG_FUN_REGISTERER_NAME){
            when(request){
                REGISTER_ACT_TO_STACK -> {
                    val act= params.first() as Activity
                    registerAct(act)
                }
                POP_STACK -> {
                    val act= params.first() as Activity
                    popStack(act)
                }
            }
        } else
            throw IllegalAccessExc(this::class.java,
                "process() tidak boleh diakses selain dari SimpleAbsActFragView.registerActiveAct()")
    }


    private fun registerAct(act: Activity){
        try{
            act as AppCompatActivity
            val obs= ActLifecycleObs(act)
            obs.onDestroyFun[KEY_ACT_MANAGER]= { key, lifecycle -> popStack(lifecycle as AppCompatActivity) }
            actStack.push(act)
        } catch (castExc: ClassCastException){
            loge("act tidak memiliki lifecycle sehingga tidak dapat diregister")
        }
    }

    private fun popStack(act: Activity){
        val topActName= actStack.peek().className()
        val actName= act.className()

        if(topActName == actName)
            actStack.pop()
    }

    fun getActStack(): Array<Activity> {
        return actStack.copy(true).toTypedArray()
    }

    fun peekStack(): Activity {
        return actStack.peek()
    }
}