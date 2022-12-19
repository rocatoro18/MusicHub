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
import com.google.gson.Gson
import com.rocatoro.musichub.R
import com.rocatoro.musichub.activities.MusicHubStore.home.MusicHubHomeActivity
import com.rocatoro.musichub.activities.client.address.list.ClientAddressListActivity
import com.rocatoro.musichub.activities.client.home.ClientHomeActivity
import com.rocatoro.musichub.activities.client.orders.detail.ClientOrdersDetailActivity
import com.rocatoro.musichub.activities.client.products.list.ClientProductsListActivity
import com.rocatoro.musichub.activities.delivery.home.DeliveryHomeActivity
import com.rocatoro.musichub.models.*
import com.rocatoro.musichub.utils.SharedPref

class EstadoPedidoClientAdapter(val context: Activity, val estado_pedidos: ArrayList<Estado_Transporte>): RecyclerView.Adapter<EstadoPedidoClientAdapter.EstadoPedidoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EstadoPedidoViewHolder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_estado_pedido,parent,false)

        return EstadoPedidoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return estado_pedidos.size
    }

    override fun onBindViewHolder(holder:EstadoPedidoViewHolder,position: Int) {
        val pedido = estado_pedidos[position] // EACH ADDRESS

        holder.textViewNumeroVenta.text = "Número Venta # ${pedido.numero_venta}"
        if(pedido.numero_guia == 0){
            holder.textViewNumeroGuia.text = "ESPERANDO ACTUALIZACIÓN POR TRANSPORTISTA"
        } else {
            holder.textViewNumeroGuia.text = "${pedido.numero_guia}"
        }
        if(pedido.estado_transporte == null){
            holder.textViewEstadoTransporte.text = "ESPERANDO ACTUALIZACIÓN POR TRANSPORTISTA"
        } else {
            holder.textViewEstadoTransporte.text = "${pedido.estado_transporte}"
        }


        /*
        holder.itemView.setOnClickListener {
            goToOrderDetail(order)
        }
        */

    }

    private fun goToOrderDetail(order: Order){
        val i = Intent(context,ClientOrdersDetailActivity::class.java)
        i.putExtra("order",order.toJson())
        context.startActivity(i)
    }

    class EstadoPedidoViewHolder(view: View): RecyclerView.ViewHolder(view){

        val textViewNumeroVenta: TextView
        val textViewNumeroGuia: TextView
        val textViewEstadoTransporte: TextView

        init {
            textViewNumeroVenta = view.findViewById(R.id.textview_numero_venta)
            textViewNumeroGuia = view.findViewById(R.id.textview_numero_guia)
            textViewEstadoTransporte = view.findViewById(R.id.textview_estado_transporte)
        }

    }

}