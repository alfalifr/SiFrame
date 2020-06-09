package sidev.lib.android.siframe.lifecycle.activity

import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class SingleFragAct_BarContentNav_Simple: SingleFragAct_BarContentNav(){
    override lateinit var fragment: Fragment

    override fun _initActBar(actBarView: View) {}
    override fun _initNavBar(navBarView: BottomNavigationView) {}
    override fun _initView(layoutView: View) {}
}

/*
//import sidev.kuliah.agradia.R

class SingleFragAct_BarContentNav_Simple : SingleFragAct_BarContentNav() {
    override lateinit var fragment: Fragment

    override fun initActBar(actBarView: View) {}
    override fun initNavBar(navBarView: BottomNavigationView) {}
    override fun initView(layoutView: View) {}

    override val contentLayoutId: Int
        get() = _ConfigBase.LAYOUT_ACT_SINGLE_FRAG //R.layout._t_act_single_frag
    override val isContentLayoutInflatedFirst: Boolean
        get() = true
    override val fragContainerId= _ConfigBase.ID_FR_CONTAINER //R.id.fr_container

    override var isFragLate= false
    /*
    override val layoutId: Int
        get() = R_layout._t_act_single_frag
 */
/*
    override fun initView_int(layoutView: View) {
        super.initView_int(layoutView)
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setDisplayShowCustomEnabled(true)

        isFragLate= getIntentData(EXTRA_TYPE_LATE, default = isFragLate)
        if(isFragLate){
            val fragName= getIntentData<String?>(EXTRA_CLASS_NAME)
            fragment= Class.forName(fragName).newInstance() as Fragment
        }

        attachFrag()
    }
 */

  /*  override fun onStart() {
        super.onStart()

    }
*/
/*
    protected fun attachFrag(){
      commitFragment(fragContainerId, fragment)
/*
        val fragTrans= supportFragmentManager.beginTransaction()
        fragTrans.replace(R.id.fr_container, fragment)
        fragTrans.commit()
 */
    }

 */
}
 */