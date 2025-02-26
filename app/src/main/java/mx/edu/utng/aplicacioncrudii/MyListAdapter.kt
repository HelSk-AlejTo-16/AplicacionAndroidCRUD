package mx.edu.utng.aplicacioncrudii

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import android.app.Activity

class MyListAdapter(
    private val context: Activity,
    private var discos: List<EmpModelClass> // Cambiamos a una lista de objetos EmpModelClass
) : RecyclerView.Adapter<MyListAdapter.ViewHolder>() {

    // ViewHolder: Contiene las referencias a las vistas de cada ítem
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val idText: TextView = itemView.findViewById(R.id.textViewId)
        val nameText: TextView = itemView.findViewById(R.id.textViewName)
        val artistText: TextView = itemView.findViewById(R.id.textViewArtista)
        val generoText: TextView = itemView.findViewById(R.id.textViewGenero)
        val fechaText: TextView = itemView.findViewById(R.id.textViewFecha)
        val precioText: TextView = itemView.findViewById(R.id.textViewPrecio)
        val imageView: ImageView = itemView.findViewById(R.id.imageViewAlbum)
    }

    // Inflar la vista de cada ítem
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.custom_list, parent, false) // Usamos custom_list.xml
        return ViewHolder(view)
    }

    // Asignar datos a las vistas
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val disco = discos[position]

        holder.idText.text = "Id: ${disco.userId}"
        holder.nameText.text = "Nombre: ${disco.userName}"
        holder.artistText.text = "Artista: ${disco.userArtist}"
        holder.generoText.text = "Género: ${disco.userGenero}"
        holder.fechaText.text = "Fecha: ${disco.userFechadelanzamiento}"
        holder.precioText.text = "Precio: $${disco.userPrecio}"

        // Cargar la imagen con Glide
        Glide.with(context as Activity)
            .load(disco.userImagen)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.error)
            .into(holder.imageView)
    }
    // Retornar el número de ítems
    override fun getItemCount(): Int {
        return discos.size
    }

    // Método para actualizar los datos
    fun updateData(newData: List<EmpModelClass>) {
        discos = newData
        notifyDataSetChanged() // Notificar al RecyclerView que los datos han cambiado
    }
}