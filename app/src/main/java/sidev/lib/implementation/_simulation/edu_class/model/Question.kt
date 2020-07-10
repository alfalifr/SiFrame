package sidev.kuliah.tekber.edu_class.model

import sidev.lib.android.siframe.model.DataWithId
import sidev.lib.android.siframe.model.FK_M
import java.io.Serializable

/**
 * Modelnya blum kepake di app.
 */
data class Question(private val _id: String,
                var content: FK_M<ContentQuestion>?,
                var scoreIfCorrect: Int,
                var isCorrect: Boolean= false
): DataWithId(_id)