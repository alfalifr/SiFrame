package sidev.lib.android.std.tool.util

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import sidev.lib.exception.NetworkExc

object _NetworkUtil{
    enum class Method{ GET, POST }
    val CODE_SUCCESS_COMMON= 1
    val CODE_FAIL_COMMON= 2
    val CODE_FAIL_NO_INTERNET= 3

    interface NetworkCallback{
        fun onSucc(code: Int, response: String?)
        fun onFail(code: Int, response: String?, error: Throwable?){}
        fun onNetworkNotActive(){
            _ThreadUtil.delayRun(2000) {
                onFail(CODE_FAIL_NO_INTERNET, null, NetworkExc(msg = "Jaringan tidak aktif"))
            }
        }
    }

    /**
     * (+) Ada cache. Jadi enak saat upload/download file besar seperti gambar.
     * (-) Gakda custom header.
     */
    object Volley{
        private var RQ: RequestQueue?= null
        /*
                @JvmStatic
                fun RQ(aktifitas: Context): RequestQueue{
                    if(RQ == null){
                        RQ= Volley.newRequestQueue(aktifitas)
                    }
                    return RQ
                }
        */
        @JvmStatic
        fun <T>addRequest(konteks: Context, req: Request<T>): RequestQueue?{
//            if(RQ == null){
            RQ = com.android.volley.toolbox.Volley.newRequestQueue(konteks)
            RQ!!.add(req)
            return RQ
        }
        @JvmOverloads
        fun get(c: Context, url: String, params: RequestParams?= null, callback: NetworkCallback){
            request(c, Method.GET, url, params, callback)
        }
        @JvmOverloads
        fun get(c: Context, url: String, params: RequestParams?= null,
                onSuccess: (code: Int, response: String?) -> Unit){
            val callback= object : NetworkCallback {
                override fun onSucc(code: Int, response: String?) { onSuccess(code, response) }
            }
            request(c, Method.GET, url, params, callback)
        }

        @JvmOverloads
        fun post(c: Context, url: String, params: RequestParams?= null, callback: NetworkCallback){
            request(c, Method.POST, url, params, callback)
        }
        @JvmOverloads
        fun post(c: Context, url: String, params: RequestParams?= null,
                onSuccess: (code: Int, response: String?) -> Unit){
            val callback= object : NetworkCallback {
                override fun onSucc(code: Int, response: String?) { onSuccess(code, response) }
            }
            request(c, Method.POST, url, params, callback)
        }

        @JvmOverloads
        fun request(c: Context, method: Method, url: String, params: RequestParams?= null, callback: NetworkCallback): RequestQueue? {
            if(isNetworkActive(c)){
                val methodVolley= when(method){
                    Method.GET -> Request.Method.GET
                    Method.POST -> Request.Method.POST
                }
                return addRequest(c, StringRequest(methodVolley, url,
                        Response.Listener { data ->  callback.onSucc(CODE_SUCCESS_COMMON, data) },
                        Response.ErrorListener { error: VolleyError? ->
                            callback.onFail(
                                CODE_FAIL_COMMON, null, error
                            )
                        }
                    )
                )
            } else {
                callback.onNetworkNotActive()
            }
            return null
        }
    }

    /**
     * (+) Header bisa dicustom.
     * (-) Gakda cache. Cuma plain library.
     */
    object Loopj{
        @JvmOverloads
        fun get(c: Context, url: String, params: RequestParams?= null, token: String?= null, callback: NetworkCallback){
            request(c, Method.GET, url, params, token, callback)
        }
        @JvmOverloads
        fun get(c: Context, url: String, params: RequestParams?= null, token: String?= null,
                onSuccess: (code: Int, response: String?) -> Unit){
            val callback= object : NetworkCallback {
                override fun onSucc(code: Int, response: String?) { onSuccess(code, response) }
            }
            request(c, Method.GET, url, params, token, callback)
        }

        @JvmOverloads
        fun post(c: Context, url: String, params: RequestParams?= null, token: String?= null, callback: NetworkCallback){
            request(c, Method.POST, url, params, token, callback)
        }
        @JvmOverloads
        fun post(c: Context, url: String, params: RequestParams?= null, token: String?= null,
                onSuccess: (code: Int, response: String?) -> Unit){
            val callback= object : NetworkCallback {
                override fun onSucc(code: Int, response: String?) { onSuccess(code, response) }
            }
            request(c, Method.POST, url, params, token, callback)
        }

        @JvmOverloads
        fun request(c: Context, method: Method, url: String, params: RequestParams?= null, token: String?= null, callback: NetworkCallback){
            if(isNetworkActive(c)){
                val connection= AsyncHttpClient()

                connection.addHeader("Accept", "application/json")
                if(token != null) //Kemungkinan format Laravel
                    connection.addHeader("Authorization", "Bearer $token")

                val handler= object: AsyncHttpResponseHandler(){
                    override fun onSuccess(
                        statusCode: Int,
                        headers: Array<out Header>?,
                        responseBody: ByteArray?
                    ) {
                        callback.onSucc(statusCode,
                            if(responseBody != null) String(responseBody)
                            else null
                        )
                    }

                    override fun onFailure(
                        statusCode: Int,
                        headers: Array<out Header>?,
                        responseBody: ByteArray?,
                        error: Throwable?
                    ) {
                        callback.onFail(statusCode,
                            if(responseBody != null) String(responseBody)
                            else null,
                            error
                        )
                    }
                }
                when(method){
                    Method.GET -> connection.get(url, params, handler)
                    Method.POST -> connection.post(url, params, handler)
                }
            } else {
                callback.onNetworkNotActive()
            }
        }
    }

    fun isNetworkActive(c: Context): Boolean{
        val connectivity= c.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork=
            if(Build.VERSION.SDK_INT >= 23) connectivity.activeNetwork
            else connectivity.activeNetworkInfo
        return activeNetwork != null
    }
}