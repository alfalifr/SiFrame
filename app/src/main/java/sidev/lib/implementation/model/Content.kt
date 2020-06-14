package sidev.lib.implementation.model

import sidev.lib.android.siframe.model.DataWithId

data class Content(private var _id: String,
            var title: String, var desc: String): DataWithId(_id)