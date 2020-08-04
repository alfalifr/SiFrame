package com.sigudang.android.fragments.BoundProcessFrag

import android.content.Intent
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import androidx.core.util.set
//import com.sigudang.android.activities._BoundDetailAct.BoundDetailOwnAbsAct
import com.sigudang.android.models.Bound
import com.sigudang.android.utilities.constant.Constants
import kotlinx.android.synthetic.main._simul_sigud_comp_vp_dot_indicator.view.*
import sidev.lib.android.siframe.arch.intent_state.ViewIntent
import sidev.lib.android.siframe.arch.intent_state.ViewState
import sidev.lib.android.siframe.arch.viewmodel.FiewModel
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.TopMiddleBottomBase
import sidev.lib.android.siframe.lifecycle.fragment.Frag
import sidev.lib.android.siframe.lifecycle.fragment.VpFrag
import sidev.lib.android.siframe.lifecycle.fragment.mvi.MviVpFrag
import sidev.lib.android.siframe.tool.util.`fun`.getExtra
import sidev.lib.android.siframe.tool.util.`fun`.keys
import sidev.lib.android.siframe.tool.util.`fun`.loge
import sidev.lib.implementation.R
import sidev.lib.universal.`fun`.clone
import sidev.lib.universal.tool.util.ThreadUtil
import java.util.ArrayList

abstract class BoundVpProcessFrag: VpFrag<Frag>(), TopMiddleBottomBase{
    final override var bottomContainer: ViewGroup?= null
    final override var middleContainer: ViewGroup?= null
    final override var topContainer: ViewGroup?= null

    final override val bottomLayoutId: Int
        get() = R.layout._simul_sigud_comp_vp_dot_indicator

    protected lateinit var boundData: Bound
    private val boundDataListForSingleProcess= SparseArray<Bound>()


    override fun _initDataFromIntent(intent: Intent) {
        super._initDataFromIntent(intent)
        boundData= intent.getExtra(Constants.DATA_BOUND)!!
        //Init data awal dalam boundProcess.
        val newInstance= boundData.clone()
        boundDataListForSingleProcess[0]= newInstance
    }

    override fun __initView(layoutView: View) {
        super.__initView(layoutView)
        __initTopMiddleBottomView(layoutView)
    }

    final override fun _initBottomView(bottomView: View) {
        loge("_initBottomView() vpFragList.size= ${vpFragList.size}")
//        bottomView.pageIndicatorView.visibility= View.GONE
        if(vpFragList.size > 1)
            bottomView.pageIndicatorView.setViewPager(vp)
        else
            bottomView.pageIndicatorView.visibility= View.GONE
    }

    override fun _initMiddleView(middleView: View) {}
    override fun _initTopView(topView: View) {}


    /**
     * Ambil Bound berdasarkan [fragment].
     * @return bound sesuai index dari [fragment] atau [getLastModifiedBoundData],
     *   atau null jika [fragment] tidak ada dalam [vpFragList].
     */
    fun getBoundData(fragment: Frag): Bound? {
        val fragIndex= vpFragList.indexOf(fragment)

        return if(fragIndex >= 0){
            var bound= boundDataListForSingleProcess[fragIndex]
            if(bound == null){
                bound= getLastModifiedBoundData()
                boundDataListForSingleProcess[fragIndex]= bound
            }
            bound
        } else null
    }

    /**
     * Membuat instance Bound baru yg merupakan salinan dari bound yg trahir diubah.
     * @return instance baru bound,
     *   atau [currentBound] jika [fragment] tidak ada dalam [vpFragList].
     */
    fun setBoundCheckpoint(fragment: Frag, currentBound: Bound?): Bound?{
        val fragIndex= vpFragList.indexOf(fragment)
        return if(fragIndex >= 0){
            val lastModBound= currentBound ?: getLastModifiedBoundData()
            val boundNewInstance= lastModBound.clone()
            boundDataListForSingleProcess[fragIndex]= boundNewInstance
            boundNewInstance
        } else currentBound
    }

    /**
     * Last-Modified adalah bound yg ada di index akhir.
     */
    fun getLastModifiedBoundData(): Bound{
        val key= boundDataListForSingleProcess.keys.last()
        return boundDataListForSingleProcess[key]
    }
}