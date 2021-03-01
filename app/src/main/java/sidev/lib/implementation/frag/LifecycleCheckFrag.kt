package sidev.lib.implementation.frag

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.page_lifecycle_check.*
import sidev.lib.android.siframe.lifecycle.fragment.Frag
import sidev.lib.android.siframe.tool.util.`fun`.startSingleFragAct_config
import sidev.lib.android.std.tool.util.`fun`.get
import sidev.lib.android.std.tool.util.`fun`.loge
import sidev.lib.check.notNull
import sidev.lib.implementation.R

class LifecycleCheckFrag: Frag() {
    override val layoutId: Int = R.layout.page_lifecycle_check

    private var order: Int= -1

    override fun _initDataFromIntent(intent: Intent) {
        super._initDataFromIntent(intent)
        order= intent["_order_"] ?: 0
    }

    override fun _initView(layoutView: View) {
        layoutView.apply {
            tv.text= "Frag ke $order"
            btn.apply {
                text= "Ke frag ${order+1}"
                setOnClickListener {
                    startSingleFragAct_config<LifecycleCheckFrag>("_order_" to order+1)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loge("LifecycleCheckFrag ======== $order onCreate")
    }

    override fun onStart() {
        super.onStart()
        loge("LifecycleCheckFrag ======== $order onStart")
    }

    override fun onResume() {
        super.onResume()
        loge("LifecycleCheckFrag ======== $order onResume")
    }

    override fun onPause() {
        super.onPause()
        loge("LifecycleCheckFrag ======== $order onPause")
    }

    /**
     * Called when the Fragment is no longer started.  This is generally
     * tied to [Activity.onStop] of the containing
     * Activity's lifecycle.
     */
    override fun onStop() {
        super.onStop()
        loge("LifecycleCheckFrag ======== $order onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        loge("LifecycleCheckFrag ======== $order onDestroy")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        loge("LifecycleCheckFrag ======== $order onAttach")
    }

    override fun onDetach() {
        super.onDetach()
        loge("LifecycleCheckFrag ======== $order onDetach")
    }
}