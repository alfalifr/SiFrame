package sidev.lib.android.siframe.intfc.lifecycle.sidebase

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import sidev.lib.android.siframe.adapter.SimpleRvAdp
import sidev.lib.android.siframe.intfc.prop.RvAdpProp
import sidev.lib.android.std.tool.util.`fun`.*
import sidev.lib.check.*

/** [TopMiddleBottomBase] yg [topView] dan [bottomView]-nya dapat di nested ke dalam [SimpleRvAdp] */
interface NestedTopMiddleBottomBase: TopMiddleBottomBase, RvAdpProp{
    override val rvAdp: SimpleRvAdp<*, *>?

    /**
     * <10 Juli 2020> => Untuk sementara, [RecyclerView] yg dimaksud
     * adalah rv yg adapaternya menggunakan [SimpleRvAdp].
     */
    val isTopContainerNestedInRv
        get()= false
//  val isMiddleContainerNestedInRv get()= false
    val isBottomContainerNestedInRv
        get()= false

    override val topView: View?
        get()= if(!isTopContainerNestedInRv) super.topView
            else rvAdp?.headerView
    override val bottomView: View?
        get()= if(!isBottomContainerNestedInRv) super.bottomView
            else rvAdp?.footerView


    override fun __initTopMiddleBottomView(layoutView: View){
        val c= layoutView.context
        if(isTopContainerNestedInRv){
//            loge("Mulai operasi penambahan topLayoutId pada RecyclerView")
            var msg: String?= null
            val resName=
                try{ c.resources.getResourceName(layoutId)!! }
                catch (e: Exception){ "<layoutId>" }

            (layoutView.findViewById(rvId)
                ?: layoutView.findViewByType<RecyclerView>())
                .notNull { rv ->
                    rv.adapter.asNotNull { adp: SimpleRvAdp<*, *> ->
                        (topContainer.notNullTo { it.getChildAt(0) }
                            ?: c.inflate(topLayoutId)
                        ).notNull { topView ->
                            topView.detachFromParent() //Agar tidak menimbulkan error saat di-attach di RvAdp.
                            adp.headerView= topView
                            topContainer.notNull { it.removeAllViews() }
                            _initTopView(topView)
                        }
                    }.asnt<RecyclerView.Adapter<*>, SimpleRvAdp<*, *>>{
                        msg= "adater dari ${c.resources.getResourceName(rv.id)} bkn merupakan SimpleRvAdp."
                    }
                }.isNull { msg= "layout: \"$resName\" tidak memiliki RecyclerView." }

            if(msg != null) loge(msg!!)
        }

        if(isBottomContainerNestedInRv){
//            loge("Mulai operasi penambahan bottomLayoutId pada RecyclerView")
            var msg: String?= null
            val resName=
                try{ c.resources.getResourceName(layoutId)!! }
                catch (e: Exception){ "<layoutId>" }

            (layoutView.findViewById(rvId)
                ?: layoutView.findViewByType<RecyclerView>()).notNull { rv ->
                rv.adapter.asNotNull { adp: SimpleRvAdp<*, *> ->
                    (bottomContainer.notNullTo { it.getChildAt(0) }
                        ?: c.inflate(bottomLayoutId)
                    ).notNull { bottomView ->
                        rv.addOnGlobalLayoutListener {
/*
                            val bottomRv= it.yEndInWindow
                            val bottomViewSize= bottomView.size
                            val bottomViewHeight= bottomView.height
                            val screenHeight= it.screenHeight //_ViewUtil.getScreenHeight(ctx)
                            val bottom= bottomRv +bottomViewSize[1] //_ViewUtil.getScreenHeight(ctx)
                            val bool= bottom >= screenHeight
                            loge("bottomRv= $bottomRv bottom= $bottom bottomViewHeight= $bottomViewHeight bottomViewSize= ${bottomViewSize.string} screenHeight= $screenHeight bool= $bool")
 */
                            fun attachBottomViewInRv(){
                                bottomView.detachFromParent() //Agar tidak menimbulkan error saat di-attach di RvAdp.
                                adp.footerView= bottomView
                                bottomContainer?.removeAllViews()
                                _initBottomView(bottomView)
                            }

                            when{
                                it.yEndInWindow +bottomView.size[1] >= it.screenHeight -> attachBottomViewInRv()
                                bottomContainer != null ->{ //Ini udah di-init oleh super karena OnGlobalLayout dilakukan scr trahir stlah kode di sini udah dieksekusi.
                                    bottomContainer!!.forcedAddView(bottomView)
                                    _initBottomView(bottomView)
                                }
                                else -> { //Jika gakda bottomContainer, maka nested aja bottomView di dalam rvAdp
                                    loge("bottomContainer tidak ada, bottomView di-nested ke dalam rvAdp.")
                                    attachBottomViewInRv()
                                }
                            }
                        }
                    }
                }.asnt<RecyclerView.Adapter<*>, SimpleRvAdp<*, *>>{
                    msg= "adater dari ${c.resources.getResourceName(rv.id)} bkn merupakan SimpleRvAdp."
                }
            }.isNull { msg= "layout: \"$resName\" tidak memiliki RecyclerView." }

            if(msg != null) loge(msg!!)
        }
        super.__initTopMiddleBottomView(layoutView)
    }
}