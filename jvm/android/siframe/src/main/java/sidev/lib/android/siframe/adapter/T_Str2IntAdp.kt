package sidev.lib.android.siframe.adapter
/*
import android.content.Context
import sidev.lib.jvm.android.adapter.layoutmanager.LinearLayoutManagerResp

open class T_Str2IntAdp<D: Str2IntModel>(c: Context, dataList: ArrayList<D>?)
    : SimpleAbsRecyclerViewAdapter<D, LinearLayoutManagerResp>(c, dataList){
    override val itemLayoutId: Int
        get() = R.layout.item_bound_track_amount

    var txtUpper= "outbound"
        protected set
    var txtLower= "keluar"
        protected set

    protected var intUpper= 0
    protected var intLower= 0

    var isAmountFixed= true
        set(v){
            field= v
            notifyDataSetChanged_()
        }

    var unit= "pcs"
        set(v){
            field= v
            if(dataListFull != null){
                for(data in dataListFull!!)
                    data.txt2= "($v)"
                notifyDataSetChanged_()
            }
        }

    override fun bindVH(vh: SimpleViewHolder, pos: Int, data: D) {
        val v= vh.itemView
        v.tv_title.text= data.txt1
//        containerView.et_number.setText(data.int.toString())
        v.tv_unit.text= data.txt2

        val numberPicker= NumberPickerComp(ctx, v.rl_number_picker_container)
        numberPicker.setCount(data.int)
        numberPicker.setOnCountChange { v, before, after, direction ->
            modifyDataAt(pos){ dataInt ->
                dataInt.int= after
                dataInt
            }
        }
    }

    override fun setupLayoutManager(): LinearLayoutManagerResp {
        return LinearLayoutManagerResp(ctx)
    }

    /**
     * Gunakan method ini untuk nge-set @see intUpper dan @see intLower
     * biar lebih teratur
     * @param intUpper untuk menunjukan total diminta
     * @param intLower untuk menunjukan progres
     */
    fun setNumber(intUpper: Int= this.intUpper, intLower: Int= this.intLower){
        this.intUpper= intUpper
        this.intLower= intLower
        if(dataListFull != null){
            dataListFull!![0].int= intUpper
            dataListFull!![1].int= intLower
            dataListFull!![2].int= intUpper -intLower
            notifyDataSetChanged_()
        }
    }

    /**
     * Gunakan method ini untuk nge-set @see trackTxtUpper dan @see intLower
     * biar lebih teratur
     */
    fun setText(txtUpper: String= this.txtUpper, txtLower: String= this.txtLower){
        this.txtUpper= txtUpper
        this.txtLower= txtLower
        if(dataListFull != null){
            dataListFull!![0].txt1= txtUpper
            dataListFull!![1].txt1= txtLower
            notifyDataSetChanged_()
        }
    }
}
 */