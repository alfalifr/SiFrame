package sidev.lib.android.siframe.arch.viewmodel

import sidev.lib.android.siframe.intfc.lifecycle.ExpirableBase
import sidev.lib.universal.`fun`.notNullTo

/**
 * Sbg penanda bahwa kelas turunan ini merupakan ViewModel dalam arsitektur.
 */
interface ArchViewModel: ExpirableBase{
    /**
     * Menambahkan nilai scr manual ke ViewModel.
     * Jika nilai yg ditambahkan merupakan tipe data LifeData<*>, maka akan error.
     * Gunakan <code>addLifeData()</code> sbg gantinya.
     */
    fun <T> addValue(key: String, data: T, replaceExisting: Boolean= true): LifeData<T>?
    /**
     * Menambahkan nilai yg sudah dibungkus ke dalam LifeData scr manual ke ViewModel.
     * @return true jika lifeData berhasil dimasukkan.
     *          Bbrp kondisi jika return false:
     *            -ArchViewModel ini sudah kadaluwarsa.
     *            -
     */
    fun <T> addLifeData(key: String, lifeData: T, replaceExisting: Boolean= true): Boolean

    /**
     * @return null jika FiewModel ini expired atau data sebelumnya belum ada.
     */
    fun <T> get(key: String): LifeData<T>?

    /**
     * @param forceReload false jika data sudah ada di {@link #mData} sehingga tidak direload dari repo.
     *                      true jika data sudah ada namun tetap direload dari repo.
     */
    fun <T> observe(reqCode: String, vararg params: Pair<String, Any>,
                    /*safeValue: Boolean= true, */
                    loadLater: Boolean= false, forceReload: Boolean= false,
                    onPreLoad: (() -> Unit)?= null, //fungsi saat LifeData msh loading
                    onObserve: (T) -> Unit): LifeData<T>?

    /**
     * Untuk mereload data dari repo. Fungsi ini berguna bagi data yg sudah ada di {@link #mData}.
     * Fungsi ini juga digunakan scr internal untuk melakukan sendRequest() yg didahului dg LifeData.onPreLoad()
     * @return true jika data sudah ada di {@link #mData}.
     *          false jika data belum ada di {@link #mData} sehingga
     *          tidak dilakukan sendRequest(reqCode, *params) ke repo.
     */
    fun reload(reqCode: String, vararg params: Pair<String, Any>): Boolean
}