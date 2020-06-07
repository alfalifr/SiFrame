package sidev.lib.android.siframe.lifecycle.activity

import android.util.Log
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.Fragment
import sidev.lib.android.siframe.customizable._init._ConfigBase
import sidev.lib.android.siframe.tool.`var`._SIF_Constant
import sidev.lib.android.siframe.tool.util.`fun`.commitFrag

//import sidev.kuliah.agradia.R

abstract class SingleFragAct_BarContentNav : SimpleAbsBarContentNavAct() {
    abstract var fragment: Fragment

    override val contentLayoutId: Int
        get() = _ConfigBase.LAYOUT_ACT_SINGLE_FRAG //R.layout._t_act_single_frag
    override val isContentLayoutInflatedFirst: Boolean
        get() = true
    open val fragContainerId= _ConfigBase.ID_FR_CONTAINER //R.id.fr_container

    open protected var isFragLate= false
    open protected var isDataAsync= false
/*
    override val layoutId: Int
        get() = R_layout._t_act_single_frag
*/

    override fun initView_int(layoutView: View) {
        super.initView_int(layoutView)
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setDisplayShowCustomEnabled(true)

        isFragLate= getIntentData(_SIF_Constant.EXTRA_TYPE_LATE, default = isFragLate)
        initFrag()

        /**
         * Karena jika fragment langsung dipasang ke container, maka data pada fragment gak bisa direload.
         * Jadi inisiasi fragment harus setelah data didownload.
         */
        isDataAsync= getIntentData(_SIF_Constant.EXTRA_DATA_ASYNC, default = isDataAsync)
        if(!isDataAsync)
            attachFrag()
    }

  /*  override fun onStart() {
        super.onStart()

    }
*/
    protected fun initFrag(){
        if(isFragLate){
            val fragName= getIntentData<String?>(_SIF_Constant.FRAGMENT_NAME)!!
            fragment= Class.forName(fragName).newInstance() as Fragment
            Log.e("SingleFragAct_BarContentNav", "fragName= $fragName, NEW!!!")
        }
    }

    protected fun attachFrag(){
        commitFrag(fragContainerId, fragment)
    }
}
