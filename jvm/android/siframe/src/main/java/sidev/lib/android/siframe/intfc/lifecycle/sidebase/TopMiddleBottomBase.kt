package sidev.lib.android.siframe.intfc.lifecycle.sidebase

import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import sidev.lib.android.siframe._customizable._Config
import sidev.lib.android.siframe.adapter.SimpleRvAdp
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.base.LifecycleSideBase
import sidev.lib.android.siframe.lifecycle.fragment.RvFrag
import sidev.lib.android.siframe.tool.`var`._SIF_Constant
import sidev.lib.android.siframe.tool.util.`fun`.addOnGlobalLayoutListener
import sidev.lib.android.siframe.tool.util.`fun`.findViewByType
import sidev.lib.android.siframe.tool.util.`fun`.inflate
import sidev.lib.android.siframe.tool.util.`fun`.loge
import sidev.lib.universal.`fun`.asNotNull
import sidev.lib.universal.`fun`.notNull

interface TopMiddleBottomBase: LifecycleSideBase {
    var topContainer: View?
    var middleContainer: View?
    var bottomContainer: View?

    val topContainerId: Int
        get()= _Config.ID_RL_TOP_CONTAINER
    val middleContainerId: Int
        get()= _Config.ID_RL_MIDDLE_CONTAINER
    val bottomContainerId: Int
        get()= _Config.ID_RL_BOTTOM_CONTAINER
    val rvId: Int
        get()= _Config.ID_RV

    /**
     * <10 Juli 2020> => Untuk sementara, [RecyclerView] yg dimaksud adalah [SimpleRvAdp].
     */
    val isTopContainerNestedInRv
        get()= false
    val isMiddleContainerNestedInRv
        get()= false
    val isBottomContainerNestedInRv
        get()= false

    val topLayoutId: Int
        get()= _Config.INT_EMPTY
    val middleLayoutId: Int
        get()= _Config.INT_EMPTY
    val bottomLayoutId: Int
        get()= _Config.INT_EMPTY

    fun __initTopMiddleBottomView(layoutView: View){
        topContainer= try{ layoutView.findViewById(topContainerId) } catch (e: Exception){ null }
        middleContainer= try{ layoutView.findViewById(middleContainerId) } catch (e: Exception){ null }
        bottomContainer= try{ layoutView.findViewById(bottomContainerId) } catch (e: Exception){ null }

        val c= layoutView.context
        if(topContainer != null){
            c.inflate(topLayoutId, topContainer as ViewGroup)
                .notNull { v ->
                    (topContainer as ViewGroup).addView(v)
// /*
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
        if(bottomContainer != null){
            c.inflate(bottomLayoutId, bottomContainer as ViewGroup)
                .notNull { v ->
                    (bottomContainer as ViewGroup).addView(v)
                    _initBottomView(v)
                }
        }
        //initTopView()
    }

    fun _initTopView(topView: View)
    fun _initMiddleView(middleView: View)
    fun _initBottomView(bottomView: View)
}