package sidev.lib.android.siframe.lifecycle.fragment

import android.view.View
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import sidev.lib.android.siframe.adapter.RvAdp
import sidev.lib.android.siframe.customizable._init._Config

abstract class RvFrag<R: RvAdp<*, *>> : SimpleAbsFrag(){
    override val layoutId: Int
        get() = _Config.LAYOUT_RV //R.layout.content_abs_rv

    lateinit var rv: RecyclerView
        protected set
    lateinit var rvAdp: R //RvAdp<*, *>
        protected set
    lateinit var pb: ProgressBar
        protected set

    var onRefreshListener: (() -> Unit)?= null

    abstract fun initRvAdp(): R //RvAdp<*, *>

    override fun __initView(layoutView: View) {
        super.__initView(layoutView)
        rv= layoutView.findViewById(_Config.ID_RV) //.rv
        layoutView.findViewById<SwipeRefreshLayout>(_Config.ID_SRL).setOnRefreshListener {
            onRefreshListener?.invoke()
        }
        rvAdp= initRvAdp()
        rvAdp.rv= rv
        pb= layoutView.findViewById(_Config.ID_PB)
    }

    fun showLoading(show: Boolean= true){
        pb.visibility= if(show) View.VISIBLE
            else View.GONE
    }

    fun showRefresh(show: Boolean= true){
        layoutView.findViewById<SwipeRefreshLayout>(_Config.ID_SRL)
            .isRefreshing= show
    }
}