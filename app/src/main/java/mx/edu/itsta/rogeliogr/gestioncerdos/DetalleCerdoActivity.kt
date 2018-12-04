package mx.edu.itsta.rogeliogr.gestioncerdos

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_detalle_cerdo.*
import kotlinx.android.synthetic.main.content_detalle_cerdo.*
import mx.edu.itsta.rogeliogr.gestioncerdos.Entidades.Cerdo
import mx.edu.itsta.rogeliogr.gestioncerdos.utileria.util


class DetalleCerdoActivity : AppCompatActivity() {

    private var mDb: GCDataBase? = null
    private lateinit var mDbWorkerThread: DbWorkerThread
    private val mUiHandler = Handler()
    private var id_cerdo: Long = 0
    private var tipo:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_cerdo)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        mDbWorkerThread = DbWorkerThread("dbWorkerThread")
        mDbWorkerThread.start()

        mDb = GCDataBase.getInstance(this)
        val msg = intent.getStringExtra("ID")
        id_cerdo = msg.toLong()

        Thread.sleep(1000)
        mostrarDetalle()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.detalle_cerdo_menu, menu)
        if(tipo == 1) {
            menu?.findItem(R.id.action_add_reproduccion)?.setVisible(true)
            menu?.findItem(R.id.action_add_parto)?.setVisible(true)

        }else{
            menu?.findItem(R.id.action_add_reproduccion)?.setVisible(false)
            menu?.findItem(R.id.action_add_parto)?.setVisible(false)
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_add_peso -> {
            agregarPeso()
            true
        }
        R.id.action_add_vacuna->{
            agregarVacuna()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }


    fun mostrarDetalle() {
        val task = Runnable {
            val cerdo = mDb?.cerdoDao()?.getByID(id_cerdo)
            mUiHandler.post {
                if (cerdo != null) {
                    lbldcID.text = "ID: " + cerdo?.id_cerdo
                    lbldcNumero.text = "NO: " + cerdo?.numero
                    lbldcNombre.text = "Nombre: " + cerdo?.nombre
                    lbldcFecha.text = "Nacimiento: " + util.timeStampToString( cerdo?.f_nac)
                    lbldcPeso.text = "Peso: " + cerdo?.peso + " kg"
                    lbldcTipo.text = resources.getStringArray(R.array.tipo_cerdo).get(cerdo?.tipo) + " (" +
                            cerdo?.sexo + ")"

                    tipo = cerdo?.tipo

                    mostrarPesoCerdo()
                    mostrarVacunaCerdo()

                } else {
                    util.toastError("NO entontrado " + id_cerdo,this)
                }
            }
        }
        mDbWorkerThread.postTask(task)
    }

    fun agregarPeso() {
        val intent = Intent(this, AgregaPesoActivity::class.java)
        intent.putExtra("ID", ""+id_cerdo);
        startActivity(intent)
    }

    fun agregarVacuna() {
        val intent = Intent(this, AgregaVacunaActivity::class.java)
        intent.putExtra("ID", ""+id_cerdo);
        startActivity(intent)
    }

    fun mostrarPesoCerdo() {
        //entries.clear()
        val task = Runnable {
            var salida=   util.stringLenCenter("NO",4) +
                    util.stringLenCenter("Fecha",14)+
                    util.stringLenCenter("  Peso",10)+"\r\n"
            var pesos =
                    mDb?.pesoDao()?.getLast(id_cerdo,5)
            mUiHandler.post {
                var i=0
                if (!(pesos == null && pesos?.size == 0)) {
                    for (item in pesos!!) {
                        i++
                                 salida+=util.stringLenCenter(""+i,4) +
                                util.stringLenCenter(util.timeStampToString(item.fecha),14)+
                                util.stringLenLeft(String.format("%.2f",item.peso)+" kgs",10)+"\r\n"
                    }

                }
                lbldcPesoH.setText(salida)
            }
        }
        mDbWorkerThread.postTask(task)
    }

    fun mostrarVacunaCerdo() {
        val task = Runnable {
            var salida=   util.stringLenCenter("NO",4) +
                     util.stringLenCenter("Fecha",14)+
                    util.stringLen("  Nombre",20)+"\r\n"
            var vacunas =
                mDb?.vacunaDao()?.getLast(id_cerdo,5)
            mUiHandler.post {
                var i=0
                if (!(vacunas == null && vacunas?.size == 0)) {
                    for (item in vacunas!!) {
                        i++
                                 salida+=util.stringLenCenter(""+i,4) +
                                util.stringLenCenter(util.timeStampToString(item.fecha),14)+
                                util.stringLen(item.nombre,20)+"\r\n"
                    }
                }
                lbldcVacunas.setText(salida)
            }
        }
        mDbWorkerThread.postTask(task)
    }

    fun verListaPesos_onClick(view: View){
        val intent = Intent(this, ListaPesosActivity::class.java)
        intent.putExtra("ID", ""+id_cerdo);
        startActivity(intent)
    }

    fun verListaVacunas_onClick(view: View){
        val intent = Intent(this, ListaVacunasActivity::class.java)
        intent.putExtra("ID", ""+id_cerdo);
        startActivity(intent)
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
            Thread.sleep(1000)
        }
        mostrarDetalle()
    }
}
