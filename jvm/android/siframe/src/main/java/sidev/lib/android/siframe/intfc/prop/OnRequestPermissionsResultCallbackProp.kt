package sidev.lib.android.siframe.intfc.prop

import androidx.core.app.ActivityCompat
import sidev.lib.android.siframe.intfc.listener.OnRequestPermissionsResultCallback
import java.util.*

interface OnRequestPermissionsResultCallbackProp: ActivityCompat.OnRequestPermissionsResultCallback {

    //val onRequestPermissionResultCallback: ActivityCompat.OnRequestPermissionsResultCallback?

    /**
     * Isi pair Boolean `false` jika [OnRequestPermissionsResultCallback]
     * hanya dipanggil sekali lalu dihilangkan dari list.
     *
     */
    val onRequestPermissionResultCallbacks: Map<String, Pair<OnRequestPermissionsResultCallback, Boolean>>?

    /**
     * Callback for the result from requesting permissions. This method
     * is invoked for every call on [.requestPermissions].
     *
     *
     * **Note:** It is possible that the permissions request interaction
     * with the user is interrupted. In this case you will receive empty permissions
     * and results arrays which should be treated as a cancellation.
     *
     *
     * @param requestCode The request code passed in [.requestPermissions]
     * @param permissions The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     * which is either [android.content.pm.PackageManager.PERMISSION_GRANTED]
     * or [android.content.pm.PackageManager.PERMISSION_DENIED]. Never null.
     *
     * @see .requestPermissions
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(onRequestPermissionResultCallbacks != null){
            val removedKey = Stack<String>()
            for((key, c) in onRequestPermissionResultCallbacks!!){
                c.first.onRequestPermissionsResult(requestCode, permissions, grantResults)
                if(!c.second)
                    removedKey.add(key)
            }
            for(key in removedKey)
                removeOnRequestPermissionResultCallback(key)
        }
        //onRequestPermissionResultCallback?.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    fun addOnRequestPermissionResultCallback(
        c: OnRequestPermissionsResultCallback,
        isPersistent: Boolean = true,
        replaceExisting: Boolean = true
    ): Boolean

    /**
     * Return string key dari [OnRequestPermissionsResultCallback] yang terbentuk.
     */
    fun addOnRequestPermissionResultCallback(
        key: String = "",
        isPersistent: Boolean = true,
        replaceExisting: Boolean = true,
        callback: (
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
        ) -> Unit
    ): OnRequestPermissionsResultCallback?

    /**
     * Return `true` jika [OnRequestPermissionsResultCallback] dg [key] ada di [onRequestPermissionResultCallbacks].
     */
    fun removeOnRequestPermissionResultCallback(key: String): Boolean
}