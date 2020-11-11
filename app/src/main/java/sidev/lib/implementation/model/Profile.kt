package sidev.lib.implementation.model

import sidev.kuliah.tekber.edu_class.model.Presence_
import sidev.lib.android.siframe.model.DataWithId

data class Profile(private var _id: String,
        var name: String, var email: String, var pswd: String): DataWithId<Profile>(_id){
        @Deprecated("Masih blum diimplement scr benar.", ReplaceWith("copy()"))
        override fun copy_(prop: Map<String, Any?>): Profile = copy()
}