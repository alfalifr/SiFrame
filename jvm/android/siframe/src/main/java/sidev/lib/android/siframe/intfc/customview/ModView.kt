package sidev.lib.android.siframe.intfc.customview

//import sidev.lib.android.siframe.customizable.view.intfc.CustomView

/**
 * Interface yg digunakan untuk view yang bagian dalamnya dapat diubah dari luar.
 */
interface ModView : CustomView {
    var isTouchable: Boolean
    var isTouchInterceptable: Boolean
}