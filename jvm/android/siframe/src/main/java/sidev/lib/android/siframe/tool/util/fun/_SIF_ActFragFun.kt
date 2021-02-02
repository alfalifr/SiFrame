package sidev.lib.android.siframe.tool.util.`fun`

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import sidev.lib._config_.CodeModification
import sidev.lib.android.siframe.intfc.lifecycle.rootbase.ActFragBase
import sidev.lib.android.siframe.intfc.lifecycle.rootbase.FragBase
import sidev.lib.android.siframe.lifecycle.activity.Act
import sidev.lib.android.siframe.lifecycle.activity.SingleFragAct_Simple
import sidev.lib.android.siframe.lifecycle.fragment.Frag
import sidev.lib.android.siframe.`val`._SIF_Config
import sidev.lib.android.siframe.`val`._SIF_Constant
import sidev.lib.android.siframe.intfc.prop.OnRequestPermissionsResultCallbackProp
//import sidev.lib.android.siframe.intfc.listener.OnRequestPermissionsResultCallback
import sidev.lib.android.std.`val`._Constant
import sidev.lib.android.std.tool.util.`fun`.startAct
import sidev.lib.annotation.ChangeLog
import sidev.lib.annotation.Unsafe
import sidev.lib.annotation.Warning


@ChangeLog(
    "Kamis, 10 Des 2020",
    "Fungsi ini ditambahkan untuk menambah fitur dari fungsi pada lib AndroidStdLib",
    CodeModification.ADDED
)
@ChangeLog("Sabtu, 10 Okt 2020", "param permission jadi vararg agar lebih robust")
@ChangeLog(
    "Sabtu, 10 Okt 2020",
    "param ditambah `callback` agar dapat digunakan saat proses pengajuan permission dilakukan",
    CodeModification.ADDED
)
@Warning(
    "Param `callback` berpotensi tidak dipanggil jika `permissions` belum di-grant dan `this Activity` bukan `OnRequestPermissionsResultCallbackProp`."
)
fun Activity.checkPermission_sif(
    vararg permissions: String,
    isOneTime: Boolean = true,
    callback: ((reqCode: Int, permissions: Array<out String>, grantResults: IntArray) -> Unit)? = null
): Boolean {
    if (Build.VERSION.SDK_INT >= 23) {
        val ungrantedPermissions= ArrayList<String>()
        val grantedPermissions= ArrayList<String>()

        for(permission in permissions){
            val result= checkCallingOrSelfPermission(permission)
            if(result != PackageManager.PERMISSION_GRANTED)
                ungrantedPermissions += permission
            else
                grantedPermissions += permission
        }

        return if(ungrantedPermissions.isEmpty()){
            callback?.invoke(
                _Constant.REQ_PERMISSION,
                permissions,
                IntArray(permissions.size){ PackageManager.PERMISSION_GRANTED }
            )
            true
        } else {
            //val checkAttachment = callback != null && this is OnRequestPermissionsResultCallbackProp
            val callbackOwner = if(callback != null && this is OnRequestPermissionsResultCallbackProp) this else null
            val callbackObj = callbackOwner?.addOnRequestPermissionResultCallback(
                _SIF_Constant.REQ_PERMISSION_CALLBACK_KEY,
                true,
                true,
                callback = callback!!
            )
            callback?.invoke(
                _Constant.REQ_PERMISSION,
                grantedPermissions.toTypedArray(),
                IntArray(grantedPermissions.size){ PackageManager.PERMISSION_GRANTED }
            )
/*
                onRequestPermissionResultCallback=
                    ActivityCompat.OnRequestPermissionsResultCallback { reqCode, permissions, grantResults ->
                        callback.invoke(reqCode, permissions, grantResults)
                }
 */
            if(isOneTime)
                callbackOwner?.addOnRequestPermissionResultCallback(
                    callbackObj!!, //Jika callbackOwner != null, maka callbackOwner juga != null.
                    false,
                    true
                )
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

@JvmOverloads
fun <T : Fragment> Context.startSingleFragAct(
    fragClass: Class<out T>,
    vararg params: Pair<String, Any?>,
    waitForResult: Boolean = false,
    reqCode: Int = 0,
    isCustomActBar: Boolean = true,
    intentPreSettingFun: ((Intent) -> Unit)? = null
) {
    startAct<SingleFragAct_Simple>(
        SingleFragAct_Simple::class.java,
        Pair(_SIF_Constant.FRAGMENT_NAME, fragClass.name),
        Pair(_SIF_Constant.EXTRA_IS_CUSTOM_ACT_BAR, isCustomActBar),
        *params,
        waitForResult = waitForResult,
        reqCode = reqCode,
        intentPreSettingFun = intentPreSettingFun
    )
}
inline fun <reified T : Fragment> Context.startSingleFragAct(
    vararg params: Pair<String, Any?>,
    waitForResult: Boolean = false,
    reqCode: Int = 0,
    isCustomActBar: Boolean = true,
    noinline intentPreSettingFun: ((Intent) -> Unit)? = null
) {
    startAct<SingleFragAct_Simple>(
        Pair(_SIF_Constant.FRAGMENT_NAME, T::class.java.name),
        Pair(_SIF_Constant.EXTRA_IS_CUSTOM_ACT_BAR, isCustomActBar),
        *params,
        waitForResult = waitForResult,
        reqCode = reqCode,
        intentPreSettingFun = intentPreSettingFun
    )
}

@JvmOverloads
fun <T : Fragment> Context.startSingleFragAct_config(
    fragCls: Class<out T>,
    vararg params: Pair<String, Any?>,
    waitForResult: Boolean = false,
    reqCode: Int = 0,
    isCustomActBar: Boolean = true,
    intentPreSettingFun: ((Intent) -> Unit)? = null
) {
    startAct(
        _SIF_Config.CLASS_SINGLE_FRAG_ACT,
        Pair(_SIF_Constant.FRAGMENT_NAME, fragCls.name),
        Pair(_SIF_Constant.EXTRA_IS_CUSTOM_ACT_BAR, isCustomActBar),
        *params,
        waitForResult = waitForResult,
        reqCode = reqCode,
        intentPreSettingFun = intentPreSettingFun
    )
}
inline fun <reified T : Fragment> Context.startSingleFragAct_config(
    vararg params: Pair<String, Any?>,
    waitForResult: Boolean = false,
    reqCode: Int = 0,
    isCustomActBar: Boolean = true,
    noinline intentPreSettingFun: ((Intent) -> Unit)? = null
) {
    startAct(
        _SIF_Config.CLASS_SINGLE_FRAG_ACT,
        Pair(_SIF_Constant.FRAGMENT_NAME, T::class.java.name),
        Pair(_SIF_Constant.EXTRA_IS_CUSTOM_ACT_BAR, isCustomActBar),
        *params,
        waitForResult = waitForResult,
        reqCode = reqCode,
        intentPreSettingFun = intentPreSettingFun
    )
}

@JvmOverloads
fun <T : Fragment> Fragment.startSingleFragAct(
    fragClass: Class<out T>,
    vararg params: Pair<String, Any?>,
    waitForResult: Boolean = false,
    reqCode: Int = 0,
    customActBar: Boolean = true,
    intentPreSettingFun: ((Intent) -> Unit)? = null
) {
    startAct<SingleFragAct_Simple>(
        SingleFragAct_Simple::class.java,
        Pair(_SIF_Constant.FRAGMENT_NAME, fragClass.name),
        Pair(_SIF_Constant.EXTRA_IS_CUSTOM_ACT_BAR, customActBar),
        *params,
        waitForResult = waitForResult,
        reqCode = reqCode,
        intentPreSettingFun = intentPreSettingFun
    )
}
inline fun <reified T : Fragment> Fragment.startSingleFragAct(
    vararg params: Pair<String, Any?>,
    waitForResult: Boolean = false,
    reqCode: Int = 0,
    customActBar: Boolean = true,
    noinline intentPreSettingFun: ((Intent) -> Unit)? = null
) {
    startAct<SingleFragAct_Simple>(
        Pair(_SIF_Constant.FRAGMENT_NAME, T::class.java.name),
        Pair(_SIF_Constant.EXTRA_IS_CUSTOM_ACT_BAR, customActBar),
        *params,
        waitForResult = waitForResult,
        reqCode = reqCode,
        intentPreSettingFun = intentPreSettingFun
    )
}

@JvmOverloads
fun <T : Fragment> Fragment.startSingleFragAct_config(
    fragCls: Class<out T>,
    vararg params: Pair<String, Any?>,
    waitForResult: Boolean = false,
    reqCode: Int = 0,
    customActBar: Boolean = true,
    intentPreSettingFun: ((Intent) -> Unit)? = null
) {
    startAct(
        _SIF_Config.CLASS_SINGLE_FRAG_ACT,
        Pair(_SIF_Constant.FRAGMENT_NAME, fragCls.name),
        Pair(_SIF_Constant.EXTRA_IS_CUSTOM_ACT_BAR, customActBar),
        *params,
        waitForResult = waitForResult,
        reqCode = reqCode,
        intentPreSettingFun = intentPreSettingFun
    )
}
inline fun <reified T : Fragment> Fragment.startSingleFragAct_config(
    vararg params: Pair<String, Any?>,
    waitForResult: Boolean = false,
    reqCode: Int = 0,
    customActBar: Boolean = true,
    noinline intentPreSettingFun: ((Intent) -> Unit)? = null
) {
    startAct(
        _SIF_Config.CLASS_SINGLE_FRAG_ACT,
        Pair(_SIF_Constant.FRAGMENT_NAME, T::class.java.name),
        Pair(_SIF_Constant.EXTRA_IS_CUSTOM_ACT_BAR, customActBar),
        *params,
        waitForResult = waitForResult,
        reqCode = reqCode,
        intentPreSettingFun = intentPreSettingFun
    )
}



fun <T : Activity> setSingleFragAct(cls: Class<T>, asDefault: Boolean = false){
    if(!asDefault)
        _SIF_Config.CLASS_SINGLE_FRAG_ACT= cls
    else
        _SIF_Config.CLASS_SINGLE_FRAG_ACT_DEFAULT= cls
}

fun resetSingleFragActToDefault(){
    _SIF_Config.CLASS_SINGLE_FRAG_ACT= _SIF_Config.CLASS_SINGLE_FRAG_ACT_DEFAULT
}


@Unsafe("Act yg dihasilkan tidak dapat di-reinstantiate pada Android.")
fun createSimpleAct(layoutId: Int, initViewFunc: ((View) -> Unit)? = null): Act = object : Act(){
    override val layoutId: Int get() = layoutId
    override fun _initView(layoutView: View) {
        initViewFunc?.invoke(layoutView)
    }
}

@Unsafe("Frag yg dihasilkan tidak dapat di-reinstantiate pada Android.")
fun createSimpleFrag(layoutId: Int, initViewFunc: ((View) -> Unit)? = null): Frag = object : Frag(){
    override val layoutId: Int get() = layoutId
    override fun _initView(layoutView: View) {
        initViewFunc?.invoke(layoutView)
    }
}


fun FragBase.forcedAttach(callingLifecyle: ActFragBase){
    onLifecycleDetach()
    onLifecycleAttach(callingLifecyle)
}


/*

@JvmOverloads
fun Activity.setResult(
    vararg params: Pair<String, Any?>,
    resCode: Int = Activity.RESULT_OK,
    isFinished: Boolean = true
) {
    val intent= _AnkoInternals.createIntent<Any>(params = params)
    setResult(resCode, intent)
    if(isFinished) this.finish()
}
@JvmOverloads
fun Fragment.setResult(
    vararg params: Pair<String, Any?>,
    resCode: Int = Activity.RESULT_OK,
    isFinished: Boolean = true
) {
    val intent= _AnkoInternals.createIntent<Any>(params = params)
    activity!!.setResult(resCode, intent)
    if(isFinished) activity!!.finish()
}


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


fun FragBase.forcedAttach(callingLifecyle: ActFragBase){
    onLifecycleDetach()
    onLifecycleAttach(callingLifecyle)
}


@ChangeLog("Sabtu, 10 Okt 2020", "param permission jadi vararg agar lebih robust")
@ChangeLog(
    "Sabtu, 10 Okt 2020",
    "param ditambah `callback` agar dapat digunakan saat proses pengajuan permission dilakukan",
    CodeModification.ADDED
)
fun Activity.checkPermission(
    vararg permissions: String,
    callback: ((reqCode: Int, permissions: Array<out String>, grantResults: IntArray) -> Unit)? = null
): Boolean {
    if (Build.VERSION.SDK_INT >= 23) {
        val ungrantedPermissions= ArrayList<String>()

        for(permission in permissions){
            if(checkCallingOrSelfPermission(permission) != PackageManager.PERMISSION_GRANTED)
                ungrantedPermissions += permission
        }

        return if(ungrantedPermissions.isEmpty()){
            callback?.invoke(
                _SIF_Constant.REQ_PERMISSION,
                permissions,
                IntArray(permissions.size){ PackageManager.PERMISSION_GRANTED }
            )
            true
        } else {
            if(callback != null && this is ActFragBase)
                onRequestPermissionResultCallback=
                    ActivityCompat.OnRequestPermissionsResultCallback { reqCode, permissions, grantResults ->
                        callback.invoke(reqCode, permissions, grantResults)
                }

            ActivityCompat.requestPermissions(
                this,
                ungrantedPermissions.toTypedArray(),
                _SIF_Constant.REQ_PERMISSION
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
            _SIF_Constant.REQ_PERMISSION,
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
        _SIF_BitmapUtil.pickImageGallery(
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


fun <T> Activity.getIntent(key: String, default: T? = null): T? {
    return try { (intent.extras!![key] as T)!! }
    catch (e: Exception) { default }
}
fun <T> Fragment.getIntent(key: String, default: T? = null): T? {
    return try { (activity!!.intent.extras!![key] as T)!! }
    catch (e: Exception) { default }
}
fun <T> Intent.getExtra(key: String, default: T? = null): T? {
    return try { (this.extras!![key] as T)!! }
    catch (e: Exception) { default }
}


val Activity.actReqCode: Int?
    get()= getIntent(_SIF_Constant.REQ_CODE)

val Fragment.actReqCode: Int?
    get()= getIntent(_SIF_Constant.REQ_CODE)

@JvmOverloads
fun <T : Activity> Context.startAct(
    actClass: Class<out T>,
    vararg params: Pair<String, Any?>,
    waitForResult: Boolean = false,
    reqCode: Int = 0
) {
    var inParamsSize= params.size +1
    val callingLifeCycleName=
        if(this is LifecycleOwner) Pair(_SIF_Constant.CALLING_LIFECYCLE, this::class.java.name)
        else { inParamsSize = params.size; null }
    val inParams= Array(inParamsSize){ if(it < params.size) params[it] else callingLifeCycleName!! }

    val intent= _AnkoInternals.createIntent(this, actClass, inParams)
    if(!waitForResult || this !is Activity)
        startActivity(intent)
    else{
        intent.putExtra(_SIF_Constant.REQ_CODE, reqCode)
        startActivityForResult(intent, reqCode)
    }
}
inline fun <reified T : Activity> Context.startAct(
    vararg params: Pair<String, Any?>,
    waitForResult: Boolean = false,
    reqCode: Int = 0
) {
    this.startAct(T::class.java, *params, waitForResult = waitForResult, reqCode = reqCode)
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
    reqCode: Int = 0
) {
//    context!!.startAct<T>(*params)
    val callingLifeCycleName= Pair(_SIF_Constant.CALLING_LIFECYCLE, this::class.java.name)
    val inParams= Array(params.size + 1){ if(it < params.size) params[it] else callingLifeCycleName!! }

    val intent= _AnkoInternals.createIntent(context!!, actClass, inParams)
    if(!waitForResult)
        startActivity(intent)
    else{
        intent.putExtra(_SIF_Constant.REQ_CODE, reqCode)
        startActivityForResult(intent, reqCode)
    }
}
inline fun <reified T : Activity> Fragment.startAct(
    vararg params: Pair<String, Any?>,
    waitForResult: Boolean = false,
    reqCode: Int = 0
) {
    this.startAct(T::class.java, *params, waitForResult = waitForResult, reqCode = reqCode)
}

@JvmOverloads
fun <T : Fragment> Context.startSingleFragAct(
    fragClass: Class<out T>,
    vararg params: Pair<String, Any?>,
    waitForResult: Boolean = false,
    reqCode: Int = 0,
    isCustomActBar: Boolean = true
) {
    startAct<SingleFragAct_Simple>(
        SingleFragAct_Simple::class.java,
        Pair(_SIF_Constant.FRAGMENT_NAME, fragClass.name),
        Pair(_SIF_Constant.EXTRA_IS_CUSTOM_ACT_BAR, isCustomActBar),
        *params,
        waitForResult = waitForResult,
        reqCode = reqCode
    )
}
inline fun <reified T : Fragment> Context.startSingleFragAct(
    vararg params: Pair<String, Any?>,
    waitForResult: Boolean = false,
    reqCode: Int = 0,
    isCustomActBar: Boolean = true
) {
    startAct<SingleFragAct_Simple>(
        Pair(_SIF_Constant.FRAGMENT_NAME, T::class.java.name),
        Pair(_SIF_Constant.EXTRA_IS_CUSTOM_ACT_BAR, isCustomActBar),
        *params,
        waitForResult = waitForResult,
        reqCode = reqCode
    )
}

@JvmOverloads
fun <T : Fragment> Context.startSingleFragAct_config(
    fragCls: Class<out T>,
    vararg params: Pair<String, Any?>,
    waitForResult: Boolean = false,
    reqCode: Int = 0,
    isCustomActBar: Boolean = true
) {
    startAct(
        _SIF_Config.CLASS_SINGLE_FRAG_ACT,
        Pair(_SIF_Constant.FRAGMENT_NAME, fragCls.name),
        Pair(_SIF_Constant.EXTRA_IS_CUSTOM_ACT_BAR, isCustomActBar),
        *params,
        waitForResult = waitForResult,
        reqCode = reqCode
    )
}
inline fun <reified T : Fragment> Context.startSingleFragAct_config(
    vararg params: Pair<String, Any?>,
    waitForResult: Boolean = false,
    reqCode: Int = 0,
    isCustomActBar: Boolean = true
) {
    startAct(
        _SIF_Config.CLASS_SINGLE_FRAG_ACT,
        Pair(_SIF_Constant.FRAGMENT_NAME, T::class.java.name),
        Pair(_SIF_Constant.EXTRA_IS_CUSTOM_ACT_BAR, isCustomActBar),
        *params,
        waitForResult = waitForResult,
        reqCode = reqCode
    )
}

@JvmOverloads
fun <T : Fragment> Fragment.startSingleFragAct(
    fragClass: Class<out T>,
    vararg params: Pair<String, Any?>,
    waitForResult: Boolean = false,
    reqCode: Int = 0,
    customActBar: Boolean = true
) {
    startAct<SingleFragAct_Simple>(
        SingleFragAct_Simple::class.java,
        Pair(_SIF_Constant.FRAGMENT_NAME, fragClass.name),
        Pair(_SIF_Constant.EXTRA_IS_CUSTOM_ACT_BAR, customActBar),
        *params,
        waitForResult = waitForResult,
        reqCode = reqCode
    )
}
inline fun <reified T : Fragment> Fragment.startSingleFragAct(
    vararg params: Pair<String, Any?>,
    waitForResult: Boolean = false,
    reqCode: Int = 0,
    customActBar: Boolean = true
) {
    startAct<SingleFragAct_Simple>(
        Pair(_SIF_Constant.FRAGMENT_NAME, T::class.java.name),
        Pair(_SIF_Constant.EXTRA_IS_CUSTOM_ACT_BAR, customActBar),
        *params,
        waitForResult = waitForResult,
        reqCode = reqCode
    )
}

@JvmOverloads
fun <T : Fragment> Fragment.startSingleFragAct_config(
    fragCls: Class<out T>,
    vararg params: Pair<String, Any?>,
    waitForResult: Boolean = false,
    reqCode: Int = 0,
    customActBar: Boolean = true
) {
    startAct(
        _SIF_Config.CLASS_SINGLE_FRAG_ACT,
        Pair(_SIF_Constant.FRAGMENT_NAME, fragCls.name),
        Pair(_SIF_Constant.EXTRA_IS_CUSTOM_ACT_BAR, customActBar),
        *params,
        waitForResult = waitForResult,
        reqCode = reqCode
    )
}
inline fun <reified T : Fragment> Fragment.startSingleFragAct_config(
    vararg params: Pair<String, Any?>,
    waitForResult: Boolean = false,
    reqCode: Int = 0,
    customActBar: Boolean = true
) {
    startAct(
        _SIF_Config.CLASS_SINGLE_FRAG_ACT,
        Pair(_SIF_Constant.FRAGMENT_NAME, T::class.java.name),
        Pair(_SIF_Constant.EXTRA_IS_CUSTOM_ACT_BAR, customActBar),
        *params,
        waitForResult = waitForResult,
        reqCode = reqCode
    )
}

@JvmOverloads
fun Activity.setResult(
    vararg params: Pair<String, Any?>,
    resCode: Int = Activity.RESULT_OK,
    isFinished: Boolean = true
) {
    val intent= _AnkoInternals.createIntent<Any>(params = params)
    setResult(resCode, intent)
    if(isFinished) this.finish()
}
@JvmOverloads
fun Fragment.setResult(
    vararg params: Pair<String, Any?>,
    resCode: Int = Activity.RESULT_OK,
    isFinished: Boolean = true
) {
    val intent= _AnkoInternals.createIntent<Any>(params = params)
    activity!!.setResult(resCode, intent)
    if(isFinished) activity!!.finish()
}



fun <T : Activity> setSingleFragAct(cls: Class<T>, asDefault: Boolean = false){
    if(!asDefault)
        _SIF_Config.CLASS_SINGLE_FRAG_ACT= cls
    else
        _SIF_Config.CLASS_SINGLE_FRAG_ACT_DEFAULT= cls
}

fun resetSingleFragActToDefault(){
    _SIF_Config.CLASS_SINGLE_FRAG_ACT= _SIF_Config.CLASS_SINGLE_FRAG_ACT_DEFAULT
}


@Unsafe("Act yg dihasilkan tidak dapat di-reinstantiate pada Android.")
fun createSimpleAct(layoutId: Int, initViewFunc: ((View) -> Unit)? = null): Act = object : Act(){
    override val layoutId: Int get() = layoutId
    override fun _initView(layoutView: View) {
        initViewFunc?.invoke(layoutView)
    }
}

@Unsafe("Frag yg dihasilkan tidak dapat di-reinstantiate pada Android.")
fun createSimpleFrag(layoutId: Int, initViewFunc: ((View) -> Unit)? = null): Frag = object : Frag(){
    override val layoutId: Int get() = layoutId
    override fun _initView(layoutView: View) {
        initViewFunc?.invoke(layoutView)
    }
}

 */