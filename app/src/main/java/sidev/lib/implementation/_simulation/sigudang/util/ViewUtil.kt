package sidev.lib.implementation._simulation.sigudang.util

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.sigudang.android._template.model.PictTextModel
import com.sigudang.android.models.SendMethodModel
import sidev.lib.implementation.R

object ViewUtil {
    private fun setCompData_pictTxt_int(v: View, txt: String?, pict: Int?, ivRes: Int, tvRes: Int){
        var ivVis= View.VISIBLE
        var txt= txt ?: ""

        if(pict != null)
            v.findViewById<ImageView>(ivRes).setImageResource(pict)

        if(pict == null && txt.isBlank())
            ivVis= View.GONE
/*
        if(data != null){
            txt= data.txt
            if(data.pict != null)
                v.findViewById<ImageView>(ivRes).setImageResource(data.pict!!)
            else
                ivVis= View.GONE
        } else
            ivVis= View.GONE
 */

        v.findViewById<ImageView>(ivRes).visibility= ivVis
        v.findViewById<TextView>(tvRes).text= txt
    }

    fun setCompData_ivTv(v: View, data: PictTextModel?){
        setCompData_pictTxt_int(v, data?.txt, data?.pict?.resId, R.id.iv, R.id.tv)
    }
    fun setCompData_dropDown(v: View, data: PictTextModel?){
        setCompData_pictTxt_int(v, data?.txt, data?.pict?.resId, R.id.iv_side_txt, R.id.tv)
    }
    fun setCompData_dropDown(v: View, data: SendMethodModel?){
        setCompData_pictTxt_int(v, data?.name, data?.img?.resId, R.id.iv_side_txt, R.id.tv)
    }
    fun setCompData_dropDown(v: View, txt: String?, pict: Int?){
        setCompData_pictTxt_int(v, txt, pict, R.id.iv_side_txt, R.id.tv)
    }
}

/*
object ViewUtil {
    private fun setCompData_pictTxt_int(v: View, data: PictTextModel?, ivRes: Int, tvRes: Int, ctx: Context? = null){
        var ivVis= View.VISIBLE
        var txt= ""
        if(data != null){
            txt= data.txt
            if(data.pict != null) {
                when {
                    data.pict!!.bm != null -> v.findViewById<ImageView>(ivRes).setImageBitmap(data.pict!!.bm)
                    data.pict!!.resId != null -> v.findViewById<ImageView>(ivRes).setImageResource(data.pict!!.resId!!)
                    data.pict!!.dir != null -> if(ctx != null) RequestUtil(ctx).loadImageToImageView(v.findViewById(ivRes), RequestUtil.IMAGE_SIZE_TUMBNAIL_MEDIUM, data.pict!!.dir!!)
                }
                //v.findViewById<ImageView>(ivRes).setImageResource(data.pict!!)
            } else
                ivVis= View.GONE
        } else
            ivVis= View.GONE

        v.findViewById<ImageView>(ivRes).visibility= ivVis
        v.findViewById<TextView>(tvRes).text= txt
    }

    fun setCompData_ivTv(v: View, data: PictTextModel?, context: Context? = null){
        setCompData_pictTxt_int(v, data, R.id.iv, R.id.tv, context)
    }
    fun setCompData_dropDown(v: View, data: PictTextModel?, context: Context? = null){
        setCompData_pictTxt_int(v, data, R.id.iv_side_txt, R.id.tv, context)
    }
}
 */