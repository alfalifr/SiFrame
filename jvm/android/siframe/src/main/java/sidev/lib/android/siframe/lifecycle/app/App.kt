package sidev.lib.android.siframe.lifecycle.app

import android.content.Context
import sidev.lib.android.siframe.tool.util.log.LogApp
import sidev.lib.android.siframe.tool.util.log.LogHP
import sidev.lib.android.std.`val`._Config
import sidev.lib.android.std.lifecycle.app.StdApp
import sidev.lib.android.std.tool.util.`fun`.loge
import kotlin.system.exitProcess

open class App: StdApp(){
    companion object{
        /**
         * Harusnya aman jika menaruh contex app sbg static karena semua proses di app pastinya
         * didahului oleh instansiasi kelas Application.
         */
        val ctx: Context
            get()= StdApp.ctx
    }
    private var uncaughtExceptionHandler: Thread.UncaughtExceptionHandler?= null
    protected var logHpError: LogHP?= null
    var logOnFileActive: Boolean = _Config.LOG_ON_FILE
        set(v){
            _Config.LOG_ON_FILE = v
            if(v){
                if(logHpError == null){
                    logHpError= LogHP(this)
                    logHpError!!.letakFolder("Error")
//            loge("logHpError.alamatFile= ${logHpError.alamatFile}")
                    uncaughtExceptionHandler = Thread.UncaughtExceptionHandler { thread, throwable ->
                        LogApp.e(throwable::class.java.simpleName, throwable.message, throwable)
                        try{
                            logHpError!!.printError(thread, throwable)
                            throwable.printStackTrace()
                        } catch(error: Exception){
                            LogApp.e(error::class.java.simpleName, error.message, error)
                            error.printStackTrace()
                        }
                        exitProcess(1)
                    }
                }
//            if(_Config.DEBUG){ -> tidak perlu semenjak _Config.LOG_ON_FILE juga membutuhkan _Config.DEBUG
                Thread.setDefaultUncaughtExceptionHandler(uncaughtExceptionHandler)
            } else {
                logHpError= null
                if(Thread.getDefaultUncaughtExceptionHandler() == uncaughtExceptionHandler)
                    Thread.setDefaultUncaughtExceptionHandler(null)
                uncaughtExceptionHandler= null
            }
            field = v
        }
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
        logOnFileActive = _Config.LOG_ON_FILE
        LogApp.log = _Config.LOG
//        ctx= this
//        LogApp.log= BuildConfig.MODE_LOG
/*
        if(logOnFileActive){
            logHpError= LogHP(this)
            logHpError!!.letakFolder("Error")
//            loge("logHpError.alamatFile= ${logHpError.alamatFile}")

//            if(_Config.DEBUG){ -> tidak perlu semenjak _Config.LOG_ON_FILE juga membutuhkan _Config.DEBUG
            Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
                LogApp.e(throwable::class.java.simpleName, throwable.message, throwable)
                try{
                    logHpError!!.printError(thread, throwable)
                    throwable.printStackTrace()
                } catch(error: Exception){
                    LogApp.e(error::class.java.simpleName, error.message, error)
                    error.printStackTrace()
                }
                exitProcess(1)
            }
//            }
        }
 */
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