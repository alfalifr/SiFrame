package sidev.lib.implementation._simulation.sigudang.util

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import com.loopj.android.http.RequestParams
//import com.sigudang.android.BuildConfig
//import com.sigudang.android.activities.BaseActivity
//import com.sigudang.android.activities.auth.AuthActivity
//import com.sigudang.android.interfaces.CallBack
import com.sigudang.android.utilities.constant.Constants
import com.squareup.picasso.Picasso
import sidev.lib.android.siframe.tool.util.`fun`.toast
import sidev.lib.implementation._simulation.sigudang.interfaces.CallBack
import java.lang.Exception

class RequestUtil(private val context: Context) {
    //------------ BLA BLA ---------------
    // ini untuk tipe ukuran gambar yang akan ditampilkan di aplikasi android

    val authMgr = AuthManager(context)

    companion object {
        val IMAGE_SIZE_TUMBNAIL_SMALL = 0
        val IMAGE_SIZE_TUMBNAIL_MEDIUM = 1
        val IMAGE_SIZE_DETAILED = 2
    }

    private val currentDate: String
        get() {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = System.currentTimeMillis()
            val dateFormat = SimpleDateFormat("yyyy-MM-dd")
            return dateFormat.format(calendar.time)
        }


    /*fun post(requestParams: RequestParams, endPoint: String, token: String, callBack : CallBack) {
        val connectivity = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivity.activeNetworkInfo

        if(activeNetwork != null){
            val connection = AsyncHttpClient()

            connection.addHeader("Accept", "application/json")
            connection.addHeader("Authorization", "Bearer $token")

            // untuk memasukan parameter dari post request
            connection.post(endPoint, requestParams, object : AsyncHttpResponseHandler(){
                override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?) {
                    val message = String(responseBody!!)

                    if(message.isNotEmpty()) {
                        if (Util.searchString(message, Constants.UNAUTHENTICATE_MESSAGE)) {
                            AuthManager(context).logout(
                                true,
                                Util.getSharedPref(
                                    context,
                                    Constants.KEY_PREF_IS_LOGGING_OUT
                                ) == null
                            )
                            //  (context as AppCompatActivity).finish()
                        } else {
                            callBack.response(message)
                            Log.i("response post success", message)
                        }
                    } else
                        callBack.response(Constants.ERROR_RESPONSE)
                }

                override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {
                    if(responseBody != null) {
                        val message = String(responseBody)

                        callBack.response(message)
                        Log.i("response post failed", message)

                        if(message.isNotEmpty()) {
                            if (Util.searchString(message, Constants.UNAUTHENTICATE_MESSAGE)) {
                                AuthManager(context).logout(
                                    true,
                                    Util.getSharedPref(
                                        context,
                                        Constants.KEY_PREF_IS_LOGGING_OUT
                                    ) == null
                                )
                                //  (context as AppCompatActivity).finish()
                            }
                        } else callBack.response(Constants.ERROR_RESPONSE)
                    } else callBack.response(Constants.ERROR_RESPONSE)
                }
            })
        } else {
            callBack.response(Constants.ERROR_RESPONSE)
            Toast.makeText(context, "Mohon maaf, Anda tidak terkoneksi ke internet!", Toast.LENGTH_SHORT).show()
        }
    } */

    fun getBitmapFromURL(src: String): Bitmap? {
        try {
            val connectivity = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = connectivity.activeNetworkInfo

            return if(activeNetwork != null) {
                val url = URL(src)
                val connection = url.openConnection() as HttpURLConnection
                connection.doInput = true
                connection.connect()
                val input = connection.inputStream
                BitmapFactory.decodeStream(input)
            } else
                null
        } catch (e: IOException) {
            // Log exception
            return null
        }

    }

    fun post(endpoint: String, params: RequestParams, cb: CallBack, bearerToken: String? = null) {
        if(hasInternetAccess(cb)) {
            val connection = AsyncHttpClient()
            connection.addHeader("Accept", "application/json")
            if(bearerToken != null)
                connection.addHeader("Authorization", "Bearer $bearerToken")
            // untuk memasukan parameter dari post request
            connection.post(endpoint, params, object : AsyncHttpResponseHandler(){
                override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?) {
                    if(isUnauthorized(statusCode))
                        return
                    actToResponseBody(responseBody, statusCode, cb)
                }

                override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {
                    if(isUnauthorized(statusCode))
                        return
                    actToResponseBody(responseBody, statusCode, cb)
                }

            })
        }
    }

    fun get(endpoint: String, cb: CallBack, bearerToken: String? = null) {
        if(hasInternetAccess(cb)) {
            val connection = AsyncHttpClient()
            connection.addHeader("Accept", "application/json")
            if (bearerToken != null)
                connection.addHeader("Authorization", "Bearer $bearerToken")

            connection.get(endpoint, object : AsyncHttpResponseHandler() {
                override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?) {
                    if(isUnauthorized(statusCode))
                        return
                    actToResponseBody(responseBody, statusCode, cb)
                }

                override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {
                    if(isUnauthorized(statusCode))
                        return
                    actToResponseBody(responseBody, statusCode, cb)
                }
            })
        }
    }

    private fun isUnauthorized(code: Int): Boolean {
        if(code == Constants.RESPONSE_UNAUTHORIZED) {
            Log.e("ke revoke", "ok")
            authMgr.clearUserSession()
            val act = context//(context as BaseActivity)
            act.toast("Anda orang lain yang masuk dengan akun ini!")
//            act.jumpToLogin()

            return true
        }

        return false
    }

    private fun actToResponseBody(responseBody: ByteArray?, statusCode: Int, cb: CallBack) {
        if (responseBody != null) {
            val message = String(responseBody)
            Log.d("res", "$statusCode $message")
            Log.e("res", "$statusCode $message")
            Log.i("resres", "$statusCode $message")
            if(containsErrors(message))
                cb.response(Constants.ERROR_RESPONSE, Constants.RESPONSE_ERROR)
            else
                cb.response(message, statusCode)
        } else
            cb.response(Constants.ERROR_RESPONSE, Constants.RESPONSE_ERROR)
    }

    private fun hasInternetAccess(cb: CallBack) : Boolean  {
        val connectivity = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivity.activeNetworkInfo

        if(activeNetwork == null) {
            cb.response(Constants.ERROR_RESPONSE, Constants.RESPONSE_ERROR)
            return false
        }

        return true
    }

    private fun containsErrors(response: String): Boolean{
        return Util.searchString(response, "exception", "trace", "error")
    }

    fun loadImageToImageView(iv: ImageView, ivSizeType: Int, url: String){
        val isDirLocal= try{ Util.isDirLocal(url!!) }
        catch(e: Exception){ false }

        val dir=
            if (isDirLocal) url
            else Endpoints.ENDPOINT_ROOT + url

        Log.e("SERVER_UTIL", "isDirLocal= $isDirLocal url= $url dir= $dir")

        if(!isDirLocal){
            var dim = 500
            when(ivSizeType){
                IMAGE_SIZE_TUMBNAIL_SMALL -> {
                    dim = 50
                }
                IMAGE_SIZE_TUMBNAIL_MEDIUM -> {
                    dim = 100
                }
            }

            val scaleType = iv.scaleType

            if(scaleType == ImageView.ScaleType.FIT_CENTER || scaleType == ImageView.ScaleType.FIT_END
                || scaleType == ImageView.ScaleType.FIT_START || scaleType == ImageView.ScaleType.FIT_XY)
                Picasso.get().load(dir).fit().centerInside().into(iv)
            else
                Picasso.get().load(dir).resize(dim, dim).centerCrop().into(iv)
        } else if(url != "") {
            val bm= T_BitmapUtil.decode(dir!!)!!
            iv.setImageBitmap(bm)
        }
    }
}
