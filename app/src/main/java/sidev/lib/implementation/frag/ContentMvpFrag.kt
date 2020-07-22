package sidev.lib.implementation.frag

import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.page_rv_btn.view.*
import sidev.lib.android.siframe.arch.presenter.Presenter
import sidev.lib.android.siframe.lifecycle.fragment.mvp.MvpFrag
import sidev.lib.android.siframe.tool.util.`fun`.toast
import sidev.lib.implementation.R
import sidev.lib.implementation.adp.ContentAdp
import sidev.lib.implementation.model.Content
import sidev.lib.implementation.presenter.ContentPresenter
import sidev.lib.implementation.util.Const


class ContentMvpFrag : MvpFrag(){
    override val layoutId: Int
        get() = R.layout.page_rv_btn
    override val isInterruptable: Boolean
        get() = false

    lateinit var rvAdp: ContentAdp


    override fun initPresenter(): Presenter? = ContentPresenter(this)

    override fun _initView(layoutView: View) {
        rvAdp= ContentAdp(context!!)
        rvAdp.rv= layoutView.rv
        (layoutView.btn as Button).text= "Login"
        layoutView.btn.setOnClickListener {
            showPb(includeRv = false)
            sendRequest(ContentPresenter.REQ_LOGIN, ContentPresenter.DATA_UNAME to "uname")
        }
        layoutView.srl.setOnRefreshListener {
            showPb()
            sendRequest(ContentPresenter.REQ_GET_CONTENT)
        }
        showPb()
        sendRequest(ContentPresenter.REQ_GET_CONTENT)
    }

    fun showPb(show: Boolean= true, includeRv: Boolean= true){
        layoutView.pb.visibility= if(show) View.VISIBLE
            else View.GONE
        layoutView.srl.isRefreshing= show
        if(includeRv){
            layoutView.rv.visibility= if(show) View.GONE
                else View.VISIBLE
            layoutView.tv_no_data.visibility= if(show || rvAdp.dataList != null) View.GONE
                else View.VISIBLE
        }
    }

    override fun onInterruptedWhenBusy() {
        toast("Msh jalan bro :(")
    }

    override fun onPresenterSucc(
        request: String,
        result: Int,
        data: Map<String, Any>?,
        resCode: Int
    ) {
        showPb(false)
        when(request){
            ContentPresenter.REQ_LOGIN -> {
                if(resCode == Const.RES_OK)
                    toast("Login Berhasil bro!!!")
                else
                    toast("Login gagal bro :(")
            }
            ContentPresenter.REQ_GET_CONTENT -> {
                val data= try{ data!![Const.DATA_CONTENT] as ArrayList<Content> }
                catch (e: Exception){ null }
                rvAdp.dataList= data
                layoutView.tv_no_data.visibility= if(data.isNullOrEmpty()) View.VISIBLE
                else View.GONE
            }
        }
    }

    override fun onPresenterFail(
        request: String,
        result: Int?,
        msg: String?,
        e: Exception?,
        resCode: Int
    ) {
        showPb(false)
        val msg= when(request){
            ContentPresenter.REQ_LOGIN -> "Terjadi kesalahan saat login :( \n msg= $msg"
            ContentPresenter.REQ_GET_CONTENT -> "Terjadi kesalahan saat download data :( \n msg= $msg"
            else -> "Terjadi kesalahan mboh pas ngapain :("
        }
        toast(msg)
    }
}