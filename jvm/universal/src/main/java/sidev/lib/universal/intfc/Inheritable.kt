package sidev.lib.universal.intfc

interface Inheritable {
//    val isSuper: Boolean
    var isInherited: Boolean

    fun _configInheritable()

    /**
     * @return true jika func dijalankan.
     * Tidak seharusnya dirubah.
     */
    fun doWhenNotIherited(func: () -> Unit): Boolean{
        _configInheritable()
        return if(!isInherited){
            func()
            true
        } else
            false
    }
}