package sidev.lib.android.siframe.intfc.lifecycle.sidebase.base

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import sidev.lib.android.siframe.intfc.prop.*

/**
 * Isi fun memiliki nama berawal _ bertujuan agar tidak dipanggil pada implementasi.
 * Fun yg berawal dg __ brarti harus dipanggil.
 */
interface ComplexLifecycleSideBase: LifecycleSideBase,
    ActProp, CtxProp, ViewProp, IntentProp, FragmentManagerProp {

    /**
     * Untuk menginit inheritor dari interface lifecyle.Base.
     * Harus dipanggil agar object keinit.
     */
    override val _prop_act: AppCompatActivity?
    override val _prop_ctx: Context?
    override val _prop_view: View?
    override val _prop_intent: Intent?
    override val _prop_fm: FragmentManager?
}