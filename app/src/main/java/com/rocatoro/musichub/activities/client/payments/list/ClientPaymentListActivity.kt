package com.rocatoro.musichub.activities.client.payments.list

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.rocatoro.musichub.R
import com.rocatoro.musichub.activities.client.payments.create.ClientPaymentCreateActivity

class ClientPaymentListActivity : AppCompatActivity() {

    val TAG = "PaymentList"

    var toolbar: Toolbar? = null
    var fabCreatePayment: FloatingActionButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_payment_list)

        toolbar = findViewById(R.id.toolbar)
        toolbar?.title = "Mis métodos de pago"
        toolbar?.setTitleTextColor(ContextCompat.getColor(this,R.color.white))
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        fabCreatePayment = findViewById(R.id.fab_payment_create)

        fabCreatePayment?.setOnClickListener{goToPaymentCreate()}

    }

    private fun goToPaymentCreate(){
        val i = Intent(this,ClientPaymentCreateActivity::class.java)
        startActivity(i)
    }

}