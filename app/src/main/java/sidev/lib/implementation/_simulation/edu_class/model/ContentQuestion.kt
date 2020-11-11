package sidev.kuliah.tekber.edu_class.model

//import sidev.kuliah.tekber.edu_class.intfc.Content
import sidev.lib.android.siframe.model.DataWithId
import sidev.lib.android.siframe.model.FK_M
import sidev.lib.implementation._simulation.edu_class.model.Content
import java.io.Serializable

/**
 * @param question pertanyaannya.
 * @param answerKind jenis pertanyaan (PILGAN, MUTLIPLE, dan ISIAN). Nilai dari int ini ada di kelas Const.QUESTION_KIND_MULTIPLE dkk.
 * @param answerChoice jika answerKind selain PILGAN atau MULTIPLE, maka answerChoice null.
 * @param answerByTeacher jika answerKind merupakan PILGAN atau MULTIPLE, maka answerByReader berisi Int yg merepresentasikan index jwb.
 * @param answerByReader jika answerKind merupakan PILGAN atau MULTIPLE, maka answerByReader berisi Int yg merepresentasikan index jwb.
 */
data class ContentQuestion(private val _id: String, var question: String,
                           var answerKind: Int,
                           var answerChoice: ArrayList<String>?,
                            var answerByTeacher: ArrayList<String>?,
                            var answerByReader: ArrayList<String>?): Content<ContentQuestion>(_id){
    @Deprecated("Masih blum diimplement scr benar.", ReplaceWith("copy()"))
    override fun copy_(prop: Map<String, Any?>): ContentQuestion = copy()
}