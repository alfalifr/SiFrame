package sidev.lib.android.siframe.view

import android.content.Context
import android.text.TextWatcher
import android.util.AttributeSet
import sidev.lib.android.siframe.intfc.customview.CustomView
import sidev.lib.check.notNullTo

open class ModEt: androidx.appcompat.widget.AppCompatEditText, CustomView{
    private var mListener: ArrayList<TextWatcher>?= null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    /**
     * Untuk menambahkan hanya satu [TextWatcher] saja pada view ini.
     */
    fun addOnceTextChangedListener(watcher: TextWatcher){
        clearTextChangedListener()
        addTextChangedListener(watcher)
    }

    override fun addTextChangedListener(watcher: TextWatcher?) {
        super.addTextChangedListener(watcher)
        if(mListener == null) mListener= ArrayList()
        if(watcher != null)
            mListener!!.add(watcher)
    }

    override fun removeTextChangedListener(watcher: TextWatcher?) {
        super.removeTextChangedListener(watcher)
        mListener?.remove(watcher)
    }

    /**
     * @return true jika [mListener] tidak null dan ada isinya.
     */
    fun clearTextChangedListener(): Boolean{
        return mListener.notNullTo { listeners ->
            val bool= listeners.isNotEmpty()
            for(l in listeners)
                removeTextChangedListener(l)
            listeners.clear()
            mListener= null
            bool
        } ?: false
    }
}