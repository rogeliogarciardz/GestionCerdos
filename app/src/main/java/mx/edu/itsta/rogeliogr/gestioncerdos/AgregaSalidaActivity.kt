package mx.edu.itsta.rogeliogr.gestioncerdos

import android.app.DatePickerDialog
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_agrega_salida.*
import mx.edu.itsta.rogeliogr.gestioncerdos.Entidades.Cerdo
import mx.edu.itsta.rogeliogr.gestioncerdos.Entidades.GCDataBase
import mx.edu.itsta.rogeliogr.gestioncerdos.Entidades.Salida
import mx.edu.itsta.rogeliogr.gestioncerdos.utileria.DbWorkerThread
import mx.edu.itsta.rogeliogr.gestioncerdos.utileria.util
import java.util.*

class AgregaSalidaActivity : AppCompatActivity() {

    private var mDb: GCDataBase? = null
    private lateinit var mDbWorkerThread: DbWorkerThread
    private val mUiHandler = Handler()
    private var id_cerdo: Long = 0
    var fechap = Date()
    val c = Calendar.getInstance()
    val year = c.get(Calendar.YEAR)
    val month = c.get(Calendar.MONTH)
    val day = c.get(Calendar.DAY_OF_MONTH)
    var cerdo: Cerdo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agrega_salida)

        mDbWorkerThread = DbWorkerThread("dbWorkerThread")
        mDbWorkerThread.start()

        mDb = GCDataBase.getInstance(this)


        txtscFecha.text = "" + day + "-" + (month+1) + "-" + year
        fechap = util.stringtoDate("" + day + "-" + (month+1) + "-" + year)
        var msg = intent.getStringExtra("ID")
        id_cerdo = msg.toLong()

        obtenerInfoCerdo(id_cerdo)
    }

    fun txtscFecha_onClick(view: View){

        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            // Display Selected date in textbox
            txtscFecha.text = "" + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year
            fechap = util.stringtoDate("" + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year)
            //toastError("F: "+fechap.toString() )
        }, year, month, day)
        dpd.show()
    }

    fun btnAgregarSalida_onClick(view: View){
        var smotivo:String

        smotivo = txtscmotivo.text.toString()

        if(smotivo.isEmpty()){
            util.toastError("Escribe un motivo",this)
            return
        }


        var salidavo = Salida()
        salidavo.motivo=smotivo
        salidavo.observaciones = txtscobservaciones.text.toString()
        salidavo.fecha=fechap.time
        salidavo.id_cerdo=id_cerdo
        salidavo.numero=cerdo?.numero!!
        salidavo.f_nac=cerdo?.f_nac!!

        val task = Runnable { mDb?.salidaDao()?.insert(salidavo)
            mUiHandler.post {

                val task = Runnable { mDb?.cerdoDao()?.deleteById(id_cerdo)
                    mUiHandler.post {
                        util.toastError("Cerdo retirado de la granja",this)
                        onBackPressed()
                    }
                }
                mDbWorkerThread.postTask(task)

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

    fun obtenerInfoCerdo(idc:Long){
        val task = Runnable {
            cerdo = mDb?.cerdoDao()?.getByID(idc)
            mUiHandler.post {
                util.toastError("OK!!"+cerdo?.numero!!,this)
            }
        }
        mDbWorkerThread.postTask(task)
    }
}
