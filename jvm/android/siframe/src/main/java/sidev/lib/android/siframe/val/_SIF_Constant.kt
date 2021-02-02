package sidev.lib.android.siframe.`val`

import androidx.fragment.app.Fragment
import sidev.lib.android.std.`val`._Constant
import kotlin.reflect.KClass

/**
 * Untuk val yg berawalan dg _ merupakan suffix.
 */
object _SIF_Constant{
    internal object Internal{
        val TAG_SINGLE_FRAG_ACT= "_sif_tag_single_frag_act_"
        val TAG_FRAG_PREFIX= "_sif_tag_frag_"
        val TAG_TOP_MIDDLE_BOTTOM_PREFIX= "_sif_tag_top_middle_bottom_"
        val TAG_DEFAULT_PREFIX= "_sif_tag_default@klafaj_@_"

        fun <T: Fragment> getSingleFragActTag(fragClass: KClass<T>): String
            = "$TAG_SINGLE_FRAG_ACT::${fragClass.qualifiedName}"
        fun <T: Fragment> Any.getFragTag(fragClass: KClass<T>): String
            = "$TAG_FRAG_PREFIX::${this::class.qualifiedName}::${fragClass.qualifiedName}"
    }

    val TEMPLATE_VIEW_TYPE_ACT_BAR_DEFAULT= 0
    val TEMPLATE_VIEW_TYPE_ACT_BAR_SQUARE= 1

    val REQ_PERMISSION get()= _Constant.REQ_PERMISSION
    const val REQ_PERMISSION_CALLBACK_KEY: String = "_sif_permission_callback_"
    val REQ_PICK_GALLERY get()= _Constant.REQ_PICK_GALLERY
    val REQ_PICK_GALLERY_MULTIPLE get()= _Constant.REQ_PICK_GALLERY_MULTIPLE

    val REQ_CODE get()= _Constant.REQ_CODE
    val EXTRA_DATA get()= _Constant.EXTRA_DATA

    val MAIN_REF get()= _Constant.MAIN_REF
    val _KEY_PREF_EXP_TIME get()= _Constant._KEY_PREF_EXP_TIME

    val FRAGMENT_NAME get()= _Constant.FRAGMENT_NAME
    val EXTRA_IS_CUSTOM_ACT_BAR get()= _Constant.EXTRA_IS_CUSTOM_ACT_BAR
    val EXTRA_TITLE get()= _Constant.EXTRA_TITLE
    val CALLING_LIFECYCLE get()= _Constant.CALLING_LIFECYCLE

    const val DRAWER_START_LAYOUT_ID= "drawer_start_layout_id"
    const val DRAWER_END_LAYOUT_ID= "drawer_end_layout_id"

    const val PROP_STACK= "::STACK"

    //    const val EXTRA_CLASS_NAME= "class_name"
    const val EXTRA_TYPE_LATE= "type_late"
    const val EXTRA_DATA_ASYNC= "data_async"

    val BLOCK_VIEW_TAG= "block_view"

    const val STATIC_BOOLEAN= "stat_bool"
    const val STATIC_BOOLEAN_TYPE= "HashMap<String, Boolean>"
    const val STATIC_SINGLE_FRAG_LISTENER= "single_frag_liste"
    const val STATIC_SINGLE_FRAG_LISTENER_TYPE= "ArrayList<(Fragment) -> Unit>"

    /**
     * Tidak untuk dirubah oleh koder!!!
     */
    var REG_ACT_FUN_REGISTERER_NAME= ""
/*
        set(v){
            field= v
            Log.e("_SIF_Constant", "REG_ACT_FUN_REGISTERER_NAME= $v")
        }
 */
    
    /**
     * Tidak untuk dirubah oleh koder!!!
     */
    var REG_FRAG_FUN_REGISTERER_NAME= ""
/*
        set(v){
            field= v
            Log.e("_SIF_Constant", "REG_FRAG_FUN_REGISTERER_NAME= $v")
        }
 */
}