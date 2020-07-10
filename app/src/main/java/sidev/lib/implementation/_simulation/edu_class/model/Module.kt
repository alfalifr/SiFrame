package sidev.kuliah.tekber.edu_class.model

import sidev.lib.android.siframe.model.DataWithId
import sidev.lib.android.siframe.model.FK_M
import sidev.lib.android.siframe.model.PictModel
import java.io.Serializable

/**
 * @param duration sebaiknya .start dan .end hanya berupa angka (int).
 *      Jika di db duration bisa dipecah jadi duration_start dan duration_end.
 * @param pageList berisi banyak fk ke model Page.
 */
data class Module(private val _id: String,
                  var name: String, var desc: String,
                  var img: PictModel?,
                  var duration: Duration,
                  var pageList: FK_M<Page>?
): DataWithId(_id)