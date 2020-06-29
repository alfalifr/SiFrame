package sidev.lib.implementation._simulation.edc.util

object Edc_Util {

    //untuk mengubah dari double menjadi formatted nomimal dalam bentuk string tanpa simbol mata uang.
    fun convertToFormattedValue(value : Number) : String {
        var valTemp = value.toString()
            .replace(".", ",")
        var finalVal = ""

        var startInd= valTemp.indexOf(",")
        if(startInd < 0)
            startInd= valTemp.length

        //untuk smtr gakpake angka di belakang koma (,)
        valTemp= valTemp.substring(0, startInd)

        for(i in startInd -1 downTo 0){
            finalVal = if((i - startInd) % 3 == 0 && i != 0)
                "." + valTemp[i] + finalVal
            else
                valTemp[i] + finalVal
        }
        return "$finalVal"
    }
}