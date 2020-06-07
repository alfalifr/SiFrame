package sidev.lib.android.siframe.tool.util

object __StringUtil {
    fun angkaString(angka: Int, pjg: Int): String{
        return angkaString(angka.toString(), pjg)
    }
    fun angkaString(angka: String, pjg: Int): String{
        var strAngka= angka
        val selisihPjg= pjg -strAngka.length
        for(i in selisihPjg downTo 1)
            strAngka= "0" +strAngka
        return strAngka
    }
}