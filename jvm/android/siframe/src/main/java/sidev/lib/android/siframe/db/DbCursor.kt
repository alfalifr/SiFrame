package sidev.lib.android.siframe.db

import android.database.Cursor
import androidx.core.database.getDoubleOrNull
import androidx.core.database.getIntOrNull
import androidx.core.database.getStringOrNull
import sidev.lib.`val`.SuppressLiteral
import sidev.lib.android.siframe.tool.util.`fun`.loge

/** Wrapper dari [Cursor] yg disesuaikan dg gaya bahasa Kotlin. */
open class DbCursor(val delegate: Cursor): Iterable<List<Pair<String, Any?>>> {
    companion object{
        const val ROW_NUMBER_INIT= -1
        const val ROW_NUMBER_UNKNOWN= -2
    }

    val columnNames: Array<String>
        get()= delegate.columnNames
    val count: Int
        get()= delegate.count
    val columnCount: Int
        get()= delegate.columnCount
    val isFirst: Boolean
        @JvmName("isFirst")
        get()= delegate.isFirst
    val isLast: Boolean
        @JvmName("isLast")
        get()= delegate.isLast
    val isBeforeFirst: Boolean
        @JvmName("isBeforeFirst")
        get()= delegate.isBeforeFirst
    val isAfterLast: Boolean
        @JvmName("isAfterLast")
        get()= delegate.isAfterLast
    val isClosed: Boolean
        @JvmName("isClosed")
        get()= delegate.isClosed

    private var isInternalEdit: Boolean= false
    var currentRowNumber: Int= ROW_NUMBER_INIT
        get()= if(field <= ROW_NUMBER_UNKNOWN) delegate.position.also { field= it } else field
        set(v){
            if(isInternalEdit || moveToPosition_int(v, false))
                field= v
        }

    fun close()= delegate.close()
    fun moveToFirst(): Boolean= moveToPosition(0)
    fun moveToLast(): Boolean= delegate.moveToLast().also {
        isInternalEdit= true
        currentRowNumber= delegate.position
        isInternalEdit= false
    }
    fun moveToNext(): Boolean = moveToPosition(currentRowNumber +1)
    fun moveToPrevious(): Boolean= moveToPosition(currentRowNumber -1)
    fun moveToPosition(pos: Int): Boolean = moveToPosition_int(pos).also{
        loge("moveToPosition() pos= $pos bool= $it")
    }
    fun move(offset: Int): Boolean= moveToPosition(currentRowNumber +offset)

    private fun moveToPosition_int(pos: Int, alsoAssignPosition: Boolean= true): Boolean{
        return delegate.moveToPosition(pos).also {
            isInternalEdit= true
            if(alsoAssignPosition && it) currentRowNumber= pos
            isInternalEdit= false
        }
    }

    fun getType(columnIndex: Int): Int = delegate.getType(columnIndex)
    fun isNull(columnIndex: Int): Boolean = delegate.isNull(columnIndex)

    @Suppress(SuppressLiteral.UNCHECKED_CAST, SuppressLiteral.IMPLICIT_CAST_TO_ANY)
    operator fun <T> get(columnIndex: Int): T = when(getType(columnIndex)){
        Cursor.FIELD_TYPE_STRING -> delegate.getStringOrNull(columnIndex)
        Cursor.FIELD_TYPE_INTEGER -> delegate.getIntOrNull(columnIndex)
        Cursor.FIELD_TYPE_FLOAT -> delegate.getDoubleOrNull(columnIndex)
        else -> null
    } as T

    @Suppress(SuppressLiteral.UNCHECKED_CAST, SuppressLiteral.IMPLICIT_CAST_TO_ANY)
    operator fun <T> get(rowIndex: Int, columnIndex: Int): T{
        val beforeRowNumber= currentRowNumber
        val res= (if(moveToPosition(rowIndex)){
            when(getType(columnIndex)){
                Cursor.FIELD_TYPE_STRING -> delegate.getStringOrNull(columnIndex)
                Cursor.FIELD_TYPE_INTEGER -> delegate.getIntOrNull(columnIndex)
                Cursor.FIELD_TYPE_FLOAT -> delegate.getDoubleOrNull(columnIndex)
                else -> null
            }
        } else null) as T

        currentRowNumber= beforeRowNumber
        return res
    }

    fun getAllColumnAtRow(rowIndex: Int): List<Pair<String, Any?>>{
        val list= ArrayList<Pair<String, Any?>>()
        val beforeRowNumber= currentRowNumber
        if(moveToPosition(rowIndex)){
            for((i, name) in columnNames.withIndex()){
                @Suppress(SuppressLiteral.IMPLICIT_CAST_TO_ANY)
                val value= when(getType(i)){
                    Cursor.FIELD_TYPE_STRING -> delegate.getStringOrNull(i)
                    Cursor.FIELD_TYPE_INTEGER -> delegate.getIntOrNull(i)
                    Cursor.FIELD_TYPE_FLOAT -> delegate.getDoubleOrNull(i)
                    else -> null
                }
                list += Pair(name, value)
            }
        }
        currentRowNumber= beforeRowNumber
        return list
    }

    /**
     * Mengambil list dari semua nilai dari tiap kolom dan baris yg diratakan.
     * Hasil return yaitu Triple<rowNumber, columnName, columnValue>.
     */
    fun flatten(): List<Triple<Int, String, Any?>>{
        val resList= ArrayList<Triple<Int, String, Any?>>()
        for((rowNumber, row) in iterator().withIndex()){
            for((colName, value) in row){
                resList += Triple(rowNumber, colName, value)
            }
        }
        return resList
    }

    override operator fun iterator(): Iterator<List<Pair<String, Any?>>> = object : Iterator<List<Pair<String, Any?>>>{
        var itrHasStarted= false
        var index: Int= 0
        override fun hasNext(): Boolean =
            if(!itrHasStarted) moveToFirst().also { itrHasStarted= true } else moveToNext()
        override fun next(): List<Pair<String, Any?>> = getAllColumnAtRow(index++)
    }
}