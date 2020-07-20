package sidev.lib.universal.structure.data

/**
 * Versi ringan untuk wadah suatu nilai.
 */
open class BoxedVal<T>(){
    constructor(value: T?): this(){ this.value= value }
    open var value: T?= null

    override fun equals(other: Any?): Boolean {
        return if(other is BoxedVal<*>) value == other.value
        else this === other
    }
/*
    override fun hashCode(): Int {
        var result = value?.hashCode() ?: 0
        result = 31 * result + (vala?.hashCode() ?: 0)
        return result
    }
 */
    override fun hashCode(): Int {
        return value?.hashCode() ?: super.hashCode()
    }

    override fun toString(): String
            = "${this::class.simpleName}(value=$value)"
/*
    fun copy(): BoxedVal<T>{
        val box= BoxedVal<T>()
//        val aa= SparseArra
        box.value= value//try{ Gson()(value as Object).clone() as? T } catch (e: Exception){ value }
        return box
    }
 */
}