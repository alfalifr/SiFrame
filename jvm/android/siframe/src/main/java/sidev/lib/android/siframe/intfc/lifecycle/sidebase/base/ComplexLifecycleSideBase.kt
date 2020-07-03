package sidev.lib.android.siframe.intfc.lifecycle.sidebase.base

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import sidev.lib.android.siframe.intfc.prop.ActProp

/**
 * Isi fun memiliki nama berawal _ bertujuan agar tidak dipanggil pada implementasi.
 * Fun yg berawal dg __ brarti harus dipanggil.
 */
interface ComplexLifecycleSideBase: LifecycleSideBase, ActProp {

    /**
     * Untuk menginit inheritor dari interface lifecyle.Base.
     * Harus dipanggil agar object keinit.
     */
    override val _prop_act: AppCompatActivity?
    val _sideBase_view: View?
    val _sideBase_intent: Intent?
    val _sideBase_ctx: Context?
    val _sideBase_fm: FragmentManager?
}