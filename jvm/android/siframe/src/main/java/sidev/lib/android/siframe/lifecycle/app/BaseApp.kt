package sidev.lib.android.siframe.lifecycle.app

import android.app.Application
import sidev.lib.android.siframe.util.log.LogApp
import sidev.lib.android.siframe.util.log.LogHP
import kotlin.system.exitProcess

open class BaseApp: Application(){
    lateinit var logHpError: LogHP
    override fun onCreate() {
        super.onCreate()
//        LogApp.log= BuildConfig.MODE_LOG

        logHpError= LogHP(this)
        logHpError.letakFolder("Error")

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