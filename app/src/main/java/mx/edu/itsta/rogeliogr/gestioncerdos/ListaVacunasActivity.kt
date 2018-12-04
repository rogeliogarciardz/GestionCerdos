package mx.edu.itsta.rogeliogr.gestioncerdos

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.github.mikephil.charting.data.Entry
import kotlinx.android.synthetic.main.activity_lista_vacunas.*
import mx.edu.itsta.rogeliogr.gestioncerdos.Entidades.GCDataBase
import mx.edu.itsta.rogeliogr.gestioncerdos.Entidades.Vacuna
import mx.edu.itsta.rogeliogr.gestioncerdos.Listas.ListaVacunasCerdoAdapter
import mx.edu.itsta.rogeliogr.gestioncerdos.utileria.DbWorkerThread

class ListaVacunasActivity : AppCompatActivity() {


    private var mDb: GCDataBase? = null
    private lateinit var mDbWorkerThread: DbWorkerThread
    private val mUiHandler = Handler()

    var id_cerdo: Long = 0
    var vacunas: List<Vacuna>? = null
    val entries: ArrayList<Entry> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_vacunas)

        mDbWorkerThread = DbWorkerThread("dbWorkerThread")
        mDbWorkerThread.start()

        mDb = GCDataBase.getInstance(this)

        var msg = intent.getStringExtra("ID")
        id_cerdo = msg.toLong()


        Thread.sleep(1000)

        rvListaVacunas.layoutManager = LinearLayoutManager(this)

        mostrarVacunasCerdo()
    }

    private fun mostrarVacunasCerdo() {
        val task = Runnable {

            vacunas = mDb?.vacunaDao()?.getAll(id_cerdo)
            mUiHandler.post {
                if (!(vacunas == null && vacunas?.size == 0)) {
                    rvListaVacunas.adapter = ListaVacunasCerdoAdapter(
                        vacunas!!,
                        this,
                        { partItem: Vacuna -> partItemClicked(partItem) })
                    //txtLog.text = "Total cerdos: "+pesos?.size
                }

            }
        }
        mDbWorkerThread.postTask(task)

    }

    private fun partItemClicked(partItem: Vacuna) {

    }


    override fun onDestroy() {
        super.onDestroy()
        GCDataBase.destroyInstance()
        mDbWorkerThread.quit()
    }

    override fun onResume() {
        super.onResume()

        if (mDbWorkerThread == null) {
            mDbWorkerThread = DbWorkerThread("dbWorkerThread")
            mDbWorkerThread.start()
        }
        //     mostrarPesoCerdo()

    }
}
