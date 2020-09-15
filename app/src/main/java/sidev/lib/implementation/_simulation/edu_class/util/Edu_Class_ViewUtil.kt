package sidev.lib.implementation._simulation.edu_class.util

import android.view.View
import android.widget.ImageView
import sidev.lib.android.siframe.model.PictModel
import sidev.lib.android.siframe.tool.util._ResUtil
import sidev.lib.android.siframe.tool.util._ViewUtil
import sidev.lib.android.siframe.tool.util._ViewUtil.Comp.enableFillTxt
import sidev.lib.android.siframe.tool.util._ViewUtil.Comp.getEt
import sidev.lib.android.siframe.tool.util.`fun`.loge
import sidev.lib.check.notNull
import sidev.lib.implementation.R

object Edu_Class_ViewUtil {
    fun setImg(iv: ImageView, img: PictModel?){
        if(img == null) return
        if(img.bm != null)
            iv.setImageBitmap(img.bm)
        else if(img.dir != null)
            _ViewUtil.loadImageToIv(iv, img.dir)
    }

    object Comp{
        fun enableEd(compView: View, enable: Boolean = true){
            enableFillTxt(compView, enable)
            getEt?.invoke(compView).notNull { et ->
                loge("_simul_edu_ enableEd() enable= $enable et != NULL")
                et.setTextColor(_ResUtil.getColor(et.context, R.color.hitam))
                if(enable)
                    et.setBackgroundResource(R.drawable.shape_solid_rect)
                else
                    et.background= null
            }
        }
    }
}