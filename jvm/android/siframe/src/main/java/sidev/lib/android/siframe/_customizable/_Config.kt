package sidev.lib.android.siframe._customizable

import android.app.Activity
//import sidev.lib.android.siframe.customizable.R
import sidev.lib.android.siframe.R

/**
 * Isi dari kelas ini jangan diubah.
 * Mengubah nilai dari variabel yang ada diperbolehkan
 * untuk melakukan penyesuaian.
 */
object _Config {
    object ExternalRef{
        val ID_CONTENT= android.R.id.content
    }

    /**
     * Flag apakah mode debug diijinkan dalam app ini.
     */
    var DEBUG= true

    /**
     * Flag apakah proses log diijinkan dalam app ini.
     *
     * Jika [LOG] di-assign true tapi [DEBUG] false, maka hasil akhirnya [LOG] false.
     * Artinya, [LOG] diijinkan jika [DEBUG] juga diijinkan.
     */
    var LOG= DEBUG
        get()= field && DEBUG

    /**
     * Flag apakah proses log yg disimpan pada file diijinkan dalam app ini.
     *
     * Jika [LOG_ON_FILE] di-assign true tapi [LOG] false, maka hasil akhirnya [LOG_ON_FILE] false.
     * Artinya, [LOG_ON_FILE] diijinkan jika [LOG] juga diijinkan.
     */
    var LOG_ON_FILE= false
        get()= field && LOG


    var INT_EMPTY= 0


    var DB_NAME= "DEFAULT"
    var DB_VERSION= 1


    var TEMPLATE_VIEW_ACT_BAR_TYPE=
        _Constant.TEMPLATE_VIEW_TYPE_ACT_BAR_DEFAULT


    var STRING_APP_NAME= R.string.app_name

    var DIMEN_ACT_BAR_HEIGHT= R.dimen.act_bar_height
    var DIMEN_ACT_BAR_OFFSET= R.dimen.act_bar_offset
    var DIMEN_ACT_BAR_OFFSET_NEG= R.dimen.act_bar_offset_neg
    var DIMEN_DRAWER_HORIZONTAL_WIDTH= R.dimen.drawer_horizontal_width
    var DIMEN_DRAWER_HORIZONTAL_WIDTH_PERCENT= R.dimen.drawer_horizontal_width_percent

    var LAYOUT_ACT_SIMPLE= R.layout._sif_act_simple
    var LAYOUT_ACT_SINGLE_FRAG= R.layout._sif_act_single_frag
    var LAYOUT_BG_COMP_ACT_BAR_DEFAULT= R.layout._sif_bg_act_bar_default
//    var LAYOUT_ACT_BAR_SIMPLE= 0 //blum ada layout ternyata
    var LAYOUT_ITEM_ADP_CONTAINER= R.layout._sif_item_adp_container
    var LAYOUT_OVERLAY_BLOCK= R.layout._sif_overlay_block
    var LAYOUT_DIALOG_CONTAINER= R.layout._sif_dialog_container
    var LAYOUT_DIALOG_CONFIRM= R.layout._sif_dialog_confirm
    var LAYOUT_DIALOG_LIST= R.layout._sif_dialog_list
    var LAYOUT_BLANK= R.layout._sif_page_blank
    var LAYOUT_TXT_CENTER= R.layout._sif_page_txt_center
    var LAYOUT_RV= R.layout._sif_page_rv //R.layout.content_abs_rv
    var LAYOUT_VP= R.layout._sif_page_vp //R.layout.content_abs_rv
    var LAYOUT_DL= R.layout._sif_page_drawer //R.layout.content_abs_rv


    var LAYOUT_COMP_ACT_BAR_DEFAULT= R.layout._sif_comp_action_bar_default
    var LAYOUT_COMP_BTN_ACTION= R.layout._sif_comp_btn_action
    var LAYOUT_COMP_ITEM_TXT_DESC= R.layout._sif_comp_item_txt_desc


    //Common ID
    var ID_RV= R.id.rv
    var ID_SRL= R.id.srl
    var ID_TV= R.id.tv
//    var ID_ET= R.id.et
    var ID_PB= R.id.pb
    var ID_VP= R.id.vp
//    var ID_SV= R.id.sv
    //Specific ID
    var ID_FR_CONTAINER= R.id.fr_container
    var ID_IV_ICON= R.id.iv_icon
    var ID_IV_BACK= R.id.iv_back
    var ID_IV_ARROW= R.id.iv_arrow
    var ID_IV_ACTION= R.id.iv_action
    var ID_IV_CHECK= R.id.iv_check
    var ID_IV_OVERLAY= R.id.iv_overlay
    var ID_IV_INDICATION= R.id.iv_indication
    var ID_IV_PLUS= R.id.iv_plus
    var ID_IV_MINUS= R.id.iv_minus
    var ID_TV_TITLE= R.id.tv_title
    var ID_TV_DESC= R.id.tv_desc
    var ID_TV_NO_DATA= R.id.tv_no_data
    var ID_ET_SEARCH= R.id.et_search
    var ID_ET_NUMBER= R.id.et_number
    var ID_BTN_RIGHT= R.id.btn_right
    var ID_BTN_LEFT= R.id.btn_left

    var ID_VG_CONTENT_CONTAINER= R.id.vg_content_container
    var ID_RL_BTN_CONTAINER= R.id.rl_btn_container
    var ID_RL_TOP_CONTAINER= R.id.rl_top_container
    var ID_RL_BOTTOM_CONTAINER= R.id.rl_bottom_container
//    var ID_RL_TOP_CONTAINER_OUTSIDE= R.id.rl_top_container_outside
//    var ID_RL_BOTTOM_CONTAINER_OUTSIDE= R.id.rl_bottom_container_outside
    var ID_RL_MIDDLE_CONTAINER= R.id.rl_middle_container

    var ID_VG_START_DRAWER_CONTAINER= R.id.vg_start_drawer_container
    var ID_VG_END_DRAWER_CONTAINER= R.id.vg_end_drawer_container
//    var ID_SL_BOTTOM_DRAWER_CONTAINER= R.id.sl_bottom_drawer_container
//    var ID_SL_TOP_DRAWER_CONTAINER= R.id.sl_top_drawer_container

//    var ID_VG_CONTENT_CONTAINER= R.id.ll_content_container
    var ID_LL_BAR_ACT_CONTAINER= R.id.ll_bar_act_container
    var ID_LL_BAR_NAV_CONTAINER= R.id.bnv_bar_nav_container
    var ID_LL_SEARCH_CONTAINER_OUTER= R.id.ll_search_container_outer
    var ID_LL_RV_CONTAINER= R.id.ll_rv_container

    var DRAW_IC_WARNING= R.drawable.ic_warning
    var DRAW_SHAPE_BORDER_ROUND= R.drawable.shape_border_round
    var DRAW_SHAPE_BORDER_ROUND_BG= R.drawable.shape_border_round_bg
//    var DRAW_SHAPE_SOLID_SQUARE_ROUND= R.drawable.shape_solid_square_round
    var DRAW_SHAPE_SOLID_RECT_ROUND_EDGE= R.drawable.shape_solid_rect_round_edge
    var DRAW_PSWD_SHOWN= R.drawable.ic_eye_yes
    var DRAW_PSWD_HIDDEN= R.drawable.ic_eye_no

    var STYLE_NO_ACT_BAR= R.style.AppTheme_NoActionBar
    var STYLE_APP= R.style.AppTheme

    var FORMAT_DATE= "dd/MM/yyyy"
    var FORMAT_TIME= "HH:mm:ss"
    var FORMAT_TIMESTAMP= "$FORMAT_DATE $FORMAT_TIME"

    var ENDPOINT_ROOT= ""

    var RES_OK= Activity.RESULT_OK
    var RES_NOT_OK= -2
}