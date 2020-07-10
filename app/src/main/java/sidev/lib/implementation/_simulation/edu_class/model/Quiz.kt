package sidev.kuliah.tekber.edu_class.model

import sidev.lib.android.siframe.model.DataWithId
import sidev.lib.android.siframe.model.FK_M
import java.io.Serializable

/**
 * Modelnya blum kepake di app.
 */
data class Quiz(private val _id: String,
            var title: String, var desc: String?,
            var questions: FK_M<Question>?,
            var correctCount: Int
): DataWithId(_id)