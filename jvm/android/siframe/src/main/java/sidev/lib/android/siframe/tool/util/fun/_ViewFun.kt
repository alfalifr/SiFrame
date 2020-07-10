package sidev.lib.android.siframe.tool.util.`fun`

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Build
import android.view.*
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.annotation.RequiresApi
import androidx.core.view.children
import androidx.fragment.app.Fragment
import org.jetbrains.anko.layoutInflater
import sidev.lib.android.external._AnkoInternals.runOnUiThread
//import sidev.lib.android.siframe.adapter.RvAdp
import sidev.lib.android.siframe.adapter.SimpleRvAdp
import sidev.lib.android.siframe.tool.`var`._SIF_Constant
import sidev.lib.android.siframe.tool.util._ViewUtil
import sidev.lib.universal.`fun`.notNullTo


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

fun SimpleRvAdp<*, *>.notifyDatasetChanged_ui(){
    ctx.runOnUiThread{
        notifyDataSetChanged_()
    }
}

/**
 * Iterasi ini menggunakan Depth-First Traversal.
 */
fun View.iterateChildren(func: (child: View) -> Unit){
    if(this is ViewGroup){
        for(child in this.children){
            func(child)
            if(child is ViewGroup)
                child.iterateChildren(func)
        }
    }
}
/**
 * Fungsi ini melakukan iterasi terhadap [ViewParent]
 * yg merupakan satu garis keturunan dan merupakan [ViewGroup].
 */
fun View.iterateParent(func: (parent: View) -> Unit){
    var parent= this.parent
    while(parent != null && parent is ViewGroup){
        func(parent)
        parent= parent.parent
    }
}
/*
/**
 * @param func return true jika berhenti
 */
 <7 Juli 2020> => Dihilangkan karena stop bisa dilakukan dg return@function.
fun View.iterateChildrenWithStop(func: (child: View) -> Boolean){
    if(this is ViewGroup){
        for(child in this.children){
            if(!func(child)){
                if(child is ViewGroup)
                    child.iterateChildrenWithStop(func)
            } else break
        }
    }
}
 */

fun Activity.getRootView(): View{
    return this.findViewById<View>(android.R.id.content).rootView
}
fun Fragment.getRootView(): View?{
    return activity.notNullTo { act -> act.getRootView() }
        ?: this.view?.rootView
}

/**
 * Menemukan first occurrence.
 *
 * @param direction [_SIF_Constant.DIRECTION_DOWN] jika meng-iterate view children.
 *   [_SIF_Constant.DIRECTION_UP] jika meng-iterate view parent.
 * @param includeItself true jika hasil return juga mengembalikan view tempat
 *   fungsi ini menempel.
 *
 * @return view dg tipe [T]. View dapat berupa child, parent, ataupun
 *   view tempat fungsi ini menempel.
 */
inline fun <reified T: View> View.findViewByType(
    direction: Int= _SIF_Constant.DIRECTION_DOWN,
    includeItself: Boolean= true
): T? {
    if(includeItself && this is T) return this

    var viewRes: T?= null

    if(direction == _SIF_Constant.DIRECTION_UP){
        this.iterateParent { parent ->
            if(parent is T){
                viewRes= parent
                return@iterateParent
            }
        }
    } else{ //Scr default akan meng-iterate ke bawah atau ke child.
        this.iterateChildren { child ->
            if(child is T){
                viewRes= child
                return@iterateChildren
            }
        }
    }
    return viewRes
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

fun View.setPadding_(left: Int= this.paddingLeft, top: Int= this.paddingTop, right: Int= this.paddingRight, bottom: Int= this.paddingBottom){
    this.setPadding(left, top, right, bottom)
}

fun View.setChildPadding(childIndex: Int= 0, left: Int= this.paddingLeft, top: Int= this.paddingTop, right: Int= this.paddingRight, bottom: Int= this.paddingBottom){
    val parent= when(this){
        is ViewGroup -> this
        else -> this.parent as ViewGroup
    }
//    val child
//    val a= ListView
    when(val child= parent.getChildAt(childIndex)){
        is View -> child.setPadding(left, top, right, bottom)
//        is Adp ->
    }
//    parent.getChildAt(childIndex).setPadding(left, top, right, bottom)
}



fun <D> Intent.getIntentData(key: String, default: D?= null): D? {
    return this.extras?.get(key) as? D ?: default
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
fun Fragment.inflate(layoutId: Int, vg: ViewGroup?= null, attachToRoot: Boolean= false): View? {
    return try{ this.layoutInflater.inflate(layoutId, vg, attachToRoot) }
    catch (e: Resources.NotFoundException){ null }
}



fun RadioGroup.getSelectedInd(): Int{
    for(i in 0 until this.childCount){
        val child= getChildAt(i)
        if(child is RadioButton){
            if(child.isChecked)
                return i
        }
    }
    return -1
}

/**
 * Fungsi praktis untuk memasang listener pada View saat pertama kali ditampilkan pada layar.
 *
 * @param justOnce true jika kode di dalam lambda [l] dijalankan sekali dan listener
 *   segera dihilangkan ([ViewTreeObserver.removeOnGlobalLayoutListener]) setelah listener dipanggil.
 * @param l ada kode fungsi listener.
 */
fun View.addOnGlobalLayoutListener(justOnce: Boolean= true, l: (View) -> Unit): ViewTreeObserver.OnGlobalLayoutListener{
    val innerL= object: ViewTreeObserver.OnGlobalLayoutListener{
        @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
        override fun onGlobalLayout() {
            l(this@addOnGlobalLayoutListener)
            if(justOnce)
                this@addOnGlobalLayoutListener
                    .viewTreeObserver.removeOnGlobalLayoutListener(this)
        }
    }
    this.viewTreeObserver.addOnGlobalLayoutListener(innerL)
    return innerL
}
