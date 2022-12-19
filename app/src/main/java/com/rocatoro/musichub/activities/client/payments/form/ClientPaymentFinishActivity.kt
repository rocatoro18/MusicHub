package com.rocatoro.musichub.activities.client.payments.form

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.rocatoro.musichub.R
import com.rocatoro.musichub.activities.client.home.ClientHomeActivity
import com.rocatoro.musichub.utils.SharedPref

class ClientPaymentFinishActivity : AppCompatActivity() {

    var btnHome: Button? = null
    var sharedPref: SharedPref? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_payment_finish)

        sharedPref = SharedPref(this)

        btnHome = findViewById(R.id.btn_home)

        btnHome?.setOnClickListener { goHome() }

    }

    private fun goHome(){
        sharedPref?.remove("payment")
        sharedPref?.remove("address")
        sharedPref?.remove("order")
        val i = Intent(this,ClientHomeActivity::class.java)
        startActivity(i)
    }

}