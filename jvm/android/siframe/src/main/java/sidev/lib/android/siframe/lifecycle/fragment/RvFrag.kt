package sidev.lib.android.siframe.lifecycle.fragment

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.android.synthetic.main._sif_page_rv.view.*
import sidev.lib.android.siframe.R
import sidev.lib.android.siframe._customizable._Config
import sidev.lib.android.siframe.adapter.SimpleRvAdp
import sidev.lib.android.siframe.adapter.layoutmanager.LinearLm
import sidev.lib.android.siframe.intfc.listener.RvScrollListener
import sidev.lib.android.siframe.intfc.prop.RvAdpProp
import sidev.lib.android.siframe.tool.util.`fun`.inflate
import sidev.lib.universal.`fun`.asNotNull
import sidev.lib.universal.`fun`.asNotNullTo
import sidev.lib.universal.`fun`.isNull
import sidev.lib.universal.`fun`.notNull

//import sidev.lib.android.siframe.tool.util.`fun`.loge

abstract class RvFrag<Adp: SimpleRvAdp<*, *>> : Frag(), RvAdpProp{
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
    override lateinit var rvAdp: Adp //RvAdp<*, *>
        protected set
    lateinit var pb: ProgressBar
        protected set
    var noDataTxt: String= "Tidak ada data"
        set(v){
            field= v
            layoutView.tv_no_data.text= v
        }

    var onRefreshListener: (() -> Unit)?= null

    abstract fun initRvAdp(): Adp //RvAdp<*, *>

    override fun __initView(layoutView: View) {
        super.__initView(layoutView)
//        rv= layoutView.findViewById(_Config.ID_RV) //.rv
//        scrollView= layoutView.findViewById(_Config.ID_SV) //.rv
        pb= layoutView.findViewById(_Config.ID_PB)
        layoutView.findViewById<SwipeRefreshLayout>(_Config.ID_SRL)
            .setOnRefreshListener { onRefreshListener?.invoke() }

        val rvContainer= layoutView.findViewById<View>(_Config.ID_LL_RV_CONTAINER) as ViewGroup
        rvAdp= initRvAdp()
        rvAdp.setupLayoutManager(context!!).asNotNullTo { llm: LinearLayoutManager ->
            val rvLayoutId=
                if(llm.orientation == LinearLayoutManager.HORIZONTAL) R.layout._sif_comp_rv_horizontal
                else R.layout._sif_comp_rv_vertical
            rv= inflate(rvLayoutId, rvContainer) as RecyclerView
        }.isNull {
            rv= inflate(R.layout._sif_comp_rv_vertical, rvContainer) as RecyclerView
        }
        rvAdp.rv= rv
        rvContainer.addView(rv)

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
//                loge("scrollY= $scrollY dy= $dy scrollX= $scrollX dx= $dx")
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

    fun showNoData(show: Boolean= true){
        layoutView.tv_no_data.visibility= if(show) View.VISIBLE
        else View.GONE
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