package mx.edu.itsta.rogeliogr.gestioncerdos

import android.app.DatePickerDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import kotlinx.android.synthetic.main.activity_agregar_peso.*
import mx.edu.itsta.rogeliogr.gestioncerdos.Entidades.Peso
import mx.edu.itsta.rogeliogr.gestioncerdos.utileria.util
import java.lang.NumberFormatException
import java.util.*

class AgregarPesoActivity : AppCompatActivity() {

    private var mDb: GCDataBase? = null
    private lateinit var mDbWorkerThread: DbWorkerThread
    private val mUiHandler = Handler()
    private var id_cerdo: Long = 0
    var fechap = Date()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_peso)

        mDbWorkerThread = DbWorkerThread("dbWorkerThread")
        mDbWorkerThread.start()

        mDb = GCDataBase.getInstance(this)

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        txtapFecha.text = "" + day + "-" + (month+1) + "-" + year
        fechap = util.stringtoDate("" + day + "-" + (month+1) + "-" + year)
        var msg = intent.getStringExtra("ID")
        id_cerdo = msg.toLong()

    }

    fun txtapFecha_onClick(view: View){
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)


        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            // Display Selected date in textbox
            txtapFecha.text = "" + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year
            fechap = util.stringtoDate("" + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year)
            //toastError("F: "+fechap.toString() )
        }, year, month, day)
        dpd.show()
    }

    fun btnAgregarPeso_onClick(view:View){

        var peso=0.0
        if(txtapPeso.text.toString().length<=0){
            util.toastError("Debe escribir el nombre del cerdo",this )
            txtapPeso.requestFocus()
            return
        }
        try {
            peso = txtapPeso.text.toString().toDouble()
        }catch(ex: NumberFormatException){
            util.toastError("El peso es un debe ser positivo",this)
            txtapPeso.requestFocus()
            return
        }
        if(peso <=0 ){
            util.toastError("El peso es un debe ser positivo",this)
            txtapPeso.requestFocus()
            return
        }
        var pesovo = Peso()
        pesovo.peso=peso
        pesovo.fecha=fechap.time
        pesovo.id_cerdo=id_cerdo

        val task = Runnable { mDb?.pesoDao()?.insert(pesovo)
            mUiHandler.post {
                this.onBackPressed()
            }
        }
        mDbWorkerThread.postTask(task)

    }



}
