package com.sigudang.android.utilities.view

import android.app.Activity
import android.content.Context
import android.content.res.Resources
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
import android.util.Log
import android.util.TypedValue
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.sigudang.android.Model.ViewDimension
import android.view.animation.TranslateAnimation
import android.widget.Toast
import sidev.lib.implementation._simulation.sigudang.interfaces.CallBack

//import com.sigudang.android.interfaces.Callback

//class ini digunakan tempat utility khusus view
object ViewUtil{
    val DURATION_ANIMATION_DEFAULT : Long = 200
    val DURATION_ANIMATION_LONG : Long = 500

    // Untuk mengubah ukuran dari DP ke PX
    fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics).toInt()
    }

    fun dpToPx(dp : Int): Int {
        return (dp*Resources.getSystem().displayMetrics.density).toInt()
    }

    // Untuk mengubah type face di navigation view
    /*
    open fun applyFontToMenuItem(menuItem : MenuItem, fontStr : String, context : Context) {
        val font = Typeface.createFromAsset(context.assets, fontStr)
        val text = SpannableString(menuItem.title)
        text.setSpan(TypefaceFont("", font), 0, text.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
    } */

    fun getScreenWidth(act: Activity) : Int {
        val dm = DisplayMetrics()
        act.getWindowManager().getDefaultDisplay().getMetrics(dm)
        val lebar = dm.widthPixels

        return lebar
    }

    fun setViewHeight(view: View, height: Int){
        view.layoutParams.height = height
    }

    fun calculateNoOfColumns(
        context: Context,
        columnWidthDp: Float
    ): Int { // For example columnWidthdp=180
        val displayMetrics = context.resources.displayMetrics
        val screenWidthDp = displayMetrics.widthPixels / displayMetrics.density
        return (screenWidthDp / columnWidthDp + 0.5).toInt()
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

    // Untuk merubah dimensi dengan transisi
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun changeViewDimen(viewGroup : ViewGroup, v : View, dimen : ViewDimension) {
        TransitionManager.beginDelayedTransition(
            viewGroup, TransitionSet()
                .addTransition(ChangeBounds()).setDuration(500)
        )

        v.requestLayout()

        val params = v.layoutParams as ViewGroup.MarginLayoutParams

        if(dimen.height != -1) params.height = dimen.height
        if(dimen.width != -1) params.width = dimen.width
        if(dimen.marginLeft != -1) params.leftMargin = dimen.marginLeft
        if(dimen.marginRight != -1) params.rightMargin = dimen.marginRight
        if(dimen.marginTop != -1) params.topMargin = dimen.marginTop
        if(dimen.marginBottom != -1) params.bottomMargin = dimen.marginBottom

        Log.d("change dimen status : ", " Ok")
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

    // Untuk menambahkan fragment
    fun loadFragment(fragmentManager : FragmentManager, fragment : Fragment, container : Int, tag : String){
        fragmentManager
                .beginTransaction()
                .replace(container, fragment, null)
                .commit()
    }


    //
    fun slideToBottom(view: View, duration: Long, callBack: CallBack) {
        val animate = TranslateAnimation(0f, 0f, 0f, view.height.toFloat())
        animate.duration = duration
        //animate.fillAfter = true

        animate.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationRepeat(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                callBack.response("", 0)
            }

            override fun onAnimationStart(animation: Animation?) {}

        })

        view.startAnimation(animate)
        view.visibility = View.GONE
    }

    fun slideToTop(view: View, duration: Long) {
        val animate = TranslateAnimation(0f, 0f, view.height.toFloat(), 0f)
        animate.duration = 500
        animate.fillAfter = true
        view.startAnimation(animate)
        view.visibility = View.VISIBLE
    }

    fun toast(c: Context, msg: String, ln: Int= Toast.LENGTH_LONG){
        Toast.makeText(c, msg, ln).show()
    }
}