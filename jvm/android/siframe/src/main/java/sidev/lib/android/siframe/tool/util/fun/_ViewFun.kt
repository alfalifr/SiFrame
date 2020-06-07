package sidev.lib.android.siframe.tool.util.`fun`

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.fragment.app.Fragment
import org.jetbrains.anko.layoutInflater
import sidev.lib.android.external._AnkoInternals.runOnUiThread
import sidev.lib.android.siframe.adapter.SimpleAbsRecyclerViewAdapter
import sidev.lib.android.siframe.tool.util._ViewUtil


/*
===============================
Extending
===============================
 */


/*
===============================
New
===============================
 */

fun SimpleAbsRecyclerViewAdapter<*, *>.notifyDatasetChanged_ui(){
    ctx.runOnUiThread{
        notifyDataSetChanged_()
    }
}

fun View.iterateChild(func: (child: View) -> Unit){
    if(this is ViewGroup){
        for(child in this.children){
            func(child)
            if(child is ViewGroup)
                child.iterateChild(func)
        }
    }
}

/**
 * @param func return true jika berhenti
 */
fun View.iterateChildWithStop(func: (child: View) -> Boolean){
    if(this is ViewGroup){
        for(child in this.children){
            if(!func(child)){
                if(child is ViewGroup)
                    child.iterateChildWithStop(func)
            } else break
        }
    }
}

/**
 * Menemukan first occurrence
 */
inline fun <reified T: View> View.findViewByType(): T? {
    var stop= false
    var childRes: T?= null
    this.iterateChildWithStop { child ->
        stop= child is T
        if(stop)
            childRes= child as T
        stop
    }
    return childRes
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

/**
 * @return jika berhasil di-detach dari parent
 */
fun View.detachFromParent(): Boolean{
    return when(val parent= this.parent){
        is ViewGroup -> {
            parent.removeView(this)
            true
        }
        else -> false
    }
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

fun Context.inflate(layoutId: Int, vg: ViewGroup?= null, attachToRoot: Boolean= false): View? {
    return try{ this.layoutInflater.inflate(layoutId, vg, attachToRoot) }
    catch (e: Resources.NotFoundException){ null }
}
