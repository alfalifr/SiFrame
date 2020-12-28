package sidev.lib.android.std.tool.util.`fun`

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.*
import android.widget.*
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.IdRes
import androidx.annotation.RequiresApi
import androidx.core.view.children
import androidx.fragment.app.Fragment
import org.jetbrains.anko.layoutInflater
import sidev.lib.android.std.`val`._Constant
//import sidev.lib.android.siframe.adapter.RvAdp
//import sidev.lib.android.siframe.adapter.SimpleRvAdp
//import sidev.lib.android.siframe._val._SIF_Constant
import sidev.lib.android.std.tool.util.*
import sidev.lib.check.asNotNullTo
import sidev.lib.check.notNullTo
import sidev.lib.collection.iterator.iteratorSimple
import sidev.lib.collection.sequence.NestedSequence
import sidev.lib.collection.sequence.nestedSequenceSimple
import sidev.lib.exception.ClassCastExc
import sidev.lib.exception.ParameterExc
import sidev.lib.number.notNegativeOr
import sidev.lib.number.plus
import sidev.lib.number.round
import sidev.lib.number.toNumber
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
val View.childrenTree: NestedSequence<View>
    get()= nestedSequenceSimple(this){
        if(it is ViewGroup && it.childCount > 0) it.children.iterator()
        else null
    }
/*
    get()= object :
        NestedSequence<View> {
        override fun iterator(): NestedIteratorSimple<View>
            = object: NestedIteratorSimpleImpl<View>(this@childrenTree){
            override fun getOutputIterator(nowInput: View): Iterator<View>? {
                return if(nowInput is ViewGroup && nowInput.childCount > 0) nowInput.children.iterator()
                else null
            }
        }
    }
 */
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
val View.parentsTree: NestedSequence<View>
    get()= nestedSequenceSimple(this){
        it.parent.asNotNullTo { v: View -> iteratorSimple(v) }
    }
/*
    get()= object : NestedSequence<View>{
        override fun iterator(): NestedIterator<*, View>
            = object : NestedIteratorSimpleImpl<View>(this@parentsTree){
            override fun getOutputIterator(nowInput: View): Iterator<View>?
                = nowInput.parent.asNotNullTo { v: View -> newIteratorSimple(v) }
        }
    }
 */
/*
    get()= object: Sequence<View>{
        override fun iterator(): Iterator<View>
            = object: Iterator<View>{
            override fun hasNext(): Boolean
                    = this@parentsTree.parent is View

            override fun next(): View
                    = this@parentsTree.parent as View
        }
    }
 */

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



inline fun <reified T: View> View.findViewByType(
    direction: Int= _Constant.DIRECTION_DOWN,
//    tag: Any?= null,
    includeItself: Boolean= true
): T?
    = findViewByType(T::class.java, direction, includeItself)

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
@JvmOverloads
fun <T: View> View.findViewByType(
    clazz: Class<T>,
    direction: Int= _Constant.DIRECTION_DOWN,
//    tag: Any?= null,
    includeItself: Boolean= true
): T? {
    if(includeItself && clazz.isAssignableFrom(this::class.java)) //this::class.java.name == clazz.name
        return this as T
//    var viewRes: T?= null

    if(direction == _Constant.DIRECTION_UP){
        for(parent in this.parentsTree){
//            loge("parent= $parent parent is T= ${parent is T}")
            if(clazz.isAssignableFrom(parent::class.java))
                return parent as T
        }
    } else{ //Scr default akan meng-iterate ke bawah atau ke child.
        for(child in this.childrenTree){
//            loge("T= ${T::class.java.simpleName} child= ${child::class.java.simpleName} idName= ${child.idName} idPkg= ${child.idPackage} idRes= ${child.idResName} child is T= ${child is T}")
            if(clazz.isAssignableFrom(child::class.java))
                return child as T
        }
    }
    return null
}


inline fun <reified T: View> View.findView(
    @IdRes id: Int?= null,
    tag: Any?= null,
    direction: Int= _Constant.DIRECTION_DOWN,
    includeItself: Boolean= true
): T?
    = findView(T::class.java, id, tag, direction, includeItself)

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
@JvmOverloads
fun <T: View> View.findView(
    clazz: Class<T>,
    @IdRes id: Int?= null,
    tag: Any?= null,
    direction: Int= _Constant.DIRECTION_DOWN,
    includeItself: Boolean= true
): T?{
    if(includeItself && clazz.isAssignableFrom(this::class.java) //this::class.java.name == clazz.name
        && (id == null || id == this.id) && (tag == null || tag == this.tag))
        return this as T

    if(direction == _Constant.DIRECTION_UP){
        for(parent in this.parentsTree){
//            loge("parent= $parent parent is T= ${parent is T}")
            if(clazz.isAssignableFrom(parent::class.java) //parent::class.java.name == clazz.name
                && (id == null || id == parent.id) && (tag == null || tag == parent.tag))
                return parent as T
        }
    } else{ //Scr default akan meng-iterate ke bawah atau ke child.
        for(child in this.childrenTree){
            loge("T= ${clazz.simpleName} child= ${child::class.java.simpleName} idName= ${child.idName} idPkg= ${child.idPackage} idRes= ${child.idResName} child is T= ${clazz.isAssignableFrom(child::class.java)}")
            if(clazz.isAssignableFrom(child::class.java) //child::class.java.name == clazz.name
                && (id == null || id == child.id) && (tag == null || tag == child.tag))
                return child as T
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
 * Mengganti semua view isi semua `this.extension` [View] dg view yg memiliki [layoutId].
 *
 * @return -> [View] hasil inflate dari [layoutId],
 *   -> null jika `this.extension` dan parent bkn merupakan [ViewGroup] atau jika [layoutId] tidak bisa di-inflate.
 */
fun View.changeView(layoutId: Int): View? {
    return when(this){
        is ViewGroup -> this
        else -> parent as? ViewGroup
    }.notNullTo { vg ->
        context.inflate(layoutId, vg, false).notNullTo { inflatedView ->
            vg.removeAllViews()
            vg.addView(inflatedView)
            inflatedView
        }
    }
}

/**
 * @return -> [View] hasil inflate dari [layoutId],
 *   -> null jika `this.extension` dan parent bkn merupakan [ViewGroup] atau jika [layoutId] tidak bisa di-inflate.
 */
@JvmOverloads
fun View.addView(layoutId: Int, index: Int= -1): View? {
    return when(this){
        is ViewGroup -> this
        else -> parent as? ViewGroup
    }.notNullTo { vg ->
        context.inflate(layoutId, vg, false).notNullTo { inflatedView ->
            val innerIndex= index.notNegativeOr(vg.childCount) // ?: vg.childCount
            vg.addView(inflatedView, innerIndex)
            inflatedView
        }
    }
}

/** @return [ViewGroup] tempat `this.extension` menempel, null jika `this.extension` tidak menempel pada [ViewGroup]. */
fun View.detachFromParent(): ViewGroup? {
    return when(val parent= this.parent){
        is ViewGroup -> {
            parent.removeView(this)
            parent
        }
        else -> null
    }
}

val View.indexInParent: Int
    get()= parent.asNotNullTo { vg: ViewGroup -> vg.indexOfChild(this) } ?: -1

/**
 * Melepaskan semua immediate child view dari `this.extension` [ViewGroup].
 * @return list semua immediate child, kosong jika `this.extension` tidak punya child view.
 */
fun ViewGroup.detachAllViews(): List<View>{
    val views= ArrayList<View>()
    for(view in children){
        views += view
        removeView(view)
    }
    return views
}

/** @return [ViewGroup] yg menjadi tempat [v] menempel sebelumnya, null jika [v] sebelumnya tidak nempel pada ViewGroup apapun. */
@JvmOverloads
fun ViewGroup.forcedAddView(v: View, index: Int= childCount): ViewGroup? {
    val previousVg= v.detachFromParent()
    addView(v, index)
    return previousVg
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

var View.size: IntArray
    get()= _ViewUtil.getViewSize(this.activity, this)
    set(v){
        if(v.size != 2)
            throw ParameterExc(paramName = "View.size", detMsg = "IntArray yg diinputkan harus berukuran 2")
        _ViewUtil.setViewSize(this, v[0], v[1])
    }
var View.LayoutParamSize: IntArray
    get()= IntArray(2){if(it == 0) layoutParams.width else layoutParams.height }
    set(v){
        if(v.size != 2)
            throw ParameterExc(paramName = "View.size", detMsg = "IntArray yg diinputkan harus berukuran 2")
        _ViewUtil.setViewSize(this, v[0], v[1])
    }
/*
fun View.setSize(width: Int, height: Int){
    return _ViewUtil.setViewSize(this, width, height)
}
 */

/*
fun Int.dpToPx(): Int{
    return _ViewUtil.dpToPx(this)
}
 */

/**
 * Properti yg mengubah this [Number] dp menjadi px.
 * Sama dg fungsi [_ViewUtil.dpToPx].
 */
val Number.dp: Float get()= _ViewUtil.dpToPx(this.toFloat())

/**
 * Properti yg mengubah this [Number] sp menjadi px.
 * Sama dg fungsi [_ViewUtil.spToPx].
 */
val Number.sp: Float get()= _ViewUtil.spToPx(this.toFloat())


fun ViewGroup.changeView(v: View){
    removeAllViews()
    addView(v)
}

/*
fun View.setBgColorRes(@ColorRes colorId: Int){
    _ViewUtil.setBgColorTintRes(this, colorId)
}
 */

fun ImageView.setBgColorRes(@ColorRes colorId: Int){
    _ViewUtil.setColorTintRes(this, colorId)
}

@JvmOverloads
fun Context.inflate(layoutId: Int, vg: ViewGroup?= null, attachToRoot: Boolean= false): View? {
    return try{ this.layoutInflater.inflate(layoutId, vg, attachToRoot) }
    catch (e: Resources.NotFoundException){ null }
}
@JvmOverloads
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
                fromClass = child::class, toClass = RadioButton::class,
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


/*
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
@JvmOverloads
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
    this.addOnGlobalLayoutListener(true) {
        l(it.width, it.height)
    }
}

/**
 * Meng-assign teks yang sama ke `this.extension` `TextView`.
 * Fungsi ini berguna agar `this.extension` melakukan validasi ulang terhadap
 * text yang sama namun memiliki aturan yg berbeda.
 */
fun TextView.reassignText(){
    text= text
}

var EditText.txt: String
    set(v)= this.setText(v)
    get()= this.text.toString()

var TextView.txt: String
    set(v){ this.text= v }
    get()= this.text.toString()

var TextView.txtStyle: Int
    set(v)= setTypeface(typeface, v)
    get()= typeface.style

@get:ColorInt
var TextView.txtColor: Int
    set(@ColorInt v)= setTextColor(v)
    get()= currentTextColor

@get:Deprecated("Tidak dapat mengambil `txtColorRes` dari view", level = DeprecationLevel.ERROR)
var TextView.txtColorRes: Int
    get()= -1
    set(@ColorRes v)= setTextColor(_ResUtil.getColor(context, v))

/**
 * Menambah jml angka yg tertera pada [TextView.getText] sebanyak [diff].
 * @return jml setelah ditambah [diff], null jika ternyata `this.extension` [TextView.getText]
 *   sebelumnya bkn merupakan Number.
 */
@JvmOverloads
fun TextView.addTxtNumberBy(diff: Number, roundDigitPlace: Int?= null): Number?{
    return try{
        var res= text.toString().toNumber() + diff
        if(roundDigitPlace != null)
            res= res.round(roundDigitPlace) //Stlh percobaan, ternyata round biasa lebih aman dibangkan FLOOR.
        text= res.toString()
        res
    } catch (e: NumberFormatException){ null }
}


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

var View.bg: Drawable?
    get()= background
    set(v){ _ViewUtil.setBgDrawable(this, v) }

var View.bgAlpha: Int
    get()= _ViewUtil.getBgAlpha(this)
    set(v){ _ViewUtil.setBgAlpha(this, v) }

/**
 * Mengambil `int` color tint
 * return -1 jika `this.extension` tidak punya bg atau bg-nya tidak punya tint.
 */
@get:ColorInt
var View.bgColorTint: Int
    get()= _ViewUtil.getBgColorTintInt(this)
    set(@ColorInt v){ _ViewUtil.setBgColorTintInt(this, v) }

/**
 * Mengubah warna tint dari bg menggunakan [ColorRes]. Tidak dapat mengambil [ColorRes] karena
 * tidak mungkin dilakukan pada View.
 */
@get:Deprecated("Tidak dapat mengambil `bgColorTintRes` dari view", level = DeprecationLevel.ERROR)
var View.bgColorTintRes: Int
    get()= -1
    set(@ColorRes v){ _ViewUtil.setBgColorTintRes(this, v) }

/**
 * Mengambil `int` color tint
 * return -1 jika `this.extension` tidak punya drawable atau drawable-nya tidak punya tint.
 */
@get:ColorInt
var ImageView.colorTint: Int
    get()= _ViewUtil.getColorTintInt(this)
    set(@ColorInt v){ _ViewUtil.setColorTintInt(this, v) }

/**
 * Mengubah warna tint dari drawable menggunakan [ColorRes]. Tidak dapat mengambil [ColorRes] karena
 * tidak mungkin dilakukan pada View.
 */
@get:Deprecated("Tidak dapat mengambil `colorTintRes` dari view", level = DeprecationLevel.ERROR)
var ImageView.colorTintRes: Int
    get()= -1
    set(@ColorRes v){ _ViewUtil.setColorTintRes(this, v) }

var ImageView.img: Drawable?
    get()= drawable
    set(v){ _ViewUtil.setImgDrawable(this, v) }

/**
 * Mengubah warna tint dari drawable menggunakan [ColorRes]. Tidak dapat mengambil [ColorRes] karena
 * tidak mungkin dilakukan pada View.
 */
@get:Deprecated("Tidak dapat mengambil `imgRes` dari ImageView", level = DeprecationLevel.ERROR)
var ImageView.imgRes: Int
    get()= -1
    set(@ColorRes v){ _ViewUtil.setImgRes(this, v) }


val Context.screenWidth: Int
    get()= _ViewUtil.getScreenWidth(this)

val Context.screenHeight: Int
    get()= _ViewUtil.getScreenHeight(this)


/** Menunjukan apakah view dalam `this.extension` dapat ditumpuk. */
val ViewGroup.isChildStackable: Boolean
    get()= this !is LinearLayout

/**
 * Menunjukan apakah `this.extension` view dapat ditumpuk.
 * @return -> `true` jika `this.extension` berada di dalam ViewGroup selain [LinearLayout],
 *   -> `false` jika sebaliknya atau tidak berada di dalam [ViewGroup].
 */
val View.isStackable: Boolean
    get()= parent.asNotNullTo { vg: ViewGroup -> vg.isChildStackable } ?: false

/**
 * Menjadikan `this.extension` view stackable, yaitu dg membungkusnya ke dalam [RelativeLayout]
 * atau membiarkannya jika sudah [isStackable].
 *
 * @return -> [ViewGroup] yg merupakan [View.getParent] dari `this.extension`,
 *   -> `null` jika `this.extension` tidak dapat dijadikan stackable karena `this.extension`
 *   belum menempel pada ViewGroup apapun.
 */
fun View.makeStackable(): ViewGroup? {
    return if(isStackable) parent as ViewGroup
    else {
        parent.asNotNullTo { vg: ViewGroup ->
            val vgWrapper= RelativeLayout(context)
            vgWrapper.layoutParams= layoutParams
            layoutParams= RelativeLayout.LayoutParams(layoutParams.width, layoutParams.height)
            vg.addView(vgWrapper, indexInParent)
            vgWrapper.forcedAddView(this)
            vgWrapper
        }
    }
}