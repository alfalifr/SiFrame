package com.sigudang.android.utilities.constant

import android.app.Activity
//import com.sigudang.android.BuildConfig
//import com.sigudang.android.fragments.BoundProcessFrag.viewpager.BoundOverviewFrag.crossdocking.BoundOverviewFrag_Cross_Abs

object Constants {
    val EXTRAS_PRODUCT_DETAIL = "extras_product_detail"

    val API_KEY = "Tada"//BuildConfig.SIGUDANG_API_KEY
    val CLIENT_ID = "Hihe" //BuildConfig.SERVER_CLIENT_ID
    val CLIENT_SECRET = "Ok"//BuildConfig.SERVER_CLIENT_SECRET

    val UNAUTHENTICATE_MESSAGE = "Unauthenticated"
    val SUCCESS_RESPONSE = "{\"code\" : 200, \"message\" : \"success response\"}"
    val ERROR_RESPONSE = "{\"code\" : 400, \"message\" : \"an error occured\"}"

    // ini adalah untuk semua response code
    val RESPONSE_SUCCESS = 200
    val RESPONSE_NEED_REFRESH = 100
    val RESPONSE_ERROR = 400
    val RESPONSE_ACCESS_DENIED = 403
    val RESPONSE_UNAUTHORIZED = 401
    val RESPONSE_DATA_ALREADY_EXIST = 103
    val RESPONSE_INCOMPLETE_REQUEST = 104
    val RESPONSE_DATA_EMPTY = 106

    // tipe untuk list commodity
    val COM_TYPE_TOKEL_MINE = 0
    val COM_TYPE_TOKEL_BUY = 1
    val COM_TYPE_KOPERASI_MINE = 2
    val COM_TYPE_KOPERASI_BUY = 3
    val COM_TYPE_DISTRIBUTOR_MINE = 4

    // tipe monitoring
    val MONITORING_TYPE_OPD = 1
    val MONITORING_TYPE_DISDAG = 2

    //tipe list transaksi
    val TRANSAC_TYPE_FRAGMENT = 0
    val TRANSAC_TYPE_CHILDFRAGMENT = 1
    val TRANSAC_TYPE_ACTIVITY = 2

    // untuk sorting list
    val SORT_COMMODITY_NAME = 0
    val SORT_COMMODITY_STOCK = 1
    val SORT_TRANSACTION_NAME = 0
    val SORT_TRANSACTION_PRICE = 1
    val SORT_TRANSACTION_DATE = 2

    // untuk filter lsit
    val FILTER_COMMODITY_AVAILABLE = 0
    val FILTER_COMMODITY_EMPTY = 1
    val FILTER_TRANSACTION_UNCONFIRM = 0
    val FILTER_TRANSACTION_REJECTED = 1
    val FILTER_TRANSACTION_APPROVED = 2
    val FILTER_TRANSACTION_BUY = 3
    val FILTER_TRANSACTION_SELL = 4

    // commodity detail id_order bar
    val ACTBAR_COM_DETAIL = 0
    val ACTBAR_COM_DETAIL_ORDER = 1

    // status transaksi
    val STATUS_TRANSACTION_UNCONFIRMED = 1
    val STATUS_TRANSACTION_REJECTED = 2
    val STATUS_TRANSACTION_APPROVED = 3

    // tipe transaksi
    val TYPE_TRANSACTION_BUY = 1
    val TYPE_TRANSACTION_SELL = 2

    val DETAIL_TYPE_REGULAR = 0
    val DETAIL_TYPE_KOPERASI = 1
    val DETAIL_TYPE_DISTRIBUTOR = 2

    val ORDERING_TYPE_FULLY_AVAILABLE = 0
    val ORDERING_TYPE_PARTIAL_AVAILABLE = 1
    val ORDERING_TYPE_UNAVAILABLE = 2
    val ORDERING_TYPE_ZERO = 3

    val MAP_TYPE_KEY = "map_type_key"
    val MAP_TYPE_KOPERASI = 411
    val MAP_TYPE_TOKEL = 412
    val MAP_TYPE_DISTRIBUTOR = 413

    val MAP_KEYS = arrayOf("admin", "koperasi", "kelontong", "swk", "ukm", "distributor")

    val INTENT_LAT_ADD = "intent_lat_add"
    val INTENT_LONG_ADD = "intent_long_add"
    val INTENT_RQ_LOC_ADD = 193

    // data untuk informasi
    val KEY_PREF_USER_ROLE_TYPE = "pref_user_role_type"
    val KEY_PREF_USER_LOGIN = "pref_user_profile"
    val KEY_PREF_IS_LOGGING_OUT = "is_logging_out"
    val USER_PROFILE_TYPE_OWN = 2345
    val USER_PROFILE_TYPE_OTHER = 5432

    // untuk response json
    val RESPONSE_NOTHING_CHANGED = "{\"code\" : 201}"

    //untuk notifikasi
    val NOTIF_TAG = "FcmMessagingService"
    val NOTIF_TITLE = "title"
    val NOTIF_BODY = "body"
    val NOTIF_TYPE = "type"
    val NOTIF_ID_ORDER = "id_order"

    // extra keys
    val EXTRA_TRANSACTION_ITEM = "extra_transaction_item"
    val EXTRA_PRODUCT_DETAIL = "extras_product_detail"
    val EXTRA_INTENT_DATA= "data"
    val EXTRA_INTENT_BOUND= "bound"
    val EXTRA_INTENT_PRESENTER_REQUEST= "presenter_request"
    val EXTRA_INTENT_DATE= "date"
    val EXTRA_INTENT_CAN_EDIT= "edit"
    val EXTRA_INTENT_ROLE_TYPE= "role_type"
    val EXTRA_INTENT_CAN_PUT_IN_LATER= "put_in_later"
    val EXTRA_INTENT_BOUND_REQUESTED= "requested"
    val EXTRA_CROSSDOCKING= "CROSS" //BoundOverviewFrag_Cross_Abs.EXTRA_CROSSDOCKING
    val EXTRA_INTENT_BTN_CONFIRM_VISIBLE= "btn_confirm"
    val EXTRA_INTENT_STATIC_REGISTER= "static_reg"
    val EXTRA_INTENT_TITLE= "title"
    val EXTRA_INTENT_LIST= "list"
    val EXTRA_RESULT= "result"
    val PICK_IMAGE_GALLERY_REQUEST= 2
    val INTENT_PICK_IMAGE_MULTIPLE= 3

    val DATA_BOUND= "_bound_"

    val FORMAT_DATE= "dd-MM-yyyy HH:mm:ss"

    val REQUEST_SCAN= 10
    val REQUEST_SCAN_PREVIEW= 11
    val REQUEST_DEFAULT= 9

    val USER_TYPE_LESSEE_OWNER = 3
    val USER_TYPE_LESSEE_STAFF = 4
    val USER_TYPE_WAREHOUSE_OWNER = 5
    val USER_TYPE_WAREHOUSE_STAFF = 6

    val USER_ROLE_LESSEE = 1
    val USER_ROLE_WAREHOUSE = 2

    val STATE_ROLE_LESSEE= USER_ROLE_LESSEE
    val STATE_ROLE_WAREHOUSE= USER_ROLE_WAREHOUSE

    val INBOUND= 1
    val OUTBOUND= 2
    val CROSSDOCKING= 3

    //universal
    val OK= Activity.RESULT_OK

    /**
     * Ini adalah object khusu menyimpan paramter dari setiap post request endpoint
     */
    object ApiParams{
        val LOGIN = arrayOf(
            "client_id",
            "client_secret",
            "email",
            "password",
            "sg_key",
            "fcm_token"
        )

        val REGISTER = arrayOf(
            "name",
            "email",
            "id_USER_TYPES",
            "password",
            "password_confirmation",
            "sg_key"
        )

        val OTP = arrayOf(
            "phone",
            "key"
        )

        val OTP_VERIFY = arrayOf(
            "sms_token"
        )

        val INBOUND_DATA_SEND = arrayOf(
            "courier_type",
            "shipping",
            "sender",
            "note",
            "evidences" // ini array, jadi nanti kyk evidences[0] .. dst
        )

        val INBOUND_PRODUCT_RECEIVED_AND_PUT = arrayOf(
            "order_items", // type array
            "product_container" // type array
        )

        val ORDER_CONFIRM = arrayOf(
            "is_accepted"
        )
    }

    /**
     * Aturan:
     *  Nama depan BoundStatus harus dimulai dg jenis transaksinya
     *  cth. Inbound, Outbound, atau Crossdocking
     */

    object BoundStatus{
        val CANCELLED= 19

        val INBOUND_CREATED= 1  //--> Nunggu konfirm
        val INBOUND_CONFIRMED= 2 //--> Kirim bukti resi pengiriman // Opsi kirim pihak ke-3
        val INBOUND_ON_SHIPMENT= 3 //--> Sudah sampe blum >> Warehouse yang nekan
        val INBOUND_ARRIVED_AND_PICKED= 4 //--> Proses memasukan ke kontener, scan barang & kontener
        val INBOUND_PUT= 5 //--> Proses memasukan ke kontener, scan barang & kontener
        val INBOUND_OVERDUE= 6 //--> Terlambat tidak mengirim dari lessee >> Sistem
        val INBOUND_CANCELED= CANCELLED //--> Terlambat tidak mengirim dari lessee >> Sistem

        val OUTBOUND_CREATED= 7 //--> Nunggu konfirm
//        val OUTBOUND_CONFIRMED= 7 //--> Garek mencet
//        val OUTBOUND_MANAGED_AND_SHIPPED= 8 //--> Scan, Finished
        val OUTBOUND_MANAGED= 8 //--> Scan, Finished
        val OUTBOUND_WAITING_FOR_SHIPMENT= 9 //--> Scan, Finished
        val OUTBOUND_SHIPPED= 10 //--> Scan, Finished
        val OUTBOUND_OVERDUE= 11 //--> Terlambat tidak mengirim dari lessee >> Sistem

        val CROSSDOCKING_CREATED= 12 //--> Nunggu konfirm
        val CROSSDOCKING_CONFIRMED= 13 //--> Kirim bukti resi pengiriman // Opsi kirim pihak ke-3
        val CROSSDOCKING_ON_SHIPMENT= 14 //--> Sudah sampe blum >> Lessee yang nekan
        val CROSSDOCKING_ARRIVED_AND_MANAGED= 15 //--> Tinggal mencet
//        val CROSSDOCKING_ARRIVED= 121 //--> Tinggal mencet
//        val CROSSDOCKING_MANAGED= 16 //--> Tinggal mencet
//        val CROSSDOCKING_MANAGED_AND_SHIPPED= 13 //--> Tinggal mencet
        val CROSSDOCKING_WAITING_FOR_SHIPMENT= 16 //--> Scan, Finished
        val CROSSDOCKING_SHIPPED= 17 //--> Tinggal mencet
        val CROSSDOCKING_OVERDUE= 18 //--> Terlambat tidak mengirim dari lessee >> Sistem

        val TXT_PRE= "<pre>"
        val TXT_PENDING= "Pending"
        val TXT_CONFIRMED= "Terkonfirmasi" //--> Kirim bukti resi pengiriman // Opsi kirim pihak ke-3
        val TXT_SENT= "Dikirim" //--> Sudah sampe blum >> Warehouse yang nekan
        val TXT_WAIT_FOR_SHIPMENT= "Menunggu Dikirim" //--> Sudah sampe blum >> Warehouse yang nekan
        val TXT_PROCESSED= "Diproses" //--> Proses memasukan ke kontener, scan barang & kontener
        val TXT_FINISH= "Selesai" //--> Terlambat tidak mengirim dari lessee >> Sistem
        val TXT_FINISH_MANAGED= "Selesai Diproses" //--> Terlambat tidak mengirim dari lessee >> Sistem
        val TXT_TO_WARHOUSE= "Menuju Gudang"
        val TXT_OVERDUE= "Melebihi Tempo"
        val TXT_CANCELLED= "Dibatalkan"
    }

    object Presenter {
        // kode request untuk untuk semau yang ada di presenter apapun
        val REQ_BOUND_LIST = "bound_list"
        val REQ_BOUND_DETAIL = "bound_detail"
        val REQ_WH_CONTAINERS = "wh_containers"
        val REQ_SEND_SHIPPING_METHOD = "send_shipping_type"
        val REQ_PROC_INBOUND_DATA_SEND = "send_inbound_data"
        val REQ_PROC_INBOUND_CONFIRM = "inbound_confirm"
        val REQ_PROC_INBOUND_PRODUCT_RECEIVED = "inbound_product_receved"
        val REQ_PROC_INBOUND_PRODUCT_RECEIVED_AND_PUT_IN = "inbound_product_receved_and_put_in"
        val REQ_PROC_INBOUND_PUT_IN = "inbound_put_in"
        val REQ_AUTH_REGISTER = "auth_register"
        val REQ_AUTH_IS_LOGGED_IN = "auth_is_logged_in"
        val REQ_AUTH_LOGIN = "auth_login"
        val REQ_AUTH_LOGOUT = "auth_logout"
        val REQ_AUTH_SEND_OTP = "auth_send_otp"
        val REQ_AUTH_VERIFY_OTP = "auth_verify_otp"
        val REQ_AUTH_RESET_OTP = "auth_reset_otp"
        val REQ_AUTH_IS_PHONE_VERIFIED = "auth_is_phone_verified"

        // kode data kirim/hasil request
        val DATA_PROC_INBOUND_CONFIRM = "data_inbound_confirm"
        val DATA_BOUND_LIST = "data_bound_list"
        val DATA_BOUND_DETAIL = "data_bound_detail"
        val DATA_SEND_SHIPPING_METHOD = "data_send_shipping_type"
        val DATA_WH_CONTAINERS = "data_wh_containers"
        val DATA_AUTH_ALREADY_LOGGED_IN = "data_auth_already_logged_in"
        val DATA_AUTH_PHONE_VERIFIED = "data_auth_phone_verified"
    }
/*
    object Role{
        val LESSEE= 1
        val WAREHOUSE= 2
    }
 */
}