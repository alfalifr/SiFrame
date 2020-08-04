package sidev.lib.android.siframe.intfc.lifecycle.sidebase

import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import org.jetbrains.anko.runOnUiThread
import sidev.lib.android.siframe.R
import sidev.lib.android.siframe._customizable._Config
import sidev.lib.android.siframe.adapter.SimpleRvAdp
import sidev.lib.android.siframe.adapter.layoutmanager.LayoutManagerResp
import sidev.lib.android.siframe.intfc.lifecycle.LifecycleBase
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.base.LifecycleSideBase
import sidev.lib.android.siframe.lifecycle.fragment.Frag
import sidev.lib.android.siframe.tool.`var`._SIF_Constant
import sidev.lib.android.siframe.tool.util.`fun`.*
import sidev.lib.universal.`fun`.*
import sidev.lib.universal.exception.IllegalStateExc

interface NestedTopMiddleBottomFragmentBase: TopMiddleBottomFragmentBase, NestedTopMiddleBottomBase {
    override val topLayoutId: Int
        get() = super<TopMiddleBottomFragmentBase>.topLayoutId
    override val bottomLayoutId: Int
        get() = super<TopMiddleBottomFragmentBase>.bottomLayoutId

    override fun __initTopMiddleBottomView(layoutView: View){
        __initTopMiddleBottomViewContainer(layoutView)

        val c= layoutView.context
        if(isTopContainerNestedInRv && topFragment != null && topContainer != null){
            loge("Mulai operasi penambahan topLayoutId pada RecyclerView")
            var msg: String?= null
            val resName=
                try{ c.resources.getResourceName(layoutId)!! }
                catch (e: Exception){ "<layoutId>" }

            (layoutView.findViewById(rvId)
                ?: layoutView.findViewByType<RecyclerView>())
                .notNull { rv ->
                    rv.adapter.asNotNull { adp: SimpleRvAdp<*, *> ->
                        _prop_fm.commitFrag(topFragment!!, topContainer!!, forceReplace = false)
                            //Di-init di awal karena fragmentManager tidak dapat menemukan id di kontainer di dalam rvAdp.
                            // Tujuan utamanya hanya agar fragment dapat meng-create view, stlah itu view dialihkan ke variabel lain.

                            .asNotNull { frag: Frag ->
                                topFragment= frag
                                frag.addOnViewCreatedListener { view, bundle ->
                                    view.detachFromParent()
                                    adp.headerView= view
                                    _initTopView(view)
                                }
                            }
                    }.asnt<RecyclerView.Adapter<*>, SimpleRvAdp<*, *>>{
                        msg= "adater dari ${c.resources.getResourceName(rv.id)} bkn merupakan SimpleRvAdp"
                    }
                }.isNull { msg= "layout: \"$resName\" tidak memiliki RecyclerView" }

            if(msg != null) loge(msg!!)
        }

        if(middleContainer != null){
            c.inflate(middleLayoutId, middleContainer)
                .notNull { v ->
                    middleContainer!!.addView(v)
                    _initMiddleView(v)
                }
        }

        if(isBottomContainerNestedInRv && bottomFragment != null && bottomContainer != null){
            loge("Mulai operasi penambahan bottomLayoutId pada RecyclerView")
            var msg: String?= null
            val resName=
                try{ c.resources.getResourceName(layoutId)!! }
                catch (e: Exception){ "<layoutId>" }

            (layoutView.findViewById(rvId)
                ?: layoutView.findViewByType<RecyclerView>()).notNull { rv ->
                rv.adapter.asNotNull { adp: SimpleRvAdp<*, *> ->
                    rv.addOnGlobalLayoutListener { //Diletakan di onGlobalLayout agar rv.yEndInWindow bisa keitung dulu.
                        _prop_fm.commitFrag(bottomFragment!!, bottomContainer!!)

                        //Di-init di awal karena fragmentManager tidak dapat menemukan id di kontainer di dalam rvAdp.
                        // Tujuan utamanya hanya agar fragment dapat meng-create view, stlah itu view dialihkan ke variabel lain.
                            .asNotNull { frag: Frag ->
                                bottomFragment= frag
///*
                                fun postBottomFrag(view: View, lm: LayoutManagerResp?){
                                    val rvYEnd= rv.yEndInWindow
//                                    val headerHeight= adp.headerView?.height ?: 0
//                                    val headerMeasuredHeight= adp.headerView?.size?.get(1) ?: 0

                                    val totalYEnd= rvYEnd + view.size[1]
                                    val parentYEnd= rv.parentsTree.find { it.id == R.id.root_page_container }
                                        .asNotNullTo { vg: ViewGroup -> vg.yEndInWindow }
                                        ?: rv.screenHeight

//                                    loge("rv.parent= ${rv.parent}")
//                                    loge("rv.height= ${rv.height} rvYEnd= $rvYEnd headerHeight= $headerHeight headerMeasuredHeight= $headerMeasuredHeight totalYEnd= $totalYEnd view.size[1]= ${view.size[1]} rv.screenHeight= ${rv.screenHeight} parentYEnd= $parentYEnd")

                                    view.detachFromParent() //Biar lebih aman
                                    if(totalYEnd >= parentYEnd)
                                        adp.footerView= view
                                    else bottomContainer!!.addView(view)
//                                    _initBottomView(view)

                                    lm?.onLayoutCompletedListener= null
                                }
// */
                                frag.addOnViewCreatedListener { view, bundle ->
//                                    loge("Nested bottom frag.addOnViewCreatedListener rv.yEnd= ${rv.yEndInWindow}")
                                    view.detachFromParent()
                                }
                                frag.addOnActiveListener { view, parent, pos ->
//                                    loge("Nested bottom frag.addOnActiveListener frag= $frag currentState= ${frag.currentState} parent= $parent pos= $pos")
                                    if(frag.currentState == LifecycleBase.State.STARTED) return@addOnActiveListener

                                    rv.layoutManager.asNotNull { lm: LayoutManagerResp ->
                                        lm.setOnLayoutCompletedListener { state ->
//                                            loge("Nested bottom frag.setOnLayoutCompletedListener state= $state")
                                            rv.post {
                                                //TODO <4 Agustus 2020> => Udah bisa perhitungan bottomFrag, namun kadang" msh nge-lag.
                                                _initBottomView(view)
                                                view.post { postBottomFrag(view, lm) }
                                            }
                                        }
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
//        super<TopMiddleBottomFragmentBase>.__initTopMiddleBottomView(layoutView)
    }
}