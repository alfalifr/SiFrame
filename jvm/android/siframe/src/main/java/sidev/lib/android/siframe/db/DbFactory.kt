package sidev.lib.android.siframe.db

//TODO: Pindahkan ke library sendiri.
object DbFactory {
    fun createAttribute(
        name: String, index: Int, type: Attribute.Type, isPrimary: Boolean= false
    ): Attribute = object: Attribute{
        override val name: String = name
        override val index: Int = index
        override val type: Attribute.Type = type
        override val isPrimary: Boolean = isPrimary
    }
}