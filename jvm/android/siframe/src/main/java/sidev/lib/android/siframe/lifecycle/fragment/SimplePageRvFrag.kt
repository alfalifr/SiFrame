package sidev.lib.android.siframe.lifecycle.fragment

import android.view.View
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import sidev.lib.android.siframe.adapter.SimpleAbsRecyclerViewAdapter
import sidev.lib.android.siframe.customizable._init._ConfigBase

abstract class SimplePageRvFrag : SimpleAbsFrag(){
    override val layoutId: Int
        get() = _ConfigBase.LAYOUT_RV //R.layout.content_abs_rv

    lateinit var rv: RecyclerView
        protected set
    lateinit var rvAdp: SimpleAbsRecyclerViewAdapter<*, *>
        protected set
    lateinit var pb: ProgressBar
        protected set

    var onRefreshListener: (() -> Unit)?= null

    abstract fun initRvAdp(): SimpleAbsRecyclerViewAdapter<*, *>

    override fun initView_int(layoutView: View) {
        super.initView_int(layoutView)
        rv= layoutView.findViewById(_ConfigBase.ID_RV) //.rv
        layoutView.findViewById<SwipeRefreshLayout>(_ConfigBase.ID_SRL).setOnRefreshListener {
            onRefreshListener?.invoke()
        }
        rvAdp= initRvAdp()
        rvAdp.rv= rv
        pb= layoutView.findViewById(_ConfigBase.ID_PB)
    }

    fun showLoading(show: Boolean= true){
        pb.visibility= if(show) View.VISIBLE
            else View.GONE
    }
}