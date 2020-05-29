package sidev.lib.android.siframe.intfc.`fun`

import android.view.View
import sidev.lib.android.siframe.intfc.view.ViewPagerActView
import sidev.lib.android.siframe.lifecycle.fragment.SimpleAbsFrag

/**
 * Untuk melakukan interaksi antara Fragment dalam ViewPager terhadap Activity
 * saat halaman Fragment aktif.
 */
interface RegisterOnPageFragActiveListenerFun {
    fun registerOnPageFragActiveListener(act: ViewPagerActView<SimpleAbsFrag>, frag: SimpleAbsFrag, func: (vParent: View, pos: Int) -> Unit){
        act.registerOnPageFragToActListener(frag, func)
    }
}