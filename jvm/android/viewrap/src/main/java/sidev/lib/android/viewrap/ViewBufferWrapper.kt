package sidev.lib.android.viewrap

import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import sidev.lib.android.std.tool.util.`fun`.addOnGlobalLayoutListener
import sidev.lib.android.std.tool.util.`fun`.detachFromParent
import sidev.lib.android.std.tool.util.`fun`.forcedAddView
import sidev.lib.android.std.tool.util.`fun`.indexInParent
import sidev.lib.android.std.tool.util.`fun`.loge
import sidev.lib.android.std.tool.util.`fun`.makeStackable
import sidev.lib.check.notNull
import sidev.lib.number.isNegative
import sidev.lib.number.isNotNegative


/**
 * Interface yg digunakan untuk memanajemen sebuah [view] yg dapat ditimpa dg sebuah [bufferView].
 * Interface ini berguna saat suatu [view] msh buffer dan tidak ingin mengeksposnya scr langsung,
 * namun menggunakan [bufferView].
 */
interface ViewBufferWrapper<V: View>: ViewWrapper<V>{
    val bufferView: View?
    val bufferAnimation: Animation?
    val isBufferShown: Boolean

    /*
        fun showAnimation(show: Boolean= true){
            if(show && bufferView != null){
                if(view !is ViewGroup)
                    detachViewFromParent()?.addView(bufferView)
                else if(view != null)
                    view!!.asNotNull { vg: ViewGroup -> vg.addView(bufferView) }
            } else if(view != null){
                bufferView?.detachFromParent().notNull {
                    if(view !is ViewGroup)
                        it.addView(view)
                }
            }
        }
     */
    fun showBuffer(show: Boolean= true, keepView: Boolean= false){
        if(show && bufferView != null){
            view?.addOnGlobalLayoutListener { v ->
                if(!isBufferShown) return@addOnGlobalLayoutListener
                    //Karena addOnGlobalLayoutListener terjadi nti setelah semua kode di sini dijalankan.

                bufferView!!.layoutParams= v.layoutParams
                if(v.layoutParams.width.isNegative())
                    bufferView!!.layoutParams.width= v.width
                if(v.layoutParams.height.isNegative())
                    bufferView!!.layoutParams.height= v.height

                v.makeStackable()
                val viewIndex= v.indexInParent
                loge("bufferView.parent= ${bufferView!!.parent} viewIndex= $viewIndex")
                if(viewIndex.isNotNegative()){
                    if(!keepView) detachViewFromParent()?.addView(bufferView, viewIndex)
                    else (v.parent as ViewGroup).addView(bufferView, viewIndex +1)
                }
            }
/*
            view?.indexInParent.notNull {
                if(!it.isNegative()) {
                    if(!keepView) detachViewFromParent()?.addView(bufferView, it)
                    else (view!!.parent as ViewGroup).addView(bufferView, it +1)
                }
            }
 */
        } else if(view != null){
            bufferView?.indexInParent?.notNull { index ->
                if(!index.isNegative())
                    bufferView?.detachFromParent().notNull {
                        if(view!!.indexInParent.isNegative()) it.forcedAddView(view!!, index)
                    }
            }
        }
    }
}