package sidev.lib.android.siframe.tool.`var`

import android.util.Log
import android.view.View

/**
 * Untuk val yg berawalan dg _ merupakan suffix.
 */
object _SIF_Constant{
    val DIRECTION_UP= View.FOCUS_UP
    val DIRECTION_DOWN= View.FOCUS_DOWN

    val REQ_PERMISSION= 1
    val REQ_PICK_GALLERY= 2
    val REQ_PICK_GALLERY_MULTIPLE= 3

    val REQ_CODE= "req_code"
    val EXTRA_DATA= "extra_data"

    val MAIN_REF= "main_ref"
    val _KEY_PREF_EXP_TIME= "_pref_exp_time"

    val FRAGMENT_NAME= "frag_name"
    val EXTRA_IS_CUSTOM_ACT_BAR= "is_custom_act_bar"
    val EXTRA_TITLE= "title"
    val CALLING_LIFECYCLE= "calling_lifecycle"
    val DRAWER_START_LAYOUT_ID= "drawer_start_layout_id"
    val DRAWER_END_LAYOUT_ID= "drawer_end_layout_id"

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