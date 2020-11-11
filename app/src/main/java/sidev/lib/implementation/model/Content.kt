package sidev.lib.implementation.model

import sidev.lib.android.siframe.model.DataWithId

data class Content(private var _id: String,
            var title: String, var desc: String): DataWithId<Content>(_id){
    @Deprecated("Masih blum diimplement scr benar.", ReplaceWith("copy()"))
    override fun copy_(prop: Map<String, Any?>): Content = copy()
}