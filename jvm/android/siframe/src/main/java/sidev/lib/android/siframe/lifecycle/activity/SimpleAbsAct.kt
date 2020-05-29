package sidev.lib.android.siframe.lifecycle.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.CallSuper
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import sidev.lib.android.siframe.customizable._init._ConfigBase
import sidev.lib.android.siframe.intfc.listener.OnBackPressedListener
import sidev.lib.android.siframe.intfc.view.BackBtnActView
import sidev.lib.android.siframe.intfc.view.SimpleAbsActFragView

/**
 * Kelas dasar dalam framework yang digunakan sbg kelas Activity utama
 * sbg pengganti dari AppCompatActivity
 */
abstract class SimpleAbsAct : AppCompatActivity(), SimpleAbsActFragView, BackBtnActView {
    override val actBackBtn: Activity
        get() = this
    override var backBtnViewList= ArrayList<View>()
    override var onBackPressedListenerList: ArrayList<OnBackPressedListener>
        = ArrayList()

    override val styleId: Int
        get() = _ConfigBase.STYLE_APP
    override lateinit var layoutView: View
    open val isViewInitFirst= true

//    private val onDestroyListenerQueue= RunQueue<Any?, Unit_>()


    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        setStyle(this)
        super.onCreate(savedInstanceState)
        setContentView(layoutId)

        Log.e("SimpleAbsAct", "onCreate isViewInitFirst= $isViewInitFirst name= ${this::class.java.simpleName}")

        val v= findViewById<View>(android.R.id.content).getRootView()
        layoutView= v

        if(isViewInitFirst){
            Log.e("SimpleAbsAct", "::layoutView.isInitialized ${::layoutView.isInitialized} name= ${this::class.java.simpleName}")
            initView_int(v)
            initView(v)
        }
    }

    override fun <D> getIntentData(key: String, i: Intent?, default: D?): D {
        return if(i != null) super.getIntentData(key, i, default)
        else intent?.extras?.get(key) as D? ?: default as D
    }


    fun updateActBarCustomView(layoutId: Int){
        val actBarView= layoutInflater.inflate(layoutId, null, false)
        updateActBarCustomView(actBarView)
    }
    fun updateActBarCustomView(v: View){
//        if(supportActionBar().customView == null){
        val actBar= supportActionBar
        if(actBar != null){
//            val actBarView= layoutInflater.inflate(layoutId, null, false)
            actBar.customView= v
            actBar.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
            actBar.setDisplayShowCustomEnabled(true)
            actBar.setCustomView(v,
                ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT))

            (actBar.customView.parent as Toolbar).setContentInsetsAbsolute(0,0)
//            supportActionBar().hide()
//            supportActionBar()
        }
    }
    fun actBar(): View?{
        return supportActionBar?.customView
    }

    @CallSuper
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        onActResultListener?.onActResult(requestCode, resultCode, data)
    }

    var onActResultListener: OnActResultListener?= null
    interface OnActResultListener{
        fun onActResult(reqCode: Int, resCode: Int, data: Intent?)
    }
    fun setOnActResultListener(l: (reqCode: Int, resCode: Int, data: Intent?) -> Unit){
        onActResultListener= object : OnActResultListener {
            override fun onActResult(reqCode: Int, resCode: Int, data: Intent?) {
                l(reqCode, resCode, data)
            }
        }
    }

    @CallSuper
    override fun onBackPressed() {
        if(!isBackPressedHandled())
            super.onBackPressed()
    }

    @CallSuper
    override fun onDestroy() {
        super.onDestroy()
//        onDestroyListenerQueue.iterateRunQueue(null)
        Log.e("SimpleAbsAct", "Activity ${this::class.java.simpleName} is destroyed!!!")
    }

    /*
    private val onBackPressedListenerList= ArrayList<OnBackPressedListener>()
    interface OnBackPressedListener{
        fun onBackPressed_(): Boolean
    }
    fun addOnBackPressedListener(l: () -> Boolean){
        onBackPressedListenerList.add(object: OnBackPressedListener{
            override fun onBackPressed_(): Boolean {
                return l()
            }
        })
    }
    private fun iterateOnBackPressedListener(): Boolean{
        var bool= true
        for(l in onBackPressedListenerList)
            bool= bool && l.onBackPressed_()
        return bool
    }
 */
}