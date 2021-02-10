package sidev.lib.android.std.lifecycle.app

import android.app.Application
import android.content.Context
import android.content.pm.ApplicationInfo
import sidev.lib.android.std.`val`._Config

//import sidev.lib.android.std._val._Config
//import sidev.lib.android.siframe.tool.util.log.LogApp
//import sidev.lib.android.siframe.tool.util.log.LogHP
//import kotlin.system.exitProcess

open class StdApp: Application(){
    companion object{
        /**
         * Harusnya aman jika menaruh contex app sbg static karena semua proses di app pastinya
         * didahului oleh instansiasi kelas Application.
         */
        lateinit var ctx: Context
            private set
    }

    /**
     * Called when the application is starting, before any activity, service,
     * or receiver objects (excluding content providers) have been created.
     *
     *
     * Implementations should be as quick as possible (for example using
     * lazy initialization of state) since the time spent in this function
     * directly impacts the performance of starting the first activity,
     * service, or receiver in a process.
     *
     *
     * If you override this method, be sure to call `super.onCreate()`.
     *
     *
     * Be aware that direct boot may also affect callback order on
     * Android [android.os.Build.VERSION_CODES.N] and later devices.
     * Until the user unlocks the device, only direct boot aware components are
     * allowed to run. You should consider that all direct boot unaware
     * components, including such [android.content.ContentProvider], are
     * disabled until user unlock happens, especially when component callback
     * order matters.
     */
    override fun onCreate() {
        super.onCreate()
        ctx= this

        val appInfo = packageManager.getApplicationInfo(packageName, 0)
        _Config.DEBUG = 0 != appInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE
    }
    /*
    var logHpError: LogHP?= null
        internal set
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
        ctx= this
//        LogApp.log= BuildConfig.MODE_LOG

        if(_Config.LOG_ON_FILE){
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
    }
 */
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