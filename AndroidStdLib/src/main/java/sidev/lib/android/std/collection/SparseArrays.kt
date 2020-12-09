package sidev.lib.android.std.collection

import android.os.Build
import android.util.SparseArray
import android.util.SparseBooleanArray
import android.util.SparseIntArray
import android.util.SparseLongArray
import androidx.annotation.RequiresApi
import androidx.core.util.set
import sidev.lib.structure.data.Arrangeable


internal class SparseArrayArrangeable<T>(val origin: SparseArray<T>): Arrangeable<T> {
    override val size: Int
        get() = origin.size()

    override fun get(index: Int): T = origin[index]
    override fun set(index: Int, element: T): T {
        val old= origin[index]
        origin[index]= element
        return old
    }
    override fun set_(index: Int, element: T) {
        origin[index]= element
    }

    /**
     * Returns a string representation of the object.
     */
    override fun toString(): String = origin.toString()

    /**
     * Indicates whether some other object is "equal to" this one. Implementations must fulfil the following
     * requirements:
     *
     * * Reflexive: for any non-null value `x`, `x.equals(x)` should return true.
     * * Symmetric: for any non-null values `x` and `y`, `x.equals(y)` should return true if and only if `y.equals(x)` returns true.
     * * Transitive:  for any non-null values `x`, `y`, and `z`, if `x.equals(y)` returns true and `y.equals(z)` returns true, then `x.equals(z)` should return true.
     * * Consistent:  for any non-null values `x` and `y`, multiple invocations of `x.equals(y)` consistently return true or consistently return false, provided no information used in `equals` comparisons on the objects is modified.
     * * Never equal to null: for any non-null value `x`, `x.equals(null)` should return false.
     *
     * Read more about [equality](https://kotlinlang.org/docs/reference/equality.html) in Kotlin.
     */
    override fun equals(other: Any?): Boolean = origin.equals(other)

    /**
     * Returns a hash code value for the object.  The general contract of `hashCode` is:
     *
     * * Whenever it is invoked on the same object more than once, the `hashCode` method must consistently return the same integer, provided no information used in `equals` comparisons on the object is modified.
     * * If two objects are equal according to the `equals()` method, then calling the `hashCode` method on each of the two objects must produce the same integer result.
     */
    override fun hashCode(): Int = origin.hashCode()
}

internal class SparseIntArrayArrangeable(val origin: SparseIntArray): Arrangeable<Int> {
    override val size: Int
        get() = origin.size()

    override fun get(index: Int): Int = origin[origin.keyAt(index)]
    override fun set(index: Int, element: Int): Int {
        val old= this[index]
        origin[origin.keyAt(index)]= element
        return old
    }
    override fun set_(index: Int, element: Int) {
        origin[origin.keyAt(index)]= element
    }

    /**
     * Returns a string representation of the object.
     */
    override fun toString(): String = origin.toString()

    /**
     * Indicates whether some other object is "equal to" this one. Implementations must fulfil the following
     * requirements:
     *
     * * Reflexive: for any non-null value `x`, `x.equals(x)` should return true.
     * * Symmetric: for any non-null values `x` and `y`, `x.equals(y)` should return true if and only if `y.equals(x)` returns true.
     * * Transitive:  for any non-null values `x`, `y`, and `z`, if `x.equals(y)` returns true and `y.equals(z)` returns true, then `x.equals(z)` should return true.
     * * Consistent:  for any non-null values `x` and `y`, multiple invocations of `x.equals(y)` consistently return true or consistently return false, provided no information used in `equals` comparisons on the objects is modified.
     * * Never equal to null: for any non-null value `x`, `x.equals(null)` should return false.
     *
     * Read more about [equality](https://kotlinlang.org/docs/reference/equality.html) in Kotlin.
     */
    override fun equals(other: Any?): Boolean = origin.equals(other)

    /**
     * Returns a hash code value for the object.  The general contract of `hashCode` is:
     *
     * * Whenever it is invoked on the same object more than once, the `hashCode` method must consistently return the same integer, provided no information used in `equals` comparisons on the object is modified.
     * * If two objects are equal according to the `equals()` method, then calling the `hashCode` method on each of the two objects must produce the same integer result.
     */
    override fun hashCode(): Int = origin.hashCode()
}

internal class SparseLongArrayArrangeable(val origin: SparseLongArray): Arrangeable<Long> {
    @get:RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    override val size: Int
        get() = origin.size()

    override fun get(index: Int): Long = origin[index]
    override fun set(index: Int, element: Long): Long {
        val old= origin[index]
        origin[index]= element
        return old
    }
    override fun set_(index: Int, element: Long) {
        origin[index]= element
    }

    /**
     * Returns a string representation of the object.
     */
    override fun toString(): String = origin.toString()

    /**
     * Indicates whether some other object is "equal to" this one. Implementations must fulfil the following
     * requirements:
     *
     * * Reflexive: for any non-null value `x`, `x.equals(x)` should return true.
     * * Symmetric: for any non-null values `x` and `y`, `x.equals(y)` should return true if and only if `y.equals(x)` returns true.
     * * Transitive:  for any non-null values `x`, `y`, and `z`, if `x.equals(y)` returns true and `y.equals(z)` returns true, then `x.equals(z)` should return true.
     * * Consistent:  for any non-null values `x` and `y`, multiple invocations of `x.equals(y)` consistently return true or consistently return false, provided no information used in `equals` comparisons on the objects is modified.
     * * Never equal to null: for any non-null value `x`, `x.equals(null)` should return false.
     *
     * Read more about [equality](https://kotlinlang.org/docs/reference/equality.html) in Kotlin.
     */
    override fun equals(other: Any?): Boolean = origin.equals(other)

    /**
     * Returns a hash code value for the object.  The general contract of `hashCode` is:
     *
     * * Whenever it is invoked on the same object more than once, the `hashCode` method must consistently return the same integer, provided no information used in `equals` comparisons on the object is modified.
     * * If two objects are equal according to the `equals()` method, then calling the `hashCode` method on each of the two objects must produce the same integer result.
     */
    override fun hashCode(): Int = origin.hashCode()
}

internal class SparseBooleanArrayArrangeable(val origin: SparseBooleanArray): Arrangeable<Boolean> {
    @get:RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    override val size: Int
        get() = origin.size()

    override fun get(index: Int): Boolean = origin[index]
    override fun set(index: Int, element: Boolean): Boolean {
        val old= origin[index]
        origin[index]= element
        return old
    }
    override fun set_(index: Int, element: Boolean) {
        origin[index]= element
    }

    /**
     * Returns a string representation of the object.
     */
    override fun toString(): String = origin.toString()

    /**
     * Indicates whether some other object is "equal to" this one. Implementations must fulfil the following
     * requirements:
     *
     * * Reflexive: for any non-null value `x`, `x.equals(x)` should return true.
     * * Symmetric: for any non-null values `x` and `y`, `x.equals(y)` should return true if and only if `y.equals(x)` returns true.
     * * Transitive:  for any non-null values `x`, `y`, and `z`, if `x.equals(y)` returns true and `y.equals(z)` returns true, then `x.equals(z)` should return true.
     * * Consistent:  for any non-null values `x` and `y`, multiple invocations of `x.equals(y)` consistently return true or consistently return false, provided no information used in `equals` comparisons on the objects is modified.
     * * Never equal to null: for any non-null value `x`, `x.equals(null)` should return false.
     *
     * Read more about [equality](https://kotlinlang.org/docs/reference/equality.html) in Kotlin.
     */
    override fun equals(other: Any?): Boolean = origin.equals(other)

    /**
     * Returns a hash code value for the object.  The general contract of `hashCode` is:
     *
     * * Whenever it is invoked on the same object more than once, the `hashCode` method must consistently return the same integer, provided no information used in `equals` comparisons on the object is modified.
     * * If two objects are equal according to the `equals()` method, then calling the `hashCode` method on each of the two objects must produce the same integer result.
     */
    override fun hashCode(): Int = origin.hashCode()
}