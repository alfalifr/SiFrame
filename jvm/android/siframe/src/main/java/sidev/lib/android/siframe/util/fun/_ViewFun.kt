package sidev.lib.android.siframe.util.`fun`

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import sidev.lib.android.external._AnkoInternals.runOnUiThread
import sidev.lib.android.siframe.adapter.SimpleAbsRecyclerViewAdapter
import sidev.lib.android.siframe.util._ViewUtil


fun SimpleAbsRecyclerViewAdapter<*, *>.notifyDatasetChanged_ui(){
    ctx.runOnUiThread{
        notifyDataSetChanged_()
    }
}

fun Fragment.changeView(vId: Int, layoutId: Int){
    activity?.changeView(vId, layoutId)
}
fun Activity.changeView(vId: Int, layoutId: Int){
    val vg= findViewById<ViewGroup>(vId)
    vg?.changeView(layoutId)
}

/**
 * @return view yang barusan diinflate dari id
 * @param layoutId
 */
fun View.changeView(layoutId: Int): View {
    val vg= when(this){
        is ViewGroup -> this
        else -> parent as ViewGroup
    }
    val v= LayoutInflater.from(context).inflate(layoutId, vg, false)
    vg.removeAllViews()
    vg.addView(v)
    context.toast("containerView == null = ${v == null}")
    return v
}

fun View.addView(layoutId: Int, index: Int?= null): View {
    val vg= when(this){
        is ViewGroup -> this
        else -> parent as ViewGroup
    }
    val v= LayoutInflater.from(context).inflate(layoutId, vg, false)
    val index= index ?: vg.childCount
    vg.addView(v, index)
    context.toast("containerView == null = ${v == null}")
    return v
}



fun <D> Intent.getIntentData(key: String, default: D?= null): D? {
    return this.extras?.get(key) as D? ?: default
}


fun View.getActivity(): Activity?{
    return _ViewUtil.getActivity(this)
}

fun View.getSize(): Array<Int>{
    return _ViewUtil.getViewSize(getActivity(), this)
}
fun View.setSize(width: Int, height: Int){
    return _ViewUtil.setViewSize(this, width, height)
}

fun Int.dpToPx(): Int{
    return _ViewUtil.dpToPx(this)
}

fun ViewGroup.changeView(v: View){
    removeAllViews()
    addView(v)
}
