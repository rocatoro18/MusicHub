package com.rocatoro.musichub.activities.client.products.list

import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar;
import android.os.Bundle
import android.util.Log
import android.widget.Toast
//import android.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.rocatoro.musichub.R
import com.rocatoro.musichub.activities.adapters.ProductsAdapter
import com.rocatoro.musichub.models.Product
import com.rocatoro.musichub.models.User
import com.rocatoro.musichub.providers.ProductsProvider
import com.rocatoro.musichub.utils.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClientProductsListActivity : AppCompatActivity() {

    val TAG = "ClientProducts"

    var recyclerViewProducts: RecyclerView? = null
    var adapter: ProductsAdapter? = null
    var user: User? = null
    var productsProvider: ProductsProvider? = null
    var products: ArrayList<Product> = ArrayList()

    var sharedPref: SharedPref? = null

    var idCategory: String? = null

    var toolbar: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_products_list)

        idCategory = intent.getStringExtra("idCategory") // RETRIEVING PARAMETER FROM CATEGORIES ADAPTER

        sharedPref = SharedPref(this)

        toolbar = findViewById(R.id.toolbar)

        toolbar?.title = "Productos"
        toolbar?.setTitleTextColor(ContextCompat.getColor(this,R.color.white))


        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        getUserFromSession()

        productsProvider = ProductsProvider(user?.sessionToken!!)

        recyclerViewProducts = findViewById(R.id.recyclerview_products)
        recyclerViewProducts?.layoutManager = LinearLayoutManager(this)

        getProducts()

    }

    private fun getProducts(){
        productsProvider?.findByCategory(idCategory!!)?.enqueue(object: Callback<ArrayList<Product>>{
            override fun onResponse(call: Call<ArrayList<Product>>, response: Response<ArrayList<Product>>
            ) {
                if(response.body() != null) {
                    products = response.body()!!
                    adapter = ProductsAdapter(this@ClientProductsListActivity,products)
                    recyclerViewProducts?.adapter = adapter
                }
            }

            override fun onFailure(call: Call<ArrayList<Product>>, t: Throwable) {
                Toast.makeText(this@ClientProductsListActivity,t.message,Toast.LENGTH_LONG).show()
                Log.d(TAG,"Error: ${t.message}")
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