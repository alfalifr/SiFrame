package sidev.lib.universal.structure.type

import sidev.lib.universal.`fun`.inferredType
import kotlin.reflect.full.createType

/**
 * Tipe data yg digunakan untuk menunjukan tipe null.
 * Tipe ini digunakan pada proses [Any.inferredType] untuk menunjukan inferredType dari null.
 */
object Null{
    val type = this::class.createType(nullable = true)
}