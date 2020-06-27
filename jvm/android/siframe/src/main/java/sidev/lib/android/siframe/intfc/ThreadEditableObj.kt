package sidev.lib.android.siframe.intfc

interface ThreadEditableObj {
    fun wait_(){
        (this as Object).wait()
    }
}