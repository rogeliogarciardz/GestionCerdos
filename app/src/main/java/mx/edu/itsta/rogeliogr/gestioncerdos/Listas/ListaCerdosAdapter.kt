package mx.edu.itsta.rogeliogr.gestioncerdos.Listas

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.cerdo_lista.view.*
import mx.edu.itsta.rogeliogr.gestioncerdos.Entidades.Cerdo
import mx.edu.itsta.rogeliogr.gestioncerdos.R
import mx.edu.itsta.rogeliogr.gestioncerdos.utileria.util

class ListaCerdosAdapter(val items : List<Cerdo>, val context: Context, val clickListener: (Cerdo) -> Unit) : RecyclerView.Adapter<ViewHolder>() {


    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(
            R.layout.cerdo_lista,
            parent,
            false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items.get(position), clickListener,context)
        //holder?.nombreFormula?.text = items.get(position)
    }
}

class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {

    fun bind(item:Cerdo, clickListener: ( Cerdo) -> Unit,context: Context) {
        var tipo:Array<String> = context.resources.getStringArray(R.array.tipo_cerdo)// arrayOf("SEMENTAL","VIENTRE","MACHO DE ENGORDA","HEMBRA DE REMPLAZO")

        itemView.lblID.text = "ID: "+item.id_cerdo
        itemView.lblNumero.text = "NO: "+item.numero
        itemView.lblNombre.text = "Nombre: "+item.nombre
        itemView.lblTipo.text = "["+  tipo[item.tipo]+"] ("+ item.sexo +")"
        itemView.lblFnac.text = "Nacimiento: "+ util.timestampToString( item.f_nac)
        itemView.lblPeso.text = "Peso: "+item.peso+" kg"
        //itemView.imvImagen.setImageResource(item.imagen)
        itemView.setOnClickListener { clickListener(item) }
    }
}