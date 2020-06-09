package sidev.lib.android.siframe.lifecycle.activity

import android.view.View
import androidx.fragment.app.Fragment


class SingleFragAct_Simple: SingleFragAct(){
    override lateinit var fragment: Fragment
    override fun _initView(layoutView: View) {}
}

/*
/**
 * Implementasi sederhana dari SingleFragAct
 */
class SingleFragAct_Simple : SingleFragAct() {
    override lateinit var fragment: Fragment
    override fun initView(layoutView: View) {}

    override val layoutId: Int
        get() = _ConfigBase.LAYOUT_ACT_SINGLE_FRAG //R_layout._t_act_single_frag
/*
    companion object{
        const val EXTRA_CLASS_NAME= "class_name"
        const val EXTRA_TYPE_LATE= "type_late"
    }*/
    override var isFragLate= false

    override fun initView_int(layoutView: View) {
        super.initView_int(layoutView)
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setDisplayShowCustomEnabled(true)

        isFragLate= getIntentData(_SIF_Constant.EXTRA_TYPE_LATE, default = isFragLate)
        if(isFragLate){
            val fragName= getIntentData<String?>(_SIF_Constant.FRAGMENT_NAME)!!
            fragment= Class.forName(fragName).newInstance() as Fragment
        }

        initFrag()
    }

    /*  override fun onStart() {
          super.onStart()

      }
  */
    private fun initFrag(){
        val fragTrans= supportFragmentManager.beginTransaction()
        fragTrans.replace(_ConfigBase.ID_FR_CONTAINER, fragment)
        fragTrans.commit()
    }
}
 */