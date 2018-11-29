package mx.edu.itsta.rogeliogr.gestioncerdos.Entidades

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query


@Dao
interface PesoDAO{
    @Query("SELECT * FROM pesos WHERE id_cerdo=:idc ORDER BY fecha ASC")
    fun getAll(idc:Long): List<Peso>

    @Query("SELECT * FROM pesos WHERE id_cerdo=:idc ORDER BY fecha DESC LIMIT :cantidad")
    fun getLast(idc:Long,cantidad:Int): List<Peso>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entidadData: Peso)

    @Query("DELETE from pesos WHERE id_peso=:idc")
    fun deleteById(idc:Long)

}