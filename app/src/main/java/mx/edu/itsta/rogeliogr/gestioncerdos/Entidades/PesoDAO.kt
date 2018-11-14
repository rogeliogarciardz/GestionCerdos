package mx.edu.itsta.rogeliogr.gestioncerdos.Entidades

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query


@Dao
interface PesoDAO{
    @Query("SELECT * FROM pesos WHERE id_cerdo=:idc ORDER BY fecha DESC LIMIT 5")
    fun getAll(idc:Long): List<Peso>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entidadData: Peso)

   // @Query("DELETE from pesos")
   // fun deleteAll()

}