package mx.edu.itsta.rogeliogr.gestioncerdos.Entidades

import android.arch.persistence.room.*
import android.arch.persistence.room.ForeignKey.CASCADE
import java.util.*

@Entity(tableName = "pesos",foreignKeys = [
    ForeignKey(entity = Cerdo::class,
        parentColumns = ["id_cerdo"],
        childColumns = ["id_cerdo"],
        onDelete = CASCADE)])

data class Peso(@PrimaryKey(autoGenerate=true) var id_peso:Long?,
                 @ColumnInfo(name = "id_cerdo") var id_cerdo:Long,
                 @ColumnInfo(name="Fecha") var fecha:Long,
                 @ColumnInfo(name="Peso") var peso:Double
)
{
    constructor():this(null ,0, Date().time,0.0)
}
