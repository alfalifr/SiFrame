package sidev.lib.implementation._simulation.sigudang.util

import java.text.SimpleDateFormat
import java.util.*

object T_TimeUtil {

    val month: Array<String>
        get() = arrayOf(
            "Januari",
            "Februari",
            "Maret",
            "April",
            "Mei",
            "Juni",
            "Juli",
            "Agustus",
            "September",
            "Oktober",
            "November",
            "Desember"
        )

    /**
     * Sementara ini hanya untuk format dd/MM/yyyy
     * update:
     * 17 Des 2019: udah bisa format dd?MM?yyyy -> dg ? sbg @param delimiter
     */
    fun parseToAlphabet(date: String, monthLen: Int= 3, delimiter: String= "/"): String{
        val dateArr= date.split(delimiter)

        var monthName= month[dateArr[1].toInt() -1]
        if(monthLen > 0)
            monthName= monthName.substring(0, monthLen)
        return "${dateArr[0]} $monthName ${dateArr[2]}"
    }

    fun parseToNamedMonth(date: String, monthLen: Int= 3, patternIn: String= "yyyy-MM-dd", patternOut: String= "dd MMM yyyy")
    : String{
        val sdfOut= SimpleDateFormat(patternOut, Locale.getDefault())
        val sdfIn= SimpleDateFormat(patternIn)

        val dateObj = sdfIn.parse(date)
        return sdfOut.format(dateObj)
    }

    fun timestamp(pattern: String= "yyyyddMMhhmmss"): String{
        val simpleDateFormat = SimpleDateFormat(pattern)
        return simpleDateFormat.format(Date())
    }
    fun getDateString(cal: Calendar, pattern: String= "dd/MM/yyyy"): String{
        val simpleDateFormat = SimpleDateFormat(pattern)
        return simpleDateFormat.format(cal.time)
    }

    /**
     * Otomatis menghitung tanggal yang sesuai
     * @param month dimulai dari 1
     */
    fun getDateString(day: Int, month: Int, year: Int, pattern: String= "dd/MM/yyyy"): String{
        val cal= Calendar.getInstance()
        val month= if(month in 1 .. 12) month
        else if(month > 12) 12
        else 1

        cal.set(Calendar.YEAR, year)
        cal.set(Calendar.MONTH, month-1)

        val dayMax= cal.getActualMaximum(Calendar.DAY_OF_MONTH)
        val day= if(day in 1 .. dayMax) day
        else if(day > dayMax) dayMax
        else 1

        cal.set(Calendar.DATE, day)

//            cal.set(year, month-1, day)
        return getDateString(
            cal,
            pattern
        )
    }

    fun toCalObj(time: String, pattern: String= "dd/MM/yyyy"): Calendar{
        val simpleDateFormat = SimpleDateFormat(pattern)
        val cal= Calendar.getInstance()
        cal.time= simpleDateFormat.parse(time)
        return cal
    }
}