package sidev.lib.android.std.db

import java.util.*

//TODO: Pindahkan ke library sendiri.
interface Attribute {
    val name: String
    val index: Int
    val type: Type
    val isPrimary: Boolean

    enum class Type{
        INTEGER,
        STRING,
        DOUBLE,
        //FLOAT,
        NULL //Attribute dg tipe ini tidak akan disimpan pada DB.
        ;
        companion object{
            fun getByName(typeName: String): Type = when (typeName.toUpperCase(Locale.ROOT)){
                INTEGER.name -> INTEGER
                "LONG" -> INTEGER
                STRING.name -> STRING
                "TEXT" -> STRING
                DOUBLE.name -> DOUBLE
                //FLOAT.name -> FLOAT
                else -> NULL
            }
            fun getByClass(typeClass: Class<*>): Type = getByName(when(typeClass){
                Int::class.java, Integer::class.java -> INTEGER.name
                Long::class.java, java.lang.Long::class.java -> INTEGER.name
                Boolean::class.java, java.lang.Boolean::class.java -> INTEGER.name
                String::class.java -> STRING.name
                Double::class.java, java.lang.Double::class.java -> DOUBLE.name
                Float::class.java, java.lang.Float::class.java -> DOUBLE.name
                else -> NULL.name
            })
//                .also { loge("Attribute.Type.getByClass() typeClass= $typeClass Int::class.java= ${Int::class.java}") }
        }
    }
}