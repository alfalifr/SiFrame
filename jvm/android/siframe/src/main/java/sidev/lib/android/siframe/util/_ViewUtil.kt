package sidev.lib.android.siframe.util

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.res.Resources
import android.graphics.Point
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.ViewGroup
import android.transition.ChangeBounds
import android.transition.TransitionSet
import android.transition.TransitionManager
import android.util.DisplayMetrics
import android.util.TypedValue
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import org.jetbrains.anko.layoutInflater

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
        for(i in 0 until et.size){
            if(et[i].text.toString().equals("", ignoreCase = true))
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
}