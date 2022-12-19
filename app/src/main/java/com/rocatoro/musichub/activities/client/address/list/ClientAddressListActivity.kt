package com.rocatoro.musichub.activities.client.address.list

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rocatoro.musichub.R
import com.rocatoro.musichub.activities.adapters.AddressAdapter
import com.rocatoro.musichub.activities.client.address.create.ClientAdressCreateActivity
import com.rocatoro.musichub.activities.client.payments.form.ClientPaymentFormActivity
import com.rocatoro.musichub.activities.client.payments.list.ClientPaymentListActivity
import com.rocatoro.musichub.models.*
import com.rocatoro.musichub.providers.AddressProvider
import com.rocatoro.musichub.providers.OrdersProvider
import com.rocatoro.musichub.providers.SolicitudTransporteProvider
import com.rocatoro.musichub.utils.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList

class ClientAddressListActivity : AppCompatActivity() {

    val TAG = "AddressList"

    var toolbar: Toolbar? = null
    var fabCreateAddress: FloatingActionButton? = null

    var recyclerViewAddress: RecyclerView? = null
    var adapter: AddressAdapter? = null

    var addressProvider: AddressProvider? = null
    var ordersProvider: OrdersProvider? = null
    var solicitudTransporteProvier: SolicitudTransporteProvider? = null

    var sharedPref: SharedPref? = null
    var user: User? = null
    var address = ArrayList<Address>()

    var btnNext: Button? = null

    var a: Address? = null

    val gson = Gson()

    var selectedProducts = ArrayList<Product>()

    var selectedProductsToTransport = ArrayList<ProductToTransport>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_address_list)

        sharedPref = SharedPref(this)

        getProductsFromSharedPref()

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
        ordersProvider = OrdersProvider(user?.sessionToken!!)
        solicitudTransporteProvier = SolicitudTransporteProvider(user?.sessionToken!!)

        fabCreateAddress = findViewById(R.id.fab_address_create)

        fabCreateAddress?.setOnClickListener{goToAddressCreate()}

        //btnNext?.setOnClickListener { getAddressForSession() }

        btnNext?.setOnClickListener { goToClientPaymentList() }

        getAddress()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createOrder(idAddress: String){
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
            numero_venta = numeroventa
        )

        val solicitudTransporte = SolicitudTransporte(
            numeroVenta = numeroventa,
            //productos = "Productos varios de Music+",
            //productos = selectedProductsToTransport,
            productos = "Productos Music+",
            nombreDestinatario = user?.name!!,
            direccionDestino = a?.address!!,
            fechaEntrega = 20221212,
            idCliente = user?.id!!.toInt()
        )

        Log.d(TAG,"solicitud Transporte: $solicitudTransporte")
        Log.d(TAG,"Productos para transporte: $selectedProductsToTransport")


        solicitudTransporteProvier?.create(solicitudTransporte)?.enqueue(object: Callback<ResponseHttp>{
            override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {
                Log.d(TAG,"RESPUESTA SERVER TRANSPORTE: ${response.body()}")
                if (response.body() != null){
                    Toast.makeText(this@ClientAddressListActivity, "Solicitud de transporte enviada", Toast.LENGTH_LONG).show()

                }
                else {
                    Toast.makeText(this@ClientAddressListActivity, "Ocurrió un error en la petición", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                Toast.makeText(this@ClientAddressListActivity,"Error: ${t.message}",Toast.LENGTH_LONG).show()
            }
        })

        ordersProvider?.create(order)?.enqueue(object: Callback<ResponseHttp>{
            override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {

                if (response.body() != null){
                    Toast.makeText(this@ClientAddressListActivity, "${response.body()?.message}", Toast.LENGTH_LONG).show()

                }
                else {
                    Toast.makeText(this@ClientAddressListActivity, "Ocurrió un error en la petición", Toast.LENGTH_LONG).show()
                }

            }

            override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                Toast.makeText(this@ClientAddressListActivity,"Error: ${t.message}",Toast.LENGTH_LONG).show()
            }
        })

    }

    private fun getProductsFromSharedPref(){

        if(!sharedPref?.getData("order").isNullOrBlank()){ // EXISTS ORDER IN SHARE PREFERENCES
            val type = object: TypeToken<ArrayList<Product>>(){}.type
            selectedProducts = gson.fromJson(sharedPref?.getData("order"),type)
            //selectedProductsToTransport = gson.fromJson(sharedPref?.getData("orderToTransport"),type)
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getAddressForSession(){
        if(!sharedPref?.getData("address").isNullOrBlank()){
            //val a = gson.fromJson(sharedPref?.getData("address"),Address::class.java) // SI EXISTE UNA DIRECCIÓN
            a = gson.fromJson(sharedPref?.getData("address"),Address::class.java) // SI EXISTE UNA DIRECCIÓN
            createOrder(a?.id!!)
            //goToPaymentsForm()
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

    private fun goToClientPaymentList(){
        val i = Intent(this,ClientPaymentListActivity::class.java)
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