package mx.edu.itsta.rogeliogr.gestioncerdos.Entidades

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context


@Database(entities = arrayOf(
    Cerdo::class,
    Peso::class,
    Vacuna::class,
    Desparacitacion::class,
    Reproduccion::class,
    Parto::class,
    Salida::class),version=10)

abstract class GCDataBase: RoomDatabase(){
    abstract fun cerdoDao(): CerdoDAO
    abstract fun pesoDao(): PesoDAO
    abstract fun vacunaDao(): VacunaDAO
    abstract fun desparacitacionDao(): DesparacitacionDAO
    abstract fun reproduccionDao():ReproduccionDAO
    abstract fun partoDao():PartoDAO
    abstract fun salidaDao():SalidaDAO

    companion object {
        private var INSTANCE: GCDataBase? = null

        fun getInstance(context: Context): GCDataBase? {
            if (INSTANCE == null) {
                synchronized(GCDataBase::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        GCDataBase::class.java, "gcerdos.db")
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}

