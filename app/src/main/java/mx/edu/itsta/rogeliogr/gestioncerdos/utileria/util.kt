package mx.edu.itsta.rogeliogr.gestioncerdos.utileria

import android.content.Context
import android.widget.Toast
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object util{
    fun toastError(msg:String,context: Context){
        Toast.makeText(context,"(!) "+msg, Toast.LENGTH_SHORT).show()
    }

    fun timeStampToString(ts:Long):String{
        var d =  Date(ts)
        val format = SimpleDateFormat("dd/MM/yyyy")
        return format.format(d)
    }

    fun timeStampToString(ts:Long,format:String):String{
        var d =  Date(ts)
        val format = SimpleDateFormat(format)
        return format.format(d)
    }

    fun stringtoDate(dates: String): Date {
        val sdf = SimpleDateFormat("dd-MM-yyyy",
            Locale.getDefault())
        var date: Date? = null
        try {
            date = sdf.parse(dates)
            println(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return date!!
    }
}