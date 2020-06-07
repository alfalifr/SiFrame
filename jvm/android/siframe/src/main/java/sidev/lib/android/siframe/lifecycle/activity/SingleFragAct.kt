package sidev.lib.android.siframe.lifecycle.activity

import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.Fragment
//import com.sigudang.android.R
//ximport com.sigudang.android._template.util.ReceiverFun.commitFragment
//import sidev.kuliah.agradia.R
import sidev.lib.android.siframe.customizable._init._ConfigBase
import sidev.lib.android.siframe.tool.`var`._SIF_Constant
import sidev.lib.android.siframe.tool.util.`fun`.commitFrag

/**
 * Kelas dasar dalam framework yang digunakan sbg Activity yang berisi satu fragment
 */
abstract class SingleFragAct : SimpleAbsAct() {
    abstract var fragment: Fragment
    override val layoutId: Int
        get() = _ConfigBase.LAYOUT_ACT_SINGLE_FRAG
    open val fragContainerId= _ConfigBase.ID_FR_CONTAINER
/*
    companion object{
        const val EXTRA_CLASS_NAME= "class_name"
        const val EXTRA_TYPE_LATE= "type_late"
        const val EXTRA_DATA_ASYNC= "data_async"
    }
 */
    open protected var isFragLate= false
    open protected var isDataAsync= false

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
    private fun initFrag(){
        if(isFragLate) {
            val fragTrans= supportFragmentManager.beginTransaction()
            fragTrans.replace(fragContainerId, fragment)
            fragTrans.commit()
        }
    }

    protected fun attachFrag(){
        commitFrag(fragContainerId, fragment)
    }
}
