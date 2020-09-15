package sidev.lib.android.siframe.arch.viewmodel

import androidx.lifecycle.LifecycleOwner

/**
 * Sbg penanda bahwa kelas turunan ini merupakan ViewModel dalam arsitektur.
 */
interface ObservableViewModel: ArchViewModel{

    /**
     * Fungsi yg digunakan untuk merequest data yg disimpan pada [ObservableViewModel] ini.
     * Jika nilai belum terdapat dalam [ObservableViewModel] ini, maka fungsi ini akan
     * memanggil repo untuk mendapatkan data.
     *
     * @param loadLater true jika ternyata nilai yg direquest dg kode [reqCode] belum terdapat
     *   di dalam [ObservableViewModel] ini, namun tidak diload dari repo setelah fungsi [observe] ini
     *   dipanggil. Load dari repo dapat dijalankan kemudian dg memanggil fungsi [reload].
     *
     * @param forceReload false jika data sudah ada di {@link #mData} sehingga tidak direload dari repo.
     *   true jika data sudah ada namun tetap direload dari repo.
     *
     * @param isValueTemporary true jika nilai yg diperoleh dari hasil request dihapus setelah
     *   nilai sudah sudah sampai ke view. Penghapusan nilai atau perubahan nilai menjadi
     *   null tidak menyebabkan fungsi [onObserve] dipanggil.
     *   [isValueTemporary] berguna jika saat terjadi screen rotation sehingga hasil request
     *   yg seharusnya ditampilkan sekali tidak tampil untuk kedua kalinya.
     *
     * @param onPreLoad adalah fungsi yg akan dipanggil segera setelah request dikirim namun hasil
     *   belum didapat sehingga fungsi [onObserve] belum bisa dipanggil.
     *
     * @param onObserve adalah fungsi yg dipanggil setiap kali terjadi perubahan nilai pada nilai
     *   di dalam [ObservableViewModel] ini. [onObserve] juga dipanggil setelah mendapat hasil nilai
     *   dari request yang dikirim.
     */
    fun <T> observe(owner: LifecycleOwner,
                    reqCode: String, vararg params: Pair<String, Any>,
                    isValueTemporary: Boolean= false,
                    loadLater: Boolean= false, forceReload: Boolean= false,
                    onPreLoad: (() -> Unit)?= null, //fungsi saat LifeData msh loading
                    onObserve: (T) -> Unit): LifeData<T>?

    /**
     * Untuk mereload data dari repo. Fungsi ini berguna bagi data yg sudah ada di {@link #mData}.
     * Fungsi ini juga digunakan scr internal untuk melakukan [sendRequest] yg didahului dg [LifeData.onPreLoad]
     * Pemanggilan fungsi ini harus didahului dg fungsi [observe] agar dapat berjalan dg lancar.
     *
     * @param params dapat dikosongi jika sebelumnya pada fungsi [observe] sudah menge-pass params.
     *   Jika belum menge-pass params pada fungsi [observe], maka params yg dikirim adalah yg kosongan.
     *
     * @return true jika data sudah ada di {@link #mData}.
     *   false jika data belum ada di {@link #mData} sehingga
     *   tidak dilakukan sendRequest(reqCode, *params) ke repo.
     */
    fun reload(reqCode: String, vararg params: Pair<String, Any>): Boolean
}