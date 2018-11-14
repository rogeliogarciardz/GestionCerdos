package mx.edu.itsta.rogeliogr.gestioncerdos

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import kotlinx.android.synthetic.main.activity_detalle_cerdo.*
import kotlinx.android.synthetic.main.content_detalle_cerdo.*
import mx.edu.itsta.rogeliogr.gestioncerdos.utileria.util

class DetalleCerdoActivity : AppCompatActivity() {


    private var mDb: GCDataBase? = null
    private lateinit var mDbWorkerThread: DbWorkerThread
    private val mUiHandler = Handler()
    private var id_cerdo: Long = 0
    val entries :ArrayList<BarEntry> = ArrayList()


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

        var msg = intent.getStringExtra("ID")
        id_cerdo = msg.toLong()

        Thread.sleep(1000)

        mostrarDetalle()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.detalle_cerdo_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_add_peso -> {
            agregarPeso()
            true
        }

        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }


    fun mostrarDetalle() {
        val task = Runnable {
            var cerdo = mDb?.cerdoDao()?.getByID(id_cerdo)
            mUiHandler.post {
                if (cerdo != null) {
                    lbldcID.text = "ID: " + cerdo?.id_cerdo
                    lbldcNumero.text = "NO: " + cerdo?.numero
                    lbldcNombre.text = "Nombre: " + cerdo?.nombre
                    lbldcFecha.text = "Nacimiento: " + util.timestampToString( cerdo?.f_nac)
                    lbldcPeso.text = "Peso: " + cerdo?.peso + " kg"
                    lbldcTipo.text = resources.getStringArray(R.array.tipo_cerdo).get(cerdo?.tipo) + " (" +
                            cerdo?.sexo + ")"

                    mostrarPesoCerdo()

                } else {
                    util.toastError("NO entontrado " + id_cerdo,this)
                }

            }
        }
        mDbWorkerThread.postTask(task)

    }

    fun agregarPeso() {
       /* if (id_cerdo > 0) {
            var peso = Peso()
            peso.id_cerdo = id_cerdo

            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            peso.fecha = "" + day + "-" + (month + 1) + "-" + year
            peso.peso = 123.50

            val task = Runnable {
                mDb?.pesoDao()?.insert(peso)
                mUiHandler.post {
                    mostrarPesoCerdo()
                }
            }
            mDbWorkerThread.postTask(task)

        }*/

        val intent = Intent(this, AgregarPesoActivity::class.java)
        intent.putExtra("ID", ""+id_cerdo);
        startActivity(intent)
    }

    fun mostrarPesoCerdo() {
        entries.clear()
        val task = Runnable {
            var salida=   "[   Fecha  ]\n"
            var salida2 = "[  Peso  ]\n"
            var pesos =
                    mDb?.pesoDao()?.getLast(id_cerdo)
            mUiHandler.post {
                var i=0
                if (!(pesos == null && pesos?.size == 0)) {
                    for (item in pesos!!) {
                        i++
                        //salida+="["+ util.timestampToString(item.fecha) + "]\t["+ item.peso + " kgs]\r\n"
                        //salida+= String.format("[ %s ][ %-2.00f kgs ]\r\n",util.timestampToString(item.fecha), item.peso)
                        salida+=util.timestampToString(item.fecha)+"\r\n"
                        salida2+=String.format("%.2f",item.peso)+" kgs"+"\r\n"

                        entries.add(BarEntry( i.toFloat() , item.peso.toFloat()))

                    }

                }
                lbldcPesoH.setText(salida)
                lbldcPesoH2.setText(salida2)
                mostrarGraficaPeso()
            }
        }
        mDbWorkerThread.postTask(task)

    }

    fun mostrarGraficaPeso(){
        // in this example, a LineChart is initialized from xml
        var barChart = chartPesos




        val dataset = BarDataSet(entries,"Fecha")

      /*  val labels:ArrayList<String> = ArrayList()
        labels.add("Paquete #1")
        labels.add("Paquete #2")
        labels.add("Paquete #3")
        labels.add("Paquete #4")
        labels.add("Paquete #4")
*/

        val data = BarData( dataset)
        barChart.data = data

        //barChart.labelFor = labels
        //dataset.setColors(ColorTemplate.COLORFUL_COLORS)
        var desc = Description()
        desc.text = "Peso"
        barChart.description =desc

        barChart.animateY(3000)
        barChart.setFitBars(false)

    }

    fun verListaPesos_onClick(view: View){
        val intent = Intent(this, ListaPesosActivity::class.java)
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
        mostrarDetalle()
    }
}
