package sidev.lib.implementation.frag

import android.content.Context
import android.view.View
import sidev.lib.android.siframe.lifecycle.fragment.RvFrag
import sidev.lib.android.siframe.tool.SQLiteHandler
import sidev.lib.android.siframe.tool.SQLiteHandler.CollectionTypeHandler
import sidev.lib.android.siframe.tool.util.`fun`.loge
import sidev.lib.collection.toArrayList
import sidev.lib.implementation.adp.PolarEcgAdp
import sidev.lib.implementation.model.PolarEcgData
import java.lang.reflect.Field
import java.util.*

class PolarEcgDbFrag : RvFrag<PolarEcgAdp>(){
    override fun initRvAdp(): PolarEcgAdp = PolarEcgAdp(context!!)

    override fun _initView(layoutView: View) {
        rvAdp.dataList = PolarEcgHandler(context!!).attribName.toArrayList()
    }
}

class PolarEcgHandler(c: Context): SQLiteHandler<PolarEcgData>(c, EcgCollectionHandler()){
    override val modelClass: Class<PolarEcgData>
        get() = PolarEcgData::class.java

    /**
     * Membuat instance model [M] dg nilai attribut [petaNilai] dg key adalah nama field
     * pada kelas model [M] dan value adalah nilai dari atribut.
     *
     * <2, 15 Juni 2020> Sementara tidak mengepass fk karena berada di beda tabel.
     */
    override fun createModel(petaNilai: Map<String, *>): PolarEcgData {
        val samples= petaNilai["samples"] as MutableList<Int>
        val timeStamp= petaNilai["timeStamp"] as Long
        return PolarEcgData(samples, timeStamp)
    }
}


//@SuppressWarnings(SuppressLiteral.UNCHECKED_CAST)
internal class EcgCollectionHandler : CollectionTypeHandler<PolarEcgData> {
    override fun <C : List<PolarEcgData>> flattenQueryResult(
        dataList: C,
        collectionAttribNameList: Array<String>
    ): C {
        var lastTimestamp = dataList!![0]!!.timeStamp
        var lastSamples: MutableList<Int?> = ArrayList()
        val newList: MutableList<PolarEcgData> = ArrayList()
        val limit = dataList.size
        for (i in 1 until limit) {
            val data = dataList[i]!!
            val isSameRow = data.timeStamp === lastTimestamp
            if (!isSameRow) {
                newList.add(
                    PolarEcgData(lastSamples, data.timeStamp)
                )
                // Jika row berbeda, maka buat list rrs yg baru.
                lastSamples = ArrayList()
            }
            lastSamples.add(data.samples[0])

            // Jika ternyata sampai akhir adalah row yg sama.
            if (i == limit - 1 && isSameRow) {
                newList.add(
                    PolarEcgData(lastSamples, data.timeStamp)
                )
            }
            lastTimestamp = data.timeStamp
        }
        //@SuppressWarnings(SuppressLiteral.UNCHECKED_CAST)
        return newList as C
    }

    override fun resolveColumnType(field: Field): Class<*>? {
        loge("EcgCollectionHandler.resolveColumnType() field= $field name= ${field.name}")
        return if (field.name == "samples") Int::class.java else null
    }
}