package sidev.lib.android.siframe.util

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import sidev.lib.android.siframe.intfc.util.ResetableUtil
import java.io.File
import java.io.IOException
import java.lang.Exception

object _ActFragUtil: ResetableUtil {
    private var requestCode= -1
    private var resultCode= -1
    private var intent: Intent?= null
    private var act: Activity?= null

    override fun reset() {
        requestCode= -1
        resultCode= -1
        intent= null
        act= null
    }

    private inline fun ifActNull(func: () -> Unit){
        if(act == null)
            func()
    }

    private inline fun actNotNull_once(func: () -> Unit){
        try{
            act!!
            func()
            reset()
        } catch (e: Exception){
            throw e
        }
    }
    private inline fun actNotNull(func: () -> Unit){
        try{
            act!!
            func()
        } catch (e: Exception){
            throw e
        }
    }

    fun getResult(act: Activity, requestCode: Int, resultCode: Int, intent: Intent?): Result {
        Log.e("ActFragUtil", "getResult requestCode == Constants.PICK_IMAGE_GALLERY_REQUEST = ${requestCode == _Constant.REQ_PICK_GALLERY}")
        this.act= act
        this.requestCode= requestCode
        this.resultCode= resultCode
        this.intent= intent
        return Result
    }

    /**
     * Tidak seharusnya dipanggil secara langsung
     */
    object Result{
        fun getBitmap(func: (bm: Bitmap, imgPath: String, imgFile: File?) -> Unit): Result? {
            actNotNull {
                Log.e("ActFragUtil", "getBitmap requestCode == Constants.PICK_IMAGE_GALLERY_REQUEST = ${requestCode == _Constant.REQ_PICK_GALLERY}")
                if (requestCode == _Constant.REQ_PICK_GALLERY
                    && resultCode == AppCompatActivity.RESULT_OK
                    && intent != null && intent!!.data != null) {

                    Log.e("ActFragUtil", "getBitmap requestCode MASUK!!!")
                    val uri = intent!!.data!!
                    try {
                        var bitmap = MediaStore.Images.Media.getBitmap(act!!.contentResolver, uri)
                        bitmap= _BitmapUtil.resizePict(bitmap, 500)

                        val pictFile = _BitmapUtil.savePict(bitmap, _EnvUtil.projectTempDir(act!!))

                        func(bitmap, uri.toString(), pictFile)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
                return this
            }
            return null
        }
    }
}