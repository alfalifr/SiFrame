package sidev.lib.android.siframe.intfc.`fun`

interface InternalEditFun {
    var isInternalEdit: Boolean
    fun internalEdit(func: () -> Unit){
        val isInternalEdit_int= isInternalEdit
        func()
        isInternalEdit= isInternalEdit_int
    }
}