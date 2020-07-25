package com.sigudang.android.models

import java.io.Serializable

/**
 * Sama dengan model Bound, tapi punya 2 jenis jumlah, yaitu amountIn dan amountOut
 */
data class CrossdockingModel(var id: Long,
                             var invoice: String?, var date: String, var lessee: UserBusiness,
                             var warehouse: UserBusiness?= null,
                             private var _productList: ArrayList<BoundProduct>? = null,
                             private var _amountIn: ArrayList<Int>? = null,
                             private var _amountOut: ArrayList<Int>?= null,
                             var status: Int= 0,
                             var sender: ShipperModel?= null,
                             var send: BoundSendModel?= null): Serializable{

    var amountIn: List<Int>? = _amountIn
        private set(v){
            field= v
            _amountIn= v as ArrayList
        }
    var amountOut: List<Int>? = _amountOut
        private set(v){
            field= v
            _amountOut= v as ArrayList
        }

    var productList: ArrayList<BoundProduct>? = _productList
        set(v){
            if(v != null){
                //Jika sebelumnya productList null
                if(field == null){
                    amountIn= ArrayList()
                    amountOut= ArrayList()
                }

                if(amountIn!!.size < v.size)
                    for(i in 0 until (v.size -amountIn!!.size)){
                        (amountIn as ArrayList).add(0)
                        (amountOut as ArrayList).add(0)
                    }
            } else{
                amountIn= null
                amountOut= null
            }

            field= v
            _productList= v
        }

    var inbound: Bound?= null
        private set
        get(){
            if(field == null){
                field= Bound(id, invoice, date, lessee, warehouse, productList?.toCollection(ArrayList()), status= status, shipper= sender)
                for((i, product) in field!!.productList!!.withIndex()){
                    product.boundSend= null //Karena alamat dan jenis pengiriman inbound selalu sama untuk tiap productnya
                    product.amount= amountIn!![i]
                }
            }
            return field
        }
    var outbound: Bound?= null
        private set
        get(){
            if(field == null){
                field= Bound(id, invoice, date, lessee, warehouse, productList?.toCollection(ArrayList()), status = status, shipper = sender)
                for((i, product) in field!!.productList!!.withIndex())
                    product.amount= amountOut!![i]
            }
            return field
        }
}