package sidev.lib.android.siframe.model

import sidev.lib.android.siframe.model.intfc.Data
import sidev.lib.android.siframe.model.intfc.ModelId
import sidev.lib.android.siframe.model.intfc.StorageKind

open class DataWithId(@ModelId(StorageKind.ANY) open var id: String): Data