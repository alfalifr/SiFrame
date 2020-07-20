package sidev.lib.implementation._simulation.edc.model

import id.go.surabaya.ediscont.models.Commodity
import java.io.Serializable

data class Transaction(val _id: String,
//                  var commUser: FK_M<CommodityUser__tahap2>?,  //<10 Mei 2020> <tahap2> => diganti jadi CommodityUser__tahap2
                  var commodity: Commodity?,
                  var personName: String?,
                  var date: String?, //Tipe data msh BLUM FIX!!!
                  var sum: Int?,
                  var orderType: Int?,
                  var status: Int?)
//                  var isCancelRequested: Boolean= false)
    : Serializable