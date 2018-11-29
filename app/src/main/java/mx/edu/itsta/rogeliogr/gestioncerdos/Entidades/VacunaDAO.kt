package mx.edu.itsta.rogeliogr.gestioncerdos.Entidades

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query

@Dao
interface VacunaDAO{
    @Query("SELECT * FROM vacunas WHERE id_cerdo=:idc ORDER BY fecha DESC")
    fun getAll(idc:Long): List<Vacuna>

    @Query("SELECT * FROM vacunas WHERE id_cerdo=:idc ORDER BY fecha DESC LIMIT :cantidad")
    fun getLast(idc:Long,cantidad:Int): List<Vacuna>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entidadData: Vacuna)

    // @Query("DELETE from pesos")
    // fun deleteAll()

}