package sidev.lib.android.siframe.intfc.lifecycle.sidebase.base

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import sidev.lib.android.siframe.customizable._init._ConfigBase

/**
 * Isi fun memiliki nama berawal _ bertujuan agar tidak dipanggil pada implementasi.
 */
interface ComplexLifecycleSideBase: LifecycleSideBase {
    val layoutId: Int
    val styleId: Int
        get() = _ConfigBase.STYLE_APP

    /**
     * Untuk menginit inheritor dari interface lifecyle.Base.
     * Harus dipanggil agar object keinit.
     */
    fun _initInheritorBase()
    val _sideBase_act: AppCompatActivity
    val _sideBase_view: View
    val _sideBase_intent: Intent
    val _sideBase_ctx: Context
    val _sideBase_fm: FragmentManager
}