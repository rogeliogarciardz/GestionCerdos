package mx.edu.itsta.rogeliogr.gestioncerdos

import android.app.DatePickerDialog
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_agrega_reproduccion.*
import mx.edu.itsta.rogeliogr.gestioncerdos.Entidades.GCDataBase
import mx.edu.itsta.rogeliogr.gestioncerdos.Entidades.Reproduccion
import mx.edu.itsta.rogeliogr.gestioncerdos.utileria.DbWorkerThread
import mx.edu.itsta.rogeliogr.gestioncerdos.utileria.util
import java.util.*

class AgregaReproduccionActivity : AppCompatActivity() {

    private var mDb: GCDataBase? = null
    private lateinit var mDbWorkerThread: DbWorkerThread
    private val mUiHandler = Handler()
    private var id_cerdo: Long = 0
    var fechamonta = Date()
    var fechacelo = Date()
    var fechadestete = Date()
    val c = Calendar.getInstance()
    val year = c.get(Calendar.YEAR)
    val month = c.get(Calendar.MONTH)
    val day = c.get(Calendar.DAY_OF_MONTH)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agrega_reproduccion)

        mDbWorkerThread = DbWorkerThread("dbWorkerThread")
        mDbWorkerThread.start()

        mDb = GCDataBase.getInstance(this)


        txtrepfechacelo.text = "" + day + "-" + (month+1) + "-" + year
        txtrepfechamonta.text =  "" + day + "-" + (month+1) + "-" + year
        txtdrepfdestete.text = "" + day + "-" + (month+1) + "-" + year
        fechacelo = util.stringtoDate("" + day + "-" + (month+1) + "-" + year)
        fechamonta= util.stringtoDate("" + day + "-" + (month+1) + "-" + year)
        fechadestete = util.stringtoDate("" + day + "-" + (month+1) + "-" + year)


        var msg = intent.getStringExtra("ID")
        id_cerdo = msg.toLong()
    }

    fun txtrepfechamonta_onClick(view: View){

        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            // Display Selected date in textbox
            txtrepfechamonta.text = "" + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year
            fechamonta = util.stringtoDate("" + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year)
            //toastError("F: "+fechap.toString() )
        }, year, month, day)
        dpd.show()
    }

    fun txtrepfechacelo_onClick(view: View){

        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            // Display Selected date in textbox
            txtrepfechacelo.text = "" + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year
            fechacelo = util.stringtoDate("" + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year)
            //toastError("F: "+fechap.toString() )
        }, year, month, day)
        dpd.show()
    }

    fun txtrepfechadestete_onClick(view: View){

        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            // Display Selected date in textbox
            txtdrepfdestete.text = "" + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year
            fechadestete = util.stringtoDate("" + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year)
            //toastError("F: "+fechap.toString() )
        }, year, month, day)
        dpd.show()
    }



    fun btnAgregarSalida_onClick(view: View){
        var snosemental:Long


        if(txtrepnosemental.text.toString().isEmpty()){
            util.toastError("Escribe el numero del semental",this)
            return
        }

        try{
            snosemental = txtrepnosemental.text.toString().toLong()
        }catch (ex:Exception){
            util.toastError("Escribe el numero de semental valido",this)
            return
        }

        var reproduccionvo = Reproduccion()
        reproduccionvo.no_semental=snosemental
        reproduccionvo.fecha_celo = fechacelo.time
        reproduccionvo.fecha_monta = fechamonta.time
        reproduccionvo.fecha_destete = fechadestete.time
        reproduccionvo.id_cerdo=id_cerdo

        val task = Runnable { mDb?.reproduccionDao()?.insert(reproduccionvo)
            mUiHandler.post {
               util.toastError("Reproducción agregada",this)
                onBackPressed()
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
    }
}
