package sidev.lib.android.siframe.arch.view

import android.view.View
import sidev.lib.android.siframe.tool.ViewContentExtractor

interface AutoRestoreViewClient{
    /**
     * Fungsi yg digunakan untuk mendaftarkan sebuah view [v] dg [id] untuk disimpan
     * kontentnya sesaat sebelum Activity dihancurkan.
     * Jika fungsi ini dipanggil saat [id] sudah terdaftar dan memiliki data yg disimpan,
     * maka data yg tersimpan ditampilkan pada view [v].
     */
    fun registerAutoRestoreView(id: String, v: View)

    /**
     * Digunakan untuk mengesktrak semua view yg telah disimpan di dalam [viewContentGetter].
     * Fungsi ini harus dipanggil sesaat sebelum activity dihancurkan karena fungsi ini
     * akan memanggil [ViewContentExtractor.clearAllSavedViews].
     */
    fun extractAllViewContent() //isOnScreenRotation: Boolean
}