package sidev.lib.android.siframe.tool.util.`fun`

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import sidev.lib.android.siframe.tool.util._BitmapUtil
import sidev.lib.android.siframe.lifecycle.activity.SingleFragAct_Simple
import sidev.lib.android.siframe.tool.`var`._SIF_Constant
import java.lang.Exception



fun Context.getFM(): FragmentManager? {
    return when(this){
        is AppCompatActivity -> this.supportFragmentManager
        is FragmentActivity -> this.supportFragmentManager
        else -> null
    }
}

fun Context.toast(msg: String, length: Int= Toast.LENGTH_LONG){
    Toast.makeText(this, msg, length).show()
}
fun Fragment.toast(msg: String, length: Int= Toast.LENGTH_LONG){
    context?.toast(msg, length)
}

fun Activity.commitFrag(@IdRes fragContainerId: Int, fragment: Fragment){
    when(this){
        is AppCompatActivity -> {
            val fragTrans= supportFragmentManager.beginTransaction()
            fragTrans.replace(fragContainerId, fragment)
            fragTrans.commit()
        }
/*
        else -> {
            val fragTrans= fragmentManager.beginTransaction()
            fragTrans.replace(fragContainerId, fragment)
            fragTrans.commit()
        }
 */
    }
}
fun Fragment.commitFrag(@IdRes fragContainerId: Int, fragment: Fragment){
    activity?.commitFrag(fragContainerId, fragment)
}


fun Activity.checkPermission(permission: String): Boolean {
    if (Build.VERSION.SDK_INT >= 23) {
        return if (this.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED) {
            true
        } else {
            //Log.v(TAG,"Permission is revoked");
            ActivityCompat.requestPermissions(
                this,
                arrayOf(permission),
                _SIF_Constant.REQ_PERMISSION
            )
            false
        }
    } else { //permission is automatically granted on sdk<23 upon installation
        //Log.v(TAG,"Permission is granted");
        return true
    }
}


fun Activity.pickImageGallery(){
    if(this.checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE))
        _BitmapUtil.pickImageGallery(
            this
        )
    else
        toast("Silahkan izinkan aplikasi membaca storage!")
}
fun Fragment.pickImageGallery(){
    activity?.pickImageGallery()
}


fun <T> Activity.getDefaultIntent(): T? {
    return getIntent(_SIF_Constant.EXTRA_DATA)
}
fun <T> Fragment.getDefaultIntent(): T? {
    return getIntent(_SIF_Constant.EXTRA_DATA)
}


fun <T> Activity.getIntent(key: String): T? {
    return try { intent.extras!![key] as T? }
    catch (e: Exception) { null }
}
fun <T> Fragment.getIntent(key: String): T? {
    return try { activity!!.intent.extras!![key] as T? }
    catch (e: Exception) { null }
}
fun <T> Intent.getExtra(key: String): T? {
    return try { this.extras!![key] as T? }
    catch (e: Exception) { null }
}


fun Activity.getActReqCode(): Int? {
    return getIntent(_SIF_Constant.REQ_CODE)
}
fun Fragment.getActReqCode(): Int? {
    return getIntent(_SIF_Constant.REQ_CODE)
}



inline fun <reified T: Activity> Context.startAct(vararg params: Pair<String, Any?>, waitForResult: Boolean= false, reqCode: Int= 0) {
    var inParamsSize= params.size +1
    val callingLifeCycleName=
        if(this is LifecycleOwner) Pair(_SIF_Constant.CALLING_LIFECYCLE, this::class.java.name)
        else { inParamsSize = params.size; null }
    val inParams= Array(inParamsSize){ if(it < params.size) params[it] else callingLifeCycleName!! }

    val intent= sidev.lib.android.external._AnkoInternals.createIntent(this, T::class.java, inParams)
    if(!waitForResult || this !is Activity)
        startActivity(intent)
    else{
        intent.putExtra(_SIF_Constant.REQ_CODE, reqCode)
        startActivityForResult(intent, reqCode)
    }
}
inline fun <reified T: Activity> Fragment.startAct(vararg params: Pair<String, Any?>, waitForResult: Boolean= false, reqCode: Int= 0) {
//    context!!.startAct<T>(*params)
    val callingLifeCycleName= Pair(_SIF_Constant.CALLING_LIFECYCLE, this::class.java.name)
    val inParams= Array(params.size +1){ if(it < params.size) params[it] else callingLifeCycleName!! }

    val intent= sidev.lib.android.external._AnkoInternals.createIntent(context!!, T::class.java, inParams)
    if(!waitForResult)
        startActivity(intent)
    else{
        intent.putExtra(_SIF_Constant.REQ_CODE, reqCode)
        startActivityForResult(intent, reqCode)
    }
}

inline fun <reified T: Fragment> Context.startSingleFragAct(vararg params: Pair<String, Any?>, waitForResult: Boolean= false, reqCode: Int= 0, isCustomActBar: Boolean= true) {
    startAct<SingleFragAct_Simple>(
        Pair(_SIF_Constant.FRAGMENT_NAME, T::class.java.name),
        Pair(_SIF_Constant.EXTRA_IS_CUSTOM_ACT_BAR, isCustomActBar),
        *params,
        waitForResult = waitForResult,
        reqCode = reqCode
    )
}
inline fun <reified T: Fragment> Fragment.startSingleFragAct(vararg params: Pair<String, Any?>, waitForResult: Boolean= false, reqCode: Int= 0, customActBar: Boolean= true) {
    startAct<SingleFragAct_Simple>(
        Pair(_SIF_Constant.FRAGMENT_NAME, T::class.java.name),
        Pair(_SIF_Constant.EXTRA_IS_CUSTOM_ACT_BAR, customActBar),
        *params,
        waitForResult = waitForResult,
        reqCode = reqCode
    )
}

fun Activity.setResult(vararg params: Pair<String, Any?>, resCode: Int= Activity.RESULT_OK, isFinished: Boolean= true) {
    val intent= sidev.lib.android.external._AnkoInternals.createIntent<Any>(params = params)
    setResult(resCode, intent)
    if(isFinished) this.finish()
}
fun Fragment.setResult(vararg params: Pair<String, Any?>, resCode: Int= Activity.RESULT_OK, isFinished: Boolean= true) {
    val intent= sidev.lib.android.external._AnkoInternals.createIntent<Any>(params = params)
    activity!!.setResult(resCode, intent)
    if(isFinished) activity!!.finish()
}
