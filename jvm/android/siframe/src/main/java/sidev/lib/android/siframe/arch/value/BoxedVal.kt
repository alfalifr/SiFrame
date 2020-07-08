package sidev.lib.android.siframe.arch.value

/**
 * Versi ringan untuk wadah suatu nilai.
 */
class BoxedVal<T>{
    var value: T?= null

    override fun equals(other: Any?): Boolean {
        return if(other is BoxedVal<*>) value == other.value
        else this === other
    }

    fun copy(): BoxedVal<T>{
        val box= BoxedVal<T>()
        box.value= value
        return box
    }
}