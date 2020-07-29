package sidev.lib.android.siframe.tool.util.`fun`

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import org.jetbrains.anko.contentView
import sidev.lib.android.siframe.intfc.lifecycle.rootbase.ActFragBase
import sidev.lib.android.siframe.intfc.lifecycle.rootbase.FragBase
import sidev.lib.android.siframe.tool.util._BitmapUtil
import sidev.lib.android.siframe.lifecycle.activity.SingleFragAct_Simple
import sidev.lib.android.siframe.tool.FragmentInstantiator
import sidev.lib.android.siframe.tool.`var`._SIF_Config
import sidev.lib.android.siframe.tool.`var`._SIF_Constant
import sidev.lib.android.siframe.tool.util.asResNameBy
import sidev.lib.android.siframe.tool.util.isIdDuplicatedInView
import sidev.lib.universal.`fun`.notNull
import java.lang.Exception



val Context.fragManager: FragmentManager?
    get()= when(this){
        is AppCompatActivity -> this.supportFragmentManager
        is FragmentActivity -> this.supportFragmentManager
        else -> null
    }

fun Context.toast(msg: String, length: Int= Toast.LENGTH_LONG){
    Toast.makeText(this, msg, length).show()
}
fun Fragment.toast(msg: String, length: Int= Toast.LENGTH_LONG){
    context?.toast(msg, length)
}

fun FragmentManager.commitFrag(fragment: Fragment,
                       fragContainerView: ViewGroup?= null,
                       @IdRes fragContainerId: Int= fragContainerView?.id ?: View.NO_ID,
                       tag: String= fragment::class.java.name){
    val fragTrans= beginTransaction()
    val containerId= when {
        fragContainerId != View.NO_ID -> fragContainerId
        fragContainerView != null -> {
            if(fragContainerView.id == View.NO_ID)
                fragContainerView.id= View.generateViewId()
            fragContainerView.id
        }
        else -> View.NO_ID
    }
/*
            this.contentView.notNull {
                if(containerId.isIdDuplicatedInView(it) && fragContainerView != null){
                    FragmentInstantiator.Builder(this, fragment::class).commit(fragContainerView)
                    return
                }
            }
 */
    //TODO ilangi loge
//    loge("containerId == View.NO_ID => ${containerId == View.NO_ID} containerId= $containerId idName= ${try{containerId asResNameBy this} catch (e: Exception){null}} tag= $tag")
    fragTrans.replace(containerId, fragment, tag)
    fragTrans.commit()
}
fun Context.commitFrag(fragment: Fragment,
                       fragContainerView: ViewGroup?= null,
                       @IdRes fragContainerId: Int= fragContainerView?.id ?: View.NO_ID,
                       tag: String= fragment::class.java.name){
    when(this){
        is FragmentActivity -> supportFragmentManager.commitFrag(fragment, fragContainerView, fragContainerId, tag)
    }
}
fun Fragment.commitFrag(fragment: Fragment,
                        fragContainerView: ViewGroup?= null,
                        @IdRes fragContainerId: Int= fragContainerView?.id ?: View.NO_ID,
                        tag: String= fragment::class.java.name){
//    activity?.commitFrag(fragment, fragContainerView, fragContainerId, tag)
    childFragmentManager.commitFrag(fragment, fragContainerView, fragContainerId, tag)
}

fun FragBase.forcedAttach(callingLifecyle: ActFragBase){
    onLifecycleDetach()
    onLifecycleAttach(callingLifecyle)
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


fun <T> Activity.getIntent(key: String, default: T?= null): T? {
    return try { (intent.extras!![key] as T)!! }
    catch (e: Exception) { default }
}
fun <T> Fragment.getIntent(key: String, default: T?= null): T? {
    return try { (activity!!.intent.extras!![key] as T)!! }
    catch (e: Exception) { default }
}
fun <T> Intent.getExtra(key: String, default: T?= null): T? {
    return try { (this.extras!![key] as T)!! }
    catch (e: Exception) { default }
}


val Activity.actReqCode: Int?
    get()= getIntent(_SIF_Constant.REQ_CODE)

val Fragment.actReqCode: Int?
    get()= getIntent(_SIF_Constant.REQ_CODE)


fun <T: Activity> Context.startAct(actClass: Class<out T>, vararg params: Pair<String, Any?>, waitForResult: Boolean= false, reqCode: Int= 0) {
    var inParamsSize= params.size +1
    val callingLifeCycleName=
        if(this is LifecycleOwner) Pair(_SIF_Constant.CALLING_LIFECYCLE, this::class.java.name)
        else { inParamsSize = params.size; null }
    val inParams= Array(inParamsSize){ if(it < params.size) params[it] else callingLifeCycleName!! }

    val intent= sidev.lib.android.external._AnkoInternals.createIntent(this, actClass, inParams)
    if(!waitForResult || this !is Activity)
        startActivity(intent)
    else{
        intent.putExtra(_SIF_Constant.REQ_CODE, reqCode)
        startActivityForResult(intent, reqCode)
    }
}
inline fun <reified T: Activity> Context.startAct(vararg params: Pair<String, Any?>, waitForResult: Boolean= false, reqCode: Int= 0) {
    this.startAct(T::class.java, *params, waitForResult= waitForResult, reqCode= reqCode)
}
/*
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
 */

fun <T: Activity> Fragment.startAct(actClass: Class<out T>, vararg params: Pair<String, Any?>, waitForResult: Boolean= false, reqCode: Int= 0) {
//    context!!.startAct<T>(*params)
    val callingLifeCycleName= Pair(_SIF_Constant.CALLING_LIFECYCLE, this::class.java.name)
    val inParams= Array(params.size +1){ if(it < params.size) params[it] else callingLifeCycleName!! }

    val intent= sidev.lib.android.external._AnkoInternals.createIntent(context!!, actClass, inParams)
    if(!waitForResult)
        startActivity(intent)
    else{
        intent.putExtra(_SIF_Constant.REQ_CODE, reqCode)
        startActivityForResult(intent, reqCode)
    }
}
inline fun <reified T: Activity> Fragment.startAct(vararg params: Pair<String, Any?>, waitForResult: Boolean= false, reqCode: Int= 0) {
    this.startAct(T::class.java, *params, waitForResult= waitForResult, reqCode= reqCode)
}


fun <T: Fragment> Context.startSingleFragAct(fragClass: Class<out T>, vararg params: Pair<String, Any?>, waitForResult: Boolean= false, reqCode: Int= 0, isCustomActBar: Boolean= true) {
    startAct<SingleFragAct_Simple>(
        SingleFragAct_Simple::class.java,
        Pair(_SIF_Constant.FRAGMENT_NAME, fragClass.name),
        Pair(_SIF_Constant.EXTRA_IS_CUSTOM_ACT_BAR, isCustomActBar),
        *params,
        waitForResult = waitForResult,
        reqCode = reqCode
    )
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

inline fun <reified T: Fragment> Context.startSingleFragAct_config(vararg params: Pair<String, Any?>, waitForResult: Boolean= false, reqCode: Int= 0, isCustomActBar: Boolean= true) {
    startAct(
        _SIF_Config.CLASS_SINGLE_FRAG_ACT,
        Pair(_SIF_Constant.FRAGMENT_NAME, T::class.java.name),
        Pair(_SIF_Constant.EXTRA_IS_CUSTOM_ACT_BAR, isCustomActBar),
        *params,
        waitForResult = waitForResult,
        reqCode = reqCode
    )
}


fun <T: Fragment> Fragment.startSingleFragAct(fragClass: Class<out T>, vararg params: Pair<String, Any?>, waitForResult: Boolean= false, reqCode: Int= 0, customActBar: Boolean= true) {
    startAct<SingleFragAct_Simple>(
        SingleFragAct_Simple::class.java,
        Pair(_SIF_Constant.FRAGMENT_NAME, fragClass.name),
        Pair(_SIF_Constant.EXTRA_IS_CUSTOM_ACT_BAR, customActBar),
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

inline fun <reified T: Fragment> Fragment.startSingleFragAct_config(vararg params: Pair<String, Any?>, waitForResult: Boolean= false, reqCode: Int= 0, customActBar: Boolean= true) {
    startAct(
        _SIF_Config.CLASS_SINGLE_FRAG_ACT,
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



fun <T: Activity> setSingleFragAct(cls: Class<T>, asDefault: Boolean= false){
    if(!asDefault)
        _SIF_Config.CLASS_SINGLE_FRAG_ACT= cls
    else
        _SIF_Config.CLASS_SINGLE_FRAG_ACT_DEFAULT= cls
}

fun resetSingleFragActToDefault(){
    _SIF_Config.CLASS_SINGLE_FRAG_ACT= _SIF_Config.CLASS_SINGLE_FRAG_ACT_DEFAULT
}