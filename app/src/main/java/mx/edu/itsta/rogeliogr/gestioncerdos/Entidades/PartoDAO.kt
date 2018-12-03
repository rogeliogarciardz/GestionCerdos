package mx.edu.itsta.rogeliogr.gestioncerdos.Entidades

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query


@Dao
interface PartoDAO{
    @Query("SELECT * FROM parto WHERE id_cerdo=:idr")
    fun getAll(idr:Long): List<Parto>

    @Query("SELECT * FROM parto WHERE id_parto=:idc ORDER BY Fecha_parto DESC LIMIT :cantidad")
    fun getLast(idc:Long,cantidad:Int): List<Parto>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entidadData: Parto)

}