package sidev.lib.universal.structure.data

import sidev.lib.universal.structure.prop.TagProp

open class TaggedBoxedVal<T, V>() : BoxedVal<V>(), TagProp<T>{
    constructor(tag: T?= null, value: V?= null): this(){
        this.tag= tag
        this.value= value
    }
    override var value: V?= null
    override var tag: T?= null

    override fun equals(other: Any?): Boolean {
        return when(other){
            is TaggedBoxedVal<*, *> -> value == other.value && tag == other.tag
            is BoxedVal<*> -> value == other.value
            else -> this === other
        }
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + (value?.hashCode() ?: 0)
        result = 31 * result + (tag?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String
        = "${this::class.simpleName}(value=$value, tag=$tag)"
}