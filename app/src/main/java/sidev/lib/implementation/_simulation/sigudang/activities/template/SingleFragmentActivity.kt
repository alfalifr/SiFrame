package sidev.lib.implementation._simulation.sigudang.activities.template
// /*
import android.view.View
import sidev.lib.android.siframe.lifecycle.activity.SingleFragDrawerAct_BarContentNav_Simple
import sidev.lib.android.siframe.lifecycle.fragment.Frag
import sidev.lib.check.asNotNull

open class SingleFragmentActivity : SingleFragDrawerAct_BarContentNav_Simple(){
    override fun _initActBar(actBarView: View) {
        super._initActBar(actBarView)
        waitForFrag {
            it.asNotNull { frag: Frag ->
                setActBarTitle(frag.fragTitle)
            }
        }
    }
}

// */