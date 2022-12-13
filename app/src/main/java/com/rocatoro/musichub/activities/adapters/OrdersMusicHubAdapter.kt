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
import com.rocatoro.musichub.activities.MusicHubStore.orders.detail.MusicHubOrdersDetailActivity
import com.rocatoro.musichub.activities.client.address.list.ClientAddressListActivity
import com.rocatoro.musichub.activities.client.home.ClientHomeActivity
import com.rocatoro.musichub.activities.client.orders.detail.ClientOrdersDetailActivity
import com.rocatoro.musichub.activities.client.products.list.ClientProductsListActivity
import com.rocatoro.musichub.activities.delivery.home.DeliveryHomeActivity
import com.rocatoro.musichub.models.Address
import com.rocatoro.musichub.models.Category
import com.rocatoro.musichub.models.Order
import com.rocatoro.musichub.models.Rol
import com.rocatoro.musichub.utils.SharedPref
import org.w3c.dom.Text

class OrdersMusicHubAdapter(val context: Activity, val orders: ArrayList<Order>): RecyclerView.Adapter<OrdersMusicHubAdapter.OrdersViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdersViewHolder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_orders_musichub,parent,false)

        return OrdersViewHolder(view)
    }

    override fun getItemCount(): Int {
        return orders.size
    }

    override fun onBindViewHolder(holder:OrdersViewHolder,position: Int) {
        val order = orders[position] // EACH ADDRESS

        holder.textViewOrderId.text = "Orden # ${order.id}"
        holder.textViewDate.text = "${order.timestamp}"
        holder.textViewAddress.text = "${order.address?.address}"
        holder.textViewClient.text = "${order.client?.name} ${order.client?.lastname}"

        holder.itemView.setOnClickListener {
            goToOrderDetail(order)
        }

    }

    private fun goToOrderDetail(order: Order){
        val i = Intent(context,MusicHubOrdersDetailActivity::class.java)
        i.putExtra("order",order.toJson())
        context.startActivity(i)
    }

    class OrdersViewHolder(view: View): RecyclerView.ViewHolder(view){

        val textViewOrderId: TextView
        val textViewDate: TextView
        val textViewAddress: TextView
        val textViewClient: TextView

        init {
            textViewOrderId = view.findViewById(R.id.textview_order_id)
            textViewDate = view.findViewById(R.id.textview_date)
            textViewAddress = view.findViewById(R.id.textview_address)
            textViewClient = view.findViewById(R.id.textview_client)
        }

    }

}