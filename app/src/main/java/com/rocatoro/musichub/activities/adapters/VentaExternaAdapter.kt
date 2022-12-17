package com.rocatoro.musichub.activities.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rocatoro.musichub.R
import com.rocatoro.musichub.activities.MusicHubStore.home.MusicHubHomeActivity
import com.rocatoro.musichub.activities.client.home.ClientHomeActivity
import com.rocatoro.musichub.activities.client.products.list.ClientProductsListActivity
import com.rocatoro.musichub.activities.delivery.home.DeliveryHomeActivity
import com.rocatoro.musichub.models.Category
import com.rocatoro.musichub.models.Rol
import com.rocatoro.musichub.models.VentaExterna
import com.rocatoro.musichub.utils.SharedPref

class VentaExternaAdapter(val context: Activity, val ventas: ArrayList<VentaExterna>): RecyclerView.Adapter<VentaExternaAdapter.VentaExternaViewHolder>() {

    //val sharedPref = SharedPref(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VentaExternaViewHolder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_venta_externa_musichub,parent,false)

        return VentaExternaViewHolder(view)
    }

    override fun getItemCount(): Int {
        return ventas.size
    }

    override fun onBindViewHolder(holder:VentaExternaViewHolder,position: Int) {
        val venta = ventas[position] // EACH CATEGORY

        holder.textViewVentaExterna.text = "Venta ID #${venta.id}"
        holder.textViewName.text = venta.nombre_producto
        holder.textViewQuantity.text = venta.cantidad_producto.toString()
        holder.textViewTotalAmount.text = venta.monto_total
        holder.textViewNoSerie.text = venta.no_serie_producto

    //Glide.with(context).load(category.image).into(holder.imageViewCategory)

        /*
        holder.itemView.setOnClickListener{
            goToProducts(category)
        }
         */


    }

    class VentaExternaViewHolder(view: View): RecyclerView.ViewHolder(view){

        val textViewVentaExterna: TextView
        val textViewName: TextView
        val textViewQuantity: TextView
        val textViewTotalAmount: TextView
        val textViewNoSerie: TextView

        init {
            textViewVentaExterna = view.findViewById(R.id.textview_venta_id)
            textViewName = view.findViewById(R.id.textview_name)
            textViewQuantity = view.findViewById(R.id.textview_quantity)
            textViewTotalAmount = view.findViewById(R.id.textview_total_amount)
            textViewNoSerie = view.findViewById(R.id.textview_no_serie)
        }

    }

}