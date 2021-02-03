@file:ChangeLog(
    "Kamis, 10 Des 2020",
    "Bbrp file pada _ActFragFun pada lib SiFrame dipindahkan ke sini karena fungsinya general pada framework bawaan Android.",
    CodeModification.ADDED
)
package sidev.lib.android.std.tool.util.`fun`

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import sidev.lib._config_.CodeModification
import sidev.lib.`val`.SuppressLiteral
import sidev.lib.android.std._external._AnkoInternals
import sidev.lib.android.std.`val`._Constant
import sidev.lib.android.std.tool.util._BitmapUtil
import sidev.lib.annotation.ChangeLog
import sidev.lib.annotation.Warning
import sidev.lib.async.callback
import sidev.lib.check.notNull
import sidev.lib.exception.IllegalArgExc


val Context.fragManager: FragmentManager?
    get()= when(this){
        is AppCompatActivity -> this.supportFragmentManager
        is FragmentActivity -> this.supportFragmentManager
        else -> null
    }

@JvmOverloads
fun Context.toast(msg: String, length: Int = Toast.LENGTH_LONG){
    Toast.makeText(this, msg, length).show()
}
@JvmOverloads
fun Fragment.toast(msg: String, length: Int = Toast.LENGTH_LONG){
    context?.toast(msg, length)
}

/**
 * [forceReplace] `true` maka fragment dg tag [tag] yg ada sebelumnya akan di-replace dg [fragment].
 *
 * @return [Fragment] yg di-commit,
 *   -> dapat berupa [fragment] jika sebelumnya pada `this.extension` [FragmentManager]
 *   belum terdapat fragment dg tag [tag],
 *   -> atau fragment sebelumnya yg sudah terdapat pada `this.extension` dg tag [tag].
 */
@JvmOverloads
fun FragmentManager.commitFrag(
    fragment: Fragment,
    fragContainerView: ViewGroup? = null,
    @IdRes fragContainerId: Int = fragContainerView?.id ?: View.NO_ID,
    tag: String = fragment::class.java.name, forceReplace: Boolean = true
): Fragment{
    if(!forceReplace)
        findFragmentByTag(tag).notNull { return it }

    val fragTrans= beginTransaction()
    val containerId= when {
        fragContainerId != View.NO_ID -> fragContainerId
        fragContainerView != null -> {
            if(fragContainerView.id == View.NO_ID)
                fragContainerView.id= ViewCompat.generateViewId() //View.generateViewId()
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
    fragTrans.replace(containerId, fragment, tag)
    fragTrans.commit()
    return fragment
}

@JvmOverloads
fun FragmentActivity.commitFrag(
    fragment: Fragment,
    fragContainerView: ViewGroup? = null,
    @IdRes fragContainerId: Int = fragContainerView?.id ?: View.NO_ID,
    tag: String = fragment::class.java.name, forceReplace: Boolean = true
): Fragment
    = supportFragmentManager.commitFrag(
    fragment,
    fragContainerView,
    fragContainerId,
    tag,
    forceReplace
)
@JvmOverloads
fun Fragment.commitFrag(
    fragment: Fragment,
    fragContainerView: ViewGroup? = null,
    @IdRes fragContainerId: Int = fragContainerView?.id ?: View.NO_ID,
    tag: String = fragment::class.java.name, forceReplace: Boolean = true
): Fragment
    = childFragmentManager.commitFrag(
    fragment,
    fragContainerView,
    fragContainerId,
    tag,
    forceReplace
)



@ChangeLog("Sabtu, 10 Okt 2020", "param permission jadi vararg agar lebih robust")
@ChangeLog(
    "Sabtu, 10 Okt 2020",
    "param ditambah `callback` agar dapat digunakan saat proses pengajuan permission dilakukan",
    CodeModification.ADDED
)
@Warning("Param `callback` hanya dipanggil jika semua `permissions` di-grant.")
fun Activity.checkPermission(
    vararg permissions: String,
    callback: ((reqCode: Int, permissions: Array<out String>, grantResults: IntArray) -> Unit)? = null
): Boolean {
    if(permissions.isEmpty())
        throw IllegalArgExc(
            paramExcepted = *arrayOf("permissions"),
            detailMsg = "Param `permissions` tidak boleh kosong."
        )
    if (Build.VERSION.SDK_INT >= 23) {
        val ungrantedPermissions= ArrayList<String>()

        for(permission in permissions){
            if(checkCallingOrSelfPermission(permission) != PackageManager.PERMISSION_GRANTED)
                ungrantedPermissions += permission
        }

        return if(ungrantedPermissions.isEmpty()){
            callback?.invoke(
                _Constant.REQ_PERMISSION,
                permissions,
                IntArray(permissions.size){ PackageManager.PERMISSION_GRANTED }
            )
            true
        } else {
/*
            if(callback != null && this is ActFragBase)
                onRequestPermissionResultCallback=
                    ActivityCompat.OnRequestPermissionsResultCallback { reqCode, permissions, grantResults ->
                        callback.invoke(reqCode, permissions, grantResults)
                }
 */

            ActivityCompat.requestPermissions(
                this,
                ungrantedPermissions.toTypedArray(),
                _Constant.REQ_PERMISSION
            )
            false
        }
/*
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
 */
    } else { //permission is automatically granted on sdk<23 upon installation
        //Log.v(TAG,"Permission is granted");
        callback?.invoke(
            _Constant.REQ_PERMISSION,
            permissions,
            IntArray(permissions.size){ PackageManager.PERMISSION_GRANTED }
        )
        return true
    }
}
fun Fragment.checkPermission(
    vararg permissions: String,
    callback: ((reqCode: Int, permissions: Array<out String>, grantResults: IntArray) -> Unit)? = null
): Boolean = activity?.checkPermission(*permissions, callback = callback)
    ?: run {
        logw("Fragment.checkPermission() dipanggil saat activity msh null, return false.")
        false
    }



fun Activity.pickImageGallery(){
    if(this.checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE))
        _BitmapUtil.pickImageGallery(this)
    else
        toast("Silahkan izinkan aplikasi membaca storage!")
}
fun Fragment.pickImageGallery(){
    activity?.pickImageGallery()
}


fun <T> Activity.getDefaultIntent(): T? {
    return getIntent(_Constant.EXTRA_DATA)
}
fun <T> Fragment.getDefaultIntent(): T? {
    return getIntent(_Constant.EXTRA_DATA)
}


fun <T> Activity.getIntent(key: String, default: T? = null): T? {
    @Suppress(SuppressLiteral.UNCHECKED_CAST)
    return try { (intent.extras!![key] as T)!! }
    catch (e: Exception) { default }
}
fun <T> Fragment.getIntent(key: String, default: T? = null): T? {
    @Suppress(SuppressLiteral.UNCHECKED_CAST)
    return try { (activity!!.intent.extras!![key] as T)!! }
    catch (e: Exception) { default }
}


val Activity.actReqCode: Int?
    get()= getIntent(_Constant.REQ_CODE)

val Fragment.actReqCode: Int?
    get()= getIntent(_Constant.REQ_CODE)

@JvmOverloads
fun <T : Activity> Context.startAct(
    actClass: Class<out T>,
    vararg params: Pair<String, Any?>,
    waitForResult: Boolean = false,
    reqCode: Int = 0,
    intentPreSettingFun: ((Intent) -> Unit)?= null
) {
    var inParamsSize= params.size +1
    val callingLifeCycleName=
        if(this is LifecycleOwner) Pair(_Constant.CALLING_LIFECYCLE, this::class.java.name)
        else { inParamsSize = params.size; null }
    val inParams= Array(inParamsSize){ if(it < params.size) params[it] else callingLifeCycleName!! }

    val intent= _AnkoInternals.createIntent(this, actClass, inParams)
    intentPreSettingFun?.invoke(intent)
    if(!waitForResult || this !is Activity)
        startActivity(intent)
    else{
        intent.putExtra(_Constant.REQ_CODE, reqCode)
        startActivityForResult(intent, reqCode)
    }
}
inline fun <reified T : Activity> Context.startAct(
    vararg params: Pair<String, Any?>,
    waitForResult: Boolean = false,
    reqCode: Int = 0,
    noinline intentPreSettingFun: ((Intent) -> Unit)? = null
) {
    this.startAct(T::class.java, *params, waitForResult = waitForResult, reqCode = reqCode, intentPreSettingFun = intentPreSettingFun)
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
@JvmOverloads
fun <T : Activity> Fragment.startAct(
    actClass: Class<out T>,
    vararg params: Pair<String, Any?>,
    waitForResult: Boolean = false,
    reqCode: Int = 0,
    intentPreSettingFun: ((Intent) -> Unit)? = null
) {
//    context!!.startAct<T>(*params)
    val callingLifeCycleName= Pair(_Constant.CALLING_LIFECYCLE, this::class.java.name)
    val inParams= Array(params.size + 1){ if(it < params.size) params[it] else callingLifeCycleName }

    val intent= _AnkoInternals.createIntent(context!!, actClass, inParams)
    intentPreSettingFun?.invoke(intent)
    if(!waitForResult)
        startActivity(intent)
    else{
        intent.putExtra(_Constant.REQ_CODE, reqCode)
        startActivityForResult(intent, reqCode)
    }
}
inline fun <reified T : Activity> Fragment.startAct(
    vararg params: Pair<String, Any?>,
    waitForResult: Boolean = false,
    reqCode: Int = 0,
    noinline intentPreSettingFun: ((Intent) -> Unit)? = null
) {
    this.startAct(T::class.java, *params, waitForResult = waitForResult, reqCode = reqCode, intentPreSettingFun = intentPreSettingFun)
}


@JvmOverloads
fun Activity.setResult(
    vararg params: Pair<String, Any?>,
    resCode: Int = Activity.RESULT_OK,
    isFinished: Boolean = true,
    intentPreSettingFun: ((Intent) -> Unit)? = null
) {
    val intent= _AnkoInternals.createIntent<Any>(params = params)
    intentPreSettingFun?.invoke(intent)
    setResult(resCode, intent)
    if(isFinished) this.finish()
}
@JvmOverloads
fun Fragment.setResult(
    vararg params: Pair<String, Any?>,
    resCode: Int = Activity.RESULT_OK,
    isFinished: Boolean = true,
    intentPreSettingFun: ((Intent) -> Unit)? = null
) {
    val intent= _AnkoInternals.createIntent<Any>(params = params)
    intentPreSettingFun?.invoke(intent)
    activity!!.setResult(resCode, intent)
    if(isFinished) activity!!.finish()
}
