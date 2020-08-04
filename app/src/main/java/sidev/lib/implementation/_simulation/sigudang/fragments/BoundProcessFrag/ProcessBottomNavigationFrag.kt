package com.sigudang.android.fragments.BoundProcessFrag

import android.view.View
import android.widget.Button
import com.sigudang.android.Model.Product
import com.sigudang.android.models.Bound
import kotlinx.android.synthetic.main._simul_sigud_fragment_bound_proses_bottom_btn.view.*
import sidev.lib.android.siframe.lifecycle.fragment.Frag
import sidev.lib.android.siframe.tool.util.`fun`.addOnGlobalLayoutListener
import sidev.lib.android.siframe.tool.util.`fun`.addTxtNumberBy
import sidev.lib.android.siframe.tool.util.`fun`.loge
import sidev.lib.implementation.R
import sidev.lib.universal.`fun`.isNull
import sidev.lib.universal.`fun`.notNull
import sidev.lib.universal.`fun`.round
import java.lang.Exception
import sidev.lib.universal.`fun`.plus

class ProcessBottomNavigationFrag : Frag(){
    override val layoutId: Int
        get() = R.layout._simul_sigud_fragment_bound_proses_bottom_btn

    lateinit var boundData: Bound

    val boundTotalAmount: Int
        get(){
            var total= 0
            if(boundData.productList != null)
                for(product in boundData.productList!!){
                    total += product.amount
                }
            return total
        }

    val boundTotalVolume: Double
        get(){
            var total= 0.0
            if(boundData.productList != null)
                for(product in boundData.productList!!){
                    total += (product.amount *product.product.volPerUnit)
                }
            return total
        }

    enum class BottomViewIndex{ SUMMARY, DECLINE, CONFIRM }
    var bottomViewList= ArrayList<View>()
        protected set
    var shownBottomInd_int: Array<Int> = Array(bottomViewList.size){it} //setShownColumnInd()
        set(v){
            v.sort()
            field= v
            for((i, view) in bottomViewList.withIndex()){
                view.visibility= View.GONE
            }

            for(ind in v){
                try{ bottomViewList[ind].visibility= View.VISIBLE }
                catch (e: Exception){ /*ignore*/ }
            }
        }

    override fun _initView(layoutView: View) {
        initBottomViewList()
        (layoutView.comp_btn_confirm as Button).text= "Lanjut"
        (layoutView.comp_btn_decline as Button).text= "Tolak"

        try{ readData() } catch (e: UninitializedPropertyAccessException){ /*ignore*/ }
    }

    fun initBottomViewList(){
        bottomViewList.clear()
        bottomViewList +
                layoutView.cv_summary_container +
                layoutView.comp_btn_decline +
                layoutView.comp_btn_confirm
    }

    fun readData(){
        try{
            layoutView.tv_summary_amount_number.text= boundTotalAmount.toString()
            layoutView.tv_summary_volume_number.text= boundTotalVolume.round(-2).toString()
        } catch (e: UninitializedPropertyAccessException){ /*ignore*/ }
/*
        bottomView.notNull { bottomView ->
            bottomView.tv_summary_amount_number.text= boundTotalAmount.toString()
            bottomView.tv_summary_volume_number.text= boundTotalVolume.round(-2).toString()
        }.isNull { //Jika bottoView msh null, maka tunggu saja hingga dipasang ke screen
            layoutView.addOnGlobalLayoutListener {
                bottomView!!.tv_summary_amount_number.text= boundTotalAmount.toString()
                bottomView!!.tv_summary_volume_number.text= boundTotalVolume.round(-2).toString()
            }
        }
*/
    }

    fun setProductAmount(product: Product, diff: Int){
        layoutView.tv_summary_amount_number.addTxtNumberBy(diff)
        val volum= layoutView.tv_summary_volume_number.addTxtNumberBy(diff *product.volPerUnit, -2)
        loge("volum= $volum diff= $diff")
    }
}