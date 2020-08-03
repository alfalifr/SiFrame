package sidev.lib.universal.tool.util

object KReflexUtil {
    fun setField(owner: Any, fieldName: String, value: Any?){
        owner::class.java.fields
    }
}