package sidev.lib.android.std.`val`

import android.app.Activity
import sidev.lib.jvm.android.std.R

//import sidev.lib.android.siframe.customizable.R
//import sidev.lib.android.siframe.R

/**
 * Isi dari kelas ini jangan diubah.
 * Mengubah nilai dari variabel yang ada diperbolehkan
 * untuk melakukan penyesuaian.
 */
object _Config {
    object ExternalRef{
        val ID_CONTENT= android.R.id.content
    }

    /**
     * Flag apakah mode debug diijinkan dalam app ini.
     */
    var DEBUG= true

    /**
     * Flag apakah proses log diijinkan dalam app ini.
     *
     * Jika [LOG] di-assign true tapi [DEBUG] false, maka hasil akhirnya [LOG] false.
     * Artinya, [LOG] diijinkan jika [DEBUG] juga diijinkan.
     */
    var LOG= DEBUG
        get()= field && DEBUG

    /**
     * Flag apakah proses log yg disimpan pada file diijinkan dalam app ini.
     *
     * Jika [LOG_ON_FILE] di-assign true tapi [LOG] false, maka hasil akhirnya [LOG_ON_FILE] false.
     * Artinya, [LOG_ON_FILE] diijinkan jika [LOG] juga diijinkan.
     */
    var LOG_ON_FILE= false
        get()= field && LOG


    var INT_EMPTY= 0


    var DB_NAME= "DEFAULT"
    var DB_VERSION= 1


    var STRING_APP_NAME_RES= R.string.app_name

    var FORMAT_DATE= "dd/MM/yyyy"
    var FORMAT_TIME= "HH:mm:ss"
    var FORMAT_TIMESTAMP= "$FORMAT_DATE $FORMAT_TIME"

    var ENDPOINT_ROOT= ""

    var RES_OK= Activity.RESULT_OK
    var RES_NOT_OK= -2

    var MAIN_REF= "main_ref"
    var _KEY_PREF_EXP_TIME= "_pref_exp_time"
}