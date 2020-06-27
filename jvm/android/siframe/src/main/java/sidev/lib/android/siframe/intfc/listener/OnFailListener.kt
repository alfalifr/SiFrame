package sidev.lib.android.siframe.intfc.listener

interface OnFailListener {
    fun onFail(resCode: Int, msg: String?, e: Exception?)
}