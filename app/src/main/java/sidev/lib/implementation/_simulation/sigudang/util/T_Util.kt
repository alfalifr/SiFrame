package com.sigudang.android._template.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import java.security.Policy
import androidx.core.content.ContextCompat.getSystemService
import android.hardware.camera2.CameraManager
//import android.graphics.Camera
import android.hardware.Camera
import android.os.Build
import androidx.annotation.RequiresApi
import android.hardware.camera2.CameraAccessException
import androidx.core.content.ContextCompat.getSystemService
import android.hardware.Camera.Parameters.FLASH_MODE_ON
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


object T_Util {
    fun getFM(ctx: Context): FragmentManager? {
        return when(ctx){
            is AppCompatActivity -> ctx.supportFragmentManager
            is FragmentActivity -> ctx.supportFragmentManager
            else -> null
        }
    }

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


    fun requestPermission(act: Activity, permission: String, reqCode: Int= 0){
        if (ContextCompat.checkSelfPermission(act, permission)
            != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(act, permission)) {
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(act, arrayOf(permission), reqCode)
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
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