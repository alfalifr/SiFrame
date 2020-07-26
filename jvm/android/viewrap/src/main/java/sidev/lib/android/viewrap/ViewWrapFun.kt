package sidev.lib.android.viewrap

import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import sidev.lib.android.siframe.tool.util.`fun`.childrenTree
import sidev.lib.android.siframe.tool.util.`fun`.loge
import sidev.lib.android.siframe.tool.util.idName
import sidev.lib.universal.`fun`.asNotNullTo


//fun <V: View, T: ViewBufferWrapper<V>> View.wrapWithBuffer(): T {}
/** Membungkus `this.extension` [View] dg [AnimatedGradientViewWrapper]. */
fun View.wrapWithBuffer(): AnimatedGradientViewWrapper = AnimatedGradientViewWrapper(context).apply { view= this@wrapWithBuffer }
/*
/**
 * Membungkus setiap immediate child view dalam `this.extension` dg [AnimatedGradientViewWrapper].
 *
 * @return List yg berisi [AnimatedGradientViewWrapper] dari tiap immediate child view,
 *   kosong jika `this.extension` tidak punya child atau bkn merupakan [ViewGroup].
 * */
fun View.wrapImmeadiateChildWithBuffer(): List<AnimatedGradientViewWrapper>{
    val bufferList= ArrayList<AnimatedGradientViewWrapper>()
    if(this is ViewGroup)
        for(child in children)
            bufferList += AnimatedGradientViewWrapper(context).apply { view= child }
    return bufferList
}
 */

/**
 * Membungkus setiap child view dalam `this.extension` dg [AnimatedGradientViewWrapper].
 * Child yg dibungkus dapat nested maupun tidak tergantung dari [includeNested].
 * Jika [predicate] null, maka scr default child yg dibungkus adalah yg bkn merupakan [ViewGroup]
 * atau [ViewGroup] yg tidak punya child view.
 *
 * @return List yg berisi [AnimatedGradientViewWrapper] dari tiap child view,
 *   kosong jika `this.extension` tidak punya child atau bkn merupakan [ViewGroup].
 * */
fun View.wrapChildWithBuffer(includeNested: Boolean= true, predicate: ((View) -> Boolean)?= null): List<AnimatedGradientViewWrapper>{
    val bufferList= ArrayList<AnimatedGradientViewWrapper>()
    if(this is ViewGroup){
        val childSeq= if(includeNested) childrenTree else children
        if(predicate == null){
            for(child in childSeq){
                if(child !is ViewGroup || child.childCount == 0)
                    bufferList += AnimatedGradientViewWrapper(context).apply { view= child }
            }
        } else{
            for(child in childSeq)
                if(predicate(child))
                    bufferList += AnimatedGradientViewWrapper(context).apply { view= child }
        }
    }
    return bufferList
}
