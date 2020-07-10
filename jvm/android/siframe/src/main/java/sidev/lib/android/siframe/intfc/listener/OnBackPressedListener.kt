package sidev.lib.android.siframe.intfc.listener

import sidev.lib.android.siframe.intfc.prop.TagProp

interface OnBackPressedListener: TagProp {
    /**
     * Digunakan saat programmer ingin menghilangkan listener dari daftar yg sebelumnya ditambahkan
     * menggunakan lambda. Hal tersebut dikarenakan programmer tidak dapat menghilangi listener
     * dari daftar menggunakan lambda karena lambda telah dibungkus object lain.
     */
    override val tag: String?
    fun onBackPressed_(): Boolean
}