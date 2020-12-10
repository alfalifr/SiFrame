package sidev.lib.android.siframe.intfc.lifecycle.sidebase

import android.view.View
import androidx.fragment.app.FragmentManager
import sidev.lib.android.siframe.intfc.lifecycle.FragmentHostBase
import sidev.lib.android.siframe.lifecycle.fragment.Frag
import sidev.lib.android.std.tool.util.`fun`.commitFrag
import sidev.lib.android.std.tool.util.`fun`.inflate
import sidev.lib.check.asNotNull
import sidev.lib.check.asNotNullTo
import sidev.lib.check.notNull

interface TopMiddleBottomFragmentBase: TopMiddleBottomBase, FragmentHostBase {
    var topFragment: Frag?
    var bottomFragment: Frag?
    override val _prop_fm: FragmentManager

    override val topLayoutId: Int
        get() = topFragment?.layoutId ?: super.topLayoutId
    override val bottomLayoutId: Int
        get() = bottomFragment?.layoutId ?: super.bottomLayoutId

    override fun __initTopMiddleBottomView(layoutView: View){
        __initTopMiddleBottomViewContainer(layoutView)
        
        val c= layoutView.context
        val isTopLayoutNestedInRv=
            this.asNotNullTo { nestedView: NestedTopMiddleBottomBase -> nestedView.isTopContainerNestedInRv }
                ?: false
        if(topFragment != null && topContainer != null && !isTopLayoutNestedInRv){
            _prop_fm.commitFrag(topFragment!!, topContainer!!, forceReplace = false)
                .asNotNull { frag: Frag ->
                    topFragment= frag
                    frag.addOnViewCreatedListener { view, bundle -> _initTopView(view) }
                }
        }

        if(middleContainer != null){
            c.inflate(middleLayoutId, middleContainer)
                .notNull { v ->
                    middleContainer!!.addView(v)
                    _initMiddleView(v)
                }
        }

        val isBottomLayoutNestedInRv=
            this.asNotNullTo { nestedView: NestedTopMiddleBottomBase -> nestedView.isBottomContainerNestedInRv }
                ?: false
        if(bottomFragment != null && bottomContainer != null && !isBottomLayoutNestedInRv){
            _prop_fm.commitFrag(bottomFragment!!, bottomContainer!!, forceReplace = false)
                .asNotNull { frag: Frag ->
                    bottomFragment= frag
                    frag.addOnViewCreatedListener { view, bundle -> _initBottomView(view) }
                }
        }
    }

    //Opsional karena initView udah dilakukan di dalam fragment.
    override fun _initTopView(topView: View) {}
    override fun _initMiddleView(middleView: View) {}
    override fun _initBottomView(bottomView: View) {}
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