package sidev.lib.android.siframe.arch.annotation

import sidev.lib.android.siframe.arch.intent_state.ViewIntent
import sidev.lib.android.siframe.arch.presenter.MviPresenter
import kotlin.reflect.KClass

/**
 * Digunakan untuk menandai bahwa fungsi di dalam [MviPresenter] dipanggil jika [ViewIntent]
 * memiliki tipe data sesuai [clazz].
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class ViewIntentFunction(val clazz: KClass<*>)

