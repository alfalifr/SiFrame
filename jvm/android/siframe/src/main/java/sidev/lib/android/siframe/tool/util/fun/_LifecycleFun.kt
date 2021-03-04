package sidev.lib.android.siframe.tool.util.`fun`

import androidx.lifecycle.LifecycleOwner
import sidev.lib.android.siframe.intfc.lifecycle.IdLifecyleOwner


val LifecycleOwner.name: String
    get()= if(this is IdLifecyleOwner) id else this::class.java.name