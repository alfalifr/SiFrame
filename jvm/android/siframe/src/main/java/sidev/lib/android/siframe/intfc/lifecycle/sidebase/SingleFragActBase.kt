package sidev.lib.android.siframe.intfc.lifecycle.sidebase

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.fragment.app.Fragment
import sidev.lib.android.siframe._customizable._Config
import sidev.lib.android.siframe.exception.PropertyAccessExc
import sidev.lib.android.siframe.intfc.lifecycle.rootbase.ActFragBase
import sidev.lib.android.siframe.intfc.lifecycle.rootbase.FragBase
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

    override val _prop_view: View
    override val _prop_intent: Intent
    override val _prop_ctx: Context

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
//        isFragLate= _sideBase_intent.getExtra(_SIF_Constant.EXTRA_TYPE_LATE, default = isFragLate)!! //(_SIF_Constant.EXTRA_TYPE_LATE, default = isFragLate) //getIntentData(_SIF_Constant.EXTRA_TYPE_LATE, default = isFragLate)
        __initFrag()
        giveFrag()
/*
        <12 Juli 2020> => Sementara dikomen karena dirasa belum kepake.
        /**
         * Karena jika fragment langsung dipasang ke container, maka data pada fragment gak bisa direload.
         * Jadi inisiasi fragment harus setelah data didownload.
         */
        isDataAsync= _prop_intent.getExtra(_SIF_Constant.EXTRA_DATA_ASYNC, default = isDataAsync)!! //getIntentData(_SIF_Constant.EXTRA_DATA_ASYNC, default = isDataAsync)
        if(!isDataAsync)
            __attachFrag()
 */
    }

    /*  override fun onStart() {
          super.onStart()

      }
  */

    /**
     * Digunakan saat Fragment lateinit. Biasanya saat pemanggilan pada fungsi [startSingleFragAct]
     */
    fun __initFrag(){
        _prop_intent.getExtra<String>(_SIF_Constant.FRAGMENT_NAME)
            .notNull { fragName ->
                fragment= ReflexUtil.newInstance(fragName)
            }

        try{
            __attachFrag()
        } catch (e: UninitializedPropertyAccessException){
            throw PropertyAccessExc(
                kind = PropertyAccessExc.Kind.Uninitialized,
                propertyName = "fragment",
                ownerName = this::class.java.simpleName
            )
        }
    }

    fun __attachFrag(){
        _prop_ctx.commitFrag(fragContainerId, fragment)
        if(this is ActFragBase)
            fragment.asNotNull { frag: FragBase -> frag.onLifecycleAttach(this) }
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
        try{
            func(fragment)
        } catch (e: Exception){
            getStatic<ArrayList<(Fragment) -> Unit>>(_SIF_Constant.STATIC_SINGLE_FRAG_LISTENER)
                .notNull { listener -> listener.add(func) }
                .isNull {
                    val listener= ArrayList<(Fragment) -> Unit>()
                    listener.add(func)
                    setStatic(_SIF_Constant.STATIC_SINGLE_FRAG_LISTENER, listener)
                }
        }
    }
    private fun giveFrag(){
        getStatic<ArrayList<(Fragment) -> Unit>>(_SIF_Constant.STATIC_SINGLE_FRAG_LISTENER)
            .notNull { listener ->
                listener.forEach { func ->
                    func(fragment)
                }
                removeStatic(_SIF_Constant.STATIC_SINGLE_FRAG_LISTENER)
            }
    }
}