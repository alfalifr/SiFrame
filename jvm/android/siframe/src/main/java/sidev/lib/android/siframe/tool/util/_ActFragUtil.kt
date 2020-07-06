package sidev.lib.android.siframe.tool.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import sidev.lib.android.siframe.customizable._init._Config
import sidev.lib.android.siframe.intfc.util.ResetableUtil
import sidev.lib.android.siframe.tool.`var`._SIF_Constant
import sidev.lib.android.siframe.tool.manager.ActManager
import java.io.File
import java.io.IOException
import java.lang.ClassCastException
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

    fun getRootView(act: Activity): View {
        return act.findViewById<View>(_Config.ExternalRef.ID_CONTENT).rootView
    }

    /**
     * @return view overlay agar dapat dihilangkan dengan mudah dari layar.
     *
     * Untuk sementara layout block msh hanya block biasa.
     * Ke depannya diharapkan block nya bisa berupa pengumuman dll.
     */
    fun blockLayout(c: Context): View? {
        val currentAct= try{ ActManager.peekStack() } //try{ (c.applicationContext as BaseApp).currentAct }
            catch (castExc: ClassCastException){
//                Log.e("_ActFragUtil", "blockLayout c is Activity = ${c is Activity}")
                if(c is Activity) c
                else null
            }
        return if(currentAct != null){
            val rootView= getRootView(currentAct) as ViewGroup
            val overlay= _ViewUtil.Template.overlayBlock(currentAct)
            rootView.addView(overlay)
            overlay.tag= _SIF_Constant.BLOCK_VIEW_TAG
            overlay.setOnClickListener {}

//            Log.e("_ActFragUtil", "blockLayout BERHASIL!!!")

            overlay
        } else null
    }

    /**
     * @return true jika block berhasil dihilangkan.
     *      false jika block gagal dihilangkan atau memang act tidak diblok dari awal.
     */
    fun openLayoutBlock(c: Context): Boolean {
        val currentAct= try{ ActManager.peekStack() } //try{ (c.applicationContext as BaseApp).currentAct }
        catch (castExc: ClassCastException){
//            Log.e("_ActFragUtil", "blockLayout c is Activity = ${c is Activity}")
            if(c is Activity) c
            else null
        }
        return if(currentAct != null){
            val rootView= getRootView(currentAct) as ViewGroup
            val overlay= rootView.findViewWithTag<View>(_SIF_Constant.BLOCK_VIEW_TAG)
            if(overlay != null){
                rootView.removeView(overlay)
                true
            } else false
        } else false
    }

    fun getResult(act: Activity, requestCode: Int, resultCode: Int, intent: Intent?): Result {
//        Log.e("ActFragUtil", "getResult requestCode == Constants.PICK_IMAGE_GALLERY_REQUEST = ${requestCode == _SIF_Constant.REQ_PICK_GALLERY}")
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
//                Log.e("ActFragUtil", "getBitmap requestCode == Constants.PICK_IMAGE_GALLERY_REQUEST = ${requestCode == _SIF_Constant.REQ_PICK_GALLERY}")
                if (requestCode == _SIF_Constant.REQ_PICK_GALLERY
                    && resultCode == AppCompatActivity.RESULT_OK
                    && intent != null && intent!!.data != null) {

//                    Log.e("ActFragUtil", "getBitmap requestCode MASUK!!!")
                    val uri = intent!!.data!!
                    try {
                        var bitmap = MediaStore.Images.Media.getBitmap(act!!.contentResolver, uri)
                        bitmap= _BitmapUtil.resizePict(bitmap, 500)

                        val pictFile = _BitmapUtil.savePict(bitmap, _EnvUtil.projectTempDir(act!!))
                        val fileDir= pictFile!!.absolutePath

                        func(bitmap, fileDir, pictFile)
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