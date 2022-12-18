package com.rocatoro.musichub.activities.client.shopping_bag

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rocatoro.musichub.R
import com.rocatoro.musichub.activities.adapters.ShoppingBagAdapter
import com.rocatoro.musichub.activities.client.address.create.ClientAdressCreateActivity
import com.rocatoro.musichub.activities.client.address.list.ClientAddressListActivity
import com.rocatoro.musichub.models.Product
import com.rocatoro.musichub.models.ProductToTransport
import com.rocatoro.musichub.utils.SharedPref

class ClientShoppingBagActivity : AppCompatActivity() {

    var toolbar: Toolbar? = null

    var recyclerViewShoppingBag: RecyclerView? = null
    var textViewTotal: TextView? = null
    var buttonNext: Button? = null

    var adapter: ShoppingBagAdapter? = null

    var sharedPref: SharedPref? = null
    var gson = Gson()

    var selectedProducts = ArrayList<Product>()
    var selectedProductsToTransport = ArrayList<ProductToTransport>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_shopping_bag)

        sharedPref = SharedPref(this)

        toolbar = findViewById(R.id.toolbar)
        toolbar?.title = "Mi Carrito"
        toolbar?.setTitleTextColor(ContextCompat.getColor(this,R.color.white))
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        recyclerViewShoppingBag = findViewById(R.id.recyclerview_shopping_bag)
        textViewTotal = findViewById(R.id.textview_total)
        buttonNext = findViewById(R.id.btn_next)

        recyclerViewShoppingBag?.layoutManager = LinearLayoutManager(this)

        getProductsFromSharedPref()

        buttonNext?.setOnClickListener { goToAddressList() }

    }

    private fun goToAddressList(){
        val i = Intent(this, ClientAddressListActivity::class.java)
        startActivity(i)
    }

    fun setTotal(total: Double){
        textViewTotal?.text = "${total}$"
    }

    private fun getProductsFromSharedPref(){

        if(!sharedPref?.getData("order").isNullOrBlank()){ // EXISTS ORDER IN SHARE PREFERENCES
            val type = object: TypeToken<ArrayList<Product>>(){}.type
            selectedProducts = gson.fromJson(sharedPref?.getData("order"),type)
            //selectedProductsToTransport = gson.fromJson(sharedPref?.getData("orderToTransport"),type)

            adapter = ShoppingBagAdapter(this,selectedProducts)
            recyclerViewShoppingBag?.adapter = adapter

        }

    }

}