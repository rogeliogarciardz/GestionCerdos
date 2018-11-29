package mx.edu.itsta.rogeliogr.gestioncerdos

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_agrega_cerdo.*

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import mx.edu.itsta.rogeliogr.gestioncerdos.Entidades.Cerdo
import mx.edu.itsta.rogeliogr.gestioncerdos.Listas.ListaCerdosAdapter
import mx.edu.itsta.rogeliogr.gestioncerdos.utileria.RecyclerItemClickListener
import java.util.*

class MainActivity : AppCompatActivity()  {



    private var mDb: GCDataBase? = null
    private lateinit var mDbWorkerThread: DbWorkerThread
    private val mUiHandler = Handler()
    var cerdos: List<Cerdo>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        mDbWorkerThread = DbWorkerThread("dbWorkerThread")
        mDbWorkerThread.start()

        mDb = GCDataBase.getInstance(this)

//        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //txtLog.setText("O: "+resources.configuration.orientation)

        if(resources.configuration.orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT){

            rvListaCerdos.layoutManager = LinearLayoutManager(this)

        }else{
            rvListaCerdos.layoutManager = GridLayoutManager(this,2)//LinearLayoutManager(this)
        }

        Thread.sleep(1000)

        mostrarInformacion()

        fab.setOnClickListener { view ->
        /*    var cerdo  = Cerdo()
            cerdo.nombre="Pepa"
            cerdo.numero=56745
            cerdo.peso=33
            cerdo.tipo=3
            cerdo.sexo="H"
            cerdo.f_nac="2018-06-23"
            insertarCerdo(cerdo)
            Snackbar.make(view, "Proceso registrado", Snackbar.LENGTH_LONG).show()
            mostrarInformacion()*/
            val intent = Intent(this, AgregaCerdoActivity::class.java)
            startActivity(intent)
        }



    }

    private fun partItemClicked(partItem: Cerdo) {
       // Toast.makeText(this,"ID: "+partItem.id_cerdo,Toast.LENGTH_SHORT).show()
        val intent = Intent(this, DetalleCerdoActivity::class.java)
        intent.putExtra("ID", ""+partItem.id_cerdo);
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    /*fun insertarCerdo(cerdo: Cerdo){
        val task = Runnable { mDb?.cerdoDao()?.insert(cerdo)
            mUiHandler.post {
                mostrarInformacion()
            }
        }
        mDbWorkerThread.postTask(task)
    }*/

    fun mostrarInformacion(){
        val task = Runnable {
            cerdos =
                mDb?.cerdoDao()?.getAll()
            mUiHandler.post {
                if (cerdos != null || cerdos?.size != 0) {
                    rvListaCerdos.adapter = ListaCerdosAdapter(cerdos!!,this,{ partItem : Cerdo -> partItemClicked(partItem) })
                    txtLog.text = "Total cerdos: "+cerdos?.size
                }

            }
        }
        mDbWorkerThread.postTask(task)

        //Thread.sleep(2000)

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

    fun allVisible_Onclick(view:View){
        val task = Runnable {
            mDb?.cerdoDao()?.allVisibe()
            mUiHandler.post {

                mostrarInformacion()
            }
        }
        mDbWorkerThread.postTask(task)
    }



}
