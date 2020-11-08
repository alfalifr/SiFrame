package sidev.lib.android.siframe.intfc

@ExperimentalStdlibApi
interface ThreadEditableObj {
    fun wait_(){
        (this as Object).wait()
    }
}