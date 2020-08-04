package sidev.lib.android.siframe.intfc.lifecycle.rootbase

import android.view.View
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import sidev.lib.android.siframe.intfc.lifecycle.LifecycleViewBase
import sidev.lib.android.siframe.intfc.prop.HierarchyOrderProp
import sidev.lib.android.siframe.intfc.prop.ParentLifecycleProp

interface FragBase: ActFragBase, ParentLifecycleProp{
    /**
     * Tempat turunan [FragBase] ini menempel.
     * Dapat berupa Activity maupun Fragment turunan [ActFragBase].
     */
//    val parentLifecycle: SimpleActFragBase?
    override val _prop_parentLifecycle: ActFragBase?

    /**
     * Dipanggil saat fragment terlihat di screen.
     * Untuk case ViewPager, fungsi ini dipanggil setiap kali penggunamembuka halaman fragment ini.
     *
     * @param parentView merupakan view tempat fragment ini menempel.
     *   Null jika ternyata yg memanggil fungsi [onActive] ini bkn merupakan komponen view.
     * @param callingLifecycle merupakan turunan [LifecycleViewBase] tempat [ActFragBase] ini menempel.
     *   Null jika yg memanggil fungsi [onActive] ini bkn merupakan [LifecycleViewBase].
     */
    @CallSuper
    fun onActive(parentView: View?, callingLifecycle: LifecycleViewBase?, pos: Int){}

    /**
     * Fungsi yg dipanggil saat fragment turunan [FragBase] ini menempel di [ActFragBase].
     * Fungsi ini dipanggil saat [FragmentActivity.onAttachFragment] atau [Fragment.onAttachFragment].
     */
    @CallSuper
    fun onLifecycleAttach(callingLifecycle: ActFragBase){}
    /**
     * Fungsi yg dipanggil saat fragment turunan [FragBase] ini dilepaskan dari [ActFragBase].
     * Fungsi ini dipanggil saat [Fragment.onDetach].
     */
    @CallSuper
    fun onLifecycleDetach(){}
}