package com.sigudang.android.fragments.bottomsheet

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.sigudang.android.Model.Product
import com.sigudang.android.utilities.constant.Constants
import kotlinx.android.synthetic.main._simul_sigud_item_product_warehouse.view.*
import sidev.lib.implementation.R

class BsProductDetailFr : BottomSheetAbsFr<Nothing>() {
    override val bottomSheetType: BottomSheetType
        get() = BottomSheetType.NO_HEADER
    override val bsLayoutId: Int
        get() = R.layout._simul_sigud_content_bs_product_detail

    override fun initView(v: View) {
        showBtnConfirm(false)
        val product = arguments?.getSerializable(Constants.EXTRA_PRODUCT_DETAIL) as Product

        containerView?.findViewById<ImageView>(R.id.iv_product)?.setImageDrawable(resources.getDrawable(product.img.resId!!))
        containerView?.findViewById<TextView>(R.id.tv_product_name)?.text = product.name
        containerView?.findViewById<TextView>(R.id.tv_product_category)?.text = product.category?.name

        val whItemContainer = containerView?.findViewById<LinearLayout>(R.id.ll_product_warehouse)

        whItemContainer?.removeAllViews()

        for(i in 0..5) {
            val productWarehouseView =  layoutInflater.inflate(R.layout._simul_sigud_item_product_warehouse, null)

            productWarehouseView.tv_warehouse_name.text = "Arytama"
            productWarehouseView.tv_product_amount.text = "5"
            productWarehouseView.tv_product_volume.text = "0.3"

           whItemContainer?.addView(productWarehouseView)
        }
    }

    override fun createData(contentView: View): Nothing? {
        return null
    }
}