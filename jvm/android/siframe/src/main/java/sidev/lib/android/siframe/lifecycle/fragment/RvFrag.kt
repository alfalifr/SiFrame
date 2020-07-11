package sidev.lib.android.siframe.lifecycle.fragment

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
//import sidev.lib.android.siframe.adapter.RvAdp
import sidev.lib.android.siframe._customizable._Config
import sidev.lib.android.siframe.adapter.SimpleRvAdp
import sidev.lib.android.siframe.adapter.layoutmanager.LinearLm
import sidev.lib.android.siframe.intfc.listener.RvScrollListener
import sidev.lib.android.siframe.tool.util.`fun`.addOnGlobalLayoutListener
import sidev.lib.android.siframe.tool.util.`fun`.loge
import sidev.lib.universal.`fun`.asNotNull
import sidev.lib.universal.`fun`.asNotNullTo
import sidev.lib.universal.`fun`.isNull
import sidev.lib.universal.`fun`.notNull

//import sidev.lib.android.siframe.tool.util.`fun`.loge

abstract class RvFrag<R: SimpleRvAdp<*, *>> : Frag(){
    final override val layoutId: Int
        get() = _Config.LAYOUT_RV //R.layout.content_abs_rv

    /**
     * Jika true, maka [fullScrollIv] akan ditampilkan jika user
     * melakukan scroll ke bawah.
     *
     * Nilai ini akan sia-sia jika [NestedScrollView.mOnScrollChangeListener]
     * pada [scrollView] dimodifikasi.
     */
    open val isFullScrollIvShown= true

    lateinit var fullScrollIv: ImageView
        protected set
/*
    lateinit var scrollView: NestedScrollView
        protected set
 */
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
//        scrollView= layoutView.findViewById(_Config.ID_SV) //.rv
        pb= layoutView.findViewById(_Config.ID_PB)
        layoutView.findViewById<SwipeRefreshLayout>(_Config.ID_SRL).setOnRefreshListener {
            onRefreshListener?.invoke()
        }
        rvAdp= initRvAdp()
        rvAdp.rv= rv
        fullScrollIv= layoutView.findViewById(_Config.ID_IV_ARROW) //.rv
        fullScrollIv.setOnClickListener { fullScroll(View.FOCUS_UP) }
/*
        scrollView.setOnScrollChangeListener { v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
            if(isFullScrollIvShown)
                showFullScrollIv(scrollY > 0)
        }
// */
/*
        rv.layoutManager.asNotNullTo { lm: LinearLm ->
            lm.onScrollListener= { targetPosition, dx, dy, isAtFirstPos, isSmoothScroll ->
                if(isFullScrollIvShown)
                    showFullScrollIv(!isAtFirstPos)
            }
        }
// */
        rv.addOnScrollListener(object: RvScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                loge("scrollY= $scrollY dy= $dy scrollX= $scrollX dx= $dx")
                Log.e("RV_FRAG", "scrollY= $scrollY dy= $dy scrollX= $scrollX dx= $dx")
                if(isFullScrollIvShown)
                    showFullScrollIv(this.scrollY > 0)
            }
        })
// */
        //<7 Juli 2020> => Ini sengaja dibuat dobel (satunya ada di [TopMiddleBottomBase.__initTopMiddleBottomView])
        //  agar tampilan [NestedScrollView] menuju ke paling atas.
//        scrollView.addOnGlobalLayoutListener { fullScroll(View.FOCUS_UP) }
        showFullScrollIv(false)
    }

    fun showLoading(show: Boolean= true){
        pb.visibility= if(show) View.VISIBLE
            else View.GONE
    }

    fun showRefresh(show: Boolean= true){
        layoutView.findViewById<SwipeRefreshLayout>(_Config.ID_SRL)
            .isRefreshing= show
    }

    /**
     * Hanya digunakan secara vertikal. Hal tersebut dikarenakan RecyclerView pada
     * halaman ini berada di dalam NestedScrollView.
     *
     * Fungsi ini memanggil fungsi [NestedScrollView.scrollTo],
     * bkn [RecyclerView.scrollToPosition].
     */
    fun scrollToPosition(pos: Int, smoothScroll: Boolean= true){
        rv.layoutManager.asNotNullTo { lm: LinearLm ->
            if(smoothScroll)
                lm.smoothScrollTo(pos)
            else
                lm.scrollToPosition(pos)
        }.isNull {
//            val y= rv.y + rv.getChildAt(pos).y
            rv.scrollToPosition(pos)
        }
    }

    /**
     * @param direction [View.FOCUS_UP] untuk scroll-to-top.
     *   [View.FOCUS_DOWN] untuk scroll-to-bottom.
     */
    fun fullScroll(direction: Int){
        when(direction){
            View.FOCUS_UP -> 0
            View.FOCUS_DOWN -> rv.adapter?.itemCount ?: 0
            else -> null
        }.notNull { pos ->
            rv.layoutManager.asNotNullTo { lm: LinearLm ->
                lm.smoothScrollTo(pos)
            }.isNull {
                rv.scrollToPosition(pos)
            }
        }
//        scrollView.fullScroll(direction)
    }

    fun showFullScrollIv(show: Boolean= true){
        fullScrollIv.visibility= if(show) View.VISIBLE
            else View.GONE
    }
}