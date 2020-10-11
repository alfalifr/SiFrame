package sidev.lib.android.siframe.intfc.prop

import androidx.core.app.ActivityCompat

interface OnRequestPermissionsResultCallbackProp: ActivityCompat.OnRequestPermissionsResultCallback {

    val onRequestPermissionResultCallback: ActivityCompat.OnRequestPermissionsResultCallback?

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
        onRequestPermissionResultCallback?.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}