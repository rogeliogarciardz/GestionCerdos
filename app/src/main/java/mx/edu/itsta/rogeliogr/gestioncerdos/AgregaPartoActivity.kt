package mx.edu.itsta.rogeliogr.gestioncerdos

import android.app.DatePickerDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import kotlinx.android.synthetic.main.activity_agrega_parto.*
import kotlinx.android.synthetic.main.activity_agrega_reproduccion.*
import mx.edu.itsta.rogeliogr.gestioncerdos.Entidades.GCDataBase
import mx.edu.itsta.rogeliogr.gestioncerdos.Entidades.Parto
import mx.edu.itsta.rogeliogr.gestioncerdos.Entidades.Reproduccion
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


        txtpartoFecha.text = "" + day + "-" + (month+1) + "-" + year
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

    fun btnAgregarParto_onClick(view: View){

        var no_crias:Int
        var no_crias_vivas: Int
        var pesos_crias=""
        var observa=""
        var promedio:Double
        var i:Int

        if(txtpartoCrias.text.toString().isEmpty()){
            util.toastError("Escribe el número de crías",this)
            return
        }
        try{
            no_crias = txtpartoCrias.text.toString().toInt()
        }catch (ex:Exception){
            util.toastError("Escribe el numero de semental valido",this)
            return
        }

        if(txtpartoCriasVivas.text.toString().isEmpty()){
            util.toastError("Escribe el número de crías vivas",this)
            return
        }
        try{
            no_crias_vivas = txtpartoCriasVivas.text.toString().toInt()
        }catch (ex:Exception){
            util.toastError("Escribe el numero de semental valido",this)
            return
        }

        if(txtpartoObservaciones.text.toString().isEmpty()){
            util.toastError("Escribe una observación",this)
             return
        }else{
            observa = txtpartoObservaciones.text.toString()
        }
        if(txtpartoPesoCrias.text.toString().isEmpty()){
            util.toastError("Escribe el peso de las crias vivas",this)
            return
        }else{
            pesos_crias = txtpartoPesoCrias.text.toString()
        }

        var result: List<Int> = emptyList()

        try{
            result = pesos_crias.split(",").map { it.toInt() }
        }catch(e:Exception){
            util.toastError("Escribe el peso de las crias vivas separadas por coma",this)
        }

        i=0
        promedio =0.0

        for (item in result) {
            promedio += item
            i++
        }

        promedio/=i


        var partovo = Parto()
        partovo.id_cerdo=id_cerdo
        partovo.id_reproduccion=1
        partovo.no_crias = no_crias
        partovo.vivas = no_crias_vivas
        partovo.muertas =  (no_crias - no_crias_vivas).toLong()
        partovo.fecha_parto=fechaparto.time
        partovo.observaciones = observa
        partovo.peso_crias=pesos_crias
        partovo.promedio_pesos = promedio



        val task = Runnable { mDb?.partoDao()?.insert(partovo)
            mUiHandler.post {
                util.toastError("Parto agregado",this)
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
