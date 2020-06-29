package sidev.lib.implementation._simulation.edc.util

object Edc_Config{
    var APP_STILL_VALID= false

    var STR_BELI= "Memesan"
    var STR_BELI_LONG= "Memesan"
    var STR_JUAL= "Dipesan"
    var STR_JUAL_LONG= "Pesanan masuk"
    var STR_BELI_TRANS= "$STR_BELI ke"
    var STR_JUAL_TRANS= "$STR_JUAL oleh"
    var STR_BELI_OMZET= "Total "
    var STR_JUAL_OMZET= "$STR_JUAL oleh"

    /*
    Tampilan dalam app
     */
    val ORDER_TIME_EXPIRED= 2 //jam
    val RESET_PSWD_TIME_EXPIRED= 3 //jam


    var RESET_PASS_UNAME_EXPIRATION_DURATION= RESET_PSWD_TIME_EXPIRED* 3600L //In sec
}