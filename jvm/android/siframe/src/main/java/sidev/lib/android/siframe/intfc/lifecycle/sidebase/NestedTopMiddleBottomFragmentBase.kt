package sidev.lib.android.siframe.intfc.lifecycle.sidebase

import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import sidev.lib.android.siframe.R
import sidev.lib.android.siframe._customizable._Config
import sidev.lib.android.siframe.adapter.SimpleRvAdp
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.base.LifecycleSideBase
import sidev.lib.android.siframe.lifecycle.fragment.Frag
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
                            .notNull { loge("it == topFragment => ${it == topFragment}") }
                            .isNull { loge("topFragment is null!") }
//                        c.commitFrag(topFragment!!, topContainer!!)
                        //Di-init di awal karena fragmentManager tidak dapat menemukan id di kontainer di dalam rvAdp.
                        // Tujuan utamanya hanya agar fragment dapat meng-create view, stlah itu view dialihkan ke variabel lain.

                        topFragment!!.onViewCreatedListener= { view, bundle ->
/*
                            loge("=======topFragment!!.onViewCreatedListener=======")
//                            loge("adp.headerView?.yEndInWindow = ${adp.headerView?.yEndInWindow} adp.headerView?.height = ${adp.headerView?.height} adp.headerView?.size?.string= ${adp.headerView?.size?.string}")
                            loge("view.size.string= ${view.size.string} view.height= ${view.height}")
                            loge("rv.height = ${rv.height} rv.yEndInWindow= ${rv.yEndInWindow} rv.size.string= ${rv.size.string}")
                            loge("adp.footerView?.size?.string = ${adp.footerView?.size?.string} adp.footerView?.height = ${adp.footerView?.height} adp.footerView?.yEndInWindow = ${adp.footerView?.yEndInWindow}")
//                            loge("rv.yEndInWindow +view.size[1] = ${rv.yEndInWindow +view.size[1]} rv.yEndInWindow +(adp.headerView?.size?.get(1) ?: 0) = ${rv.yEndInWindow +(adp.headerView?.size?.get(1) ?: 0)}")
                            loge("rv.screenHeight= ${rv.screenHeight}")
 */
                            view.detachFromParent()
                            adp.headerView= view
                            _initTopView(view)
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

                        bottomFragment!!.onViewCreatedListener= { view, _ ->
/*
                            loge("=======bottomFragment!!.onViewCreatedListener=======")
                            loge("adp.headerView?.yEndInWindow = ${adp.headerView?.yEndInWindow} adp.headerView?.size?.string= ${adp.headerView?.size?.string} adp.headerView?.height = ${adp.headerView?.height} adp.headerView?.yEndInWindow= ${adp.headerView?.yEndInWindow}")
                            loge("view.size.string= ${view.size.string} view.height= ${view.height}")
                            loge("rv.yEndInWindow +view.size[1] = ${rv.yEndInWindow +view.size[1]} rv.yEndInWindow +(adp.headerView?.size?.get(1) ?: 0) = ${rv.yEndInWindow +(adp.headerView?.size?.get(1) ?: 0)}")
                            loge("rv.screenHeight= ${rv.screenHeight}")
                            loge("adp.headerView != null => ${adp.headerView != null} adp.headerView?.height = ${adp.headerView?.height} adp.headerView?.yEndInWindow = ${adp.headerView?.yEndInWindow} rv.yEndInWindow = ${rv.yEndInWindow}")
                            loge("rv.adapter == adp => ${rv.adapter == adp} rv.adapter == null => ${rv.adapter == null}")
                            loge("rv.height = ${rv.height} rv.yEndInWindow= ${rv.yEndInWindow} rv.size.string= ${rv.size.string}")
 */
                            val rvYEnd= rv.yEndInWindow
                            val headerHeight= adp.headerView?.height ?: 0
                            val headerMeasuredHeight= adp.headerView?.size?.get(1) ?: 0

                            val totalYEnd= rvYEnd +
                                    if(headerHeight == 0 || headerMeasuredHeight > rvYEnd) //Bertujuan agar perhitungan total panjang rv sesuai setelah ditambah panjang headerView
                                        headerMeasuredHeight
                                    else 0
/*
                            when{
                                headerHeight == 0 -> headerMeasuredHeight
                                headerMeasuredHeight > rvYEnd -> headerMeasuredHeight
                                else -> 0
                            }
 */
//                            loge("rvYEnd= $rvYEnd totalYEnd= $totalYEnd")

                            if(totalYEnd +view.size[1] >= rv.screenHeight){ //Jika lebar rv + bottomView >= screenHeight,
                                view.detachFromParent() // maka lepas bottomView dari kontainer awal.
                                adp.footerView= view // dan ganti masukan ke adapter agar jadi nested
                            }
                            _initBottomView(view)
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