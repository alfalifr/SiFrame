package sidev.lib.implementation._simulation.sigudang.util

import android.content.Context
import android.widget.Toast
import com.loopj.android.http.RequestParams

import com.sigudang.android.utilities.json.JObjs
import com.sigudang.android.utilities.constant.Constants

/**
 * these codes are originally initiated by Amir Mu'tashim Billah
 * 11/2019
 */

class AuthManager {

  //  var requestUtil : RequestUtil? = null
    var ctx: Context? = null

    var BASE_API_URL = ""
    var API_KEY = ""

    constructor(context: Context) {
        ctx = context
        //requestUtil = RequestUtil(context)
        BASE_API_URL = Endpoints.PUBLIC
        API_KEY = Endpoints.SIGUDANG_API_KEY
    }

    fun clearUserSession(){
        Util.setSharedPref(ctx!!, null, Constants.KEY_PREF_USER_LOGIN)
    }

    fun getUserToken(): String? {
        val loginInfo = getLoginInfo()
        if(loginInfo != null) {
            val credentialObj = JObjs(loginInfo).getJSONObject("credential")
            return credentialObj.getString("access_token")
        }
        return null
    }

    fun getUserId(): Int {
        val loginInfo = getLoginInfo()
        if(loginInfo != null)
            return JObjs(loginInfo).getInt("user_id")
        return -1
    }

    fun getUserType(): Int {
        val loginInfo = getLoginInfo()
        if(loginInfo != null)
            return JObjs(loginInfo).getInt("user_type_id")
        return -1
    }

    fun getUserBusiness(): Int {
        val loginInfo = getLoginInfo()
        if(loginInfo != null)
            return JObjs(loginInfo).getInt("user_business_id")
        return -1
    }

    fun getUserRole(): Int {
        val currentUserType = getUserType()
        if(currentUserType == Constants.USER_TYPE_LESSEE_OWNER || currentUserType == Constants.USER_TYPE_LESSEE_STAFF)
            return Constants.USER_ROLE_LESSEE
        else if(currentUserType == Constants.USER_TYPE_WAREHOUSE_OWNER || currentUserType == Constants.USER_TYPE_WAREHOUSE_STAFF)
            return Constants.USER_ROLE_WAREHOUSE

        return -1
    }

    fun getLoginInfo(): String? {
        val encryptedUserLogin = Util.getSharedPref(ctx!!, Constants.KEY_PREF_USER_LOGIN)
        if(encryptedUserLogin != null)
            return Util.decodeBase64(encryptedUserLogin)

        return null
    }

    fun clearAllAccountData() {
        if(ctx != null) {
            Util.setSharedPref(ctx!!, null, Constants.KEY_PREF_USER_LOGIN)
            Util.setSharedPref(ctx!!, "", Constants.KEY_PREF_IS_LOGGING_OUT)
        }
    }

    fun showToast(message: String) {
        Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show()
    }
}