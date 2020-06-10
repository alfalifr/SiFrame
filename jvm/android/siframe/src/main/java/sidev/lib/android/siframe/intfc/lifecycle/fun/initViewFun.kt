package sidev.lib.android.siframe.intfc.lifecycle.`fun`

import android.view.View

interface initViewFun{
    /**
     * Fungsi yg dipanggil secara internal.
     * @param layoutView kemungkinan besar adalah rootView, namun tidak harus.
     */
    fun __initView(layoutView: View)

    /**
     * @param layoutView adalah contentView pada Act yg punya bagian view dasar.
     *      Merupakan rootView pada Act yg simpel.
     */
    fun _initView(layoutView: View)
}