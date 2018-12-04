package mx.edu.itsta.rogeliogr.gestioncerdos

import android.content.pm.ActivityInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_lista_salidas.*
import kotlinx.android.synthetic.main.content_main.*
import mx.edu.itsta.rogeliogr.gestioncerdos.Entidades.Cerdo
import mx.edu.itsta.rogeliogr.gestioncerdos.Entidades.GCDataBase
import mx.edu.itsta.rogeliogr.gestioncerdos.Entidades.Salida
import mx.edu.itsta.rogeliogr.gestioncerdos.Listas.ListaCerdosAdapter
import mx.edu.itsta.rogeliogr.gestioncerdos.Listas.ListaSalidasAdapter
import mx.edu.itsta.rogeliogr.gestioncerdos.utileria.DbWorkerThread
import mx.edu.itsta.rogeliogr.gestioncerdos.utileria.util

class ListaSalidasActivity : AppCompatActivity() {

    private var mDb: GCDataBase? = null
    private lateinit var mDbWorkerThread: DbWorkerThread
    private val mUiHandler = Handler()
    lateinit var cerdos: MutableList<Cerdo>
    var salidas: List<Salida>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_salidas)

        cerdos = mutableListOf()

        mDbWorkerThread = DbWorkerThread("dbWorkerThread")
        mDbWorkerThread.start()

        mDb = GCDataBase.getInstance(this)

        //setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //txtLog.setText("O: "+resources.configuration.orientation)

        if(resources.configuration.orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT){
             rvListaSalidas.layoutManager = LinearLayoutManager(this)

        }else{
            rvListaSalidas.layoutManager = GridLayoutManager(this,2)//LinearLayoutManager(this)
        }

        Thread.sleep(1000)

  //      mostrarInformacion()
    }

    private fun mostrarInformacion() {
        val task = Runnable {
            salidas =
                    mDb?.salidaDao()?.getAll()
            mUiHandler.post {
                if (salidas != null || salidas?.size != 0) {
                    rvListaSalidas.adapter = ListaSalidasAdapter(salidas!!,this,{ partItem : Salida -> partItemClicked(partItem) })
                    //txtLog.text = "Total cerdos: "+cerdos?.size
                }

            }
        }
        mDbWorkerThread.postTask(task)
    }


    private fun partItemClicked(partItem: Salida) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun onDestroy() {
        super.onDestroy()
        GCDataBase.destroyInstance()
        mDbWorkerThread.quit()
    }

    override fun onResume() {
        super.onResume()

        if (mDbWorkerThread==null){
            mDbWorkerThread = DbWorkerThread("dbWorkerThread")
            mDbWorkerThread.start()
        }
        mostrarInformacion()

    }
}
