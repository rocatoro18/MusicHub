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
import com.rocatoro.musichub.models.Rol
import com.rocatoro.musichub.utils.SharedPref

class RolesAdapter(val context: Activity, val roles: ArrayList<Rol>): RecyclerView.Adapter<RolesAdapter.RolesViewHolder>() {

    val sharedPref = SharedPref(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RolesViewHolder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_roles,parent,false)

        return RolesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return roles.size
    }

    override fun onBindViewHolder(holder:RolesViewHolder,position: Int) {
        val rol = roles[position] // EACH ROL

        holder.textViewRol.text = rol.name
        Glide.with(context).load(rol.image).into(holder.imageViewRol)

        holder.itemView.setOnClickListener{
            goToRol(rol)
        }


    }

    private fun goToRol(rol: Rol){
        if (rol.name == "Cliente"){
            sharedPref.save("rol","Cliente")
            val i = Intent(context,ClientHomeActivity::class.java)
            context.startActivity(i)
        }
        else if (rol.name == "Music Hub ADMIN"){
            sharedPref.save("rol","Music Hub ADMIN")
            val i = Intent(context,MusicHubHomeActivity::class.java)
            context.startActivity(i)
        }
        else if (rol.name == "DELIVERY"){
            sharedPref.save("rol","Delivery")
            val i = Intent(context,DeliveryHomeActivity::class.java)
            context.startActivity(i)
        }
    }

    class RolesViewHolder(view: View): RecyclerView.ViewHolder(view){

        val textViewRol: TextView
        val imageViewRol: ImageView

        init {
            textViewRol = view.findViewById(R.id.tv_rol)
            imageViewRol = view.findViewById(R.id.iv_rol)
        }

    }

}