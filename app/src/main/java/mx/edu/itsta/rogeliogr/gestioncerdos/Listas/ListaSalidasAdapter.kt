package mx.edu.itsta.rogeliogr.gestioncerdos.Listas

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.salida_lista.view.*
import mx.edu.itsta.rogeliogr.gestioncerdos.Entidades.Salida
import mx.edu.itsta.rogeliogr.gestioncerdos.R
import mx.edu.itsta.rogeliogr.gestioncerdos.utileria.util


class ListaSalidasAdapter(val items : List<Salida>, val context: Context, val clickListener: (Salida) -> Unit ) : RecyclerView.Adapter<ViewSalidaHolder>() {

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewSalidaHolder {
        return ViewSalidaHolder(
            LayoutInflater.from(context).inflate(
                R.layout.salida_lista,
                parent,
                false))
    }

    override fun onBindViewHolder(holder: ViewSalidaHolder, position: Int) {
        holder.bind(items.get(position), clickListener, context)
        //holder?.nombreFormula?.text = items.get(position)

    }
}

class ViewSalidaHolder (view: View) : RecyclerView.ViewHolder(view) {
    fun bind(
        item: Salida,
        clickListener: (Salida) -> Unit,
        context: Context
    ) {
        //var tipo:Array<String> = context.resources.getStringArray(R.array.tipo_cerdo)// arrayOf("SEMENTAL","VIENTRE","MACHO DE ENGORDA","HEMBRA DE REMPLAZO")


        itemView.lbllsalidanumero.text =  "NO: "+item.numero
        itemView.lbllsalidaid.text = "ID: "+item.id_salida
        itemView.lbllsalidafechanacimiento.text ="Nacimiento: "+ util.timeStampToString( item.f_nac)
        itemView.lbllsalidamotivos.text = "Motivo:" + item.motivo
        itemView.lbllsalidafechasalida.text= "Fecha de salida: "+ util.timeStampToString(item.fecha)
        itemView.lbllsalidaobservaciones.text = "Observaciones: "+item.observaciones
        //itemView.imvImagen.setImageResource(item.imagen)
        itemView.setOnClickListener { clickListener(item) }


    }
}