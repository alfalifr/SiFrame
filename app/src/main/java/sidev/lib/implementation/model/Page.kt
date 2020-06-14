package sidev.lib.implementation.model

import sidev.lib.android.siframe.model.DataWithId
import sidev.lib.android.siframe.model.FK_M

data class Page(private val _id: String,
            public var content: FK_M<Content>?,
            var no: Int): DataWithId(_id)