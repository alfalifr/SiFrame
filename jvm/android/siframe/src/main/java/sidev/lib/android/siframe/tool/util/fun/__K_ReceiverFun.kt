package sidev.lib.android.siframe.tool.util.`fun`

//import org.jetbrains.anko.runOnUiThread
//import org.jetbrains.anko.toast


/*
inline fun <reified T: Activity> Any.startActForResult(simpleAbsAct: SimpleAbsAct, reqCode: Int, vararg params: Pair<String, Any?>,
                                                       noinline l: (reqCode: Int, resCode: Int, data: Intent?) -> Unit) {
    simpleAbsAct.setOnActResultListener(l)
    val i= simpleAbsAct.createIntent<T>(*params)
    simpleAbsAct.startActivityForResult(i, reqCode)
}

inline fun Activity.setResult(vararg params: Pair<String, Any?>, resCode: Int= Activity.RESULT_OK){
    val i= createIntent<Activity>(*params)
    setResult(resCode, i)
}

inline fun Fragment.setResult(vararg params: Pair<String, Any?>, resCode: Int= Activity.RESULT_OK){
    val i= createIntent<Activity>(*params)
    activity!!.setResult(resCode, i)
}





/*
inline fun <reified F: Fragment> AppCompatActivity.startSingleBoundFragAct(
//    simpleAbsAct: SimpleAbsAct, reqCode: Int,
    boundProduct: Bound,
    vararg params: Pair<String, Any?>
) {
    val pairClassName= Pair(
        SingleFragAct.EXTRA_CLASS_NAME,
        F::class.java.name
    )
    StaticManager.attachBoundData(boundProduct, this)
    val pairType= Pair(SingleFragAct.EXTRA_TYPE_LATE, true)
//    simpleAbsAct.setOnActResultListener(l)
//    val i= createIntent<SingleBoundProsesAct_Simple>(pairClassName, *params)
    startAct<SingleBoundProsesAct_Simple>(pairClassName, pairType, *params)
//    simpleAbsAct.startActivityForResult(i, reqCode)
}
 */



inline fun <reified F: Fragment> Any.startSingleFragActForResult(
    simpleAbsAct: SimpleAbsAct, reqCode: Int,
    type: Int= RECIEVER_FUN_FRAG_TYPE_SIMPLE,
    vararg params: Pair<String, Any?>,
    noinline l: (reqCode: Int, resCode: Int, data: Intent?) -> Unit
) {
    val pairClassName= Pair(
        SingleFragAct.EXTRA_CLASS_NAME,
        F::class.java.name
    )
    simpleAbsAct.setOnActResultListener(l)
    val i=
        when(type){
            RECIEVER_FUN_FRAG_TYPE_BAR_CONTENT_NAV -> simpleAbsAct.createIntent<SingleFragAct_BarContentNav>(pairClassName, *params)
            else -> simpleAbsAct.createIntent<SingleFragAct>(pairClassName, *params)
        }
    simpleAbsAct.startActivityForResult(i, reqCode)
}

inline fun <reified F: Fragment> Any.startSingleBoundFragActForResult(
    simpleAbsAct: SimpleAbsAct, boundData: Bound, reqCode: Int= Constants.REQUEST_DEFAULT,
    vararg params: Pair<String, Any?>,
    noinline l: (reqCode: Int, resCode: Int, data: Intent?) -> Unit
) {
    val pairClassName= Pair(
        SingleFragAct.EXTRA_CLASS_NAME,
        F::class.java.name
    )
    val pairData= Pair(Constants.EXTRA_INTENT_DATA, boundData)
    val pairType= Pair(SingleFragAct.EXTRA_TYPE_LATE, true)
    simpleAbsAct.setOnActResultListener(l)
    val i= simpleAbsAct.createIntent<SingleBoundProsesAct_Simple>(pairClassName, pairType, pairData, *params)
    simpleAbsAct.startActivityForResult(i, reqCode)
}



inline fun <reified F: Activity> Context.startBoundAct(
//    simpleAbsAct: SimpleAbsAct, reqCode: Int,
    boundData: Bound?= null,
    vararg params: Pair<String, Any?>
) {
    val pairData= Pair(Constants.EXTRA_INTENT_BOUND, boundData)
    val pairType= Pair(SingleFragAct.EXTRA_TYPE_LATE, true)
//    simpleAbsAct.setOnActResultListener(l)
//    val i= createIntent<SingleBoundProsesAct_Simple>(pairClassName, *params)
    startAct<F>(pairType, pairData, *params)
//    simpleAbsAct.startActivityForResult(i, reqCode)
}
inline fun <reified F: Activity> Fragment.startBoundAct(
    boundData: Bound?= null,
    vararg params: Pair<String, Any?>
) {
    context!!.startBoundAct<F>(boundData, *params)
}




inline fun <reified T: Activity> Any.startAct(c: Context, vararg params: Pair<String, Any?>) {
    c.startAct<T>(*params)
}


const val RECIEVER_FUN_FRAG_TYPE_SIMPLE= 0
const val RECIEVER_FUN_FRAG_TYPE_BAR_CONTENT_NAV= 1

inline fun <reified F: Fragment> Context.startSingleFragAct(
    type: Int= RECIEVER_FUN_FRAG_TYPE_SIMPLE,
    vararg params: Pair<String, Any?>
) {
    val pairClassName= Pair(
        SingleFragAct.EXTRA_CLASS_NAME,
        F::class.java.name
    )
    when(type){
        RECIEVER_FUN_FRAG_TYPE_BAR_CONTENT_NAV -> startAct<SingleFragAct_BarContentNav>(pairClassName, *params)
        else -> startAct<SingleFragAct>(pairClassName, *params)
    }
}

inline fun <reified F: Fragment> Fragment.startSingleFragAct(
    type: Int= RECIEVER_FUN_FRAG_TYPE_SIMPLE,
    vararg params: Pair<String, Any?>
) {
    val pairClassName= Pair(
        SingleFragAct.EXTRA_CLASS_NAME,
        F::class.java.name
    )
    val pairType= Pair(SingleFragAct.EXTRA_TYPE_LATE, true)

    when(type){
        RECIEVER_FUN_FRAG_TYPE_BAR_CONTENT_NAV -> startAct<SingleFragAct_BarContentNav_Simple>(pairClassName, pairType, *params)
        else -> startAct<SingleFragAct_Simple>(pairClassName, pairType, *params)
    }
}

inline fun <reified T: Activity> Context.startAct(vararg params: Pair<String, Any?>) {
    startActivity(
        AnkoInternals.createIntent(this, T::class.java, params)
    )
}

inline fun <reified T: Activity> Fragment.startAct(vararg params: Pair<String, Any?>) {
    context!!.startAct<T>(*params)
}

inline fun <reified T: Activity> SimpleAbsFrag.startAct(vararg params: Pair<String, Any?>) {
    ctx!!.startAct<T>(*params)
}

inline fun <reified T: Activity> Context.createIntent(vararg params: Pair<String, Any?>): Intent {
    return AnkoInternals.createIntent(this, T::class.java, params)
}

inline fun <reified T: Activity> SimpleAbsFrag.createIntent(vararg params: Pair<String, Any?>): Intent {
    return ctx!!.createIntent<T>(*params) //AnkoInternals.createIntent(this, T::class.java, params)
}

inline fun <reified T: Activity> Fragment.createIntent(vararg params: Pair<String, Any?>): Intent {
    return context!!.createIntent<T>(*params) //AnkoInternals.createIntent(this, T::class.java, params)
}

inline fun SimpleAbsFrag.toast(textResource: Int) = ctx.toast(textResource)

inline fun SimpleAbsFrag.toast(text: CharSequence) = ctx.toast(text)




inline fun <D: Any> D?.notNull(func: (any: D) -> Unit){
    if(this != null)
        func(this)
}

inline fun <I: Any, O: Any> I?.notNullFor(func: (any: I) -> O?): O?{
    return if(this != null)
        func(this)
    else null
}

inline fun <reified D: Any> D?.isNull(func: () -> Unit){
    if(this == null)
        func()
}

fun Number.isZero(): Boolean{
    return this.toInt() <= 0
}
fun Number.isNotZero(): Boolean{
    return this.toInt() > 0
}



fun AppCompatActivity.commitFragment(vId: Int, frag: Fragment){
    commitFragment(findViewById<View>(vId), frag)
}

fun AppCompatActivity.commitFragment(v: View, frag: Fragment){
    val h= v.layoutParams.height
    val w= v.layoutParams.width
    supportFragmentManager
        .beginTransaction()
        .replace(v.id, frag)
        .commit()
    v.layoutParams.height= h
    v.layoutParams.width= w
}

fun SimpleAbsFrag.commitFragment(vid: Int, frag: Fragment){
    activity.notNull {
        it.supportFragmentManager
            .beginTransaction()
            .replace(vid, frag)
            .commit()
    }
}

fun SimpleAbsFrag.commitFragment(v: View, frag: Fragment): Int{
    return activity.notNullFor {
        it.supportFragmentManager
            .beginTransaction()
            .replace(v.id, frag)
            .commit()
    } ?: -69
}
 */