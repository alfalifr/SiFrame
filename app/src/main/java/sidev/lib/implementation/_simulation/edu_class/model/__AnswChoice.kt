package sidev.kuliah.tekber.edu_class.model

import sidev.lib.android.siframe.model.DataWithId

/**
 * Gak usah, gak dipake
 */
data class __AnswChoice(private val _id: String, var choice: Array<String>?, var correctChoice: Array<Int>?): DataWithId(_id){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as __AnswChoice

        if (id != other.id) return false
        if (choice != null) {
            if (other.choice == null) return false
            if (!choice!!.contentEquals(other.choice!!)) return false
        } else if (other.choice != null) return false
        if (correctChoice != null) {
            if (other.correctChoice == null) return false
            if (!correctChoice!!.contentEquals(other.correctChoice!!)) return false
        } else if (other.correctChoice != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (choice?.contentHashCode() ?: 0)
        result = 31 * result + (correctChoice?.contentHashCode() ?: 0)
        return result
    }

}