package mx.edu.itsta.rogeliogr.gestioncerdos.Entidades

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey
import java.util.*

@Entity(tableName = "parto",foreignKeys = [
    ForeignKey(entity = Reproduccion::class,
        parentColumns = ["id_reproduccion"],
        childColumns = ["id_reproduccion"],
        onDelete = ForeignKey.CASCADE
    )])

data class Parto(@PrimaryKey(autoGenerate=true) var id_parto:Long?,
                        @ColumnInfo(name = "id_reproduccion") var id_reproduccion:Long,
                        @ColumnInfo(name = "id_cerdo") var id_cerdo:Long,
                        @ColumnInfo(name="Fecha_parto") var fecha_parto:Long,
                        @ColumnInfo(name="No_crias") var no_crias:Int,
                        @ColumnInfo(name="Vivas") var vivas:Int,
                        @ColumnInfo(name="Peso_crias") var peso_crias:String,
                        @ColumnInfo(name="Promedio_pesos") var promedio_pesos:Double,
                        @ColumnInfo(name="Muertas") var muertas:Long
)
{
    constructor():this(null ,0, 0,Date().time,0,0,"",0.0,0)
}
