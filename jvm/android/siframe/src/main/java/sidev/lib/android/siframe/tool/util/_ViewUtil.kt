package sidev.lib.android.siframe.tool.util

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.ViewGroup
import android.transition.ChangeBounds
import android.transition.TransitionSet
import android.transition.TransitionManager
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.MenuRes
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import org.jetbrains.anko.layoutInflater
import org.jetbrains.anko.textColorResource
import sidev.lib.android.siframe.customizable._init._ColorRes
import sidev.lib.android.siframe.customizable._init._Config
import sidev.lib.android.siframe.model.PictModel
import sidev.lib.android.siframe.tool.util.`fun`.getPosFrom
import sidev.lib.android.siframe.tool.util.`fun`.inflate
import sidev.lib.universal.`fun`.notNull
import sidev.lib.universal.`fun`.notNullTo
import sidev.lib.universal.tool.util.FileUtil
import java.lang.NullPointerException
import kotlin.Exception

//class ini digunakan tempat utility khusus view
object  _ViewUtil{
    val DURATION_ANIMATION_DEFAULT : Long = 200
    val DURATION_ANIMATION_LONG : Long = 500

    // Untuk mengubah ukuran dari DP ke PX
    fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics).toInt()
    }

    fun dpToPx(dp : Int): Int {
        return (dp*Resources.getSystem().displayMetrics.density).toInt()
    }

    fun getScreenWidth(act: Activity) : Int {
        val dm = DisplayMetrics()
        act.windowManager.defaultDisplay.getMetrics(dm)
        return dm.widthPixels
    }
    fun getScreenHeight(act: Activity): Int{
        val dm = DisplayMetrics()
        act.windowManager.defaultDisplay.getMetrics(dm)
        return dm.heightPixels
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
        view.layoutParams.width= width
        view.layoutParams.height= height
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
    fun changeViewDimen(v : ViewGroup, newDimension : Int) {
        TransitionManager.beginDelayedTransition(
            v, TransitionSet()
                .addTransition(ChangeBounds())
        )

        val params = v.layoutParams
        params.height = newDimension
        v.layoutParams = params
    }

    // Untuk merubah transparansi background drawable layout
    fun setBackgroundAlpha(v : View, level : Int) {
        val skipBackground = v.background as Drawable
        skipBackground.alpha = ((level.toDouble()/100) * 255).toInt()
    }

    // Untuk merubah stroke color dari view background
    fun changeDrawableStrokeColor(color : Int, context: Context, vararg v : View){
        for(i in 0 until v.size){
            val vDrawable = v[i].background as GradientDrawable
            vDrawable.setStroke(dpToPx(3), ContextCompat.getColor(context, color))
        }
    }

    // untuk merubah warna dari text
    fun changeTextColor(color : Int, context: Context, vararg tv : TextView){
        for(i in 0 until tv.size){
            tv[i].setTextColor(ContextCompat.getColor(context, color))
        }
    }

    /**
     * Untuk mengambil warna sesuai nilai asli yang diambil dari <code>colorResId</code>
     * @param colorResId --> R.color...
     * @return nilai int dari kode warna yang sebernannya
     */
    fun getColor(c: Context, colorResId: Int): Int{
        return if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            c.resources.getColor(colorResId)
        else
            c.resources.getColor(colorResId, null)
    }

    // Untuk Fade Out View
    fun fadeOut(duration : Long, vararg v : View) {
        for(i in 0 until v.size) {
            val alphaAnim  = AlphaAnimation(1.00f, 0.00f)
            alphaAnim.duration = duration
            alphaAnim.setAnimationListener(object : Animation.AnimationListener{
                override fun onAnimationRepeat(animation: Animation?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onAnimationEnd(animation: Animation?) {
                    v[i].visibility = View.GONE
                }

                override fun onAnimationStart(animation: Animation?) {
                    v[i].visibility = View.VISIBLE
                }

            })

            v[i].animation = alphaAnim
        }
    }

    // Untuk Fade In View
    fun fadeIn(duration : Long, vararg v : View) {
        for(i in 0 until v.size){
            val alphaAnim  = AlphaAnimation(0.00f, 1.00f)
            alphaAnim.duration = duration
            alphaAnim.setAnimationListener(object : Animation.AnimationListener{
                override fun onAnimationRepeat(animation: Animation?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onAnimationEnd(animation: Animation?) {
                    v[i].visibility = View.VISIBLE
                }

                override fun onAnimationStart(animation: Animation?) {
                    v[i].visibility = View.GONE
                }

            })

            v[i].animation = alphaAnim
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
    fun loadFragment(fragmentManager : FragmentManager, fragment : Fragment, container : Int, tag : String){
        fragmentManager
                .beginTransaction()
                .replace(container, fragment, tag)
                .commit()
    }

    fun <N: Number> extractNumberFromEd(ed: EditText, defaultValue: Number= 0): N{
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
    fun getViewSize(act: Activity?, view: View): Array<Int>{
        val res= Array(2){0}
        if(act != null){
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


    fun setColor(iv: ImageView, @ColorRes colorId: Int){ //, blendMode: BlendMode= BlendMode.SRC_ATOP){
        val color= _ResUtil.getColor(iv.context, colorId)
        DrawableCompat.setTint(iv.drawable, color)
//        iv.drawable.colorFilter = BlendModeColorFilter(color, blendMode) //Masih error karena masalah kompatibilitas API
    }
    fun setBgColor(v: View, @ColorRes colorId: Int){ //, blendMode: BlendMode= BlendMode.SRC_ATOP){
        val color= _ResUtil.getColor(v.context, colorId)
        try{
            DrawableCompat.setTint(v.background, color)
        } catch (e: NullPointerException){
            v.setBackgroundColor(color)
        }
//        v.background.colorFilter = BlendModeColorFilter(color, blendMode) //Masih error karena masalah kompatibilitas API
    }


    fun setImg(iv: ImageView, img: PictModel?): Boolean{
        return if(img != null){
            when {
                img.bm != null -> {
                    iv.setImageBitmap(img.bm)
                    true
                }
                img.dir != null -> loadImageToImageView(iv, img.dir)
                else -> false
            }
        } else false
    }

    fun loadImageToImageView(iv: ImageView, /*ivSizeType: Int, */url: String?): Boolean{
        val isDirLocal= try{ FileUtil.isDirLocal(url!!) }
        catch(e: Exception){ false }

        val dir=
            if (isDirLocal) url
            else _Config.ENDPOINT_ROOT + url

//        Log.e("SERVER_UTIL", "isDirLocal= $isDirLocal url= $url dir= $dir")

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

    fun setPopupMenu(anchorView: View, @MenuRes res: Int,
                     onMenuItemListener: ((menuItem: MenuItem, pos: Int) -> Boolean)?= null): PopupMenu{
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

        /**
         * Untuk mencari EditText atau field yg dapat diisi dalam komponen.
         */
        var getField: ((View) -> View?)?= null
            get()= field ?: getEt

        /**
         * Untuk mencari ImageView dalam komponen.
         */
        var getIv: ((View) -> ImageView?)?= null

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

        /**
         * Untuk mencari ProgressBar dalam komponen.
         */
        var getPb: ((View) -> ProgressBar?)?= null

        /**
         * Untuk mencari Button dalam komponen.
         */
        var getBtn: ((View) -> Button?)?= null

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

        /**
         * @param onlyPb bertujuan menghilangkan iv_action yg letaknya sama dg pb
         */
        fun showPb(compView: View, show: Boolean= true, onlyPb: Boolean= false){
            getPb?.invoke(compView).notNull { pb ->
                pb.visibility=
                    if(show) View.VISIBLE
                    else View.GONE

                if(onlyPb)
                    getIvAction?.invoke(compView).notNull { iv ->
                        iv.visibility=
                            if(show) View.GONE
                            else View.VISIBLE
                    }
            }
        }

        fun getTvTxt(compView: View): String?{
            return getTv?.invoke(compView).notNullTo { tv ->
                tv.text.toString()
            }
        }
        fun getTvTitleTxt(compView: View): String?{
            return getTvTitle?.invoke(compView).notNullTo { tv ->
                tv.text.toString()
            }
        }
        fun getTvDescTxt(compView: View): String?{
            return getTvDesc?.invoke(compView).notNullTo { tv ->
                tv.text.toString()
            }
        }
        fun getTvNoteTxt(compView: View): String?{
            return getTvNote?.invoke(compView).notNullTo { tv ->
                tv.text.toString()
            }
        }

        fun setTvTxt(compView: View, txt: String){
            getTv?.invoke(compView).notNull { tv -> tv.text= txt }
        }
        fun setTvTitleTxt(compView: View, txt: String){
            getTvTitle?.invoke(compView).notNull { tv -> tv.text= txt }
        }
        fun setTvDescTxt(compView: View, txt: String){
            getTvDesc?.invoke(compView).notNull { tv -> tv.text= txt }
        }
        fun setTvNoteTxt(compView: View, txt: String){
            getTvNote?.invoke(compView).notNull { tv -> tv.text= txt }
        }

        fun setTvNoteMode(compView: View, mode: Int){
            getTvNote?.invoke(compView).notNull { tv ->
                tv.textColorResource= when(mode){
                    MODE_WARNING -> _ColorRes.RED
                    else -> _ColorRes.TEXT_TRANS
                }
            }
        }

        fun getEtTxt(compView: View): String?{
            return getEt?.invoke(compView).notNullTo { et ->
                et.text.toString()
            }
        }
        fun getEtHint(compView: View): String?{
            return getEt?.invoke(compView).notNullTo { et ->
                et.hint.toString()
            }
        }
        fun setEtTxt(compView: View, str: String){
            getEt?.invoke(compView).notNull { et ->
                et.setText(str)
            }
        }
        fun setEtHint(compView: View, str: String){
            getEt?.invoke(compView).notNull { et ->
                et.hint= str
            }
        }


        fun enableFillTxt(compView: View, enable: Boolean= true, type: Int= TYPE_FILL_TXT_IGNORE){
            getEt?.invoke(compView).notNull { et ->
                et.isEnabled= enable
                enableFillTxt?.invoke(compView, et, type)
            }
        }


        fun setIvImg(compView: View, @DrawableRes imgRes: Int){
            getIv?.invoke(compView).notNull { iv -> iv.setImageResource(imgRes) }
        }
        fun setIvTint(compView: View, @ColorRes colorRes: Int){
            getIv?.invoke(compView).notNull { iv -> setColor(iv, colorRes) }
        }

        fun showPassword(compView: View, show: Boolean= true){
            val transfMethod =
                if(!show) PasswordTransformationMethod.getInstance()
                else null
            getEt?.invoke(compView).notNull { ed ->
                ed.transformationMethod= transfMethod
                ed.setSelection(ed.text.toString().length)
                getIvPswdIndication?.invoke(compView).notNull { iv ->
                    iv.setImageResource(
                        if(!show) _Config.DRAW_PSWD_SHOWN
                        else _Config.DRAW_PSWD_HIDDEN
                    )
                }
            }
        }

        fun setBtnHollow(compView: View){
            getBtn?.invoke(compView).notNull { btn ->
                setBtnHollow(btn)
            }
        }
        fun setBtnHollow(btn: Button){
            btn.setBackgroundResource(_Config.DRAW_SHAPE_BORDER_ROUND) //R.drawable.shape_border_square_round_edge_main
            btn.setTextColor(_ResUtil.getColor(btn.context, _ColorRes.COLOR_PRIMARY_DARK))
        }

        fun setBtnSolid(compView: View){
            getBtn?.invoke(compView).notNull { btn ->
                setBtnSolid(btn)
            }
        }
        fun setBtnSolid(btn: Button){
            btn.setBackgroundResource(_Config.DRAW_SHAPE_SOLID_SQUARE_ROUND) //R.drawable.shape_solid_square_round_edge_fill
            setBgColor(btn, _ColorRes.COLOR_PRIMARY_DARK)
            btn.setTextColor(_ResUtil.getColor(btn.context, _ColorRes.TEXT_LIGHT))
        }

        fun initPasswordField(compView: View){
            getEt?.invoke(compView).notNull { ed ->
                var isPswdShown= true
                ed.setOnClickListener {
                    isPswdShown= !isPswdShown
                    showPassword(ed, isPswdShown)
                }
                ed.callOnClick()
            }
        }

        /**
         * @param compViews berisi sederet view yg saling terhubung untuk membentuk input kode OTP
         */
        fun initSingleCodeInput(vararg compViews: View){
            val lastIndex= compViews.lastIndex
            for((i, comp) in compViews.withIndex()){
                val edPrev= try{ getEt?.invoke(compViews[i-1]) }
                catch (e: Exception){ null }
                val edNext= try{ getEt?.invoke(compViews[i+1]) }
                catch (e: Exception){ null }
                val ed= getEt?.invoke(comp)
                val c= ed?.context

                ed?.addTextChangedListener(object : TextWatcher {
                    var isTextEdited= false
                    var newlyInputedTxt= ""
                    override fun afterTextChanged(s: Editable?) {
                        if(!isTextEdited && s != null){
                            isTextEdited= true
                            if(s.isEmpty()){
                                if(newlyInputedTxt.isNotEmpty() && newlyInputedTxt[0].isLetterOrDigit())
                                    s.append(newlyInputedTxt[0])
                                else if(edPrev != null){
                                    edPrev.requestFocus()
                                    val imm=
                                        c?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                                    imm?.showSoftInput(edPrev, InputMethodManager.SHOW_IMPLICIT)
                                }
                            } else if(s.length == 1 && (s.isBlank() || !s[0].isLetterOrDigit())){
                                s.clear()
                                if(newlyInputedTxt[0].isLetterOrDigit())
                                    s.append(newlyInputedTxt[0])
                            } else if(s.length > 1){
                                if(newlyInputedTxt[0].isLetterOrDigit()){
                                    s.clear()
                                    s.append(newlyInputedTxt[0])
                                } else {
                                    val str= s.toString().replace(newlyInputedTxt[0].toString(), "")
                                    s.clear()
                                    s.append(str)
                                }
                            }

                            if(s.isNotBlank() && s.length == 1){
                                if(i == lastIndex){
                                    val imm=
                                        c?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                                    imm?.hideSoftInputFromWindow(ed.windowToken, 0)
                                } else if(edNext != null){
                                    edNext.requestFocus()
                                    val imm=
                                        c?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                                    imm?.showSoftInput(edNext, InputMethodManager.SHOW_IMPLICIT)
                                }
                            }
                            isTextEdited= false
                        }
                    }
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                        Log.e("VIEW_UTIL", "BEFORE----EARLIEST==== newlyInputedTxt= $newlyInputedTxt s= $s start= $start count= $count")
                        if(!isTextEdited){
//                            Log.e("VIEW_UTIL", "BEFORE==== newlyInputedTxt= $newlyInputedTxt s= $s start= $start count= $count")
                            newlyInputedTxt=
                                if(start > 0 || count == 1) {
                                    val str= s?.toString()?.substring(start, start +count) ?: newlyInputedTxt
                                    if(str[0].isLetterOrDigit())
                                        str
                                    else newlyInputedTxt
                                } else {
                                    if(s?.isNotEmpty() == true) s.toString().last().toString()
                                    else ""
                                }
                            Log.e("VIEW_UTIL", "newlyInputedTxt= $newlyInputedTxt s= $s start= $start count= $count")
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
        fun actBar_Primary(c: Context, type: Int= Type.ACT_BAR_DEFAULT): View {
            val actBar= c.inflate(_Config.LAYOUT_COMP_ACT_BAR_DEFAULT) as ViewGroup
            val bg= c.inflate(_Config.LAYOUT_BG_COMP_ACT_BAR_DEFAULT) as ImageView
            if(type == Type.ACT_BAR_SQUARE)
                bg.setImageResource(_ColorRes.COLOR_PRIMARY_DARK)

            val tvTitle= actBar.findViewById<TextView>(_Config.ID_TV_TITLE)
            val ivBack= actBar.findViewById<ImageView>(_Config.ID_IV_BACK)
            
            actBar.removeViewAt(0)
            actBar.addView(bg, 0)
/*
            actBar.layoutParams= RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                _ResUtil.getDimen(c, _ConfigBase.DIMEN_ACT_BAR_HEIGHT).toInt()
            )
 */
            setColor(bg, _ColorRes.COLOR_PRIMARY_DARK)
            setColor(ivBack, _ColorRes.COLOR_LIGHT)
            tvTitle.textColorResource= _ColorRes.TEXT_LIGHT

            return actBar
        }

        fun overlayBlock(c: Context): View{
            val overlay= c.inflate(_Config.LAYOUT_OVERLAY_BLOCK)!!
            val iv= overlay.findViewById<ImageView>(_Config.ID_IV_ICON)
            val tvTitle= overlay.findViewById<TextView>(_Config.ID_TV_TITLE)
            val tvDesc= overlay.findViewById<TextView>(_Config.ID_TV_DESC)

            iv.setImageResource(_Config.DRAW_IC_WARNING)
            setColor(iv, _ColorRes.RED)
            tvTitle.text= "Mohon Maaf"
            tvDesc.text= "Aplikasi telah kadaluwarsa.\nMohon update ke versi terbaru."

            return overlay
        }
    }
}