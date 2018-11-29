package mx.edu.itsta.rogeliogr.gestioncerdos.Entidades

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query


@Dao
interface CerdoDAO{
    @Query("SELECT * FROM cerdos WHERE vigente=1 ORDER BY Numero")
    fun getAll(): List<Cerdo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entidadData: Cerdo)

    @Query("DELETE from cerdos")
    fun deleteAll()

    @Query("UPDATE cerdos SET Vigente=0 WHERE id_cerdo=:idc")
    fun deleteById(idc:Long)

    @Query("SELECT * FROM cerdos WHERE Numero=:numero")
    fun getByNumero(numero:Int):List<Cerdo>

    @Query("SELECT * FROM cerdos WHERE id_cerdo=:id_cerdo")
    fun getByID(id_cerdo:Long):Cerdo
    //@Query("SELECT * FROM otraEntidadData where peso=:peso")
    //fun getByPeso(peso:Int): List<OtraEntidadData>
    @Query("UPDATE cerdos SET Vigente=1")
    fun allVisibe()

}