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
import com.rocatoro.musichub.models.Rol
import com.rocatoro.musichub.utils.SharedPref

class CategoriesAdapter(val context: Activity, val categories: ArrayList<Category>): RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder>() {

    val sharedPref = SharedPref(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_categories,parent,false)

        return CategoriesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    override fun onBindViewHolder(holder:CategoriesViewHolder,position: Int) {
        val category = categories[position] // EACH CATEGORY

        holder.textViewCategory.text = category.name
        Glide.with(context).load(category.image).into(holder.imageViewCategory)

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
    class CategoriesViewHolder(view: View): RecyclerView.ViewHolder(view){

        val textViewCategory: TextView
        val imageViewCategory: ImageView

        init {
            textViewCategory = view.findViewById(R.id.textview_category)
            imageViewCategory = view.findViewById(R.id.imageview_category)
        }

    }

}