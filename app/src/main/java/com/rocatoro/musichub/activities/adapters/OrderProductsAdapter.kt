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
import com.rocatoro.musichub.activities.client.home.ClientHomeActivity
import com.rocatoro.musichub.activities.client.products.detail.ClientProductsDetailActivity
import com.rocatoro.musichub.activities.client.shopping_bag.ClientShoppingBagActivity
import com.rocatoro.musichub.activities.delivery.home.DeliveryHomeActivity
import com.rocatoro.musichub.models.Category
import com.rocatoro.musichub.models.Product
import com.rocatoro.musichub.models.Rol
import com.rocatoro.musichub.utils.SharedPref
//import kotlinx.android.synthetic.main.activity_client_products_detail.*

class OrderProductsAdapter(val context: Activity, val products: ArrayList<Product>): RecyclerView.Adapter<OrderProductsAdapter.OrderProductsViewHolder>() {

    val sharedPref = SharedPref(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderProductsViewHolder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_order_products,parent,false)

        return OrderProductsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(holder:OrderProductsViewHolder,position: Int) {
        val product = products[position] // EACH PRODUCT

        holder.textViewName.text = product.name
        holder.textViewQuantity.text = product.quantity.toString()
        Glide.with(context).load(product.image1).into(holder.imageViewProduct)

        //holder.itemView.setOnClickListener{ goToDetail(product) }


    }


    class OrderProductsViewHolder(view: View): RecyclerView.ViewHolder(view){

        val imageViewProduct: ImageView
        val textViewName: TextView
        val textViewQuantity: TextView



        init {
            imageViewProduct = view.findViewById(R.id.imageview_product)
            textViewName = view.findViewById(R.id.textview_name)
            textViewQuantity = view.findViewById(R.id.textview_quantity)
        }

    }

}