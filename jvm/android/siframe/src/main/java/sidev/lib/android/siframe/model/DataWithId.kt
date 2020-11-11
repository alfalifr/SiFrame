package sidev.lib.android.siframe.model

//import sidev.lib.android.siframe.model.intfc.Data
import sidev.lib.android.siframe.model.intfc.ModelId
import sidev.lib.android.siframe.model.intfc.StorageKind
import sidev.lib.structure.data.Data

abstract class DataWithId<T: DataWithId<T>>(@ModelId(StorageKind.ANY) open var id: String): Data<T>