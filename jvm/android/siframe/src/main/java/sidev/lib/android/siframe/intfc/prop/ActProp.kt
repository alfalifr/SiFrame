package sidev.lib.android.siframe.intfc.prop

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity

interface ActProp: CtxProp {
    override val _prop_ctx: Activity?
}