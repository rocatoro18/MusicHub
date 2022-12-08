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
import com.rocatoro.musichub.activities.delivery.home.DeliveryHomeActivity
import com.rocatoro.musichub.models.Category
import com.rocatoro.musichub.models.Product
import com.rocatoro.musichub.models.Rol
import com.rocatoro.musichub.utils.SharedPref

class ProductsAdapter(val context: Activity, val products: ArrayList<Product>): RecyclerView.Adapter<ProductsAdapter.ProductsViewHolder>() {

    val sharedPref = SharedPref(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_product,parent,false)

        return ProductsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(holder:ProductsViewHolder,position: Int) {
        val product = products[position] // EACH PRODUCT

        holder.textViewName.text = product.name
        holder.textViewPrice.text = "${product.price}$"
        Glide.with(context).load(product.image1).into(holder.imageViewProduct)

        /*
        holder.itemView.setOnClickListener{
            goToRol(rol)
        }
        */

    }

    /*
    private fun goToRol(rol: Rol){
            val i = Intent(context,ClientHomeActivity::class.java)
            context.startActivity(i)
    }
    */
    class ProductsViewHolder(view: View): RecyclerView.ViewHolder(view){

        val textViewName: TextView
        val textViewPrice: TextView
        val imageViewProduct: ImageView

        init {
            textViewName = view.findViewById(R.id.textview_name)
            textViewPrice = view.findViewById(R.id.textview_price)
            imageViewProduct = view.findViewById(R.id.imageview_product)
        }

    }

}