package mx.edu.itsta.rogeliogr.gestioncerdos

import android.app.DatePickerDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import kotlinx.android.synthetic.main.activity_agrega_parto.*
import kotlinx.android.synthetic.main.activity_agrega_reproduccion.*
import mx.edu.itsta.rogeliogr.gestioncerdos.Entidades.GCDataBase
import mx.edu.itsta.rogeliogr.gestioncerdos.utileria.DbWorkerThread
import mx.edu.itsta.rogeliogr.gestioncerdos.utileria.util
import java.util.*

class AgregaPartoActivity : AppCompatActivity() {

    private var mDb: GCDataBase? = null
    private lateinit var mDbWorkerThread: DbWorkerThread
    private val mUiHandler = Handler()
    private var id_cerdo: Long = 0
    var fechaparto = Date()

    val c = Calendar.getInstance()
    val year = c.get(Calendar.YEAR)
    val month = c.get(Calendar.MONTH)
    val day = c.get(Calendar.DAY_OF_MONTH)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agrega_parto)

        mDbWorkerThread = DbWorkerThread("dbWorkerThread")
        mDbWorkerThread.start()

        mDb = GCDataBase.getInstance(this)


        txtrepfechacelo.text = "" + day + "-" + (month+1) + "-" + year
        txtrepfechamonta.text =  "" + day + "-" + (month+1) + "-" + year
        txtdrepfdestete.text = "" + day + "-" + (month+1) + "-" + year
        fechaparto = util.stringtoDate("" + day + "-" + (month+1) + "-" + year)

        var msg = intent.getStringExtra("ID")
        id_cerdo = msg.toLong()
    }

    fun txtpartoFecha_OnClick(view: View){
        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            // Display Selected date in textbox
            txtpartoFecha.text = "" + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year
            fechaparto = util.stringtoDate("" + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year)
            //toastError("F: "+fechap.toString() )
        }, year, month, day)
        dpd.show()
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
