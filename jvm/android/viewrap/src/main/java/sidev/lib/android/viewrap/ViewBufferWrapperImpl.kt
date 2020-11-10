package sidev.lib.android.viewrap

import android.content.Context
import android.view.View
import android.view.animation.Animation
import sidev.lib.android.std.tool.util.`fun`.size
import sidev.lib.collection.isElementZero


abstract class ViewBufferWrapperImpl<V: View>(final override val context: Context): ViewBufferWrapper<V>{
    override var view: V?= null
    override var isBufferShown: Boolean= false
        protected set

    /*
            set(v){
                field= v
                configureBufferView()
            }
     */
    final override var bufferAnimation: Animation? = null
        set(v){
            field= v
            bufferView?.animation= v
        }
    private var mIsBufferViewConfigured: Boolean= false

    /*
    override fun showBuffer(show: Boolean, keepView: Boolean){
        if(show && bufferView != null){
//            if(!mIsBufferViewConfigured)
//                configureBufferView()

            if(mIsBufferViewConfigured)
                view?.indexInParent.notNull {
                    if(!it.isNegative()) {
                        if(!keepView) detachViewFromParent()?.addView(bufferView, it)
                        else (view!!.parent as ViewGroup).addView(bufferView, it +1)
                    }
                }
            else view?.addOnGlobalLayoutListener { v ->
                bufferView!!.layoutParams= v.layoutParams
//                bufferView!!.LayoutParamSize= v.LayoutParamSize
                loge("Masuk addOnGlobalLayoutListener v.size.string= ${v.LayoutParamSize.string}")
                mIsBufferViewConfigured= true
                view?.indexInParent.notNull {
                    if(!it.isNegative()) {
                        if(!keepView) detachViewFromParent()?.addView(bufferView, it)
                        else (view!!.parent as ViewGroup).addView(bufferView, it +1)
                    }
                }
            }
        } else bufferView?.indexInParent?.notNull { index ->
            if(!index.isNegative())
                bufferView?.detachFromParent().notNull {
                    if(view!!.indexInParent.isNegative()) it.forcedAddView(view!!, index)
                }
        }
    }
 */
    override fun showBuffer(show: Boolean, keepView: Boolean) {
        isBufferShown= show
        super.showBuffer(show, keepView)
    }

    private fun configureBufferView(){
        if((view?.size?.isElementZero() == false).also { mIsBufferViewConfigured= it }){
            bufferView?.layoutParams= view!!.layoutParams
            bufferView?.size= view!!.size
//            loge("bufferView?.size= ${bufferView?.size?.string} view?.size?.string= ${view?.size?.string} bufferView?.layoutParams.width= ${bufferView?.layoutParams?.width} bufferView?.layoutParams?.height= ${bufferView?.layoutParams?.height} view!!.size.isElementEmpty()= ${view!!.size.isElementEmpty()}")
        }
    }
}