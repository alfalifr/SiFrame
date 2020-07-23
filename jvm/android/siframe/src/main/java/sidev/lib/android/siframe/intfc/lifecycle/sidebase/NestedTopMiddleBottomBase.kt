package sidev.lib.android.siframe.intfc.lifecycle.sidebase

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import sidev.lib.android.siframe._customizable._Config
import sidev.lib.android.siframe.adapter.SimpleRvAdp
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.base.LifecycleSideBase
import sidev.lib.android.siframe.intfc.prop.RvAdpProp
import sidev.lib.android.siframe.tool.util.`fun`.*
import sidev.lib.universal.`fun`.*

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
            loge("Mulai operasi penambahan topLayoutId pada RecyclerView")
            var msg: String?= null
            val resName=
                try{ c.resources.getResourceName(layoutId)!! }
                catch (e: Exception){ "<layoutId>" }

            (layoutView.findViewById(rvId)
                ?: layoutView.findViewByType<RecyclerView>())
                .notNull { rv ->
                    rv.adapter.asNotNull { adp: SimpleRvAdp<*, *> ->
                        (topContainer.asNotNullTo { vg: ViewGroup -> vg.getChildAt(0) }
                            ?: c.inflate(topLayoutId)
                        ).notNull { topView ->
                            adp.headerView= topView
                            topContainer.asNotNull { vg: ViewGroup -> vg.removeAllViews() }
                            _initTopView(topView)
                        }
                    }.asnt<RecyclerView.Adapter<*>, SimpleRvAdp<*, *>>{
                        msg= "adater dari ${c.resources.getResourceName(rv.id)} bkn merupakan SimpleRvAdp"
                    }
                }.isNull { msg= "layout: \"$resName\" tidak memiliki RecyclerView" }

            if(msg != null) loge(msg!!)
        }

        if(isBottomContainerNestedInRv){
            loge("Mulai operasi penambahan bottomLayoutId pada RecyclerView")
            var msg: String?= null
            val resName=
                try{ c.resources.getResourceName(layoutId)!! }
                catch (e: Exception){ "<layoutId>" }

            (layoutView.findViewById(rvId)
                ?: layoutView.findViewByType<RecyclerView>()).notNull { rv ->
                rv.adapter.asNotNull { adp: SimpleRvAdp<*, *> ->
                    (bottomContainer.asNotNullTo { vg: ViewGroup -> vg.getChildAt(0) }
                        ?: c.inflate(bottomLayoutId)
                    ).notNull { bottomView ->
                        rv.addOnGlobalLayoutListener {
//                            val bottom= it.yEndInWindow
//                            val screenHeight= it.screenHeight //_ViewUtil.getScreenHeight(ctx)
//                            val bool= bottom >= screenHeight
//                            loge("bottom= $bottom screenHeight= $screenHeight bool= $bool")
                            if(it.yEndInWindow >= it.screenHeight){
                                adp.footerView= bottomView
                                bottomContainer.asNotNull { vg: ViewGroup -> vg.removeAllViews() }
                                _initBottomView(bottomView)
                            }
/*
                            else{
                                (bottomContainer as ViewGroup).addView(bottomView)
                                _initBottomView(bottomView)
                            }
 */
                        }
/*
                        //Ambil Context untuk pemanggilan fungsi [_ViewUtil.getScreenHeight].
                        when(this){
                            is Activity -> this
                            is Fragment -> this.context
                            else -> null
                        }.notNull{ ctx ->
                        }.isNull {
                            adp.footerView= bottomView
                            _initBottomView(bottomView)
                        }
 */
                    }
                }.asnt<RecyclerView.Adapter<*>, SimpleRvAdp<*, *>>{
                    msg= "adater dari ${c.resources.getResourceName(rv.id)} bkn merupakan SimpleRvAdp"
                }
            }.isNull { msg= "layout: \"$resName\" tidak memiliki RecyclerView" }

            if(msg != null) loge(msg!!)
        }
        super.__initTopMiddleBottomView(layoutView)
    }
}