package sidev.lib.implementation._simulation.edc.adp
/*
import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import id.go.surabaya.ediscont.R
import id.go.surabaya.ediscont.activities.OrderDetailAct
import id.go.surabaya.ediscont.activities.TransactionDetailAct
import id.go.surabaya.ediscont.models.Transaction
import id.go.surabaya.ediscont.utilities.*
import id.go.surabaya.ediscont.utilities.modelutil.firstObj
import id.go.surabaya.ediscont.views.OrderSellView
import id.go.surabaya.ediscont.views.TransactionView
import kotlinx.android.synthetic.main.comp_item_list_text_left_3_right_2.view.*
import org.jetbrains.anko.layoutInflater
import org.jetbrains.anko.startActivity

//<Alif, 10 Mei 2020> <tahap2> => perubahan struktur Transaction
class TransactionListAdapter(val ctx: Context,
                             var transactionList: ArrayList<Transaction>?,
                             val view: TransactionView, val isOrder: Boolean)
    : RecyclerView.Adapter<TransactionListAdapter.TransactionVH>(){

    var initialList = transactionList

    val BELI= Config.STR_BELI
    val JUAL= Config.STR_JUAL

    inner class TransactionVH(v: View): RecyclerView.ViewHolder(v){
        fun bind(pos: Int){
            val transaction= transactionList!![pos]
            //val commUser= transaction.commUser.firstObj()!!
            //val commodity= commUser.commodity.firstObj()!!
            val commodity= transaction.commodity

            //menyiapkan string arah penjualan
            var transactionDirStr= transactionList!![pos].personName
            var transactionType=
                if(transactionList!![pos].orderType == Constants.TYPE_TRANSACTION_BUY) {
                    transactionDirStr= Config.STR_BELI_TRANS.toLowerCase() +" " +transactionDirStr
                    BELI
                } //ctx.getString(R.string.transaction_type_buy)
                else {
                    transactionDirStr= Config.STR_JUAL_TRANS.toLowerCase() +" "  +transactionDirStr //"jual ke " +transactionDirStr
                    JUAL
                } //ctx.getString(R.string.transaction_type_sell)
            Log.e("TRANSAC_ADP", "bind() pos= $pos")
            //menyiapkan string harga dan jml
            val commSum= transaction.sum
            //val priceStr= "Rp. ${Util.convertToFormattedValue(commUser!!.price!! * commSum !!.toLong())}"
            val priceStr= "Rp. ${Util.convertToFormattedValue(commodity!!.price!! * commSum !!.toLong())}"
            val priceSumStr= "$priceStr / $commSum unit"

            //menyiapkan string tanggal transaksi
            val transDate= transaction.date
            //val itemName= commodity.name +" (" +commUser.unit.firstObj()!!.name +")"
            val unit= commodity.unit.name
            val itemName= commodity.name +" ($unit)"

            itemView.tv_title.text= itemName
            itemView.tv_desc_1.text= transactionDirStr
            itemView.tv_desc_2.text= priceSumStr
//            itemView.tv_title_right.text= transactionType
            itemView.tv_desc_right_1.text= transDate


            when(transactionList!![pos].status){
                Constants.STATUS_TRANSACTION_UNCONFIRMED -> {
                    transactionType += "\n (Belum dikonfirmasi)"
                    itemView.tv_title_right.setTextColor(ctx.resources.getColor(R.color.black))
                    if(isOrder){
                        itemView.tv_title_right.text= "Menunggu Konfirmasi"
                        itemView.iv_indicator_right.visibility = View.GONE
                    }
                    else{
                        itemView.iv_indicator_right.visibility = View.VISIBLE
                        itemView.iv_indicator_right.setImageDrawable(ctx.resources.getDrawable(R.drawable.ic_clock))
                        itemView.iv_indicator_right.setColorFilter(ContextCompat.getColor(ctx, R.color.black), android.graphics.PorterDuff.Mode.SRC_IN)
                    }
                }

                Constants.STATUS_TRANSACTION_REJECTED -> {
                    if(isOrder)
                        itemView.tv_title_right.text= "Kadaluwarsa"
                    else {
                        val transStrAdd=
                            if(transactionType == BELI) "Ditolak"
                            else "Menolak"
                        transactionType += "\n ($transStrAdd)"
                        itemView.iv_indicator_right.visibility = View.VISIBLE
                        itemView.iv_indicator_right.setImageDrawable(ctx.resources.getDrawable(R.drawable.ic_cross))
                        itemView.iv_indicator_right.setColorFilter(ContextCompat.getColor(ctx, R.color.red), android.graphics.PorterDuff.Mode.SRC_IN)
                    }
                    itemView.tv_title_right.setTextColor(ctx.resources.getColor(R.color.red))
                }

                Constants.STATUS_TRANSACTION_APPROVED -> {
                    transactionType += "\n (Berhasil)"
                    itemView.iv_indicator_right.visibility = View.VISIBLE
                    itemView.iv_indicator_right.setImageDrawable(ctx.resources.getDrawable(R.drawable.ic_check))
                    itemView.iv_indicator_right.setColorFilter(ContextCompat.getColor(ctx, R.color.green), android.graphics.PorterDuff.Mode.SRC_IN)
                    itemView.tv_title_right.setTextColor(ctx.resources.getColor(R.color.green))
                }

                Constants.STATUS_TRANSACTION_CANCEL_REQUESTED -> {
                    itemView.iv_indicator_right.visibility = View.GONE
                    itemView.tv_title_right.setTextColor(ctx.resources.getColor(R.color.red))
//                    itemView.tv_title_right.text= "Momohon Pembatalan"
                    transactionType += "\n (Momohon Pembatalan)"
                }

                Constants.STATUS_TRANSACTION_CANCELED -> {
                    transactionType += "\n (Dibatalkan)"
                    itemView.iv_indicator_right.visibility = View.VISIBLE
                    itemView.iv_indicator_right.setImageDrawable(ctx.resources.getDrawable(R.drawable.ic_cross))
                    itemView.iv_indicator_right.setColorFilter(ContextCompat.getColor(ctx, R.color.red), android.graphics.PorterDuff.Mode.SRC_IN)
                    itemView.tv_title_right.setTextColor(ctx.resources.getColor(R.color.red))
                }
            }

//            itemView.tv_desc_1.text= transactionDirStr
            itemView.tv_title_right.text= transactionType

/*
=======================
Bagian setOnClickListener -START
=======================
 */
            if(view is OrderSellView) {
                view.updateOrderCount(transactionList!!.size)
                itemView.setOnClickListener {
                    view.comfirmOrder(transactionList!![pos]._id.toInt())
                }
            }

            if(isOrder)
                itemView.setOnClickListener {
                    if(transaction.status == Constants.STATUS_TRANSACTION_REJECTED){
                        showExpiredWarning()
                    } else
                        ctx.startActivity<OrderDetailAct>(
                            Constants.EXTRA_TRANSACTION_ITEM to transaction
                        )
                }
            else
                itemView.setOnClickListener {
                    ctx.startActivity<TransactionDetailAct>(
                        Constants.EXTRA_TRANSACTION_ITEM to transaction
                    )
                }
/*
=======================
Bagian setOnClickListener -END
=======================
 */
        }
    }

    fun showExpiredWarning(){
        // untuk peringatan jika kadaluarsa
        var textConfirm= "Mohonn maaf, Order telah kadaluwarsa!"

        val dialV= ctx.layoutInflater.inflate(R.layout.dialog_confirm, null)
        val dialog = DialogUtil.initAlertDialog(dialV)

        (dialV.findViewById<TextView>(R.id.tv_title))!!.text = textConfirm

        val leftBtn= dialV.findViewById<Button>(R.id.btn_left)!!
        val rightBtn= dialV.findViewById<Button>(R.id.btn_right)!!

        leftBtn.visibility = View.INVISIBLE
        rightBtn.text = "Kembali"

        leftBtn.setOnClickListener { dialog.dismiss() }
        rightBtn.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    fun updateData(transactionList: ArrayList<Transaction>, isInit: Boolean){
        if(isInit)
            initialList = transactionList
        this.transactionList= transactionList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): TransactionVH {
        val v= LayoutInflater.from(ctx).inflate(R.layout.comp_item_list_text_left_3_right_2, null)
        return TransactionVH(v)
    }

    override fun getItemCount(): Int {
        return when(transactionList){
            null -> 0
            else -> transactionList!!.size
        }
    }

    override fun onBindViewHolder(vh: TransactionVH, pos: Int) {
        vh.bind(pos)
    }

    fun sort(by: Int){

        when(by) {
            Constants.SORT_TRANSACTION_NAME -> {
                val tempList = transactionList
                for(i in 0 until tempList!!.size - 1){
                    var minItemIndex = i
                    //val commI= tempList[i].commUser.firstObj()!!.commodity.firstObj()!!
                    val commI= tempList[i].commodity
                    var minVal = commI!!.name

                    for(j in i until tempList.size){
                        //val commJ= tempList[j].commUser.firstObj()!!.commodity.firstObj()!!
                        val commJ= tempList[j].commodity
                        if(commJ!!.name!! < minVal!!) {
                            minItemIndex = j
                            minVal = commJ!!.name
                        }
                    }

                    val temp = tempList[i]
                    tempList[i] = tempList[minItemIndex]
                    tempList[minItemIndex] = temp
                }

                updateData(tempList, false)
            }

            Constants.SORT_TRANSACTION_PRICE -> {
                val tempList = transactionList
                for(i in 0 until tempList!!.size - 1){
                    var minItemIndex = i
                    //val commUserI= tempList[i].commUser.firstObj()!!
                    val commUserI= tempList[i].commodity
                    var minVal = commUserI!!.price!!*tempList[i].sum!!

                    for(j in i until tempList.size){
                        //val commUserJ= tempList[j].commUser.firstObj()!!
                        val commUserJ= tempList[j].commodity
                        if(commUserJ!!.price!!*tempList[j].sum!! < minVal) {
                            minItemIndex = j
                            minVal = commUserJ!!.price!!*tempList[j].sum!!
                        }
                    }

                    val temp = tempList[i]
                    tempList[i] = tempList[minItemIndex]
                    tempList[minItemIndex] = temp
                }

                updateData(tempList, false)
            }

            Constants.SORT_TRANSACTION_DATE -> {
                val tempList = transactionList
                for(i in 0 until tempList!!.size - 1){
                    var minItemIndex = i
                    var minVal = tempList[i].date!!

                    for(j in i until tempList.size){
                        if(tempList[j].date!! < minVal) {
                            minItemIndex = j
                            minVal = tempList[j].date!!
                        }
                    }

                    val temp = tempList[i]
                    tempList[i] = tempList[minItemIndex]
                    tempList[minItemIndex] = temp
                }

                updateData(tempList, false)
            }
        }
    }

    fun filter(by: Int){
        when(by) {
            Constants.FILTER_TRANSACTION_UNCONFIRM -> {
                val filteredList = ArrayList<Transaction>()

                for(i in 0 until transactionList!!.size)
                    if(transactionList!![i].status == Constants.STATUS_TRANSACTION_UNCONFIRMED)
                        filteredList.add(transactionList!![i])

                updateData(filteredList, false)
            }

            Constants.FILTER_TRANSACTION_REJECTED -> {
                val filteredList = ArrayList<Transaction>()

                for(i in 0 until transactionList!!.size)
                    if(transactionList!![i].status == Constants.STATUS_TRANSACTION_REJECTED)
                        filteredList.add(transactionList!![i])

                updateData(filteredList, false)
            }

            Constants.FILTER_TRANSACTION_APPROVED -> {
                val filteredList = ArrayList<Transaction>()

                for(i in 0 until transactionList!!.size)
                    if(transactionList!![i].status == Constants.STATUS_TRANSACTION_APPROVED)
                        filteredList.add(transactionList!![i])

                updateData(filteredList, false)
            }

            Constants.FILTER_TRANSACTION_BUY -> {
                val filteredList = ArrayList<Transaction>()

                for(i in 0 until transactionList!!.size)
                    if(transactionList!![i].orderType == Constants.TYPE_TRANSACTION_BUY)
                        filteredList.add(transactionList!![i])

                updateData(filteredList, false)
            }

            Constants.FILTER_TRANSACTION_SELL -> {
                val filteredList = ArrayList<Transaction>()

                for(i in 0 until transactionList!!.size)
                    if(transactionList!![i].orderType == Constants.TYPE_TRANSACTION_SELL)
                        filteredList.add(transactionList!![i])

                updateData(filteredList, false)
            }
        }
    }

    fun reset(){
        updateData(initialList!!, false)
    }

    fun search(keyword: String){
        val result = ArrayList<Transaction>()

        if(initialList != null) {
            if(initialList!!.size > 0) {
                for(i in 0 until initialList!!.size){
                    //val comm= initialList!![i].commUser.firstObj()!!.commodity.firstObj()!!
                    val comm= initialList!![i].commodity
                    if(Util.searchString(comm!!.name!!, keyword)) {
                        result.add(initialList!![i])
                    }
                }

                updateData(result, false)
            }
        }
    }
}

 */