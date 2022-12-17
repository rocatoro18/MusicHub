package com.rocatoro.musichub.activities.MusicHubStore.ventaexterna

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.rocatoro.musichub.R
import com.rocatoro.musichub.activities.adapters.CategoriesAdapter
import com.rocatoro.musichub.activities.adapters.VentaExternaAdapter
import com.rocatoro.musichub.models.Category
import com.rocatoro.musichub.models.Order
import com.rocatoro.musichub.models.User
import com.rocatoro.musichub.models.VentaExterna
import com.rocatoro.musichub.providers.CategoriesProvider
import com.rocatoro.musichub.providers.VentaExternaProvider
import com.rocatoro.musichub.utils.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistroVentaExternaActivity : AppCompatActivity() {

    val TAG = "RegVentaExternaDetail"
    var order: Order? = null
    val gson = Gson()

    var recyclerViewVentaExterna: RecyclerView? = null
    var ventaExternaProvider: VentaExternaProvider? = null
    var adapter: VentaExternaAdapter? = null
    var user: User? = null
    var sharedPref: SharedPref? = null
    var ventas = ArrayList<VentaExterna>()

    var toolbar: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_venta_externa)

        toolbar = findViewById(R.id.toolbar)
        toolbar?.title = "Registro Venta Externa"
        toolbar?.setTitleTextColor(ContextCompat.getColor(this,R.color.white))
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        recyclerViewVentaExterna = findViewById(R.id.recyclerview_venta_externa)

        recyclerViewVentaExterna?.layoutManager = LinearLayoutManager(this) // SPECIFY ELEMENTS WILL SHOW VERTICAL

        sharedPref = SharedPref(this)

        getUserFromSession()

        ventaExternaProvider = VentaExternaProvider(user?.sessionToken!!)

        getRegVentaExterna()

    }

    private fun getRegVentaExterna(){
        ventaExternaProvider?.getAll()?.enqueue(object: Callback<ArrayList<VentaExterna>>{
            override fun onResponse(call: Call<ArrayList<VentaExterna>>, response: Response<ArrayList<VentaExterna>>
            ) {
                if(response.body() != null) {
                    ventas = response.body()!!
                    adapter = VentaExternaAdapter(this@RegistroVentaExternaActivity, ventas)
                    recyclerViewVentaExterna?.adapter = adapter
                }
            }

            override fun onFailure(call: Call<ArrayList<VentaExterna>>, t: Throwable) {
                Log.d(TAG,"Error: ${t.message}")
                Toast.makeText(this@RegistroVentaExternaActivity,"Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun getUserFromSession(){

        val gson = Gson()

        if(!sharedPref?.getData("user").isNullOrBlank()){
            // IF USER EXIST IN SESSION
            user = gson.fromJson(sharedPref?.getData("user"),User::class.java)
            Log.d(TAG,"Usuario: $user")
        }

    }

}