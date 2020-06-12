package sidev.lib.android.siframe.lifecycle.app

import android.app.Application
import sidev.lib.android.siframe.customizable._init._Config
import sidev.lib.android.siframe.tool.util.log.LogApp
import sidev.lib.android.siframe.tool.util.log.LogHP
import kotlin.system.exitProcess

open class BaseApp: Application(){
    lateinit var logHpError: LogHP
/*
    var currentAct: SimpleAbsAct?= null
        set(v){
            val caller= ThreadUtil.getCurrentCallerFunName()
            Log.e("BASE_APP", "caller= $caller")
            Log.e("BASE_APP", "_Constant.REG_ACT_FUN_REGISTERER_NAME= ${_Constant.REG_ACT_FUN_REGISTERER_NAME}")
            Log.e("BASE_APP", "_Constant.REG_FRAG_FUN_REGISTERER_NAME= ${_Constant.REG_FRAG_FUN_REGISTERER_NAME}")

            if(caller == _Constant.REG_ACT_FUN_REGISTERER_NAME
                || caller == _Constant.REG_FRAG_FUN_REGISTERER_NAME){
                field= v!!
                Log.e("BASE_APP", "v.classSimpleName() = ${v.classSimpleName()}")
                Log.e("BASE_APP", "currentAct MASUK!!!")
            } else
                throw IllegalAccessExc(BaseApp::class.java,
                    "currentAct tidak boleh diakses selain dari SimpleAbsActFragView.registerActiveAct()")
        }
 */

    override fun onCreate() {
        super.onCreate()
//        LogApp.log= BuildConfig.MODE_LOG

        if(_Config.LOG){
            logHpError= LogHP(this)
            logHpError.letakFolder("Error")

            if(_Config.DEBUG){
                Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
                    LogApp.e(throwable::class.java.simpleName, throwable.message, throwable)
                    try{
                        logHpError.printError(thread, throwable)
                        throwable.printStackTrace()
                    } catch(error: Exception){
                        LogApp.e(error::class.java.simpleName, error.message, error)
                        error.printStackTrace()
                    }
                    exitProcess(0)
                }
            }
        }
    }
/*
    //Sampe sini!!!
    fun registerAct(act: SimpleAbsAct){
        val caller= ThreadUtil.getCurrentCallerFunName()
        Log.e("BASE_APP", "caller= $caller")
        Log.e("BASE_APP", "_Constant.REG_ACT_FUN_REGISTERER_NAME= ${_Constant.REG_ACT_FUN_REGISTERER_NAME}")
        Log.e("BASE_APP", "_Constant.REG_FRAG_FUN_REGISTERER_NAME= ${_Constant.REG_FRAG_FUN_REGISTERER_NAME}")

        if(caller == _Constant.REG_ACT_FUN_REGISTERER_NAME
            || caller == _Constant.REG_FRAG_FUN_REGISTERER_NAME) {
            actStack.add(act)
            Log.e("BASE_APP", "v.classSimpleName() = ${act.classSimpleName()}")
            Log.e("BASE_APP", "currentAct MASUK!!!")
        } else
            throw IllegalAccessExc(BaseApp::class.java,
                "registerAct tidak boleh diakses selain dari SimpleAbsActFragView.registerActiveAct()")
    }
 */
}