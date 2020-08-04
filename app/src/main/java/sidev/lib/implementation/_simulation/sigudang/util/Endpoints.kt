package sidev.lib.implementation._simulation.sigudang.util



object Endpoints {

    // Fields from default config.
    const val ENDPOINT_ROOT = "https://sigudang.com"
    const val SERVER_CLIENT_ID = 2
    const val SERVER_CLIENT_SECRET = "5xE9qElpgX9eh8r599c0DyIKD9A6Ot2vHydKpkEw"
    const val SIGUDANG_API_KEY = "70d9d9a6-19e6-5852-a37a-d6698ace2855"

    val ROOT = ENDPOINT_ROOT
    val USER = "$ROOT/api/user/"
    val ORDER = "$ROOT/api/order/"
    val MASTER = "$ROOT/api/master/"
    val WH = "$ROOT/api/wh/"
    val INBOUND_ORDER = "${ORDER}inbound/"
    val BOUND_STATUS = "status/"
    val BOUND_LIST = "list/"
    val LESSEE = "$ROOT/api/lessee/"
    val WAREHOUSE = "$ROOT/api/warehouse/"
    val PUBLIC = "$ROOT/api/public/"
    val LOGIN = "login"
    val REGISTER = "register"
    val LOGOUT = "logout"
    val USER_INFO = "user"
    val USER_ID = "userid"
    val SEND_OTP = "send-otp"
    val SMS_TOKEN_SEND = "sms-token/send"
    val SMS_TOKEN_VERIFY = "sms-token/verify"
    val SMS_TOKEN_RESET = "sms-token/reset"
    val VERIFY_OTP = "verify-otp"
    val RESET_OTP = "reset-otp"
    val PHONE_VERIFIED = "isPhoneVerified"
}