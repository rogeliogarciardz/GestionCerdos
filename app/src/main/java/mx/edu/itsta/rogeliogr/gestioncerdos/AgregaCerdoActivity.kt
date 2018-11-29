package mx.edu.itsta.rogeliogr.gestioncerdos

import android.app.DatePickerDialog
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_agrega_cerdo.*
import kotlinx.android.synthetic.main.activity_agregar_peso.*
import mx.edu.itsta.rogeliogr.gestioncerdos.Entidades.Cerdo
import mx.edu.itsta.rogeliogr.gestioncerdos.utileria.util
import java.lang.NumberFormatException
import java.util.*


class AgregaCerdoActivity : AppCompatActivity() {

    private var mDb: GCDataBase? = null
    private lateinit var mDbWorkerThread: DbWorkerThread
    private val mUiHandler = Handler()
    private var fn = Date()

    val c = Calendar.getInstance()
    val year = c.get(Calendar.YEAR)
    val month = c.get(Calendar.MONTH)
    val day = c.get(Calendar.DAY_OF_MONTH)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agrega_cerdo)
        mDbWorkerThread = DbWorkerThread("dbWorkerThread")
        mDbWorkerThread.start()

        mDb = GCDataBase.getInstance(this)

        val adapterTipoCerdo = ArrayAdapter.createFromResource(
            this,
            R.array.tipo_cerdo, android.R.layout.simple_spinner_item
        )
        adapterTipoCerdo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spTipoCerdo.adapter = adapterTipoCerdo

        val adapterSexo = ArrayAdapter.createFromResource(
            this,
            R.array.sexo, android.R.layout.simple_spinner_item
        )
        adapterSexo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spSexo.adapter = adapterSexo

        spTipoCerdo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                //Toast.makeText(applicationContext, "TX: "+p2, Toast.LENGTH_LONG).show()
                spSexo.isEnabled=false
                if(p2==0 || p2==2){
                    spSexo.setSelection(0)
                }else{

                    spSexo.setSelection(1)
                }
            }

        }

        txtFecha.setText( "" + day + "-" + (month+1) + "-" + year)
        fn = util.stringtoDate("" + day + "-" + (month+1) + "-" + year)


    }

    fun agregar_onClick(view: View){
        var numero=0

        if(txtNumero.text.toString().isEmpty()){
            toastError("Debe escribir un número positivo")
            txtNumero.requestFocus()
            return
        }
        try {
            numero = txtNumero.text.toString().toInt()
        }catch(ex:NumberFormatException){
            toastError("Debe escribir un número positivo")
            txtNumero.requestFocus()
            return
        }
        if(numero <=0 ){
            toastError("Debe escribir un número positivo")
            txtNumero.requestFocus()
            return
        }

        val task = Runnable {
            var cerdos = mDb?.cerdoDao()?.getByNumero(numero)
            mUiHandler.post {
                if (cerdos == null || cerdos?.size == 0) {
                    insertarCerdo(numero)
                }else{
                    toastError("Número de cerdo ya registrado")
                }
            }
        }
        mDbWorkerThread.postTask(task)


    }

    fun insertarCerdo(numero:Int){
        var peso=0
        if(txtNombre.text.length<=0){
            //toastError("Debe escribir el nombre del cerdo")
            txtNombre.setText("Cerdo "+numero)
            //txtNombre.requestFocus()
            //return
        }

        if(txtPeso.text.toString().length<=0){
            toastError("Debe escribir el peso del cerdo")
            txtPeso.requestFocus()
            return
        }
        try {
            peso = txtPeso.text.toString().toInt()
        }catch(ex:NumberFormatException){
            toastError("El peso es un debe ser positivo")
            txtPeso.requestFocus()
            return
        }
        if(peso <=0 ){
            toastError("El peso es un debe ser positivo")
            txtPeso.requestFocus()
            return
        }

        var cerdo  = Cerdo()
        cerdo.nombre=  txtNombre.text.toString()
        cerdo.numero=  numero
        cerdo.peso= txtPeso.text.toString().toDouble()

        cerdo.tipo=spTipoCerdo.selectedItemId.toInt()

        if(spSexo.selectedItemPosition==1||spSexo.selectedItemPosition==3) {
            cerdo.sexo = "H"
        }else{
            cerdo.sexo="M"
        }
        cerdo.f_nac=fn.time

        if(txtFecha.text.toString().length<=0){
            toastError("Debe escribir o seleccionar una fecha de nacimiento del cerdo")
            txtFecha.requestFocus()
            return
        }

        val task = Runnable { mDb?.cerdoDao()?.insert(cerdo)
            mUiHandler.post {
                this.onBackPressed()
            }
        }
        mDbWorkerThread.postTask(task)
    }

    fun txtFecha_onClick(view:View){


        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            // Display Selected date in textbox
            txtFecha.setText( "" + dayOfMonth + "-" + (monthOfYear+1) + "-" + year)
            fn = util.stringtoDate("" + dayOfMonth + "-" + (monthOfYear+1) + "-" + year)
        }, year, month, day)
        dpd.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        GCDataBase.destroyInstance()
        mDbWorkerThread.quit()
    }

    fun toastError(msg:String){
        Toast.makeText(this,"(!) "+msg, Toast.LENGTH_SHORT).show()
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
