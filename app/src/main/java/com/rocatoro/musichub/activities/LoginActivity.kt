package com.rocatoro.musichub.activities

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import com.google.gson.Gson
import com.rocatoro.musichub.R
import com.rocatoro.musichub.activities.MusicHubStore.home.MusicHubHomeActivity
import com.rocatoro.musichub.activities.client.home.ClientHomeActivity
import com.rocatoro.musichub.activities.delivery.home.DeliveryHomeActivity
import com.rocatoro.musichub.models.ResponseHttp
import com.rocatoro.musichub.models.User
import com.rocatoro.musichub.providers.UsersProvider
import com.rocatoro.musichub.utils.SharedPref
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : BaseActivity(), View.OnClickListener {

    val TAG = "LoginActivity"
    var usersProvider = UsersProvider()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        // Click event assigned to Forgot Password text
        tv_forgot_password.setOnClickListener(this)
        // Click event assigned to Login button
        btn_login.setOnClickListener(this)
        // Click event assigned to Register text
        tv_register.setOnClickListener(this)

        getUserFromSession()

    }

    // In Login screen the clickable components are Login Button, Forgot Password text and the Register text
    override fun onClick(view: View?){
        if(view != null){
            when(view.id){
                R.id.tv_forgot_password -> {
                    // Launch the register screen when the user clicks on the text
                    val intent = Intent(this@LoginActivity,ForgotPasswordActivity::class.java)
                    startActivity(intent)
                    //finish()
                }
                R.id.btn_login -> {
                    // TODO Step 6: Call validate function.
                    // Start
                    loginInRegisteredUser()
                    // End
                }
                R.id.tv_register -> {
                    // Launch the register screen when the user clicks on the text
                    val intent = Intent(this@LoginActivity,RegisterActivity::class.java)
                    startActivity(intent)
                    //finish()
                }
            }
        }
    }

    private fun validateLoginDetails(): Boolean{
        return when {
            TextUtils.isEmpty(et_email.text.toString().trim{it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email),true)
                false
            }
            TextUtils.isEmpty(et_password.text.toString().trim{it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password),true)
                false
            }
            else -> {
                true
            }
        }
    }

    private fun saveUserInSession(data: String){
        val sharedPref = SharedPref(this)
        val gson = Gson()
        val user = gson.fromJson(data,User::class.java)
        sharedPref.save("user",user)

        if(user.roles?.size!! > 1){ // HAVE MORE THAN ONE ROL
            goToSelectRol()
        }
        else { // ONLY HAVE ONE ROL
            goToClientHome()
        }

    }

    private fun goToSelectRol(){
        val i = Intent(this@LoginActivity,SelectRolesActivity::class.java)
        i.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
    }

    private fun goToClientHome(){
        val i = Intent(this@LoginActivity,ClientHomeActivity::class.java)
        i.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
    }

    private fun goToMusicHubADMINHome(){
        val i = Intent(this@LoginActivity,MusicHubHomeActivity::class.java)
        i.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
    }

    private fun goToDeliveryHome(){
        val i = Intent(this@LoginActivity,DeliveryHomeActivity::class.java)
        i.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
    }

    private fun getUserFromSession(){
        val sharedPref = SharedPref(this)
        val gson = Gson()

        if(!sharedPref.getData("user").isNullOrBlank()){
            // IF USER EXIST IN SESSION
            val user = gson.fromJson(sharedPref.getData("user"),User::class.java)

            if(!sharedPref.getData("rol").isNullOrBlank()){
                // SI EL USUARIO SELECCIONO EL ROL
                val rol = sharedPref.getData("rol")?.replace("\"","")

                if(rol == "Cliente"){
                    goToClientHome()
                }
                else if(rol == "Music Hub ADMIN"){
                    goToMusicHubADMINHome()
                }
                else if(rol == "DELIVERY"){
                    goToDeliveryHome()
                }

            }
            else {
                goToClientHome()
            }
        }

    }

    private fun loginInRegisteredUser(){
        if(validateLoginDetails()){
            // Show the progress dialog
            showProgressDialog(resources.getString(R.string.please_wait))
            // Get the text from editText and trim the space
            val email = et_email.text.toString().trim{ it <= ' '}
            val password = et_password.text.toString().trim{ it <= ' '}
            // Log-In using Firebase Authentication
            usersProvider.login(email,password)?.enqueue(object: Callback<ResponseHttp>{
                override fun onResponse(
                    call: Call<ResponseHttp>,
                    response: Response<ResponseHttp>
                ) {
                    Log.d(TAG,"Response: ${response.body()}")
                    if (response.body()?.isSuccess == true){
                        Toast.makeText(this@LoginActivity,response.body()?.message,
                            Toast.LENGTH_LONG).show()
                        hideProgressDialog()
                        saveUserInSession(response.body()?.data.toString())
                        //goToClientHome()
                    }else{
                        Toast.makeText(this@LoginActivity,"Los datos no son correctos",
                            Toast.LENGTH_LONG).show()
                        hideProgressDialog()
                    }
                }

                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    Log.d(TAG,"Hubo un error ${t.message}")
                    Toast.makeText(this@LoginActivity,"Hubo un error ${t.message}",
                    Toast.LENGTH_LONG).show()
                    hideProgressDialog()
                }

            })

        }
    }

}