package mx.edu.itsta.rogeliogr.gestioncerdos.Entidades

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey
import java.util.*


@Entity(tableName = "vacunas",foreignKeys = [
    ForeignKey(entity = Cerdo::class,
        parentColumns = ["id_cerdo"],
        childColumns = ["id_cerdo"],
        onDelete = ForeignKey.CASCADE
    )])

data class Vacuna(@PrimaryKey(autoGenerate=true) var id_vacuna:Long?,
                @ColumnInfo(name = "id_cerdo") var id_cerdo:Long,
                @ColumnInfo(name="Fecha") var fecha:Long,
                @ColumnInfo(name="Nombre") var nombre:String,
                @ColumnInfo(name="Observaciones") var observaciones:String
)
{
    constructor():this(null ,0, Date().time,"","")
}
