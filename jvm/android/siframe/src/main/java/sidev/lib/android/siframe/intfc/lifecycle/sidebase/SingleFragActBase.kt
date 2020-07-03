package sidev.lib.android.siframe.intfc.lifecycle.sidebase

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import sidev.lib.android.siframe.customizable._init._Config
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.base.ComplexLifecycleSideBase
import sidev.lib.android.siframe.lifecycle.activity.BarContentNavAct
import sidev.lib.android.siframe.lifecycle.fragment.Frag
import sidev.lib.android.siframe.tool.`var`._SIF_Constant
import sidev.lib.android.siframe.tool.util.`fun`.*
import sidev.lib.universal.`fun`.asNotNull
import sidev.lib.universal.`fun`.isNull
import sidev.lib.universal.`fun`.notNull
import sidev.lib.universal.tool.util.ReflexUtil

interface SingleFragActBase: ComplexLifecycleSideBase{
    override val layoutId: Int
        get() = _Config.LAYOUT_ACT_SINGLE_FRAG //.LAYOUT_ACT_SIMPLE

    override val _sideBase_view: View
    override val _sideBase_intent: Intent
    override val _sideBase_ctx: Context

    var fragment: Fragment
    val fragContainerId: Int
        get()= _Config.ID_FR_CONTAINER //.ID_VG_CONTENT_CONTAINER
//    val supportFm: FragmentManager
    /*
        companion object{
            const val EXTRA_CLASS_NAME= "class_name"
            const val EXTRA_TYPE_LATE= "type_late"
            const val EXTRA_DATA_ASYNC= "data_async"
        }
     */
//    var isFragLate: Boolean //= false <10 Juni 2020> => DIhilangkan karena jika intent nge-pass _SIF_Constant.FRAGMENT_NAME, maka otomatis isFragLate= true
    var isDataAsync: Boolean //= false
    var isTitleFragBased: Boolean


    override fun ___initSideBase() {
        Log.e("SingleFragActBase", "___initSideBase")
//        isFragLate= _sideBase_intent.getExtra(_SIF_Constant.EXTRA_TYPE_LATE, default = isFragLate)!! //(_SIF_Constant.EXTRA_TYPE_LATE, default = isFragLate) //getIntentData(_SIF_Constant.EXTRA_TYPE_LATE, default = isFragLate)
        __initFrag()
        giveFrag()

        /**
         * Karena jika fragment langsung dipasang ke container, maka data pada fragment gak bisa direload.
         * Jadi inisiasi fragment harus setelah data didownload.
         */
        isDataAsync= _sideBase_intent.getExtra(_SIF_Constant.EXTRA_DATA_ASYNC, default = isDataAsync)!! //getIntentData(_SIF_Constant.EXTRA_DATA_ASYNC, default = isDataAsync)
        if(!isDataAsync)
            __attachFrag()
    }

    /*  override fun onStart() {
          super.onStart()

      }
  */

    /**
     * Digunakan saat Fragment lateinit. Biasanya saat pemanggilan pada fungsi startSingleFragAct()
     */
    fun __initFrag(){
//        if(isFragLate) {
            _sideBase_intent.getExtra<String>(_SIF_Constant.FRAGMENT_NAME)
                .notNull { fragName ->
                    fragment= ReflexUtil.newInstance(fragName)
                    _sideBase_view.findViewById<View>(fragContainerId)!!
                    Log.e("SingleFragActBase", "__initFrag MULAI")
                    __attachFrag()
                    Log.e("SingleFragActBase", "__initFrag AKHIR")
                }
/*
            val fragTrans= _sideBase_fm.beginTransaction()
            fragTrans.replace(fragContainerId, fragment)
            fragTrans.commit()
 */
//        }
    }

    fun __attachFrag(){
        _sideBase_ctx.commitFrag(fragContainerId, fragment)
        if(isTitleFragBased)
            this.asNotNull { act: BarContentNavAct ->
                fragment.asNotNull { frag: Frag ->
                    try{ act.setActBarTitle(frag.fragTitle) }
                    catch (e: Exception){
                        /* Ini ditujukan agar saat terjadi kesalahan saat setActBarTitle()
                        tidak menyebabkan error.
                         */
                    }
                }
            }
    }

    fun waitForFrag(func: (Fragment) -> Unit){
        Log.e("SingleFragActBase", "waitForFrag")
        try{
            func(fragment)
            Log.e("SingleFragActBase", "waitForFrag TRY BERHASIL")
        } catch (e: Exception){
            getStatic<ArrayList<(Fragment) -> Unit>>(_SIF_Constant.STATIC_SINGLE_FRAG_LISTENER)
                .notNull { listener -> listener.add(func) }
                .isNull {
                    val listener= ArrayList<(Fragment) -> Unit>()
                    listener.add(func)
                    setStatic(_SIF_Constant.STATIC_SINGLE_FRAG_LISTENER, listener)
                }
            Log.e("SingleFragActBase", "waitForFrag CATCH")
        }
    }
    private fun giveFrag(){
        Log.e("SingleFragActBase", "giveFrag")
        getStatic<ArrayList<(Fragment) -> Unit>>(_SIF_Constant.STATIC_SINGLE_FRAG_LISTENER)
            .notNull { listener ->
                Log.e("SingleFragActBase", "giveFrag listener NOT NULL")
                listener.forEach { func ->
                    func(fragment)
                }
                removeStatic(_SIF_Constant.STATIC_SINGLE_FRAG_LISTENER)
            }
    }
}