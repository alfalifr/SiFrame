package com.sigudang.android.fragments.BoundProcessFrag

import androidx.core.util.set
import androidx.fragment.app.Fragment
import com.sigudang.android.models.Bound
import sidev.lib.android.siframe.adapter.SimpleRvAdp
import sidev.lib.android.siframe.intfc.lifecycle.rootbase.FragBase
import sidev.lib.android.siframe.lifecycle.fragment.Frag
import sidev.lib.android.siframe.lifecycle.fragment.RvFrag
import sidev.lib.android.siframe.tool.util.`fun`.keys
import sidev.lib.android.siframe.tool.util.`fun`.loge
import sidev.lib.universal.`fun`.asNotNull
import sidev.lib.universal.`fun`.asNotNullTo
import sidev.lib.universal.`fun`.clazz
import sidev.lib.universal.`fun`.clone

interface BoundProcessRootFrag: FragBase{

    /**
     * Ambil Bound berdasarkan [fragment].
     * @return bound sesuai index dari [fragment] atau [getLastModifiedBoundData],
     *   atau null jika [fragment] tidak ada dalam [vpFragList].
     */
    fun getBoundDataFromParent(): Bound? {
        loge("clazz= ${this::class} _prop_parentLifecycle is BoundVpProcessFrag => ${_prop_parentLifecycle is BoundVpProcessFrag} _prop_parentLifecycle == null => ${_prop_parentLifecycle == null} _prop_parentLifecycle?.clazz= ${_prop_parentLifecycle?.clazz}")
        return _prop_parentLifecycle.asNotNullTo { vpFrag: BoundVpProcessFrag ->
            vpFrag.getBoundData(this as Frag)
        }
    }

    /**
     * Membuat instance Bound baru yg merupakan salinan dari bound yg trahir diubah.
     * @return instance baru bound,
     *   atau [currentBound] jika [fragment] tidak ada dalam [vpFragList].
     */
    fun setBoundCheckpoint(currentBound: Bound?): Bound?{
        return _prop_parentLifecycle.asNotNullTo { vpFrag: BoundVpProcessFrag ->
            vpFrag.setBoundCheckpoint(this as Frag, currentBound)
        }
    }

    /**
     * Last-Modified adalah bound yg ada di index akhir.
     */
    fun getLastModifiedBoundData(): Bound {
        return try{ _prop_parentLifecycle.asNotNullTo { vpFrag: BoundVpProcessFrag -> vpFrag.getLastModifiedBoundData() }!! }
        catch (e: KotlinNullPointerException){ throw RuntimeException("ViewPager tempat fragment ini nempel bkn BoundVpProcessFrag") }
    }
}