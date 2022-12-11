package com.rocatoro.musichub.activities.client.address.list

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.rocatoro.musichub.R
import com.rocatoro.musichub.activities.adapters.AddressAdapter
import com.rocatoro.musichub.activities.client.address.create.ClientAdressCreateActivity
import com.rocatoro.musichub.activities.client.payments.form.ClientPaymentFormActivity
import com.rocatoro.musichub.models.Address
import com.rocatoro.musichub.models.ResponseHttp
import com.rocatoro.musichub.models.User
import com.rocatoro.musichub.providers.AddressProvider
import com.rocatoro.musichub.utils.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClientAddressListActivity : AppCompatActivity() {

    val TAG = "AddressList"

    var toolbar: Toolbar? = null
    var fabCreateAddress: FloatingActionButton? = null

    var recyclerViewAddress: RecyclerView? = null
    var adapter: AddressAdapter? = null

    var addressProvider: AddressProvider? = null

    var sharedPref: SharedPref? = null
    var user: User? = null
    var address = ArrayList<Address>()

    var btnNext: Button? = null

    val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_address_list)

        sharedPref = SharedPref(this)

        toolbar = findViewById(R.id.toolbar)
        toolbar?.title = "Mis direcciones"
        toolbar?.setTitleTextColor(ContextCompat.getColor(this,R.color.white))
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        btnNext = findViewById(R.id.btn_next)
        recyclerViewAddress = findViewById(R.id.recyclerview_address)
        recyclerViewAddress?.layoutManager = LinearLayoutManager(this)

        getUserFromSession()

        addressProvider = AddressProvider(user?.sessionToken!!)
        fabCreateAddress = findViewById(R.id.fab_address_create)

        fabCreateAddress?.setOnClickListener{goToAddressCreate()}

        btnNext?.setOnClickListener { getAddressForSession() }

        getAddress()

    }

    private fun getAddressForSession(){
        if(!sharedPref?.getData("address").isNullOrBlank()){
            val a = gson.fromJson(sharedPref?.getData("address"),Address::class.java) // SI EXISTE UNA DIRECCIÓN
            goToPaymentsForm()
        } else {
            Toast.makeText(this,"Seleccione una dirección",Toast.LENGTH_LONG).show()
        }
    }

    private fun goToPaymentsForm(){
        val i = Intent(this,ClientPaymentFormActivity::class.java)
        startActivity(i)
    }

    fun resetValue(position: Int){
        val viewHolder = recyclerViewAddress?.findViewHolderForAdapterPosition(position) // UNA DIRECCIÓN
        val view = viewHolder?.itemView
        val imageViewCheck = view?.findViewById<ImageView>(R.id.imageview_check)
        imageViewCheck?.visibility = View.GONE
    }

    private fun getAddress(){
        addressProvider?.getAddress(user?.id!!)?.enqueue(object: Callback<ArrayList<Address>>{
            override fun onResponse(call: Call<ArrayList<Address>>, response: Response<ArrayList<Address>>
            ) {
                if(response.body() != null){
                    address = response.body()!!
                    adapter = AddressAdapter(this@ClientAddressListActivity,address)
                    recyclerViewAddress?.adapter = adapter
                }
            }

            override fun onFailure(call: Call<ArrayList<Address>>, t: Throwable) {
                //Log.d(TAG,t.message!!)
                Toast.makeText(this@ClientAddressListActivity,"Error: ${t.message}",Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun goToAddressCreate(){
        val i = Intent(this,ClientAdressCreateActivity::class.java)
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