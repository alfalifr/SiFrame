package sidev.lib.android.siframe.intfc.listener

import android.content.Context
import android.util.Log
import android.view.View
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import sidev.lib.android.siframe.adapter.layoutmanager.LinearLm
import sidev.lib.universal.`fun`.*

open class RvScrollListener : RecyclerView.OnScrollListener(){
    /**
     * Total scroll scr vertical yg telah dilakukan pada [RecyclerView].
     */
    var scrollY= 0
        private set

    /**
     * Total scroll scr horizontal yg telah dilakukan pada [RecyclerView].
     */
    var scrollX= 0
        private set
    @CallSuper
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        //Anggapannya gak mungkin recyclerView.layoutManager-nya null kalo bisa discroll.
        recyclerView.layoutManager.notNullTo { lm ->
            var orientation= RecyclerView.VERTICAL

            val isAtFirstPosition=
            (lm.asNotNullTo { llm: LinearLayoutManager ->
                orientation= llm.orientation
                llm.findFirstVisibleItemPosition() == 0
            }
            ?: lm.asNotNullTo { sm: StaggeredGridLayoutManager ->
                try{
                    orientation= sm.orientation
                    sm.findFirstVisibleItemPositions(null).first() == 0
                }
                catch (e: Exception){ false }
            }
            ?: false)

            if(isAtFirstPosition){
                when(orientation){
                    RecyclerView.VERTICAL -> scrollY= 0
                    RecyclerView.HORIZONTAL -> scrollX= 0
                }
            } else{
                scrollY += dy //-1
                scrollX += dx //-1
            }
        }
    }
}