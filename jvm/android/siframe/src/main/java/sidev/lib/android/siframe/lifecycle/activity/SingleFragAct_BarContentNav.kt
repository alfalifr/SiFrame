package sidev.lib.android.siframe.lifecycle.activity

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.SingleFragActBase

//import sidev.kuliah.agradia.R

abstract class SingleFragAct_BarContentNav: SimpleAbsBarContentNavAct(), SingleFragActBase{
    override val layoutId: Int
        get() = super<SimpleAbsBarContentNavAct>.layoutId
    override val contentLayoutId: Int
        get() = super<SingleFragActBase>.layoutId

    override val _sideBase_act: AppCompatActivity
        get() = this
    override val _sideBase_view: View
        get() = contentViewContainer
    override val _sideBase_intent: Intent
        get() = intent
    override val _sideBase_ctx: Context
        get() = this
    override val _sideBase_fm: FragmentManager
        get() = supportFragmentManager

//    override lateinit var fragment: Fragment
//    override var isFragLate: Boolean= false
    override var isDataAsync: Boolean= false

    override fun ___initSideBase() {
        super<SingleFragActBase>.___initSideBase()
    }
}

/*
abstract class SingleFragAct_BarContentNav : SimpleAbsBarContentNavAct(){
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
 */