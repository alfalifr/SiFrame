package sidev.lib.android.siframe.intfc.listener

interface OnFailListener: Listener {
    fun onFail(resCode: Int, msg: String?, e: Exception?)
}