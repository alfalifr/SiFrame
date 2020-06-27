package sidev.lib.android.siframe.repository

import androidx.annotation.CallSuper
import sidev.lib.android.siframe.intfc.lifecycle.ExpirableBase

interface RepositoryCallback: ExpirableBase {
//    var callbackCtx: Context?
    var repository: Repository?

    /**
     * @return nilainya ditangkap oleh SifViewModel
     */
    fun onRepoSucc(reqCode: String, resCode: Int, data: Map<String, Any>?)
    fun onRepoFail(reqCode: String, resCode: Int, msg: String?= "", e: Exception?= null)
    @CallSuper
    fun downloadData(reqCode: String, vararg data: Pair<String, Any>) {
        val map= if(data.isEmpty()) null
            else mapOf(*data)
        repository?.postRequest(reqCode, map)
    }
    @CallSuper
    fun uploadData(reqCode: String, vararg data: Pair<String, Any>) {
        val map= if(data.isEmpty()) null
            else mapOf(*data)
        repository?.postRequest(reqCode, map)
    }
    @CallSuper
    fun sendRequest(reqCode: String, vararg data: Pair<String, Any>) {
        val map= if(data.isEmpty()) null
            else mapOf(*data)
        repository?.postRequest(reqCode, map)
    }

    fun initRepo(): Repository? {
        return null
    }
}