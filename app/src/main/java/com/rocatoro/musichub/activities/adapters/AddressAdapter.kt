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
import com.rocatoro.musichub.activities.client.products.list.ClientProductsListActivity
import com.rocatoro.musichub.activities.delivery.home.DeliveryHomeActivity
import com.rocatoro.musichub.models.Address
import com.rocatoro.musichub.models.Category
import com.rocatoro.musichub.models.Rol
import com.rocatoro.musichub.utils.SharedPref

class AddressAdapter(val context: Activity, val address: ArrayList<Address>): RecyclerView.Adapter<AddressAdapter.AddressViewHolder>() {

    val sharedPref = SharedPref(context)
    val gson = Gson()
    var prev = 0
    var positionAddressSession = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_address,parent,false)

        return AddressViewHolder(view)
    }

    override fun getItemCount(): Int {
        return address.size
    }

    private fun saveAddress(data: String){
        val ad = gson.fromJson(data,Address::class.java)
        sharedPref.save("address",ad)
    }

    override fun onBindViewHolder(holder:AddressViewHolder,position: Int) {
        val a = address[position] // EACH ADDRESS

        if (!sharedPref.getData("address").isNullOrBlank()){ // IF USER SELECT ADDRESS
            val adr = gson.fromJson(sharedPref.getData("address"),Address::class.java)

            if (adr.id == a.id){
                positionAddressSession = position
                holder.imageViewCheck.visibility = View.VISIBLE
            }

        }

        holder.textViewAddress.text = a.address
        holder.textViewPostalCode.text = a.postal_code
        holder.textViewPhone.text = a.phone

        holder.itemView.setOnClickListener {

            (context as ClientAddressListActivity).resetValue(prev)
            (context as ClientAddressListActivity).resetValue(positionAddressSession)

            prev = position

            holder.imageViewCheck.visibility = View.VISIBLE
            saveAddress(a.toJson())
        }

    }

    class AddressViewHolder(view: View): RecyclerView.ViewHolder(view){

        val textViewAddress: TextView
        val textViewPostalCode: TextView
        val textViewPhone: TextView
        val imageViewCheck: ImageView

        init {
            textViewAddress = view.findViewById(R.id.textview_address)
            textViewPostalCode = view.findViewById(R.id.textview_postal_code)
            textViewPhone = view.findViewById(R.id.textview_phone)
            imageViewCheck = view.findViewById(R.id.imageview_check)
        }

    }

}