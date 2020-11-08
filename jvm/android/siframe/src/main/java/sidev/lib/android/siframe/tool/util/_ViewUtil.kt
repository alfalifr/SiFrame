package sidev.lib.android.siframe.tool.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.*
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.*
import android.hardware.Camera
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.transition.TransitionSet
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.*
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.ViewCompat
import androidx.core.widget.ImageViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.squareup.picasso.Picasso
import org.jetbrains.anko.backgroundDrawable
import org.jetbrains.anko.layoutInflater
import org.jetbrains.anko.textColorResource
import org.jetbrains.anko.windowManager
import sidev.lib.android.siframe._customizable._ColorRes
import sidev.lib.android.siframe._customizable._Config
import sidev.lib.android.siframe.lifecycle.app.App
import sidev.lib.android.siframe.model.PictModel
import sidev.lib.android.siframe.tool.util.`fun`.*
import sidev.lib.check.notNull
import sidev.lib.check.notNullTo
import sidev.lib.jvm.tool.util.FileUtil
import sidev.lib.number.isZero
import kotlin.math.abs

//class ini digunakan tempat utility khusus view
object  _ViewUtil{
    val DURATION_ANIMATION_DEFAULT : Long = 200
    val DURATION_ANIMATION_LONG : Long = 500

    // Untuk mengubah ukuran dari DP ke PX
    @JvmOverloads
    fun dpToPx(dp: Float, context: Context? = null): Float = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        (context?.resources?.displayMetrics ?: Resources.getSystem().displayMetrics)
    )

    @JvmOverloads
    fun spToPx(sp: Float, context: Context? = null): Float = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        sp,
        (context?.resources?.displayMetrics ?: Resources.getSystem().displayMetrics)
    )
/*
    fun dpToPx(dp : Float): Int {
        return (dp * Resources.getSystem().displayMetrics.density).toInt()
    }
 */

    fun getScreenWidth(ctx: Context) : Int {
        val dm = DisplayMetrics()
        ctx.windowManager.defaultDisplay.getMetrics(dm)
        return dm.widthPixels
    }
    fun getScreenHeight(ctx: Context): Int{
        val dm = DisplayMetrics()
        ctx.windowManager.defaultDisplay.getMetrics(dm)
        return dm.heightPixels
    }

    //Untuk sementara msh berjalan scr terpisah dari Main Thread.
    fun getViewSize(v: View, l: (w: Int, h: Int) -> Unit): ViewTreeObserver.OnGlobalLayoutListener{
        val l= object : ViewTreeObserver.OnGlobalLayoutListener{
            @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
            override fun onGlobalLayout() {
                l(v.width, v.height)
                v.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        }
        v.viewTreeObserver.addOnGlobalLayoutListener(l)
        return l
    }

    /**
     * @param v untuk view yg sudah di gambar di layar.
     * @return IntArray[0]=x IntArray[0]= y
     */
    fun getViewLocationInWIndow(v: View): IntArray{
        val location= IntArray(2)
//        v.transformFromViewToWindowSpace()
        v.getLocationInWindow(location)
        return location
    }

    /**
     * @param v untuk view yg sudah di gambar di layar.
     */
    fun getViewXEndInWindow(v: View): Int{
        val x= getViewLocationInWIndow(v)[0]
        return x +v.size[0]
    }
    fun getViewYEndInWindow(v: View): Int{
        val y= getViewLocationInWIndow(v)[1]
        return y +v.size[1]
    }

    fun getViewXStartInWindow(v: View): Int{
        return getViewLocationInWIndow(v)[0]
    }
    fun getViewYStartInWindow(v: View): Int{
        return getViewLocationInWIndow(v)[1]
    }


    fun getPercentOfScreenHeight(act: Activity, percent: Float): Int{
        val screenHeight= getScreenHeight(act)
        return (screenHeight *percent).toInt()
    }
    fun getPercentOfScreenWidth(act: Activity, percent: Float): Int{
        val screenWidth= getScreenWidth(act)
        return (screenWidth *percent).toInt()
    }

    fun setViewHeight(view: View, height: Int){
        view.layoutParams.height = height
    }

    fun setViewSize(view: View, width: Int, height: Int){
        try{
            view.layoutParams.width= width
            view.layoutParams.height= height
        } catch (e: NullPointerException){
            view.layoutParams= ViewGroup.LayoutParams(width, height)
        }
    }

    fun calculateColumnNumber(
        context: Context,
        columnWidthDp: Int
    ): Int { // For example columnWidthdp=180
        val displayMetrics = context.resources.displayMetrics
        val screenWidthDp = displayMetrics.widthPixels / displayMetrics.density
        return (screenWidthDp / (columnWidthDp + 0.5)).toInt()
    }

    /**
     * @return mengembalikan Float dalam bentuk pixel
     */
    fun calculateColumnWidth(ctx: Context, columnNumber: Int): Int{
        val dm= ctx.resources.displayMetrics
//        val screenWidthDp = dm.widthPixels / dm.density
        return dm.widthPixels / columnNumber
    }

    fun initAlartDialog(res: Int, ctx: Context): AlertDialog{
        val view = ctx.layoutInflater.inflate(res, null)
        return initAlartDialog(view, ctx)
    }
    fun initAlartDialog(view: View, ctx: Context): AlertDialog{
        return AlertDialog.Builder(ctx)
            .setView(view)
            .setCancelable(true).create()
    }

    // Untuk merubah dimensi layout dengan transisi
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun changeViewDimen(v: ViewGroup, newDimension: Int) {
        TransitionManager.beginDelayedTransition(
            v, TransitionSet()
                .addTransition(ChangeBounds())
        )

        val params = v.layoutParams
        params.height = newDimension
        v.layoutParams = params
    }


    /**
     * Merubah stroke color dari view background
     */
    fun changeDrawableStrokeColor(@ColorRes colorId: Int, vararg v: View, context: Context?= null){
        if(v.isEmpty())
            return
        val ctx= context ?: v.first().context
        for(e in v){
            val vDrawable = e.background// as GradientDrawable
            if(vDrawable is GradientDrawable)
                vDrawable.setStroke(dpToPx(3f).toInt(), _ResUtil.getColor(ctx, colorId))
        }
    }

    /**
     * merubah warna dari bbrp [tv].
     */
    fun changeTextColor(@ColorRes colorId: Int, vararg tv: TextView, context: Context?= null){
        if(tv.isEmpty())
            return
        val ctx= context ?: tv.first().context
        for(element in tv)
            element.setTextColor(_ResUtil.getColor(ctx, colorId))
    }
/*
    /**
     * Untuk mengambil warna sesuai nilai asli yang diambil dari <code>colorResId</code>
     * @param colorResId --> R.color...
     * @return nilai int dari kode warna yang sebernannya
     */
    fun getColor(c: Context, colorResId: Int): Int{
        return if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            c.resources.getColor(colorResId)
        else
            c.resources.getColor(colorResId, c.theme)
    }
 */

    // Untuk Fade Out View
    fun fadeOut(vararg views: View, duration: Long = 1000) {
        fade(1f, 0f, *views, duration = duration)
    }

    // Untuk Fade In View
    fun fadeIn(vararg views: View, duration: Long = 1000) {
        fade(0f, 1f, *views, duration = duration)
    }

    fun fade(start: Float, end: Float, vararg views: View, duration: Long = 1000) {
        for(v in views){
            val alphaAnim  = AlphaAnimation(start, end)
            alphaAnim.duration = duration
//            alphaAnim.interpolator= AccelerateInterpolator() //.setInterpolator { it }
            alphaAnim.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(animation: Animation?) {}
                override fun onAnimationEnd(animation: Animation?) {
                    v.alpha = end
                    v.visibility = if (end > 0) View.VISIBLE
                    else View.GONE
                }

                override fun onAnimationStart(animation: Animation?) {
                    v.visibility = if (start > 0) View.VISIBLE
                    else View.GONE
                }
            })
//            v.animation = alphaAnim
            v.startAnimation(alphaAnim)
        }
    }

    fun isAllFieldFilled(vararg et: EditText) : Boolean{
        for(element in et){
            if(element.text.toString().equals("", ignoreCase = true))
                return false
        }
        return true
    }

    // Untuk menambahkan fragment
    fun loadFragment(
        fragmentManager: FragmentManager,
        fragment: Fragment,
        container: Int,
        tag: String
    ){
        fragmentManager
                .beginTransaction()
                .replace(container, fragment, tag)
                .commit()
    }

    fun <N : Number> extractNumberFromEd(ed: EditText, defaultValue: Number = 0): N{
        val countStr= ed.text.toString()
        return if(countStr.isNotEmpty()) countStr.toInt() as N
            else defaultValue as N
    }


    /**
     * Mengukur width dan height dari
     * @param view sebelum ditampilkan di layar
     * @return array 2 dimensi
     *          array[0] -> width
     *          array[1] -> height
     */
    fun getViewSize(act: Activity?, view: View): IntArray{
        val res= IntArray(2)
        res[0]= view.width
        res[1]= view.height
        if(res[0].isZero() && res[1].isZero() && act != null){
/*
            containerView.measure(View.MeasureSpec.makeMeasureSpec(parentView.getWidth(), MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(MAX_HEIGHT, MeasureSpec.AT_MOST));
            final int targetHeight = containerView.getMeasuredHeight();
 */
            val display = act.windowManager.defaultDisplay
//        val view = act.findViewById(R.id.YOUR_VIEW_ID);
            val outPointDisplay= Point()
            display.getSize(outPointDisplay)
            view.measure(getScreenWidth(act), getScreenHeight(act))

            val width= view.measuredWidth // view width
            val height= view.measuredHeight //view height
            res[0]= width
            res[1]= height
        }
        return res
    }


    fun getActivity(view: View): Activity?{
        var context = view.context
        while (context is ContextWrapper) {
            if (context is Activity)
                return context
            context = (context /*as ContextWrapper*/).baseContext
        }
        return null
    }

    /**
     * Mengambil `int` color tint dari [drawable].
     * return -1 jika `this.extension` [drawable] tidak punya tint atau tipenya tidak diketahui.
     */
    @ColorInt
    fun getColorTintInt(drawable: Drawable): Int { //, blendMode: BlendMode= BlendMode.SRC_ATOP){
        val color= DrawableCompat.getColorFilter(drawable)?.let { getColorTintInt(it) } ?: -1
        return if(color >= 0) color
        else when(drawable){
            is ColorDrawable -> drawable.color
            is BitmapDrawable -> drawable.paint.color
            is PaintDrawable -> drawable.paint.color
//            is VectorDrawableCompat -> drawable.setTintList()
            is GradientDrawable -> {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) drawable.color?.defaultColor ?: -1
                else -1
            }
            else -> when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && drawable is ColorStateListDrawable -> (drawable.current as ColorDrawable).color
                else -> -1
            }
        }
    }
    /**
     * Mengambil `int` color tint dari [colorFilter].
     * return -1 jika `this.extension` [colorFilter] tidak punya tint atau tipenya tidak diketahui.
     */
    @ColorInt
    fun getColorTintInt(colorFilter: ColorFilter): Int = when(colorFilter){
        is PorterDuffColorFilter -> {
            try {
                PorterDuffColorFilter::class.java.getField("mColor").get(colorFilter) as Int
            } catch (e: ClassCastException){
                loge("_ViewUtil.getColorTintInt() tidak dapat mengambil \"mColor\" dari $colorFilter")
                -1
            }
        }
//        is ColorMatrixColorFilter ->
        else -> when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && colorFilter is BlendModeColorFilter -> colorFilter.color
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && colorFilter is LightingColorFilter -> colorFilter.colorMultiply
            else -> -1
        }
    }
    /**
     * Mengambil `int` color tint dari [ImageView.getDrawable].
     * return -1 jika `this.extension` [ImageView.getDrawable] tidak punya drawable atau drawable-nya tidak punya tint.
     */
    @ColorInt
    fun getColorTintInt(iv: ImageView): Int {
        return ImageViewCompat.getImageTintList(iv)?.let {
            it.getColorForState(iv.drawableState, it.defaultColor)
        } ?: -1
//        return iv.drawable?.let { getColorTintInt(it) } ?: -1
    }
    /**
     * Mengambil `int` color tint dari [View.getBackground].
     * return -1 jika `this.extension` [View.getBackground] tidak punya bg atau bg-nya tidak punya tint.
     */
    @ColorInt
    fun getBgColorTintInt(v: View): Int {
        return ViewCompat.getBackgroundTintList(v)?.let {
            it.getColorForState(v.drawableState, it.defaultColor)
        } ?: -1
//        return v.background?.let { getColorTintInt(it) } ?: -1
    }


    fun setColorTintRes(drawable: Drawable, @ColorRes colorId: Int, ctx: Context?= null)=
        setColorTintInt(drawable, _ResUtil.getColor(ctx ?: App.ctx, colorId))
    fun setColorTintInt(drawable: Drawable, @ColorInt color: Int)= DrawableCompat.setTint(drawable, color)

    fun setColorTintRes(iv: ImageView, @ColorRes colorId: Int)= setColorTintInt(iv, _ResUtil.getColor(iv.context, colorId))
    fun setColorTintInt(iv: ImageView, @ColorInt color: Int){ //, blendMode: BlendMode= BlendMode.SRC_ATOP){
        val drawable= iv.drawable
        if(drawable != null)
            ImageViewCompat.setImageTintList(iv, ColorStateList.valueOf(color))
//            setColorTintInt(drawable, color)
        else
            iv.setImageDrawable(ColorDrawable(color))
//        iv.drawable.colorFilter = BlendModeColorFilter(color, blendMode) //Masih error karena masalah kompatibilitas API
    }

    fun setImgRes(iv: ImageView, @DrawableRes drawableId: Int) = iv.setImageResource(drawableId)
    fun setImgDrawable(iv: ImageView, drawable: Drawable?) = iv.setImageDrawable(drawable)

    fun setBgRes(v: View, @DrawableRes drawableId: Int) = v.setBackgroundResource(drawableId)
    fun setBgDrawable(v: View, drawable: Drawable?){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
            v.backgroundDrawable= drawable
        else
            v.background= drawable
    }
    /**
     * Untuk merubah transparansi background drawable dari [v] sebanyak [level].
     * [level] dihitung dg persentase dari nilai total alpha, yaitu 255.
     */
    fun setBgAlpha(v: View, level: Int) {
        val bg = v.background
        if(bg != null)
            bg.alpha = ((level.toDouble()/100) * 255).toInt()
    }
    /**
     * Mengambil nilai alpha dari [v].
     * Return 0 jika [v] tidak punya bg atau API level di bawah 19.
     */
    fun getBgAlpha(v: View): Int = v.background?.let { DrawableCompat.getAlpha(it) } ?: 0


    fun setBgColorTintRes(v: View, @ColorRes colorId: Int) = setBgColorTintInt(v, _ResUtil.getColor(v.context, colorId))
    fun setBgColorTintInt(v: View, @ColorInt color: Int){ //, blendMode: BlendMode= BlendMode.SRC_ATOP){
        val drawable= v.background
        if(drawable != null)
            ViewCompat.setBackgroundTintList(v, ColorStateList.valueOf(color))
//            setColorTintInt(drawable, color)
        else
            v.backgroundDrawable= ColorDrawable(color)
//        v.background.colorFilter = BlendModeColorFilter(color, blendMode) //Masih error karena masalah kompatibilitas API
    }


    fun setImg(iv: ImageView, img: PictModel?): Boolean{
        return if(img != null){
            when {
                img.bm != null -> {
                    iv.setImageBitmap(img.bm)
                    true
                }
                img.dir != null -> loadImageToIv(iv, img.dir)
                else -> false
            }
        } else false
    }

    fun loadImageToIv(iv: ImageView, /*ivSizeType: Int, */url: String?): Boolean{
        val isDirLocal= try{ FileUtil.dirExists(url!!) }
        catch (e: Exception){ false }

        val dir=
            if (isDirLocal) url
            else _Config.ENDPOINT_ROOT + url


        return if(!isDirLocal){
            var dim = 500
/*
            when(ivSizeType){
                IMAGE_SIZE_TUMBNAIL_SMALL -> {
                    dim = 50
                }
                IMAGE_SIZE_TUMBNAIL_MEDIUM -> {
                    dim = 100
                }
            }
 */
            Picasso.get()
                .load(dir)
                .resize(dim, dim)
                .centerCrop()
                .into(iv)
            true
        } else{
            try {
                val bm = _BitmapUtil.decode(dir!!)!!
                iv.setImageBitmap(bm)
                true
            } catch (e: Exception){ false }
        }
    }

    fun setPopupMenu(
        anchorView: View, @MenuRes res: Int,
        onMenuItemListener: ((menuItem: MenuItem, pos: Int) -> Boolean)? = null
    ): PopupMenu{
        val popupMenu= PopupMenu(anchorView.context, anchorView)
        popupMenu.inflate(res)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            onMenuItemListener?.invoke(menuItem, menuItem.getPosFrom(popupMenu.menu))
                ?: false
        }
        return popupMenu
    }

    /**
     * Untuk penyesuaian view komponen dalam view template.
     */
    object Comp{
        /*
        =======================
        Area Konstanta
        =======================
         */
        val MODE_NOTE= 1
        val MODE_WARNING= 2

        val TYPE_FILL_TXT_IGNORE= -1
        val TYPE_FILL_TXT_BORDER= 1
        val TYPE_FILL_TXT_UNDERLINE= 2

        /*
        =====================
        get-Lambda Area  --AWAL--
            Knp kok lambda daripada fungsi?
            Agar user dapat mendefinisikan sendiri caranya mencari komponen dalam ViewGroup.
        =====================
         */
        /**
         * Untuk mencari TextView dalam komponen.
         */
        var getTv: ((View) -> TextView?)?= null
            get()= field ?: { it.findViewByType() }

        /**
         * Untuk mencari TextView berupa title dalam komponen.
         */
        var getTvTitle: ((View) -> TextView?)?= null
            get()= field ?: getTv

        /**
         * Untuk mencari TextView berupa desc dalam komponen.
         */
        var getTvDesc: ((View) -> TextView?)?= null
            get()= field ?: getTv

        /**
         * Untuk mencari TextView berupa note dalam komponen.
         */
        var getTvNote: ((View) -> TextView?)?= null
            get()= field ?: getTv

        /**
         * Untuk mencari EditText dalam komponen.
         */
        var getEt: ((View) -> EditText?)?= null
            get()= field ?: { it.findViewByType() }

        /**
         * Untuk mencari EditText atau field yg dapat diisi dalam komponen.
         */
        var getField: ((View) -> View?)?= null
            get()= field ?: getEt

        /**
         * Untuk mencari ImageView dalam komponen.
         */
        var getIv: ((View) -> ImageView?)?= null
            get()= field ?: { it.findViewByType() }

        /**
         * Untuk mencari ImageView yg berupa action pada suatu bar dalam komponen.
         */
        var getIvAction: ((View) -> ImageView?)?= null
            get()= field ?: getIv

        /**
         * Untuk mencari ImageView yg menunjukan indikasi password ditunjukan atau tidak dalam komponen.
         */
        var getIvPswdIndication: ((View) -> ImageView?)?= null
            get()= field ?: getIv

        /**
         * Untuk mencari RecyclerView dalam komponen.
         */
        var getRv: ((View) -> RecyclerView?)?= null
            get()= field ?: { it.findViewByType() }

        /**
         * Untuk mencari ProgressBar dalam komponen.
         */
        var getPb: ((View) -> ProgressBar?)?= null
            get()= field ?: { it.findViewByType() }

        /**
         * Untuk mencari Button dalam komponen.
         */
        var getBtn: ((View) -> Button?)?= null
            get()= field ?: { it.findViewByType() }

        /**
         * Untuk mencari Button dalam komponen.
         */
        var getSfv: ((View) -> SurfaceView?)?= null
            get()= field ?: { it.findViewByType() }

        /**
         * Untuk fungsi kustom sesuai kriteria user.
         * @param compView komponen scr keseluruhan.
         * @param et EditText dalm compView.
         * @param type tipe fill yg diajukan.
         */
        var enableFillTxt: ((compView: View, et: EditText, type: Int) -> Unit)?= null

        /*
        =====================
        get-Lambda Area  --AKHIR--
        =====================
         */
/*
        inline fun <reified V> getDefView(compView: View): V? {
            return if(compView is V) compView
            else null
        }
 */
        inline fun <reified V : View> getDefView(
            compView: View,
            noinline defFunc: ((View) -> V?)? = null
        ): V? {
            return if(compView is V) compView
            else defFunc?.invoke(compView)
                ?: compView.findViewByType()
        }
        /**
         * @param onlyPb bertujuan menghilangkan iv_action yg letaknya sama dg pb
         */
        fun showPb(compView: View, show: Boolean = true, onlyPb: Boolean = false){
            //getPb?.invoke(compView)
            getDefView(compView, getPb).notNull { pb ->
                pb.visibility=
                    if(show) View.VISIBLE
                    else View.GONE

                if(onlyPb)
                    //getIvAction?.invoke(compView)
                    getDefView(compView, getIvAction).notNull { iv ->
                        iv.visibility=
                            if(show) View.GONE
                            else View.VISIBLE
                    }
            }
        }

        fun getTvTxt(compView: View): String?{
            return getDefView(compView, getTv).notNullTo { tv -> tv.text.toString() }
        }
        fun getTvTitleTxt(compView: View): String?{
            return getDefView(compView, getTvTitle).notNullTo { tv -> tv.text.toString() }
        }
        fun getTvDescTxt(compView: View): String?{
            return getDefView(compView, getTvDesc).notNullTo { tv -> tv.text.toString() }
        }
        fun getTvNoteTxt(compView: View): String?{
            return getDefView(compView, getTvNote).notNullTo { tv -> tv.text.toString() }
        }

        fun setTvTxt(compView: View, txt: String){
            getDefView(compView, getTv).notNull { tv -> tv.text= txt }
        }
        fun setTvTitleTxt(compView: View, txt: String){
            getDefView(compView, getTvTitle).notNull { tv -> tv.text= txt }
        }
        fun setTvDescTxt(compView: View, txt: String){
            getDefView(compView, getTvDesc).notNull { tv -> tv.text= txt }
        }
        fun setTvNoteTxt(compView: View, txt: String){
            getDefView(compView, getTvNote).notNull { tv -> tv.text= txt }
        }

        fun setTvNoteMode(compView: View, mode: Int){
            getDefView(compView, getTvNote).notNull { tv ->
                tv.textColorResource= when(mode){
                    MODE_WARNING -> _ColorRes.RED
                    else -> _ColorRes.TEXT_TRANS
                }
            }
        }

        fun getEtTxt(compView: View): String?{
            return getDefView(compView, getEt).notNullTo { et -> et.text.toString() }
        }
        fun getEtHint(compView: View): String?{
            return getDefView(compView, getEt).notNullTo { et -> et.hint.toString() }
        }
        fun setEtTxt(compView: View, str: String){
            getDefView(compView, getEt).notNull { et -> et.setText(str) }
        }
        fun setEtHint(compView: View, str: String){
            getDefView(compView, getEt).notNull { et -> et.hint= str }
        }


        fun enableFillTxt(compView: View, enable: Boolean = true, type: Int = TYPE_FILL_TXT_IGNORE){
            getDefView(compView, getEt).notNull { et ->
                et.isEnabled= enable
                enableFillTxt?.invoke(compView, et, type)
            }
        }


        fun setIvImg(compView: View, @DrawableRes imgRes: Int){
            getDefView(compView, getIv).notNull { iv -> iv.setImageResource(imgRes) }
        }
        fun setIvTint(compView: View, @ColorRes colorRes: Int){
            getDefView(compView, getIv).notNull { iv -> setColorTintRes(iv, colorRes) }
        }

        fun showPassword(compView: View, show: Boolean = true){
            val transfMethod =
                if(!show) PasswordTransformationMethod.getInstance()
                else null

            getDefView(compView, getEt).notNull { et ->
                et.transformationMethod= transfMethod
                et.setSelection(et.text.toString().length)
                getDefView(compView, getIvPswdIndication).notNull { iv ->
                    iv.setImageResource(
                        if (!show) _Config.DRAW_PSWD_SHOWN
                        else _Config.DRAW_PSWD_HIDDEN
                    )
                }
            }
        }

        fun setBtnHollow(compView: View){
            getDefView(compView, getBtn).notNull { btn -> setBtnHollow(btn) }
        }
        fun setBtnHollow(btn: Button){
            btn.setBackgroundResource(_Config.DRAW_SHAPE_BORDER_ROUND) //R.drawable.shape_border_square_round_edge_main
            btn.setTextColor(_ResUtil.getColor(btn.context, _ColorRes.COLOR_PRIMARY_DARK))
        }

        fun setBtnSolid(compView: View){
            getDefView(compView, getBtn).notNull { btn -> setBtnSolid(btn) }
        }
        fun setBtnSolid(btn: Button){
            btn.setBackgroundResource(_Config.DRAW_SHAPE_SOLID_RECT_ROUND_EDGE) //R.drawable.shape_solid_square_round_edge_fill
            setBgColorTintRes(btn, _ColorRes.COLOR_PRIMARY_DARK)
            btn.setTextColor(_ResUtil.getColor(btn.context, _ColorRes.TEXT_LIGHT))
        }

        fun initPasswordField(compView: View){
            getDefView(compView, getEt).notNull { ed ->
                var isPswdShown= true
                ed.setOnClickListener {
                    isPswdShown= !isPswdShown
                    showPassword(ed, isPswdShown)
                }
                if(Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
                    ed.performClick()
                else
                    ed.callOnClick()
            }
        }

        /**
         * <24 Juni 2020> Sementara, hanya dapat digunakan untuk kamera belakang model lama, yaitu hanya satu.
         * @return object Camera agar dapat digunakan.
         *          null jika gagal untuk meng-init SurfaceView.
         */
        fun initSurfaceView(
            compView: View,
            callback: SurfaceHolder.Callback?,
            act: Activity? = null
        ): Camera? {
            if(act != null)
                _ManifestUtil.requestPermission(act, Manifest.permission.CAMERA)
            return getDefView(compView, getSfv).notNullTo { sfv ->
                val camera= Camera.open()
                val callbackInner= callback
                    ?: object : SurfaceHolder.Callback{
                    override fun surfaceCreated(holder: SurfaceHolder?) {
                        getViewSize(sfv){ sfvW, sfvH ->
                            val params= camera.parameters
                            val viewRatio= sfvH / sfvW.toDouble()

                            val sizes= params.supportedPreviewSizes
                            val point= Point()
                            var minDiff= Double.MAX_VALUE
                            //Cari rasio supported yg paling mendekati dg rasio ukuran SurfaceView
                            for(size in sizes){
                                val w= size.width
                                val h= size.height
                                val ratio= w /h.toDouble()

                                val diff= abs(viewRatio - ratio)
                                if(minDiff > diff){
                                    point.x= w
                                    point.y= h
                                    minDiff= diff
                                }
                            }
                            params.setPreviewSize(point.x, point.y)

                            camera.parameters= params

                            try {
                                // The Surface has been created, now tell the camera where to draw
                                // the preview.
                                camera.setDisplayOrientation(90)
                                camera.setPreviewDisplay(holder)
                                camera.startPreview()
                            } catch (e: Exception) {
                                // check for exceptions
                                System.err.println(e)
                            }
                        }
                    }
                    override fun surfaceChanged(
                        holder: SurfaceHolder?,
                        format: Int,
                        width: Int,
                        height: Int
                    ) {}
                    override fun surfaceDestroyed(holder: SurfaceHolder?) {
                        camera.stopPreview()
                        camera.release()
                    }
                }
                sfv.holder.addCallback(callbackInner)
                camera
            }
        }

        /**
         * @param compViews berisi sederet view yg saling terhubung untuk membentuk input kode OTP
         */
        fun initSingleCodeInput(vararg compViews: View){
            val lastIndex= compViews.lastIndex
            for((i, comp) in compViews.withIndex()){
                val edPrev= try{ getDefView(compViews[i - 1], getEt) }
                    catch (e: Exception){ null }
                val edNext= try{ getDefView(compViews[i - 1], getEt) }
                    catch (e: Exception){ null }
                val ed= getDefView(comp, getEt)
                val c= ed?.context

                ed?.addTextChangedListener(object : TextWatcher {
                    var isTextEdited = false
                    var newlyInputedTxt = ""
                    override fun afterTextChanged(s: Editable?) {
                        if (!isTextEdited && s != null) {
                            isTextEdited = true
                            if (s.isEmpty()) {
                                if (newlyInputedTxt.isNotEmpty() && newlyInputedTxt[0].isLetterOrDigit())
                                    s.append(newlyInputedTxt[0])
                                else if (edPrev != null) {
                                    edPrev.requestFocus()
                                    val imm =
                                        c?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                                    imm?.showSoftInput(edPrev, InputMethodManager.SHOW_IMPLICIT)
                                }
                            } else if (s.length == 1 && (s.isBlank() || !s[0].isLetterOrDigit())) {
                                s.clear()
                                if (newlyInputedTxt[0].isLetterOrDigit())
                                    s.append(newlyInputedTxt[0])
                            } else if (s.length > 1) {
                                if (newlyInputedTxt[0].isLetterOrDigit()) {
                                    s.clear()
                                    s.append(newlyInputedTxt[0])
                                } else {
                                    val str =
                                        s.toString().replace(newlyInputedTxt[0].toString(), "")
                                    s.clear()
                                    s.append(str)
                                }
                            }

                            if (s.isNotBlank() && s.length == 1) {
                                if (i == lastIndex) {
                                    val imm =
                                        c?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                                    imm?.hideSoftInputFromWindow(ed.windowToken, 0)
                                } else if (edNext != null) {
                                    edNext.requestFocus()
                                    val imm =
                                        c?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                                    imm?.showSoftInput(edNext, InputMethodManager.SHOW_IMPLICIT)
                                }
                            }
                            isTextEdited = false
                        }
                    }

                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                        if (!isTextEdited) {
                            newlyInputedTxt =
                                if (start > 0 || count == 1) {
                                    val str = s?.toString()?.substring(start, start + count)
                                        ?: newlyInputedTxt
                                    if (str[0].isLetterOrDigit())
                                        str
                                    else newlyInputedTxt
                                } else {
                                    if (s?.isNotEmpty() == true) s.toString().last().toString()
                                    else ""
                                }
                        }
                    }
                })
            }
        }
    }

    /**
     * Untuk templating view dari SiFrame.
     */
    object Template{
        object Type{
            val ACT_BAR_DEFAULT= 0
            val ACT_BAR_SQUARE= 1
        }
        fun actBar_Primary(c: Context, type: Int = Type.ACT_BAR_DEFAULT): View {
            val actBar= c.inflate(_Config.LAYOUT_COMP_ACT_BAR_DEFAULT) as ViewGroup
            val bg= c.inflate(_Config.LAYOUT_BG_COMP_ACT_BAR_DEFAULT) as ImageView
            if(type == Type.ACT_BAR_SQUARE)
                bg.setImageResource(_ColorRes.COLOR_PRIMARY_DARK)

            val tvTitle= actBar.findViewById<TextView>(_Config.ID_TV_TITLE)
            val ivBack= actBar.findViewById<ImageView>(_Config.ID_IV_BACK)
            val ivAction= actBar.findViewById<ImageView>(_Config.ID_IV_ACTION)

            actBar.removeViewAt(0)
            actBar.addView(bg, 0)
/*
            actBar.layoutParams= RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                _ResUtil.getDimen(c, _ConfigBase.DIMEN_ACT_BAR_HEIGHT).toInt()
            )
 */
            setColorTintRes(bg, _ColorRes.COLOR_PRIMARY_DARK)
            setColorTintRes(ivBack, _ColorRes.COLOR_LIGHT)
            setColorTintRes(ivAction, _ColorRes.COLOR_LIGHT)
            tvTitle.textColorResource= _ColorRes.TEXT_LIGHT

            ivAction.setImageBitmap(null) //Secara default, gambar iv_action tidak ada.

            return actBar
        }

        fun overlayBlock(c: Context): View{
            val overlay= c.inflate(_Config.LAYOUT_OVERLAY_BLOCK)!!
            val iv= overlay.findViewById<ImageView>(_Config.ID_IV_ICON)
            val tvTitle= overlay.findViewById<TextView>(_Config.ID_TV_TITLE)
            val tvDesc= overlay.findViewById<TextView>(_Config.ID_TV_DESC)

            iv.setImageResource(_Config.DRAW_IC_WARNING)
            setColorTintRes(iv, _ColorRes.RED)
            tvTitle.text= "Mohon Maaf"
            tvDesc.text= "Aplikasi telah kadaluwarsa.\nMohon update ke versi terbaru."

            return overlay
        }
    }

    /**
     * Kelas untuk chaining saat pemanggilan [View.animator].
     */
    class SimpleAnimator(val v: View): Animation(){
        fun fadeIn(duration: Long = 1000): SimpleAnimator{
            fadeIn(v, duration = duration)
            return this
        }
        fun fadeOut(duration: Long = 1000): SimpleAnimator{
            fadeOut(v, duration = duration)
            return this
        }
        fun fadeTo(to: Float, from: Float = v.alpha, duration: Long = 1000): SimpleAnimator{
            fade(from, to, v, duration = duration)
            return this
        }
    }
}