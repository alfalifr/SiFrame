package sidev.lib.android.siframe.intfc.lifecycle.sidebase

import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import sidev.lib.android.siframe.R
import sidev.lib.android.siframe._customizable._Config
import sidev.lib.android.siframe.adapter.SimpleRvAdp
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.base.LifecycleSideBase
import sidev.lib.android.siframe.lifecycle.fragment.Frag
import sidev.lib.android.siframe.tool.util.`fun`.*
import sidev.lib.universal.`fun`.*

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
                        c.commitFrag(topFragment!!, topContainer!!)
                        //Di-init di awal karena fragmentManager tidak dapat menemukan id di kontainer di dalam rvAdp.
                        // Tujuan utamanya hanya agar fragment dapat meng-create view, stlah itu view dialihkan ke variabel lain.

                        topFragment!!.onViewCreatedListener= { view, bundle ->
                            loge("=======topFragment!!.onViewCreatedListener=======")
//                            loge("adp.headerView?.yEndInWindow = ${adp.headerView?.yEndInWindow} adp.headerView?.height = ${adp.headerView?.height} adp.headerView?.size?.string= ${adp.headerView?.size?.string}")
                            loge("view.size.string= ${view.size.string} view.height= ${view.height}")
                            loge("rv.height = ${rv.height} rv.yEndInWindow= ${rv.yEndInWindow} rv.size.string= ${rv.size.string}")
                            loge("adp.footerView?.size?.string = ${adp.footerView?.size?.string} adp.footerView?.height = ${adp.footerView?.height} adp.footerView?.yEndInWindow = ${adp.footerView?.yEndInWindow}")
//                            loge("rv.yEndInWindow +view.size[1] = ${rv.yEndInWindow +view.size[1]} rv.yEndInWindow +(adp.headerView?.size?.get(1) ?: 0) = ${rv.yEndInWindow +(adp.headerView?.size?.get(1) ?: 0)}")
                            loge("rv.screenHeight= ${rv.screenHeight}")
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
                        c.commitFrag(bottomFragment!!, bottomContainer!!)
                        //Di-init di awal karena fragmentManager tidak dapat menemukan id di kontainer di dalam rvAdp.
                        // Tujuan utamanya hanya agar fragment dapat meng-create view, stlah itu view dialihkan ke variabel lain.

                        bottomFragment!!.onViewCreatedListener= { view, _ ->
                            loge("=======bottomFragment!!.onViewCreatedListener=======")
                            loge("adp.headerView?.yEndInWindow = ${adp.headerView?.yEndInWindow} adp.headerView?.size?.string= ${adp.headerView?.size?.string} adp.headerView?.height = ${adp.headerView?.height} adp.headerView?.yEndInWindow= ${adp.headerView?.yEndInWindow}")
                            loge("view.size.string= ${view.size.string} view.height= ${view.height}")
                            loge("rv.height = ${rv.height} rv.yEndInWindow= ${rv.yEndInWindow} rv.size.string= ${rv.size.string}")
                            loge("rv.yEndInWindow +view.size[1] = ${rv.yEndInWindow +view.size[1]} rv.yEndInWindow +(adp.headerView?.size?.get(1) ?: 0) = ${rv.yEndInWindow +(adp.headerView?.size?.get(1) ?: 0)}")
                            loge("rv.screenHeight= ${rv.screenHeight}")
                            loge("adp.headerView != null => ${adp.headerView != null} adp.headerView?.height = ${adp.headerView?.height} adp.headerView?.yEndInWindow = ${adp.headerView?.yEndInWindow} rv.yEndInWindow = ${rv.yEndInWindow}")
                            loge("rv.adapter == adp => ${rv.adapter == adp} rv.adapter == null => ${rv.adapter == null}")
                            val rvYEnd= rv.yEndInWindow +
                                    if(adp.headerView != null && adp.headerView!!.height == 0) //Bertujuan agar perhitungan total panjang rv sesuai setelah ditambah panjang headerView
                                        adp.headerView!!.size[1] //
                                    else 0
                            loge("rvYEnd= $rvYEnd")

                            if(rvYEnd +view.size[1] >= rv.screenHeight){ //Jika lebar rv + bottomView >= screenHeight,
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



/*
    fun __initTopMiddleBottomView(layoutView: View){
        topContainer= try{ layoutView.findViewById(topContainerId) } catch (e: Exception){ null }
        middleContainer= try{ layoutView.findViewById(middleContainerId) } catch (e: Exception){ null }
        bottomContainer= try{ layoutView.findViewById(bottomContainerId) } catch (e: Exception){ null }

        val c= layoutView.context
        var topViewHasBeenAdded= false
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
                        c.inflate(topLayoutId).notNull { topView ->
                            adp.headerView= topView
                            topViewHasBeenAdded= true
                            _initTopView(topView)
                        }
                    }.asnt<RecyclerView.Adapter<*>, SimpleRvAdp<*, *>>{
                        msg= "adater dari ${c.resources.getResourceName(rv.id)} bkn merupakan SimpleRvAdp"
                    }
                }.isNull { msg= "layout: \"$resName\" tidak memiliki RecyclerView" }

            if(msg != null) loge(msg!!)
        }
        if(!topViewHasBeenAdded && topContainer != null){
            c.inflate(topLayoutId, topContainer as ViewGroup)
                .notNull { v ->
                    (topContainer as ViewGroup).addView(v)
 /*
                    <10 Juli 2020> => tidak ada scrollView lagi.
                    this.asNotNull { rvFrag: RvFrag<*> ->
                        //Jika kelas ini [RvFrag], maka cek apakah [topContainer] berada
                        // di dalam [NestedScrollView].
                        topContainer!!.findViewByType<NestedScrollView>(_SIF_Constant.DIRECTION_UP)
                            .notNull {
                                //Jika ada di dalam [NestedScrollView], maka scroll hingga ke atas.
                                // Hal ini dilatar-belakangi karena view pada screen menscroll
                                // ke posisi item pertama [RecyclerView] pada [RvFrag].
                                topContainer!!.addOnGlobalLayoutListener {
                                    rvFrag.fullScroll(View.FOCUS_UP)
                                }
                            }
                    }
// */
                    _initTopView(v)
                }
        }

        if(middleContainer != null){
            c.inflate(middleLayoutId, middleContainer as ViewGroup)
                .notNull { v ->
                    (middleContainer as ViewGroup).addView(v)
                    _initMiddleView(v)
                }
        }

        /**
         * Variabel apakah [bottomContainer] udah diisi.
         * Var ini berguna jika saat [isBottomContainerNestedInRv] == true dan [bottomLayoutId]
         * berhasil di inflate, namun ternyata [rv] yg ditemukan menggunakan [findViewByType]
         * berukuran kecil tidak sampe se-screen (it.y >= _ViewUtil.getScreenHeight(act)),
         * maka [bottomViewHasBeenAdded] jadi false.
         */
        var bottomViewHasBeenAdded= false
        val addBottomViewToContainerFunc= { bottomView: View ->
            (bottomContainer as ViewGroup).addView(bottomView)
            _initBottomView(bottomView)
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
                    c.inflate(bottomLayoutId).notNull { bottomView ->
                        rv.addOnGlobalLayoutListener {
                            val bottom= it.yEndInWindow
                            val screenHeight= it.screenHeight //_ViewUtil.getScreenHeight(ctx)
                            val bool= bottom >= screenHeight
                            loge("bottom= $bottom screenHeight= $screenHeight bool= $bool")
                            if(it.yEndInWindow >= it.screenHeight){
                                adp.footerView= bottomView
                                _initBottomView(bottomView)
                            } else
                                addBottomViewToContainerFunc(bottomView)
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
                        bottomViewHasBeenAdded= true
                    }
                }.asnt<RecyclerView.Adapter<*>, SimpleRvAdp<*, *>>{
                    msg= "adater dari ${c.resources.getResourceName(rv.id)} bkn merupakan SimpleRvAdp"
                }
            }.isNull { msg= "layout: \"$resName\" tidak memiliki RecyclerView" }

            if(msg != null) loge(msg!!)
        }
        if(!bottomViewHasBeenAdded && bottomContainer != null){
            c.inflate(bottomLayoutId, bottomContainer as ViewGroup)
                .notNull { v -> addBottomViewToContainerFunc(v) }
        }
        //initTopView()
    }
 */