package sidev.lib.android.siframe.customizable._init

import androidx.annotation.StringRes
import sidev.lib.android.siframe.customizable.R

/**
 * Isi dari kelas ini jangan diubah.
 * Mengubah nilai dari variabel yang ada diperbolehkan
 * untuk melakukan penyesuaian.
 */
object _ConfigBase {
    val DEBUG= true
    val LOG= DEBUG

    val STRING_APP_NAME= R.string.app_name

    val LAYOUT_ACT_SIMPLE= R.layout._sif_act_simple
    val LAYOUT_ACT_SINGLE_FRAG= R.layout._sif_act_single_frag
    val LAYOUT_COMP_ACT_BAR_DEFAULT= R.layout._sif_comp_action_bar_default
//    val LAYOUT_ACT_BAR_SIMPLE= 0 //blum ada layout ternyata
    val LAYOUT_ITEM_ADP_CONTAINER= R.layout._sif_item_adp_container
    val LAYOUT_BLANK= R.layout._sif_page_blank
    val LAYOUT_TXT_CENTER= R.layout._sif_page_txt_center
    val LAYOUT_RV= R.layout._sif_page_rv //R.layout.content_abs_rv


    //Common ID
    val ID_RV= R.id.rv
    val ID_SRL= R.id.srl
    val ID_TV= R.id.tv
    //Specific ID
    val ID_FR_CONTAINER= R.id.fr_container
    val ID_IV_BACK= R.id.iv_back
    val ID_IV_CHECK= R.id.iv_check
    val ID_IV_OVERLAY= R.id.iv_overlay
    val ID_TV_TITLE= R.id.tv_title
    val ID_LL_CONTENT_CONTAINER= R.id.ll_content_container
    val ID_LL_BAR_ACT_CONTAINER= R.id.ll_bar_act_container
    val ID_LL_BAR_NAV_CONTAINER= R.id.bnv_bar_nav_container

    val STYLE_NO_ACT_BAR= R.style.AppTheme_NoActionBar
    val STYLE_APP= R.style.AppTheme
}