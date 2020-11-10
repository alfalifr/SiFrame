package sidev.lib.android.siframe.tool.util

import android.content.Context
import android.content.Intent
import android.view.View
import sidev.lib.android.siframe._val._SIF_Config


object _AppUtil{
    fun checkAppValidity(c: Context){
        if(!_SIF_Config.APP_VALID)
            _ActFragUtil.blockLayout(c)
    }

    fun blockApp(c: Context): View? {
        _SIF_Config.APP_VALID= false
        return _ActFragUtil.blockLayout(c)
    }

    fun openAppBlock(c: Context): Boolean {
        _SIF_Config.APP_VALID= true
        return _ActFragUtil.openLayoutBlock(c)
    }

    fun toHomeScreen(c: Context){
        val startMain = Intent(Intent.ACTION_MAIN)
        startMain.addCategory(Intent.CATEGORY_HOME)
        startMain.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        c.startActivity(startMain)
    }
}