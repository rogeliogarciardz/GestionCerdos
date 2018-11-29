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

    fun stringLen(texto:String,tam:Int):String{
        var salida:String
        salida = texto
        if(texto.length<tam){
            var i:Int=0
            var xsp:Int
            xsp = tam- texto.length
            while(i<xsp){
                salida = salida.plus(" ")
                i++
            }
        }else{
            salida=texto.substring(0,tam)
        }
        return salida
    }

    fun stringLenCenter(texto:String,tam:Int):String{
        var salida:String
        salida = texto
        var tmp:String=""
        if(texto.length<tam){
            var i:Int=0
            var xsp:Int
            var xspa:Int
            var xspd:Int

            xsp = tam - texto.length
            xspa = xsp/2
            xspd = xsp/2

            if(xsp != (xspa+xspd))
                xspd++

            while(i<xspa){
                tmp = tmp.plus(" ")
                i++
            }
            i=0
            salida=tmp+salida
            tmp=""
            while(i<xspd){
                tmp = tmp.plus(" ")
                i++
            }
            salida = salida+tmp

        }else{
            salida=texto.substring(0,tam)
        }
        return salida
    }

    fun stringLenLeft(texto:String,tam:Int):String{
        var salida:String
        salida = ""
        if(texto.length<tam){
            var i:Int=0
            var xsp:Int
            xsp = tam- texto.length
            while(i<xsp){
                salida = salida.plus(" ")
                i++
            }
            salida = salida.plus(texto)
        }else{
            salida=texto.substring(0,tam)
        }
        return salida
    }

    fun ctabla(texto:String,tam:Int):String{
        var salida:String
        salida = "|"+ stringLenCenter(texto,tam)+"|"
        return salida
    }
}