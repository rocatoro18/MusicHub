package com.rocatoro.musichub.activities.client.payments.form

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rocatoro.musichub.R
import com.rocatoro.musichub.models.*
import com.rocatoro.musichub.providers.OrdersProvider
import com.rocatoro.musichub.providers.SolicitudTransporteProvider
import com.rocatoro.musichub.utils.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList

class ClientPaymentFormActivity : AppCompatActivity() {

    val TAG = "PaymentForm"

    var toolbar: Toolbar? = null

    var btnPay: Button? = null

    var sharedPref: SharedPref? = null
    var user: User? = null
    var payment: Payment? = null
    var address: Address? = null
    val gson = Gson()
    var selectedProducts = ArrayList<Product>()

    var textViewTotal: TextView? = null

    var total: Double? = null

    var ordersProvider: OrdersProvider? = null
    var solicitudTransporteProvier: SolicitudTransporteProvider? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_payment_form)

        sharedPref = SharedPref(this)

        getUserFromSession()
        getProductsFromSharedPref()
        getPaymentFromSharedPref()
        getAddressFromSharedPref()


        toolbar = findViewById(R.id.toolbar)
        toolbar?.title = "Pagar"
        toolbar?.setTitleTextColor(ContextCompat.getColor(this,R.color.white))
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        ordersProvider = OrdersProvider(user?.sessionToken!!)
        solicitudTransporteProvier = SolicitudTransporteProvider(user?.sessionToken!!)

        btnPay = findViewById(R.id.btn_pay)
        textViewTotal = findViewById(R.id.textview_total)

        //updateTotal()


        btnPay?.setOnClickListener { createOrder(address?.id!!,payment?.id!!) }

    }

    private fun updateTotal(){
        textViewTotal?.text = total.toString()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createOrder(idAddress: String, idPayment: String){
        // It will generate 6 digit random Number.
        // from 0 to 999999
        // It will generate 6 digit random Number.
        // from 0 to 999999
        val rnd = Random()
        val numeroventa: Int = rnd.nextInt(99999999)

        val current = LocalDateTime.now()
        val order = Order(
            products =selectedProducts,
            id_client = user?.id!!,
            id_address = idAddress,
            id_payment = idPayment,
            numero_venta = numeroventa
        )

        val solicitudTransporte = SolicitudTransporte(
            numeroVenta = numeroventa,
            //productos = "Productos varios de Music+",
            //productos = selectedProductsToTransport,
            productos = "Productos Music+",
            nombreDestinatario = user?.name!!,
            direccionDestino = address?.address!!,
            fechaEntrega = 20221231,
            idCliente = user?.id!!.toInt()
        )

        ordersProvider?.create(order)?.enqueue(object: Callback<ResponseHttp> {
            override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {

                if (response.body() != null){
                    Toast.makeText(this@ClientPaymentFormActivity, "${response.body()?.message}", Toast.LENGTH_LONG).show()
                    solicitudTransporteProvier?.create(solicitudTransporte)?.enqueue(object: Callback<ResponseHttp>{
                        override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {
                            Log.d(TAG,"RESPUESTA SERVER TRANSPORTE: ${response.body()}")
                            if (response.body() != null){
                                Toast.makeText(this@ClientPaymentFormActivity, "Solicitud de transporte enviada", Toast.LENGTH_LONG).show()
                            }
                            else {
                                Toast.makeText(this@ClientPaymentFormActivity, "Ocurrió un error en la petición", Toast.LENGTH_LONG).show()
                            }
                        }

                        override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                            Toast.makeText(this@ClientPaymentFormActivity,"Error: ${t.message}",Toast.LENGTH_LONG).show()
                        }
                    })
                    goToPaymentFinish()
                }
                else {
                    Toast.makeText(this@ClientPaymentFormActivity, "Ocurrió un error en la petición", Toast.LENGTH_LONG).show()
                }

            }

            override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                Toast.makeText(this@ClientPaymentFormActivity,"Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })


    }

    private fun goToPaymentFinish(){
        val i = Intent(this,ClientPaymentFinishActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
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

    private fun getProductsFromSharedPref(){

        if(!sharedPref?.getData("order").isNullOrBlank()){ // EXISTS ORDER IN SHARE PREFERENCES
            val type = object: TypeToken<ArrayList<Product>>(){}.type
            selectedProducts = gson.fromJson(sharedPref?.getData("order"),type)
            //selectedProductsToTransport = gson.fromJson(sharedPref?.getData("orderToTransport"),type)
            /*
            for(p in selectedProducts){
                total = total!! + (p.price * p.quantity!!)
            }

             */
        }

    }

    private fun getPaymentFromSharedPref(){
        if(!sharedPref?.getData("payment").isNullOrBlank()){ // EXISTS ORDER IN SHARE PREFERENCES
            payment = gson.fromJson(sharedPref?.getData("payment"),Payment::class.java)
            Log.d(TAG,"Payment: $payment")
        }
    }

    private fun getAddressFromSharedPref(){
        if(!sharedPref?.getData("address").isNullOrBlank()){ // EXISTS ORDER IN SHARE PREFERENCES
            address = gson.fromJson(sharedPref?.getData("address"),Address::class.java)
            Log.d(TAG,"Address: $address")
        }
    }

}