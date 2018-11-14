package mx.edu.itsta.rogeliogr.gestioncerdos

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import mx.edu.itsta.rogeliogr.gestioncerdos.Entidades.Cerdo
import mx.edu.itsta.rogeliogr.gestioncerdos.Entidades.CerdoDAO
import mx.edu.itsta.rogeliogr.gestioncerdos.Entidades.Peso
import mx.edu.itsta.rogeliogr.gestioncerdos.Entidades.PesoDAO


@Database(entities = arrayOf(Cerdo::class, Peso::class),version=5)

abstract class GCDataBase: RoomDatabase(){
    abstract fun cerdoDao(): CerdoDAO
    abstract fun pesoDao(): PesoDAO

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

