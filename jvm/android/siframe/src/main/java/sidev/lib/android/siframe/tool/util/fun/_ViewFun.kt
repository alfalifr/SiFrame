package sidev.lib.android.siframe.tool.util.`fun`

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Build
import android.util.SparseIntArray
import android.view.*
import android.widget.*
import androidx.annotation.IdRes
import androidx.annotation.RequiresApi
import androidx.core.util.set
import androidx.core.view.children
import androidx.fragment.app.Fragment
import org.jetbrains.anko.layoutInflater
import sidev.lib.android.external._AnkoInternals.runOnUiThread
//import sidev.lib.android.siframe.adapter.RvAdp
import sidev.lib.android.siframe.adapter.SimpleRvAdp
import sidev.lib.android.siframe.exception.ClassCastExc
import sidev.lib.android.siframe.exception.TypeExc
import sidev.lib.android.siframe.tool.`var`._SIF_Constant
import sidev.lib.android.siframe.tool.util.*
import sidev.lib.universal.`fun`.asNotNull
import sidev.lib.universal.`fun`.notNullTo
import sidev.lib.universal.structure.NestedIterator
import java.lang.ClassCastException


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
 * Iterasi ini menggunakan Depth-First Pre-Order.
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
 * Properti untuk meng-iterasi seluruh keturunan this [View].
 * Menggunakan metode Depth-First Pre-Order.
 */
val View.childrenTree: Iterator<View>
    get()= object : NestedIterator<View>(this){
        override fun getIterator(now: View): Iterator<View>? {
            return if(now is ViewGroup && now.childCount > 0) now.children.iterator()
            else null
        }
    }
/*
val View.childrenTree: MutableIterator<View>
    get()= object: MutableIterator<View>{
        private var index= 0
        private var childIndex= SparseIntArray()

        override fun hasNext(): Boolean
            = this@childrenTree is ViewGroup && index < childCount

        override fun next(): View{
            val child= (this@childrenTree as ViewGroup).getChildAt(index)
            var viewFromChild: View?= null
            if(child is ViewGroup){
                var childIndexItr= childIndex[index, -1]
                if(childIndexItr == -1){
                    childIndex[index]= 0
                    childIndexItr= 0
                }
                viewFromChild
            }
            return child.childrenTree
        }

        override fun remove()
            = (this@childrenTree as ViewGroup).removeViewAt(--index)
    }
 */

/**
 * Properti untuk meng-iterasi parent yg merupakan satu garis hirarki.
 */
val View.parentsTree: Iterator<View>
    get()= object: Iterator<View>{
        override fun hasNext(): Boolean
            = this@parentsTree.parent is View

        override fun next(): View
            = this@parentsTree.parent as View
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
//    tag: Any?= null,
    includeItself: Boolean= true
): T? {
    if(includeItself && this is T)
        return this
//    var viewRes: T?= null

    if(direction == _SIF_Constant.DIRECTION_UP){
        for(parent in this.parentsTree){
//            loge("parent= $parent parent is T= ${parent is T}")
            if(parent is T)
                return parent
        }
    } else{ //Scr default akan meng-iterate ke bawah atau ke child.
        for(child in this.childrenTree){
//            loge("T= ${T::class.java.simpleName} child= ${child::class.java.simpleName} idName= ${child.idName} idPkg= ${child.idPackage} idRes= ${child.idResName} child is T= ${child is T}")
            if(child is T)
                return child
        }
    }
    return null
}

/**
 * Fungsi untuk menemukan view yg sesuai dg spesifikasi [id], [tag], atau [T].
 * Fungsi ini akan selalu menyertakan spesifikasi tipe data [T] view.
 * Untuk spesifikasi [id] dan [tag] adalah opsional.
 *
 * Fungsi dapat menyertakan this [View] tempat fungsi ini dipanggil
 * maupun tidak tergantung [includeItself].
 *
 * @return view dg spesifikasi yg diinputkan
 *   dan null jika tidak ada view yg cocok dalam hirarki.
 */
inline fun <reified T: View> View.findView(
    @IdRes id: Int?= null,
    tag: Any?= null,
    direction: Int= _SIF_Constant.DIRECTION_DOWN,
    includeItself: Boolean= true
): T?{
    if(includeItself && this is T
        && (id == null || id == this.id) && (tag == null || tag == this.tag))
        return this

    if(direction == _SIF_Constant.DIRECTION_UP){
        for(parent in this.parentsTree){
//            loge("parent= $parent parent is T= ${parent is T}")
            if(parent is T && (id == null || id == parent.id) && (tag == null || tag == parent.tag))
                return parent
        }
    } else{ //Scr default akan meng-iterate ke bawah atau ke child.
        for(child in this.childrenTree){
//            loge("T= ${T::class.java.simpleName} child= ${child::class.java.simpleName} idName= ${child.idName} idPkg= ${child.idPackage} idRes= ${child.idResName} child is T= ${child is T}")
            if(child is T && (id == null || id == child.id) && (tag == null || tag == child.tag))
                return child
        }
    }
    return null
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

/*
fun View.getActivity(): Activity?{
    return _ViewUtil.getActivity(this)
}
 */

val View.activity: Activity?
    get()= _ViewUtil.getActivity(this)

fun View.getSize(): Array<Int>{
    return _ViewUtil.getViewSize(this.activity, this)
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


/**
 * Fungsi setter ini tidak akan berhasil jika [RadioGroup.getChildAt] pada posisi tersebut bkn
 * merupakan [RadioButton].
 */
var RadioGroup.selectedInd: Int
    set(v){
        val child= this.getChildAt(v)
        try{ (child as RadioButton).isChecked= true }
        catch (e: ClassCastException){
            throw ClassCastExc(
                fromClass = child::class.java, toClass = RadioButton::class.java,
                msg = "RadioGroup.getChildAt($v) bkn merupakan RadioButton."
            )
        }
//            .asNotNull { rb: RadioButton -> rb.isChecked= true }
    }
    get(){
        this.children
        for(i in 0 until this.childCount){
            val child= getChildAt(i)
            if(child is RadioButton){
                if(child.isChecked)
                    return i
            }
        }
        return -1
    }


///*
/** Returns a [MutableIterator] over the views in this view group. */
fun ViewGroup.withIndex() = object : MutableIterator<Pair<Int, View>> {
    private var index = 0

    override fun hasNext() = index < childCount
    override fun next() = Pair(index, getChildAt(index++) ?: throw IndexOutOfBoundsException())
    override fun remove() = removeViewAt(--index)
}
// */

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

/** Digunakan untuk mengambil size sesaat sebelum view ditampilkan pada layar. */
fun View.getInitSize(l: (width: Int, height: Int) -> Unit){
    this.addOnGlobalLayoutListener {
        l(it.width, it.height)
    }
}

var EditText.txt: String
    set(v)= this.setText(v)
    get()= this.text.toString()


val View.xStartInWindow: Int
    get()= _ViewUtil.getViewXStartInWindow(this)

val View.yStartInWindow: Int
    get()= _ViewUtil.getViewYStartInWindow(this)

val View.xEndInWindow: Int
    get()= _ViewUtil.getViewXEndInWindow(this)

val View.yEndInWindow: Int
    get()= _ViewUtil.getViewYEndInWindow(this)


val View.screenWidth: Int
    get()= this.context.screenWidth

val View.screenHeight: Int
    get()= this.context.screenHeight

val Context.screenWidth: Int
    get()= _ViewUtil.getScreenWidth(this)

val Context.screenHeight: Int
    get()= _ViewUtil.getScreenHeight(this)


val View.animator: _ViewUtil.SimpleAnimator
    get()= _ViewUtil.SimpleAnimator(this)