package sidev.lib.android.siframe.tool.`var`

import android.app.Activity
import sidev.lib.android.siframe.lifecycle.activity.SingleFragAct_Simple

/**
 * Tidak untuk dirubah oleh programmer!!!
 */
object _SIF_Config {
    var APP_VALID= true
        internal set
    var CLASS_SINGLE_FRAG_ACT: Class<out Activity> = SingleFragAct_Simple::class.java
}