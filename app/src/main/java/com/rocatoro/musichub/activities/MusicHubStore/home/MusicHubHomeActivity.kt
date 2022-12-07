package com.rocatoro.musichub.activities.MusicHubStore.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import com.rocatoro.musichub.R
import com.rocatoro.musichub.activities.LoginActivity
import com.rocatoro.musichub.fragments.client.ClientCategoriesFragment
import com.rocatoro.musichub.fragments.client.ClientOrdersFragment
import com.rocatoro.musichub.fragments.client.ClientProfileFragment
import com.rocatoro.musichub.fragments.musichub.MusicHubCategoryFragment
import com.rocatoro.musichub.fragments.musichub.MusicHubOrdersFragment
import com.rocatoro.musichub.fragments.musichub.MusicHubProductFragment
import com.rocatoro.musichub.models.User
import com.rocatoro.musichub.utils.SharedPref
import kotlinx.android.synthetic.main.activity_client_home.*

class MusicHubHomeActivity : AppCompatActivity() {
    private val TAG = "MusicHubHomeActivity"
    var sharedPref: SharedPref? = null

    var bottomNavigation: BottomNavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_hub_home)
        sharedPref = SharedPref(this)
        getUserFromSession()


        openFragment(MusicHubOrdersFragment())

        bottomNavigation = findViewById(R.id.bottom_navigation)

        bottomNavigation?.setOnItemSelectedListener {
            when (it.itemId){

                R.id.item_home -> {
                    openFragment(MusicHubOrdersFragment())
                    true
                }

                R.id.item_category -> {
                    openFragment(MusicHubCategoryFragment())
                    true
                }

                R.id.item_product -> {
                    openFragment(MusicHubProductFragment())
                    true
                }

                R.id.item_profile -> {
                    openFragment(ClientProfileFragment())
                    true
                }

                else -> false

            }
        }

        /**
        btn_logout.setOnClickListener {
        logout()
        }
         */


    }

    private fun openFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container,fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun logout(){
        sharedPref?.remove("user")
        val i = Intent(this@MusicHubHomeActivity,LoginActivity::class.java)
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