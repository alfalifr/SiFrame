package sidev.lib.android.siframe.tool.util

import android.R.attr.path
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import kotlin.math.absoluteValue
import android.graphics.*
import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import sidev.lib.android.siframe.tool.`var`._SIF_Constant


object _BitmapUtil{
    //menggunakan Gallery Intent
//        val PICK_IMAGE_GALLERY_REQUEST= 1

    private fun pickImageGallery_Int(actOrFrag: Any, requestCode: Int= _SIF_Constant.REQ_PICK_GALLERY){
        val intent = Intent()
        // Show only images, no videos or anything else
        intent.type= "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        // Always show the chooser (if there are multiple options available)
        when(actOrFrag){
            is Activity -> actOrFrag.startActivityForResult(Intent.createChooser(intent, "Select Picture"), requestCode)
            is Fragment -> actOrFrag.startActivityForResult(Intent.createChooser(intent, "Select Picture"), requestCode)
        }
    }
    fun pickImageGallery(act: Activity, requestCode: Int= _SIF_Constant.REQ_PICK_GALLERY){
        pickImageGallery_Int(act, requestCode)
    }
    fun pickImageGallery(frag: Fragment, requestCode: Int= _SIF_Constant.REQ_PICK_GALLERY){
        pickImageGallery_Int(frag, requestCode)
    }

    private fun pickImageGalleryMultiple_Int(actOrFrag: Any, requestCode: Int= _SIF_Constant.REQ_PICK_GALLERY_MULTIPLE){
        val intent = Intent()
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.action = Intent.ACTION_GET_CONTENT
        when(actOrFrag){
            is Activity -> actOrFrag.startActivityForResult(Intent.createChooser(intent, "Select Picture"),
                requestCode)
            is Fragment -> actOrFrag.startActivityForResult(Intent.createChooser(intent, "Select Picture"),
                requestCode)
        }
    }


    @Deprecated("Gak dipake lagi bro", ReplaceWith("pickImageGallery()"))
    fun bacaGambar(pathOrg: String): Bitmap {
        var u = 1
        val pathFile = "$path/ori/$pathOrg"
        val pathFoto: String
        var file = File("$pathFile/Foto$u.jpg")

        while (file.exists()) {
            u++
            file = File("$pathFile/Foto$u.jpg")
        }
        pathFoto = pathFile + "/Foto" + (u - 1) + ".jpg"
        file = File(pathFoto)

        return BitmapFactory.decodeFile(pathFoto)
    }

    //crop
    fun pictSquare(bm: Bitmap, size: Int): Bitmap {
        val bmResize= resizePict(bm, size)

        val pjgAwal= bmResize.width
        val lbrAwal= bmResize.height

        if(pjgAwal == lbrAwal)
            return Bitmap.createBitmap(bmResize, 0, 0, pjgAwal, lbrAwal)

        val selisihPjg= (pjgAwal -size).absoluteValue
        val selisihLbr= (lbrAwal -size).absoluteValue

        var xStart= selisihPjg/2
        var yStart= selisihLbr/2

        var sizeNew = size

        if(xStart + size > bmResize.width){
            xStart = 0
            sizeNew = bmResize.width
        }

        if(yStart + size > bmResize.height){
            yStart = 0
            sizeNew = bmResize.height
        }

        return Bitmap.createBitmap(bmResize, xStart, yStart, sizeNew, sizeNew) //--> crop tengah
    }
    fun resizePict(bm: Bitmap, size: Int): Bitmap{
        var sizeMin= bm.height
        var sizeMax= bm.width

        if(sizeMax == sizeMin)
            Bitmap.createScaledBitmap(bm, size, size, false)

        var max= "width"
        if(bm.width < bm.height){
            sizeMin= bm.width
            sizeMax= bm.height
            max= "height"
        }

        val ratioSizeMin= size/sizeMin.toDouble()
        val sizeMaxNew= sizeMax *ratioSizeMin

        var widthNew= sizeMaxNew.toInt()
        var heightNew= size
        if(max == "height"){
            widthNew= size
            heightNew= sizeMaxNew.toInt()
        }

        return Bitmap.createScaledBitmap(bm, widthNew, heightNew, false)
    }

    fun savePict(bm: Bitmap, pathFile: String,
                 fileName: String= sidev.lib.universal.tool.util.TimeUtil.timestamp()
    ): File? {
        val fileNameExt= fileName +".jpg"
        val pathFile= File(pathFile)
        val file = File("$pathFile/$fileNameExt")
        try {
            pathFile.mkdirs()
            file.createNewFile()

            val bos = ByteArrayOutputStream()
            bm.compress(Bitmap.CompressFormat.JPEG, 100, bos)
            val bitmapdata = bos.toByteArray()

            val fos = FileOutputStream(file)
            fos.write(bitmapdata)
            fos.flush()
            fos.close()
            return file
        } catch (e: Exception) {
            throw e
        }
    }

    fun extractBitmapFromIntent(ctx: Context, intent: Intent?): Bitmap?{
        val uri = intent?.data
        if(uri != null){
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(ctx.contentResolver, uri)
//                bitmap= resizePict(bitmap, 500)
                return bitmap
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return null
    }

    fun from(c: Context, dir: String?= null, file: File?= null): Bitmap? {
//        Log.e("ActFragUtil", "getBitmap requestCode MASUK!!!")

        val uri = try{ Uri.parse(dir) ?: File(dir!!).toUri() }
            catch (e: Exception){ file?.toUri() } //ActFragUtil.intent!!.data!!
        return try {
            var bitmap = MediaStore.Images.Media.getBitmap(c.contentResolver, uri)
            bitmap= resizePict(bitmap, 500)

            val pictFile = savePict(bitmap, _EnvUtil.projectTempDir(c))
            bitmap
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    fun decode(dir: String): Bitmap? {
        val imgFile = File(dir)
        return if (imgFile.exists())
            BitmapFactory.decodeFile(imgFile.absolutePath)
        else null
    }

/*
    /**
     * Crop gambar tepat di tengah
     */
    fun cropFrame(frame: Frame, newWidth: Int, newHeight: Int): Frame{
        val width = frame.metadata.width
        val height = frame.metadata.height

        val right = width / 2 + newWidth / 2
        val left = width / 2 - newWidth / 2
        val bottom = height / 2 + newHeight / 2
        val top = height / 2 - newHeight / 2
/*
        val right = width / 2 + mBoxHeight / 2
        val left = width / 2 - mBoxHeight / 2
        val bottom = height / 2 + mBoxWidth / 2
        val top = height / 2 - mBoxWidth / 2
 */

        val yuvImage =
            YuvImage(frame.grayscaleImageData.array(), ImageFormat.NV21, width, height, null)
        val byteArrayOutputStream = ByteArrayOutputStream()
        yuvImage.compressToJpeg(Rect(left, top, right, bottom), 100, byteArrayOutputStream)
        val jpegArray = byteArrayOutputStream.toByteArray()
        val bitmap = BitmapFactory.decodeByteArray(jpegArray, 0, jpegArray.size)

        val croppedFrame = Frame.Builder()
            .setBitmap(bitmap)
            .setRotation(frame.metadata.rotation)
            .build()

        return croppedFrame
    }
 */
}