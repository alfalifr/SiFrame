package sidev.lib.implementation.frag

import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.frag_txt.view.*
import kotlinx.coroutines.channels.Channel
import sidev.data.quran.Ayat
import sidev.data.quran.Quran
import sidev.data.quran.Surat
import sidev.lib.android.siframe.lifecycle.fragment.Frag
import sidev.lib.android.std.tool.util.`fun`.asyncOnContext
import sidev.lib.android.std.tool.util.`fun`.toast
import sidev.lib.implementation.R
import java.text.NumberFormat
import java.util.*
import sidev.lib.async.async

class QuranFrag: Frag() {
    override val layoutId: Int= R.layout.frag_txt

    override fun _initView(layoutView: View) {
        layoutView.pb.visibility= View.VISIBLE

        val surat= 114
        val ayatStart= Surat.ayatStart(surat)
        val ayatEnd= ayatStart + Surat.ayatCount(surat)


        asyncOnContext({
            var laf= ""
            for(i in ayatStart +1 .. ayatEnd){
                post(i)
                laf += Ayat.lafadz(i, false) +" "
            }

            val loc= Locale.forLanguageTag("AR")
            val nf= NumberFormat.getInstance(loc)
//        laf += nf
//        laf += "\n\n$loc"
            laf += "\n${loc.country}"
            laf += "\n\n${Quran.toArabInt(10)}"

            val arabInt= Quran.toArabInt(10)

            val endAyatByteArr= byteArrayOf(0xDB.toByte(), 0x9D.toByte())
            val endAyat= endAyatByteArr.toString(Charsets.UTF_8)

            val end1= "$endAyat$arabInt"
            val end2= "$arabInt$endAyat"
            val end3= "${Quran.ORNATE_LEFT_PARENTHESIS_STR}$arabInt${Quran.ORNATE_RIGHT_PARENTHESIS_STR}"
            val end4= "${Quran.ORNATE_RIGHT_PARENTHESIS_STR}$arabInt${Quran.ORNATE_LEFT_PARENTHESIS_STR}"

            laf += "\n$end1"
            laf += "\n$end2"
            laf += "\n$end3"
            laf += "\n$end4"

            post(end4)
            laf
        }, { it: Any ->
            toast("Halo lafadz i= $it", Toast.LENGTH_SHORT)
        }) {
            layoutView.tv.text= it //laf
            layoutView.pb.visibility= View.GONE
        }
    }
}