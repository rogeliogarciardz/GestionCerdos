package mx.edu.itsta.rogeliogr.gestioncerdos

import android.app.DatePickerDialog
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_agrega_vacuna.*
import mx.edu.itsta.rogeliogr.gestioncerdos.Entidades.GCDataBase
import mx.edu.itsta.rogeliogr.gestioncerdos.Entidades.Vacuna
import mx.edu.itsta.rogeliogr.gestioncerdos.utileria.DbWorkerThread
import mx.edu.itsta.rogeliogr.gestioncerdos.utileria.util
import java.util.*

class AgregaVacunaActivity : AppCompatActivity() {

    private var mDb: GCDataBase? = null
    private lateinit var mDbWorkerThread: DbWorkerThread
    private val mUiHandler = Handler()
    private var id_cerdo: Long = 0
    var fechap = Date()
    val c = Calendar.getInstance()
    val year = c.get(Calendar.YEAR)
    val month = c.get(Calendar.MONTH)
    val day = c.get(Calendar.DAY_OF_MONTH)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agrega_vacuna)


        mDbWorkerThread = DbWorkerThread("dbWorkerThread")
        mDbWorkerThread.start()

        mDb = GCDataBase.getInstance(this)

        txtavFecha.setText( "" + day + "-" + (month+1) + "-" + year)
        fechap = util.stringtoDate("" + day + "-" + (month+1) + "-" + year)

        var msg = intent.getStringExtra("ID")
        id_cerdo = msg.toLong()

    }

    fun txtavFecha_onClick(view: View){


        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            // Display Selected date in textbox
            txtavFecha.setText( "" + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year)
            fechap = util.stringtoDate("" + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year)
            //toastError("F: "+fechap.toString() )
        }, year, month, day)
        dpd.show()
    }

    fun btnAgregarVacuna_onClick(view:View){


        var vacunavo = Vacuna()

        if(txtavNombre.text.isEmpty()){
            util.toastError("Debe escribir un nombre de vacuna",this)
            txtavNombre.requestFocus()
            return
        }


        vacunavo.id_cerdo = id_cerdo
        vacunavo.fecha=fechap.time
        vacunavo.nombre = txtavNombre.text.toString()
        vacunavo.observaciones = txtavObservaciones.text.toString()

        val task = Runnable { mDb?.vacunaDao()?.insert(vacunavo)
            mUiHandler.post {
                this.onBackPressed()
            }
        }
        mDbWorkerThread.postTask(task)

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
