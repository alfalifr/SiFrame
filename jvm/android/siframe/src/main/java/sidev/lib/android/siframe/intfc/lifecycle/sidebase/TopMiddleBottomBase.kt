package sidev.lib.android.siframe.intfc.lifecycle.sidebase

import android.view.View
import android.view.ViewGroup
import sidev.lib.android.siframe.customizable._init._Config
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.base.LifecycleSideBase
import sidev.lib.android.siframe.tool.util.`fun`.inflate
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