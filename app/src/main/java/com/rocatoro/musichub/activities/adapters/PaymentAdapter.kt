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
import com.rocatoro.musichub.activities.client.payments.list.ClientPaymentListActivity
import com.rocatoro.musichub.activities.client.products.list.ClientProductsListActivity
import com.rocatoro.musichub.activities.delivery.home.DeliveryHomeActivity
import com.rocatoro.musichub.models.Address
import com.rocatoro.musichub.models.Category
import com.rocatoro.musichub.models.Payment
import com.rocatoro.musichub.models.Rol
import com.rocatoro.musichub.utils.SharedPref

class PaymentAdapter(val context: Activity, val payments: ArrayList<Payment>): RecyclerView.Adapter<PaymentAdapter.PaymentViewHolder>() {

    val sharedPref = SharedPref(context)
    val gson = Gson()
    var prev = 0
    var positionPaymentSession = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentViewHolder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_payment,parent,false)

        return PaymentViewHolder(view)
    }

    override fun getItemCount(): Int {
        return payments.size
    }

    private fun savePayment(data: String){
        val pay = gson.fromJson(data,Payment::class.java)
        sharedPref.save("payment",pay)
    }

    override fun onBindViewHolder(holder:PaymentViewHolder,position: Int) {
        val p = payments[position] // EACH ADDRESS

        if (!sharedPref.getData("payment").isNullOrBlank()){ // IF USER SELECT ADDRESS
            val pdr = gson.fromJson(sharedPref.getData("payment"),Payment::class.java)

            if (pdr.id == p.id){
                positionPaymentSession = position
                holder.imageViewCheck.visibility = View.VISIBLE
            }

        }

        holder.textViewPaymentType.text = p.payment_type
        holder.textViewProvider.text = p.provider
        holder.textViewAccountNo.text = p.account_no
        holder.textViewExpiry.text = p.expiry

        holder.itemView.setOnClickListener {

            (context as ClientPaymentListActivity).resetValue(prev)
            (context as ClientPaymentListActivity).resetValue(positionPaymentSession)

            prev = position

            holder.imageViewCheck.visibility = View.VISIBLE
            savePayment(p.toJson())
        }

    }

    class PaymentViewHolder(view: View): RecyclerView.ViewHolder(view){

        val textViewPaymentType: TextView
        val textViewProvider: TextView
        val textViewAccountNo: TextView
        val textViewExpiry: TextView
        val imageViewCheck: ImageView

        init {
            textViewPaymentType = view.findViewById(R.id.textview_payment_type)
            textViewProvider = view.findViewById(R.id.textview_provider)
            textViewAccountNo = view.findViewById(R.id.textview_account_no)
            textViewExpiry = view.findViewById(R.id.textview_expiry)
            imageViewCheck = view.findViewById(R.id.imageview_check)
        }

    }

}