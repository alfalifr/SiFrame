package sidev.lib.implementation.frag

import android.view.View
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
//import kotlinx.android.synthetic.main._sif_page_rv.view.*
//import kotlinx.android.synthetic.main._sif_page_rv.view.*
import sidev.lib.android.siframe.lifecycle.fragment.RvFrag
import sidev.lib.android.siframe.lifecycle.fragment.SimpleAbsFrag
import sidev.lib.android.siframe.tool.util.`fun`.toast
import sidev.lib.implementation.R
import sidev.lib.implementation.adp.ContentAdp
import sidev.lib.implementation.model.Content
import sidev.lib.implementation.viewmodel.ContentVm
import sidev.lib.universal.`fun`.notNull

class ContentVmFrag : SimpleAbsFrag(){
    override val layoutId: Int
        get() = R.layout.page_rv_btn

    lateinit var rvAdp: ContentAdp
    lateinit var rv: RecyclerView

//    override fun initRvAdp(): ContentAdp = ContentAdp(context!!)

    override fun _initView(layoutView: View) {
        rv= layoutView.findViewById(R.id.rv)
        rvAdp= ContentAdp(context!!)
        rvAdp.rv= rv

        getViewModel(ContentVm::class.java).observe(ContentVm.CONTENT,
        onPreLoad = { showPb() } )
        { content: ArrayList<Content>? ->
            rvAdp.dataList= content
            showPb(false)
        }!!.onFail { resCode, msg, e ->
           toast("Terjadi kesalahan saat load data")
        }

        layoutView.findViewById<Button>(R.id.btn).notNull { btn ->
            btn.text= "Login"
            btn.setOnClickListener {
                getViewModel(ContentVm::class.java).observe(ContentVm.LOGIN,
                    forceReload = true,
                    onPreLoad = { showPbLogin() } )
                { isLoginSucc: Boolean ->
                    if(isLoginSucc) toast("Login berhasil bro!!!")
                    else toast("Login gagal bro :((")
                    showPbLogin(false)
                }!!.onFail { resCode, msg, e ->
                    toast("Terjadi kesalahan saat login")
                }
            }
        }

        layoutView.findViewById<SwipeRefreshLayout>(R.id.srl)
            ?.setOnRefreshListener { getViewModel(ContentVm::class.java).reload(ContentVm.CONTENT) }
    }

    fun showPb(show: Boolean= true){
        layoutView.findViewById<View>(R.id.pb)?.visibility= if(show) View.VISIBLE
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