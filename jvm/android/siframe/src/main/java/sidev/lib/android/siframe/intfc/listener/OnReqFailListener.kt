package sidev.lib.android.siframe.intfc.listener

interface OnReqFailListener : OnFailListener{
    companion object{
        val REQ_GENERAL= "general"
    }
    override fun onFail(resCode: Int, msg: String?, e: Exception?) {
        onFail(REQ_GENERAL, resCode, msg, e)
    }
    fun onFail(reqCode: String, resCode: Int, msg: String?, e: Exception?)
}