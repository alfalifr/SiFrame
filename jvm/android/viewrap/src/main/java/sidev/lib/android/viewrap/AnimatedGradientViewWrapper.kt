package sidev.lib.android.viewrap

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.view.View
import kotlinx.android.synthetic.main.buffer_anim_gradient_rotation.view.*
import sidev.lib.android.std.tool.util.`fun`.inflate


/** Kelas Implementasi dari [ViewBufferWrapperImpl] yg [bufferView]-nya adalah layout dg id R.layout.buffer_anim_gradient_rotation. */
open class AnimatedGradientViewWrapper(context: Context)
    : ViewBufferWrapperImpl<View>(context){
    final override val bufferView: View = context.inflate(R.layout.buffer_anim_gradient_rotation)!!

    init{
        val bgAnim= bufferView.ll.background as AnimationDrawable
        bgAnim.setEnterFadeDuration(800)
        bgAnim.setExitFadeDuration(800)
        bgAnim.start()
    }
}
