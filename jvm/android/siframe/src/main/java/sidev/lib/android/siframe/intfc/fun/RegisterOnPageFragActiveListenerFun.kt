package sidev.lib.android.siframe.intfc.`fun`

import android.view.View
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.ViewPagerBase
import sidev.lib.android.siframe.lifecycle.fragment.Frag

/**
 * Untuk melakukan interaksi antara Fragment dalam ViewPager terhadap Activity
 * saat halaman Fragment aktif.
 */
interface RegisterOnPageFragActiveListenerFun {
    fun registerOnPageFragActiveListener(vpAct: ViewPagerBase<Frag>, frag: Frag, func: (vParent: View, pos: Int) -> Unit){
        vpAct.registerOnPageFragToActListener(frag, func)
    }
}