package sidev.lib.implementation._simulation.edc.adp

import android.content.Context
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.comp_item_list_text_left_3_right_2.view.*
import org.jetbrains.anko.startActivity
import sidev.lib.android.siframe.adapter.RvAdp
import sidev.lib.android.siframe.adapter.layoutmanager.LinearLm
import sidev.lib.implementation.R
import sidev.lib.implementation._simulation.edc.model.Transaction
import sidev.lib.implementation._simulation.edc.util.Edc_Config
import sidev.lib.implementation._simulation.edc.util.Edc_Const
import sidev.lib.implementation._simulation.edc.util.Edc_Util

class TransacAdp(c: Context) : RvAdp<Transaction, LinearLm>(c){
    override val itemLayoutId: Int
        get() = R.layout.comp_item_list_text_left_3_right_2


    val BELI= Edc_Config.STR_BELI
    val JUAL= Edc_Config.STR_JUAL

    var isOrder= true

    override fun bindVH(vh: SimpleViewHolder, pos: Int, data: Transaction) {
        val v= vh.itemView
        val transaction= data //transactionList!![pos]
        //val commUser= transaction.commUser.firstObj()!!
        //val commodity= commUser.commodity.firstObj()!!
        val commodity= transaction.commodity

        //menyiapkan string arah penjualan
        var transactionDirStr= transaction.personName //transactionList!![pos].personName
        var transactionType=
            if(transaction.orderType == Edc_Const.TYPE_TRANSACTION_BUY) {
                transactionDirStr= Edc_Config.STR_BELI_TRANS.toLowerCase() +" " +transactionDirStr
                BELI
            } //ctx.getString(R.string.transaction_type_buy)
            else {
                transactionDirStr= Edc_Config.STR_JUAL_TRANS.toLowerCase() +" "  +transactionDirStr //"jual ke " +transactionDirStr
                JUAL
            } //ctx.getString(R.string.transaction_type_sell)
        Log.e("TRANSAC_ADP", "bind() pos= $pos")
        //menyiapkan string harga dan jml
        val commSum= transaction.sum
        //val priceStr= "Rp. ${Util.convertToFormattedValue(commUser!!.price!! * commSum !!.toLong())}"
        val priceStr= "Rp. ${Edc_Util.convertToFormattedValue(commodity!!.price!! * commSum !!.toLong())}"
        val priceSumStr= "$priceStr / $commSum unit"

        //menyiapkan string tanggal transaksi
        val transDate= transaction.date
        //val itemName= commodity.name +" (" +commUser.unit.firstObj()!!.name +")"
        val unit= commodity.unit.name
        val itemName= commodity.name +" ($unit)"

        v.tv_title.text= itemName
        v.tv_desc_1.text= transactionDirStr
        v.tv_desc_2.text= priceSumStr
//            itemView.tv_title_right.text= transactionType
        v.tv_desc_right_1.text= transDate


        when(transaction.status){
            Edc_Const.STATUS_TRANSACTION_UNCONFIRMED -> {
                transactionType += "\n (Belum dikonfirmasi)"
                v.tv_title_right.setTextColor(ctx.resources.getColor(R.color.hitam))
                if(isOrder){
                    v.tv_title_right.text= "Menunggu Konfirmasi"
                    v.iv_indicator_right.visibility = View.GONE
                }
                else{
                    v.iv_indicator_right.visibility = View.VISIBLE
                    v.iv_indicator_right.setImageDrawable(ctx.resources.getDrawable(R.drawable.ic_clock))
                    v.iv_indicator_right.setColorFilter(ContextCompat.getColor(ctx, R.color.hitam), android.graphics.PorterDuff.Mode.SRC_IN)
                }
            }

            Edc_Const.STATUS_TRANSACTION_REJECTED -> {
                if(isOrder)
                    v.tv_title_right.text= "Kadaluwarsa"
                else {
                    val transStrAdd=
                        if(transactionType == BELI) "Ditolak"
                        else "Menolak"
                    transactionType += "\n ($transStrAdd)"
                    v.iv_indicator_right.visibility = View.VISIBLE
                    v.iv_indicator_right.setImageDrawable(ctx.resources.getDrawable(R.drawable.ic_cross))
                    v.iv_indicator_right.setColorFilter(ContextCompat.getColor(ctx, R.color.merah), android.graphics.PorterDuff.Mode.SRC_IN)
                }
                v.tv_title_right.setTextColor(ctx.resources.getColor(R.color.merah))
            }

            Edc_Const.STATUS_TRANSACTION_APPROVED -> {
                transactionType += "\n (Berhasil)"
                v.iv_indicator_right.visibility = View.VISIBLE
                v.iv_indicator_right.setImageDrawable(ctx.resources.getDrawable(R.drawable.ic_check))
                v.iv_indicator_right.setColorFilter(ContextCompat.getColor(ctx, R.color.ijo), android.graphics.PorterDuff.Mode.SRC_IN)
                v.tv_title_right.setTextColor(ctx.resources.getColor(R.color.ijo))
            }

            Edc_Const.STATUS_TRANSACTION_CANCEL_REQUESTED -> {
                v.iv_indicator_right.visibility = View.GONE
                v.tv_title_right.setTextColor(ctx.resources.getColor(R.color.merah))
//                    itemView.tv_title_right.text= "Momohon Pembatalan"
                transactionType += "\n (Momohon Pembatalan)"
            }

            Edc_Const.STATUS_TRANSACTION_CANCELED -> {
                transactionType += "\n (Dibatalkan)"
                v.iv_indicator_right.visibility = View.VISIBLE
                v.iv_indicator_right.setImageDrawable(ctx.resources.getDrawable(R.drawable.ic_cross))
                v.iv_indicator_right.setColorFilter(ContextCompat.getColor(ctx, R.color.merah), android.graphics.PorterDuff.Mode.SRC_IN)
                v.tv_title_right.setTextColor(ctx.resources.getColor(R.color.merah))
            }
        }

//            itemView.tv_desc_1.text= transactionDirStr
        v.tv_title_right.text= transactionType

/*
=======================
Bagian setOnClickListener -START
=======================
 */
/*
        if(view is OrderSellView) {
            view.updateOrderCount(transactionList!!.size)
            v.setOnClickListener {
                view.comfirmOrder(transaction._id.toInt())
            }
        }

        if(isOrder)
            v.setOnClickListener {
                if(transaction.status == Edc_Const.STATUS_TRANSACTION_REJECTED){
                    showExpiredWarning()
                } else
                    ctx.startActivity<OrderDetailAct>(
                        Edc_Const.EXTRA_TRANSACTION_ITEM to transaction
                    )
            }
        else
            v.setOnClickListener {
                ctx.startActivity<TransactionDetailAct>(
                    Edc_Const.EXTRA_TRANSACTION_ITEM to transaction
                )
            }
 */
/*
=======================
Bagian setOnClickListener -END
=======================
 */
    }

    override fun setupLayoutManager(context: Context): LinearLm = LinearLm(context)
}