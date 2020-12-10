package sidev.lib.android.siframe.tool.util

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.Fragment
import sidev.lib.android.siframe.`val`._SIF_Constant

object _SIF_BitmapUtil {
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
}