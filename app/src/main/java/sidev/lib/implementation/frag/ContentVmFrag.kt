package sidev.lib.implementation.frag

import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.android.synthetic.main.page_rv_btn.view.*
//import kotlinx.android.synthetic.main._sif_page_rv.view.*
//import kotlinx.android.synthetic.main._sif_page_rv.view.*
import sidev.lib.android.siframe.lifecycle.fragment.Frag
import sidev.lib.android.siframe.tool.util.`fun`.toast
import sidev.lib.implementation.R
import sidev.lib.implementation.adp.ContentAdp
import sidev.lib.implementation.model.Content
import sidev.lib.implementation.presenter.ContentPresenter
import sidev.lib.implementation.viewmodel.ContentVm
import sidev.lib.universal.`fun`.notNull

class ContentVmFrag : Frag(){
    override val layoutId: Int
        get() = R.layout.page_rv_btn
    override val isInterruptable: Boolean
        get() = false

    lateinit var rvAdp: ContentAdp
    lateinit var rv: RecyclerView

//    override fun initRvAdp(): ContentAdp = ContentAdp(context!!)

    override fun _initView(layoutView: View) {
        rv= layoutView.findViewById(R.id.rv)
        rvAdp= ContentAdp(context!!)
        rvAdp.rv= rv

        initVm()

        layoutView.findViewById<Button>(R.id.btn).notNull { btn ->
            btn.text= "Login"
            btn.setOnClickListener { getViewModel(ContentVm::class.java).reload(ContentVm.LOGIN) }
        }

        layoutView.findViewById<SwipeRefreshLayout>(R.id.srl)
            ?.setOnRefreshListener { getViewModel(ContentVm::class.java).reload(ContentVm.CONTENT) }
    }

    fun initVm(){
        //Untuk donwload content list
        getViewModel(ContentVm::class.java).observe(ContentVm.CONTENT,
            onPreLoad = { showPb() } )
        { content: ArrayList<Content>? ->
            showPb(false)
            rvAdp.dataList= content
            layoutView.tv_no_data.visibility= if(content.isNullOrEmpty()) View.VISIBLE
            else View.GONE
        }!!.onFail { resCode, msg, e ->
            toast("Terjadi kesalahan saat load data")
        }

        //Untuk login
        getViewModel(ContentVm::class.java).observe(ContentVm.LOGIN,
            ContentPresenter.DATA_UNAME to "uname",
            loadLater = true, //request login hanya dilakukan saat tombol ditekan, bkn saat ini juga.
            onPreLoad = { showPbLogin() } )
        { isLoginSucc: Boolean ->
            if(isLoginSucc) toast("Login berhasil bro!!!")
            else toast("Login gagal bro :((")
            showPbLogin(false)
        }!!.onFail { resCode, msg, e ->
            toast("Terjadi kesalahan saat login,\nmsg= $msg")
        }
    }

    override fun onInterruptedWhenBusy() {
        toast("Sabar bro :(", Toast.LENGTH_SHORT)
    }

    fun showPb(show: Boolean= true){
        layoutView.findViewById<View>(R.id.pb)?.visibility= if(show) View.VISIBLE
            else View.GONE
        layoutView.tv_no_data.visibility= if(!show) View.VISIBLE
            else View.GONE
        rv.visibility= if(!show) View.VISIBLE
            else View.GONE
        layoutView.findViewById<SwipeRefreshLayout>(R.id.srl)?.isRefreshing= show
    }

    fun showPbLogin(show: Boolean= true){
        layoutView.findViewById<View>(R.id.pb)?.visibility= if(show) View.VISIBLE
        else View.GONE
        layoutView.findViewById<SwipeRefreshLayout>(R.id.srl)?.isRefreshing= show
    }
}