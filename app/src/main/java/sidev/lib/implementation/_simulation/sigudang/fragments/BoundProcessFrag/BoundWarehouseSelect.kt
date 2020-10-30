package sidev.lib.implementation._simulation.sigudang.fragments.BoundProcessFrag

import android.content.Intent
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.CallSuper
import androidx.core.view.setPadding
import com.sigudang.android.Model.Warehouse
import com.sigudang.android.fragments.BoundProcessFrag.BoundProcessRootFrag
import com.sigudang.android.fragments.BoundProcessFrag.BoundVpProcessFrag
import com.sigudang.android.fragments.bottomsheet.BsWarehouseSelectFr
import com.sigudang.android.models.Bound
import com.sigudang.android.utilities.receiver.toUserBusiness
import kotlinx.android.synthetic.main._simul_sigud_component_bar_btn.view.*
import kotlinx.android.synthetic.main._simul_sigud_component_bar_title_dropdown.view.*
import kotlinx.android.synthetic.main._simul_sigud_fragment_bound_proses_bottom_btn.view.*
//import kotlinx.android.synthetic.main.component_bar_btn.view.*
//import kotlinx.android.synthetic.main.component_bar_title_dropdown.view.*
//import kotlinx.android.synthetic.main.fragment_bound_proses_bottom_btn.view.*
import sidev.lib.android.siframe.intfc.`fun`.InitPropFun
import sidev.lib.android.siframe.intfc.lifecycle.LifecycleViewBase
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.TopMiddleBottomBase
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.ViewPagerBase
import sidev.lib.android.siframe.lifecycle.fragment.Frag
import sidev.lib.android.siframe.tool.util.`fun`.dp
import sidev.lib.android.siframe.tool.util.`fun`.setPadding_
import sidev.lib.check.asNotNull
import sidev.lib.implementation.R
import sidev.lib.implementation._simulation.sigudang.dummy.warehouseList_full

open class BoundWarehouseSelect : Frag(), BoundProcessRootFrag, TopMiddleBottomBase, InitPropFun{
    final override var isInit: Boolean= false
    final override val layoutId: Int
        get() = R.layout._simul_sigud_component_bar_title_dropdown

    override var bottomContainer: ViewGroup?= null
    override var middleContainer: ViewGroup?= null
    override var topContainer: ViewGroup?= null
    override val bottomLayoutId: Int
        get() = R.layout._simul_sigud_fragment_bound_proses_bottom_btn

    lateinit var boundData: Bound

    lateinit var bsWarehouseSelect: BsWarehouseSelectFr
    var selectedWarehouse: Warehouse?= null
    private var selectedPos= -1


    @CallSuper
    override fun onActive(parentView: View?, callingLifecycle: LifecycleViewBase?, pos: Int) {
        initProp {
            callingLifecycle.asNotNull { vpFrag: BoundVpProcessFrag ->
                boundData= vpFrag.getBoundData(this)!!
            }
        }
    }

    override fun _initBottomView(bottomView: View) {
        (bottomView.comp_btn_confirm as Button).text= "Lanjut"
        bottomView.comp_btn_confirm.setOnClickListener {
            _prop_parentLifecycle.asNotNull { vpFrag: ViewPagerBase<*> -> vpFrag.pageForth() }
        }
    }

    @CallSuper
    override fun _initView(layoutView: View) {
        layoutView.setPadding(10.dp.toInt())
        bsWarehouseSelect= BsWarehouseSelectFr()
        bsWarehouseSelect.setTitle("Pilih Gudang")
        bsWarehouseSelect.setBtnConfirmText("Pilih")
        bsWarehouseSelect.dataList= warehouseList_full.toCollection(ArrayList())
        bsWarehouseSelect.onBsRvBtnClickListener= { v, data, pos ->
            Log.e("WarehouseSelectFrag", "data == null ${data == null}")
            selectedWarehouse= data //adp.getSelectedData()
//            editBoundData { boundData ->
                boundData.warehouse= selectedWarehouse?.toUserBusiness()
//            }
            Log.e("WarehouseSelectFrag", "boundProduct?.warehouse= ${data?.name}")
            selectedPos= pos
            val warehouseName=
                if(selectedWarehouse != null) selectedWarehouse!!.name
                else ""
            layoutView.comp_bar_dropdown.btn_bg.text= warehouseName
        }

        layoutView.tv_title.text= "Gudang"
        layoutView.comp_bar_dropdown.btn_bg.hint= "Pilih Gudang"
        layoutView.comp_bar_dropdown.btn_bg.setOnClickListener { v ->
            //            if(activity != null)
            bsWarehouseSelect.selectItem(selectedPos)
            bsWarehouseSelect.show(activity!!.supportFragmentManager, "")
        }
    }

    override fun _initMiddleView(middleView: View) {}
    override fun _initTopView(topView: View) {}
}