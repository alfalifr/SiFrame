package sidev.lib.android.siframe

import android.widget.ImageView
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("sidev.lib.android.siframe.test", appContext.packageName)
    }

    @Test
    fun viewColorTest() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        // Context of the app under test.
        val iv= ImageView(appContext)
//        assertEquals("sidev.lib.android.siframe.test", appContext.packageName)
    }
}
