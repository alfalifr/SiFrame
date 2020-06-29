package sidev.lib.implementation._simulation.edc.util

object Edc_Const {
    // tipe transaksi
    val TYPE_TRANSACTION_BUY = 1
    val TYPE_TRANSACTION_SELL = 2

    // status transaksi
    val STATUS_TRANSACTION_UNCONFIRMED = 1
    val STATUS_TRANSACTION_REJECTED = 2
    val STATUS_TRANSACTION_APPROVED = 3
    //<Alif -> Amir, 11 Mei 2020> <selesai:0> <baca> => ini status tambahan untuk mengakomodasi pembatalan order.
    val STATUS_TRANSACTION_CANCEL_REQUESTED = 4
    val STATUS_TRANSACTION_CANCELED = 5
}