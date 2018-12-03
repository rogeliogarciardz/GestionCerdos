package mx.edu.itsta.rogeliogr.gestioncerdos.Entidades

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey
import java.util.*

@Entity(tableName = "reproduccion",foreignKeys = [
    ForeignKey(entity = Cerdo::class,
        parentColumns = ["id_cerdo"],
        childColumns = ["id_cerdo"],
        onDelete = ForeignKey.CASCADE
    )])

data class Reproduccion(@PrimaryKey(autoGenerate=true) var id_reproduccion:Long?,
                  @ColumnInfo(name = "id_cerdo") var id_cerdo:Long,
                  @ColumnInfo(name="Fecha_celo") var fecha_celo:Long,
                  @ColumnInfo(name="Fecha_monta") var fecha_monta:Long,
                  @ColumnInfo(name="Fecha_destete") var fecha_destete:Long,
                  @ColumnInfo(name="No_semental") var no_semental:Long
)
{
    constructor():this(null ,0, Date().time, Date().time, Date().time,0)
}
