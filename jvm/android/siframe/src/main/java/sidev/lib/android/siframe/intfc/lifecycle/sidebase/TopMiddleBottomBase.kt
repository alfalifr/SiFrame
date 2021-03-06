package sidev.lib.android.siframe.intfc.lifecycle.sidebase

import android.view.View
import android.view.ViewGroup
import sidev.lib.android.siframe.`val`._SIF_Config
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.base.LifecycleSideBase
import sidev.lib.android.std.`val`._Config
import sidev.lib.android.std.tool.util.`fun`.inflate
import sidev.lib.check.asNotNullTo
import sidev.lib.check.notNull

interface TopMiddleBottomBase: LifecycleSideBase {
    var topContainer: ViewGroup?
    var middleContainer: ViewGroup?
    var bottomContainer: ViewGroup?

    /**
     * Cara mudah untuk mengambil [topView] tanpa harus mengkonversi [topContainer] ke ViewGroup.
     * Selain itu, variabel ini juga berguna bagi [NestedTopMiddleBottomBase] karena [topView]
     * tidak berada di [topContainer].
     */
    val topView: View?
        get()= topContainer.asNotNullTo { vg: ViewGroup -> vg.getChildAt(0) }
    /** Dokumentasi sama dg [topView]. */
    val bottomView: View?
        get()= bottomContainer.asNotNullTo { vg: ViewGroup -> vg.getChildAt(0) }

    val topContainerId: Int
        get()= _SIF_Config.ID_RL_TOP_CONTAINER
    val middleContainerId: Int
        get()= _SIF_Config.ID_RL_MIDDLE_CONTAINER
    val bottomContainerId: Int
        get()= _SIF_Config.ID_RL_BOTTOM_CONTAINER
    val rvId: Int
        get()= _SIF_Config.ID_RV

    val topLayoutId: Int
        get()= _Config.INT_EMPTY
    val middleLayoutId: Int
        get()= _Config.INT_EMPTY
    val bottomLayoutId: Int
        get()= _Config.INT_EMPTY

    fun __initTopMiddleBottomViewContainer(layoutView: View){
        topContainer= try{ layoutView.findViewById(topContainerId) } catch (e: Exception){ null }
        middleContainer= try{ layoutView.findViewById(middleContainerId) } catch (e: Exception){ null }
        bottomContainer= try{ layoutView.findViewById(bottomContainerId) } catch (e: Exception){ null }
    }
    fun __initTopMiddleBottomView(layoutView: View){
        __initTopMiddleBottomViewContainer(layoutView)

        val c= layoutView.context
        val isTopLayoutNestedInRv=
            this.asNotNullTo { nestedView: NestedTopMiddleBottomBase -> nestedView.isTopContainerNestedInRv }
                ?: false
        if(topContainer != null && !isTopLayoutNestedInRv){
            c.inflate(topLayoutId, topContainer)
                .notNull { v ->
                    topContainer!!.addView(v)
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
            c.inflate(middleLayoutId, middleContainer)
                .notNull { v ->
                    middleContainer!!.addView(v)
                    _initMiddleView(v)
                }
        }
/*
        /**
         * Variabel apakah [bottomContainer] udah diisi.
         * Var ini berguna jika saat [isBottomContainerNestedInRv] == true dan [bottomLayoutId]
         * berhasil di inflate, namun ternyata [rv] yg ditemukan menggunakan [findViewByType]
         * berukuran kecil tidak sampe se-screen (it.y >= _ViewUtil.getScreenHeight(act)),
         * maka [bottomViewHasBeenAdded] jadi false.
         */
 */
        val isBottomLayoutNestedInRv=
            this.asNotNullTo { nestedView: NestedTopMiddleBottomBase -> nestedView.isBottomContainerNestedInRv }
                ?: false

        if(bottomContainer != null && !isBottomLayoutNestedInRv){
            c.inflate(bottomLayoutId, bottomContainer)
                .notNull { v ->
                    bottomContainer!!.addView(v)
                    _initBottomView(v)
                }
        }
        //initTopView()
    }

    fun _initTopView(topView: View)
    fun _initMiddleView(middleView: View)
    fun _initBottomView(bottomView: View)
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