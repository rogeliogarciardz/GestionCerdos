package mx.edu.itsta.rogeliogr.gestioncerdos.Entidades

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query


@Dao
interface ReproduccionDAO{
    @Query("SELECT * FROM reproduccion WHERE id_cerdo=:idc ORDER BY Fecha_celo DESC")
    fun getAll(idc:Long): List<Reproduccion>

    @Query("SELECT * FROM reproduccion WHERE id_cerdo=:idc ORDER BY Fecha_celo DESC LIMIT :cantidad")
    fun getLast(idc:Long,cantidad:Int): List<Reproduccion>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entidadData: Reproduccion)

}