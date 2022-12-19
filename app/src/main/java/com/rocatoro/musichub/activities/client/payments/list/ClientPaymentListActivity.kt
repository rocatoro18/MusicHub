package com.rocatoro.musichub.activities.client.payments.list

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.rocatoro.musichub.R
import com.rocatoro.musichub.activities.adapters.AddressAdapter
import com.rocatoro.musichub.activities.adapters.PaymentAdapter
import com.rocatoro.musichub.activities.client.payments.create.ClientPaymentCreateActivity
import com.rocatoro.musichub.activities.client.payments.form.ClientPaymentFormActivity
import com.rocatoro.musichub.models.Address
import com.rocatoro.musichub.models.Payment
import com.rocatoro.musichub.models.User
import com.rocatoro.musichub.providers.AddressProvider
import com.rocatoro.musichub.providers.PaymentProvider
import com.rocatoro.musichub.utils.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClientPaymentListActivity : AppCompatActivity() {

    val TAG = "PaymentList"

    var toolbar: Toolbar? = null
    var fabCreatePayment: FloatingActionButton? = null
    var btnNext: Button? = null

    var recyclerViewPayment: RecyclerView? = null
    var adapter: PaymentAdapter? = null
    var paymentProvider: PaymentProvider? = null

    var p: Payment? = null
    val gson = Gson()

    var sharedPref: SharedPref? = null
    var user: User? = null
    var payment = ArrayList<Payment>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_payment_list)

        sharedPref = SharedPref(this)

        toolbar = findViewById(R.id.toolbar)
        toolbar?.title = "Mis métodos de pago"
        toolbar?.setTitleTextColor(ContextCompat.getColor(this,R.color.white))
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        fabCreatePayment = findViewById(R.id.fab_payment_create)
        btnNext = findViewById(R.id.btn_next)

        recyclerViewPayment = findViewById(R.id.recyclerview_payment)
        recyclerViewPayment?.layoutManager = LinearLayoutManager(this)


        getUserFromSession()

        paymentProvider = PaymentProvider(user?.sessionToken!!)

        fabCreatePayment?.setOnClickListener{goToPaymentCreate()}

        btnNext?.setOnClickListener { getPaymentForSession() }

        getPayment()

    }

    private fun getPayment(){
        paymentProvider?.getPayment(user?.id!!)?.enqueue(object: Callback<ArrayList<Payment>> {
            override fun onResponse(call: Call<ArrayList<Payment>>, response: Response<ArrayList<Payment>>
            ) {
                if(response.body() != null){
                    payment = response.body()!!
                    adapter = PaymentAdapter(this@ClientPaymentListActivity,payment)
                    recyclerViewPayment?.adapter = adapter
                }
            }

            override fun onFailure(call: Call<ArrayList<Payment>>, t: Throwable) {
                //Log.d(TAG,t.message!!)
                Toast.makeText(this@ClientPaymentListActivity,"Error: ${t.message}", Toast.LENGTH_LONG).show()

            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getPaymentForSession(){
        if(!sharedPref?.getData("payment").isNullOrBlank()){
            //val a = gson.fromJson(sharedPref?.getData("address"),Address::class.java) // SI EXISTE UNA DIRECCIÓN
            p = gson.fromJson(sharedPref?.getData("payment"),Payment::class.java) // SI EXISTE UNA DIRECCIÓN
            //createOrder(a?.id!!)
            //goToPaymentsForm()
            goToPaymentForm()
        } else {
            Toast.makeText(this,"Seleccione un método de pago",Toast.LENGTH_LONG).show()
        }
    }

    fun resetValue(position: Int){
        val viewHolder = recyclerViewPayment?.findViewHolderForAdapterPosition(position) // UN PAGO
        val view = viewHolder?.itemView
        val imageViewCheck = view?.findViewById<ImageView>(R.id.imageview_check)
        imageViewCheck?.visibility = View.GONE
    }

    private fun goToPaymentCreate(){
        val i = Intent(this,ClientPaymentCreateActivity::class.java)
        startActivity(i)
    }

    private fun goToPaymentForm(){
        val i = Intent(this,ClientPaymentFormActivity::class.java)
        startActivity(i)
    }

    private fun getUserFromSession(){

        val gson = Gson()

        if(!sharedPref?.getData("user").isNullOrBlank()){
            // IF USER EXIST IN SESSION
            user = gson.fromJson(sharedPref?.getData("user"), User::class.java)
            Log.d(TAG,"Usuario: $user")
        }

    }

}