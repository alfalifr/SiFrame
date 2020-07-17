package sidev.lib.android.siframe.arch.value

import android.util.SparseArray
import com.google.gson.Gson
import java.lang.Exception

/**
 * Versi ringan untuk wadah suatu nilai.
 */
open class BoxedVal<T>{
    var value: T?= null

    override fun equals(other: Any?): Boolean {
        return if(other is BoxedVal<*>) value == other.value
        else this === other
    }

    fun copy(): BoxedVal<T>{
        val box= BoxedVal<T>()
//        val aa= SparseArra
        box.value= value//try{ Gson()(value as Object).clone() as? T } catch (e: Exception){ value }
        return box
    }
}