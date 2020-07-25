package com.sigudang.android.models

//import com.sigudang.android._Dummy.sendKindModel_personal
import sidev.lib.implementation._simulation.sigudang.dummy.sendKindModel_personal
import java.io.Serializable

/**
 * Digunakan saat satu transaksi bound memiliki alamat dan jenis pengiriman yang sama
 */
data class BoundSendModel(var receipt: ReceiptModel, //var productSend: BoundProductSendModel)
                          var address: String= "", var method: SendMethodModel?= sendKindModel_personal)
    : Serializable