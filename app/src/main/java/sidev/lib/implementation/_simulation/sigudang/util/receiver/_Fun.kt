package com.sigudang.android.utilities.receiver

import android.content.Context
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.sigudang.android.Model.Warehouse
import com.sigudang.android.fragments.bottomsheet.BsPickDateFrag
import com.sigudang.android.fragments.bottomsheet.BsWarehouseSelectFr

/*
fun CharSequence.toInt(): Int? = try{ toString().toInt() } catch (e: Exception){null}
*/

fun createPopupDatePicker(oldPopup: BsPickDateFrag?= null,
                                     title: String= "<judul>",
                                     desc: String= "<desc>",
                                     btnComfirmTxt: String= "<btn_confirm>",
                                     l: (String?) -> Unit): BsPickDateFrag{
    val popup= oldPopup ?: BsPickDateFrag()
    popup.setTitle(title)
    popup.setDescription(desc)
    popup.setBtnConfirmText(btnComfirmTxt)
    popup.onBsBtnlickListener= l
//    popup.show(supportFragmentManager, "")
    return popup
}
fun createPopupWarehousePicker(oldPopup: BsWarehouseSelectFr?= null,
                                     title: String= "<judul>",
                                     desc: String= "<desc>",
                                     btnComfirmTxt: String= "<btn_confirm>",
                                     l: (Warehouse?) -> Unit): BsWarehouseSelectFr{
    val popup= oldPopup ?: BsWarehouseSelectFr()
    popup.setTitle(title)
    popup.setDescription(desc)
    popup.setBtnConfirmText(btnComfirmTxt)
    popup.onBsBtnlickListener= l
//    popup.show(supportFragmentManager, "")
    return popup
}