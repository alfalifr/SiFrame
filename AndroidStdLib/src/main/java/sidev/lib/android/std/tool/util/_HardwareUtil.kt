package sidev.lib.android.std.tool.util

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.camera2.CameraManager
//import android.graphics.Camera
import android.hardware.Camera
import android.os.Build
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


object _HardwareUtil {

//    @RequiresApi(Build.VERSION_CODES.M)
    fun Context.turnOnFlashlight(turnOn: Boolean= true): Boolean {
        var res = true
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            val camera = Camera.open()
            val parameters = camera.parameters

            val modes = parameters.supportedFlashModes
            if (modes.contains(Camera.Parameters.FLASH_MODE_TORCH))
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH)
            else if (modes.contains(Camera.Parameters.FLASH_MODE_ON))
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_ON)
            else {
                //No flash available
                res = false
            }
            camera.parameters = parameters
        } else {
            val camManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager?
            val cameraId = camManager!!.cameraIdList[0] // Usually front camera is at 0 position.
            camManager.setTorchMode(cameraId, turnOn)
        }
        return res
    }

    fun showKeyboard(v: View){
        val inputMethodManager: InputMethodManager? =
            v.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
/*
        inputMethodManager?.toggleSoftInputFromWindow(
            linearLayout.getApplicationWindowToken(),
            InputMethodManager.SHOW_FORCED, 0
        )
 */
        inputMethodManager?.showSoftInput(v, InputMethodManager.SHOW_FORCED)
    }
/*
        val camera = Camera.open()
        val parameters = camera.getParameters()
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH)
        camera.setParameters(parameters)
        camera.startPreview()
 */
/*
        val cam = Camera()//.open()
        val p = cam.getParameters()
        p.setFlashMode(android.hardware.Camera.Parameters.FLASH_MODE_TORCH)
        cam.setParameters(p)
        cam.startPreview()

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
    isFlashlightOn();
    if (camera == null && parameters == null) {
        camera = Camera.open();
        parameters = camera.getParameters();

        List<String> modes = parameters.getSupportedFlashModes();
        if (modes.contains(Camera.Parameters.FLASH_MODE_TORCH)) {
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        }
        else if (modes.contains(Camera.Parameters.FLASH_MODE_ON)) {
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
        }
        else {
            //No flash available
        }
        camera.setParameters(parameters);
    }
    if (getFlashlightState) {
        Objects.requireNonNull(camera).startPreview();
    } else {
        Objects.requireNonNull(camera).stopPreview();
    }
} else {
    isFlashlightOn();
    if (cameraManager == null) {
        cameraManager = (CameraManager) this.getSystemService(Context.CAMERA_SERVICE);
    }
    try {
        String cameraId = Objects.requireNonNull(cameraManager).getCameraIdList()[0];
        cameraManager.setTorchMode(cameraId, getFlashlightState);
    } catch (CameraAccessException e) {
        e.printStackTrace();
    }
}
 */

}