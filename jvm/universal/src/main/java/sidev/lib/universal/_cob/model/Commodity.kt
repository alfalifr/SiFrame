package id.go.surabaya.ediscont.models

import java.io.Serializable
import java.io.File

data class Commodity(val id: String?,
                var name: String?, var profile: UserProfile?,
                var kind: CommKind, var price: Long?,
                var unit: CommUnit, var stock: Double?,
                var urlPict: String?, var lastUpdate: String?, var tipeUrl: Int= 0, var pictFile: File? = null ) : Serializable