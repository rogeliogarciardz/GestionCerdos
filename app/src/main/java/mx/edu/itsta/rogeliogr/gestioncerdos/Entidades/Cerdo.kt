package mx.edu.itsta.rogeliogr.gestioncerdos.Entidades

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey
import java.util.*

@Entity(tableName = "cerdos",indices = arrayOf(Index(value = ["Nombre"],unique = true)))
data class Cerdo(@PrimaryKey(autoGenerate=true) var id_cerdo:Long?,
                 @ColumnInfo(name = "Nombre") var nombre:String,
                 @ColumnInfo(name="Numero") var numero:Int,
                 @ColumnInfo(name="Peso") var peso:Double,
                 @ColumnInfo(name="F_nac") var f_nac: Long,
                 @ColumnInfo(name="Tipo") var tipo:Int,
                 @ColumnInfo(name="Sexo") var sexo:String,
                 @ColumnInfo(name="Vigente") var vigente:Boolean
){
    constructor():this(null ,"",0,0.0, Date().time,0,"M",true)
}
