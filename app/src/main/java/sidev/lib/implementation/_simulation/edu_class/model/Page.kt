package sidev.kuliah.tekber.edu_class.model

//import sidev.kuliah.tekber.edu_class.intfc.Content
import sidev.lib.android.siframe.model.DataWithId
import sidev.lib.android.siframe.model.FK_M
import sidev.lib.implementation._simulation.edu_class.model.Content

/**
 * @param contentList berisi banyak fk ke model Content. Content dapat berupa ContentRead, ContentVideo, ContentQuestion
 * @param no halaman.
 * @param isQuiz true jika halaman merupakan ujian.
 * @param isQuizStillValid true berarti ujian msh bisa dikerjakan.
 */
data class Page(private val _id: String,
                var name: String,
                var no: Int,
                var contentList: FK_M<Content>?,
                var isQuiz: Boolean= false,
                var isQuizStillValid: Boolean= true): DataWithId(_id)