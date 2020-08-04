package com.sigudang.android.fragments.bottomsheet

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.Button
import com.sigudang.android.Model.Product
import com.sigudang.android.models.Category
import com.sigudang.android.models.Unit_
import com.sigudang.android.utilities.constant.Constants
import kotlinx.android.synthetic.main._simul_sigud_content_bs_add_product.view.*
//import kotlinx.android.synthetic.main.content_bs_add_product.view.*
//import kotlinx.android.synthetic.main.content_bs_add_product.view.comp_btn_add_photo
//import kotlinx.android.synthetic.main.content_bs_add_product.view.iv_pict_pick
//import kotlinx.android.synthetic.main.content_bs_add_product.view.v_pict_pick_click
import sidev.lib.implementation.R
import sidev.lib.implementation._simulation.sigudang.dummy.categoryProductList
import sidev.lib.implementation._simulation.sigudang.dummy.unitProductList
import sidev.lib.implementation._simulation.sigudang.util.T_BitmapUtil
import sidev.lib.implementation._simulation.sigudang.util.customview.ImageViewResp
import sidev.lib.universal.`fun`.notNull

class BsAddProductFr : BottomSheetAbsFr<Product>(), ImageViewResp.OnDrawableChangeListener {
    override val bottomSheetType: BottomSheetType
        get() = BottomSheetType.WITH_HEADER
    override val bsLayoutId: Int
        get() = R.layout._simul_sigud_content_bs_add_product

    lateinit var bsCategory: BsSimplestModelSelect<Category>
    lateinit var bsUnit: BsSimplestModelSelect<Unit_>

    private lateinit var category: Category
    private lateinit var unit: Unit_

    override fun initView(v: View) {
        setTitle("Tambah Produk")
        setDescription("Produkmu kesepian? sini ditemenin SiGudang!")

        initInnerBs()

        v.iv_pict_pick.onDrawableChangeListener= this
        v.v_pict_pick_click.setOnClickListener { v ->
            T_BitmapUtil.pickImageGallery(this)
        }
        (v.comp_btn_add_photo as Button).text= "Tambah Foto"
//        layoutView.comp_btn_change_pict_pick.visibility= View.GONE
        v.comp_btn_add_photo .setOnClickListener { v ->
            T_BitmapUtil.pickImageGallery(this)
        }

        v.add_product_et_category.setOnClickListener { v ->
            bsCategory.show(fragmentManager!!, "")
        }
        v.add_product_et_unit.setOnClickListener { v ->
            bsUnit.show(fragmentManager!!, "")
        }
    }

    fun initInnerBs(){
        bsCategory= BsSimplestModelSelect()
        bsCategory.dataList= categoryProductList.toCollection(ArrayList())
        bsCategory.onBsRvBtnClickListener= { v, data, pos ->
            contentView.add_product_et_category.setText(data?.name ?: "")
            data.notNull { category= it }
        }

        bsUnit= BsSimplestModelSelect()
        bsUnit.dataList= unitProductList.toCollection(ArrayList())
        bsUnit.onBsRvBtnClickListener= { v, data, pos ->
            contentView.add_product_et_unit.setText(data?.name ?: "")
            data.notNull { unit= it }
        }
    }


    var pictDir= ""

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == Constants.PICK_IMAGE_GALLERY_REQUEST && resultCode == Activity.RESULT_OK){
            pictDir= data?.data.toString()
            var bm= T_BitmapUtil.extractBitmapFromIntent(ctx!!, data)
            if(bm != null){
//                bm= T_BitmapUtil.resizePict(bm, 500)
                bm= T_BitmapUtil.pictSquare(bm, 500)
                contentView.iv_pict_pick.setImageBitmap(bm)
            }
        }
    }

    override fun createData(contentView: View): Product? {
        val name= contentView.add_product_et_name.text.toString()
        val l= contentView.add_product_et_length.text.toString().toDouble()
        val w= contentView.add_product_et_width.text.toString().toDouble()
        val h= contentView.add_product_et_height.text.toString().toDouble()

        return Product("wkwk", name, category, unit, l, w, h)
    }

    override fun onDrawableChange(v: View, drawable: Drawable?) {}
    override fun onBgDrawableChange(v: View, drawable: Drawable?) {}
    override fun onDrawableOverallChange(v: View) {}
}