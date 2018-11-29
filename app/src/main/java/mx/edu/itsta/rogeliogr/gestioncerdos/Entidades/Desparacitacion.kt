package mx.edu.itsta.rogeliogr.gestioncerdos.Entidades

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey
import java.util.*


@Entity(tableName = "desparacitaciones",foreignKeys = [
    ForeignKey(entity = Cerdo::class,
        parentColumns = ["id_cerdo"],
        childColumns = ["id_cerdo"],
        onDelete = ForeignKey.CASCADE
    )])

data class Desparacitacion(@PrimaryKey(autoGenerate=true) var id_desparacitacion:Long?,
                  @ColumnInfo(name = "id_cerdo") var id_cerdo:Long,
                  @ColumnInfo(name="Fecha") var fecha:Long,
                  @ColumnInfo(name="Nombre") var medicamento:String,
                  @ColumnInfo(name="Observaciones") var observaciones:String
)
{
    constructor():this(null ,0, Date().time,"","")
}
