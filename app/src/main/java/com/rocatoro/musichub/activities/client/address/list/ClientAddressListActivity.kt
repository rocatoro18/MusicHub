package com.rocatoro.musichub.activities.client.address.list

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.rocatoro.musichub.R
import com.rocatoro.musichub.activities.client.address.create.ClientAdressCreateActivity

class ClientAddressListActivity : AppCompatActivity() {

    var toolbar: Toolbar? = null
    var fabCreateAddress: FloatingActionButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_address_list)

        toolbar = findViewById(R.id.toolbar)
        toolbar?.title = "Mis direcciones"
        toolbar?.setTitleTextColor(ContextCompat.getColor(this,R.color.white))
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        fabCreateAddress = findViewById(R.id.fab_address_create)

        fabCreateAddress?.setOnClickListener{goToAddressCreate()}

    }

    private fun goToAddressCreate(){
        val i = Intent(this,ClientAdressCreateActivity::class.java)
        startActivity(i)
    }

}