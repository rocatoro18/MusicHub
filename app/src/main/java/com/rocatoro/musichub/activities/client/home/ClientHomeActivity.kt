package com.rocatoro.musichub.activities.client.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.gson.Gson
import com.rocatoro.musichub.R
import com.rocatoro.musichub.activities.LoginActivity
import com.rocatoro.musichub.models.User
import com.rocatoro.musichub.utils.SharedPref
import kotlinx.android.synthetic.main.activity_client_home.*

class ClientHomeActivity : AppCompatActivity() {
    private val TAG = "ClientHomeActivity"
    var sharedPref: SharedPref? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_home)
        sharedPref = SharedPref(this)
        getUserFromSession()

        btn_logout.setOnClickListener {
            logout()
        }

    }

    private fun logout(){
        sharedPref?.remove("user")
        val i = Intent(this@ClientHomeActivity,LoginActivity::class.java)
        startActivity(i)
    }

    private fun getUserFromSession(){

        val gson = Gson()

        if(!sharedPref?.getData("user").isNullOrBlank()){
            // IF USER EXIST IN SESSION
            val user = gson.fromJson(sharedPref?.getData("user"),User::class.java)
            Log.d(TAG,"Usuario: $user")
        }

    }

}