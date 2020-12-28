package sidev.lib.android.siframe.intfc.lifecycle

import androidx.lifecycle.LifecycleOwner
import sidev.lib.structure.prop.IdProp

interface IdLifecyleOwner: LifecycleOwner, IdProp<String>