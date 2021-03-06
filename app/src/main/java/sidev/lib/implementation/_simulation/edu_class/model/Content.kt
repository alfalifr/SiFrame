package sidev.lib.implementation._simulation.edu_class.model

import sidev.lib.android.siframe.model.DataWithId
import java.io.Serializable

abstract class Content<T: Content<T>>(id: String): DataWithId<T>(id), Serializable