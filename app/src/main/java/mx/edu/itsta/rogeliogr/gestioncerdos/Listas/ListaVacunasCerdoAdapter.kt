package mx.edu.itsta.rogeliogr.gestioncerdos.Listas

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.vacuna_lista.view.*
import mx.edu.itsta.rogeliogr.gestioncerdos.Entidades.Vacuna
import mx.edu.itsta.rogeliogr.gestioncerdos.R
import mx.edu.itsta.rogeliogr.gestioncerdos.utileria.util


class ListaVacunasCerdoAdapter(val items : List<Vacuna>, val context: Context, val clickListener: (Vacuna) -> Unit) : RecyclerView.Adapter<ViewHolderLV>() {


    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderLV {
        return ViewHolderLV(
            LayoutInflater.from(context).inflate(
                R.layout.vacuna_lista,
                parent,
                false))
    }

    override fun onBindViewHolder(holder: ViewHolderLV, position: Int) {
        holder.bind(items.get(position), clickListener)
    }
}

class ViewHolderLV (view: View) : RecyclerView.ViewHolder(view) {

    fun bind(item: Vacuna, clickListener: (Vacuna) -> Unit) {

        itemView.lblvaclFecha.text = "Fecha\r\n" + util.timeStampToString( item.fecha)
        itemView.lblvaclNombre.text = "Vacuna\r\n "+item.nombre
        itemView.lblvaclObservaciones.text = "Observaciones:\r\n" + item.observaciones
        itemView.setOnClickListener{ clickListener(item)  }
    }
}