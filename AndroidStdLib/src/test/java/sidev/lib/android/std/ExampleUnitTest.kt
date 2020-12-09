package sidev.lib.jvm.android.std

import android.util.SparseIntArray
import androidx.core.util.set
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.runners.MockitoJUnitRunner
import sidev.lib.android.std.tool.util.`fun`.asArrangeable
import sidev.lib.console.prin

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(MockitoJUnitRunner::class)
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Mock
    lateinit var sparseIntArray: SparseIntArray

    @Test
    fun sparseArrayArrangeableTest(){
        val arr= sparseIntArray
        arr[0]= 1
        arr[3]= 2
        arr[5]= 3

        val arr1= arr.asArrangeable()
        val a= arr1[3]
        val b= arr1[5]
        val a0= arr[3]

        prin("arr= $arr")
        prin("arr1= $arr1")
        prin("a= $a")
        prin("b= $b")
        prin("a0= $a0")
    }
}