package sidev.lib.android.siframe.intfc.lifecycle.sidebase

import androidx.fragment.app.Fragment
import sidev.lib.android.siframe.customizable._init._ConfigBase
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.base.ComplexLifecycleSideBase
import sidev.lib.android.siframe.tool.`var`._SIF_Constant
import sidev.lib.android.siframe.tool.util.`fun`.commitFrag
import sidev.lib.android.siframe.tool.util.`fun`.getExtra

interface SingleFragActBase: ComplexLifecycleSideBase{
    override val layoutId: Int
        get() = _ConfigBase.LAYOUT_ACT_SINGLE_FRAG

    var fragment: Fragment
    val fragContainerId: Int
        get()= _ConfigBase.ID_FR_CONTAINER
//    val supportFm: FragmentManager
    /*
        companion object{
            const val EXTRA_CLASS_NAME= "class_name"
            const val EXTRA_TYPE_LATE= "type_late"
            const val EXTRA_DATA_ASYNC= "data_async"
        }
     */
    var isFragLate: Boolean //= false
    var isDataAsync: Boolean //= false


    override fun _initInheritorBase() {
        isFragLate= _sideBase_intent.getExtra(_SIF_Constant.EXTRA_TYPE_LATE, default = isFragLate)!! //(_SIF_Constant.EXTRA_TYPE_LATE, default = isFragLate) //getIntentData(_SIF_Constant.EXTRA_TYPE_LATE, default = isFragLate)
        initFrag()

        /**
         * Karena jika fragment langsung dipasang ke container, maka data pada fragment gak bisa direload.
         * Jadi inisiasi fragment harus setelah data didownload.
         */
        isDataAsync= _sideBase_intent.getExtra(_SIF_Constant.EXTRA_DATA_ASYNC, default = isDataAsync)!! //getIntentData(_SIF_Constant.EXTRA_DATA_ASYNC, default = isDataAsync)
        if(!isDataAsync)
            attachFrag()
    }

    /*  override fun onStart() {
          super.onStart()

      }
  */

    fun initFrag(){
        if(isFragLate) {
            val fragTrans= _sideBase_fm.beginTransaction()
            fragTrans.replace(fragContainerId, fragment)
            fragTrans.commit()
        }
    }

    fun attachFrag(){
        _sideBase_ctx.commitFrag(fragContainerId, fragment)
    }
}