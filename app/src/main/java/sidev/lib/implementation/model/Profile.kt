package sidev.lib.implementation.model

import sidev.lib.android.siframe.model.DataWithId

data class Profile(private var _id: String,
        var name: String, var email: String, var pswd: String): DataWithId(_id)