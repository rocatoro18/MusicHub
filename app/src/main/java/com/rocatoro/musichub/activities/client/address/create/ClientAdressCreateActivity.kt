package com.rocatoro.musichub.activities.client.address.create

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.rocatoro.musichub.R
import com.rocatoro.musichub.activities.client.address.list.ClientAddressListActivity
import com.rocatoro.musichub.models.Address
import com.rocatoro.musichub.models.ResponseHttp
import com.rocatoro.musichub.models.User
import com.rocatoro.musichub.providers.AddressProvider
import com.rocatoro.musichub.utils.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClientAdressCreateActivity : AppCompatActivity() {

    val TAG = "ClientAddressCreate"

    var toolbar: Toolbar? = null

    var editTextAddress: EditText? = null
    var editTextPostalCode: EditText? = null
    var editTextPhone: EditText? = null
    var btnCreateAddress: Button? = null

    var addressProvider: AddressProvider? = null

    var sharedPref: SharedPref? = null

    var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_adress_create)

        toolbar = findViewById(R.id.toolbar)
        toolbar?.title = "Agregar una dirección"
        toolbar?.setTitleTextColor(ContextCompat.getColor(this,R.color.white))
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        sharedPref = SharedPref(this)

        getUserFromSession()

        addressProvider = AddressProvider(user?.sessionToken!!)

        editTextAddress = findViewById(R.id.et_address)
        editTextPostalCode = findViewById(R.id.et_postal_code)
        editTextPhone = findViewById(R.id.et_phone)
        btnCreateAddress = findViewById(R.id.btn_create_address)

        btnCreateAddress?.setOnClickListener { createAddress() }

    }

    private fun createAddress(){
        val address = editTextAddress?.text.toString()
        val postalCode = editTextPostalCode?.text.toString()
        val phone = editTextPhone?.text.toString()

        if (isValidForm(address,postalCode,phone)){
            // LAUNCH REQ TO NODEJS

            val addressModel = Address(
                id_user = user?.id!!,
                address = address,
                postal_code = postalCode,
                phone = phone
            )

            addressProvider?.create(addressModel)?.enqueue(object: Callback<ResponseHttp>{
                override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>
                ) {
                    if(response.body() != null){
                        Toast.makeText(this@ClientAdressCreateActivity,response.body()!!.message,Toast.LENGTH_LONG).show()
                        goToAddressList()
                    }
                    else {
                        Toast.makeText(this@ClientAdressCreateActivity,"Ocurrió un error en la petición",Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    Toast.makeText(this@ClientAdressCreateActivity,"Error: ${t.message}",Toast.LENGTH_LONG).show()
                }
            })

        }

    }

    private fun goToAddressList(){
        val i = Intent(this,ClientAddressListActivity::class.java)
        startActivity(i)
    }

    private fun isValidForm(address: String, postalCode: String, phone: String): Boolean{

        if(address.isNullOrBlank()){
            Toast.makeText(this,"Ingrese su dirección",Toast.LENGTH_LONG).show()
            return false
        }
        if(postalCode.isNullOrBlank()){
            Toast.makeText(this,"Ingrese su código postal",Toast.LENGTH_LONG).show()
            return false
        }
        if(phone.isNullOrBlank()){
            Toast.makeText(this,"Ingrese su teléfono",Toast.LENGTH_LONG).show()
            return false
        }
        return true
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