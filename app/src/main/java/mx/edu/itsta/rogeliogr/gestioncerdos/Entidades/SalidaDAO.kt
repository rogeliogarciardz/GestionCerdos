package mx.edu.itsta.rogeliogr.gestioncerdos.Entidades

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query


@Dao
interface SalidaDAO{
    @Query("SELECT * FROM salidas ORDER BY fecha ASC")
    fun getAll(): List<Salida>

    @Query("SELECT * FROM salidas ORDER BY fecha DESC LIMIT :cantidad")
    fun getLast(cantidad:Int): List<Salida>

    @Query("SELECT * FROM salidas WHERE id_cerdo=:idc")
    fun getByCerdoId(idc:Long): List<Salida>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entidadData:Salida)

    @Query("DELETE from salidas WHERE id_salida=:ids")
    fun deleteById(ids:Long)

}