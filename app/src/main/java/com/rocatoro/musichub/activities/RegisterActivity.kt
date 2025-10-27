package com.rocatoro.musichub.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import com.google.gson.Gson
import com.rocatoro.musichub.R
import com.rocatoro.musichub.activities.client.home.ClientHomeActivity
import com.rocatoro.musichub.databinding.ActivityRegisterBinding
import com.rocatoro.musichub.models.ResponseHttp
import com.rocatoro.musichub.models.User
import com.rocatoro.musichub.providers.UsersProvider
import com.rocatoro.musichub.utils.SharedPref
//import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : BaseActivity() {

    private lateinit var binding: ActivityRegisterBinding

    val TAG = "RegisterActivity"
    var usersProvider = UsersProvider()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)

        //setContentView(R.layout.activity_register)

        setContentView(binding.root)

        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        setupActionBar()

        binding.tvLogin.setOnClickListener {
            //val intent = Intent(this@RegisterActivity,LoginActivity::class.java)
            //startActivity(intent)
            //finish()
            onBackPressed()
        }

        binding.btnRegister.setOnClickListener{
            registerUser()
        }

    }

    private fun setupActionBar(){
        setSupportActionBar(binding.toolbarRegisterActivity)

        val actionBar = supportActionBar
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24)
        }
        binding.toolbarRegisterActivity.setNavigationOnClickListener { onBackPressed() }
    }

    private fun saveUserInSession(data: String){
        val sharedPref = SharedPref(this)
        val gson = Gson()
        val user = gson.fromJson(data,User::class.java)
        sharedPref.save("user",user)
    }

    // A function to validate the entries of new user.
    private fun validateRegisterDetails(): Boolean{
        return when {
            TextUtils.isEmpty(binding.etFirstName.text.toString().trim{it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_first_name),true)
                false
            }
            TextUtils.isEmpty(binding.etLastName.text.toString().trim{it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_last_name),true)
                false
            }
            TextUtils.isEmpty(binding.etEmail.text.toString().trim{it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email),true)
                false
            }
            TextUtils.isEmpty(binding.etPhone.text.toString().trim{it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_phone),true)
                false
            }
            TextUtils.isEmpty(binding.etPassword.text.toString().trim{it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password),true)
                false
            }
            TextUtils.isEmpty(binding.etConfirmPassword.text.toString().trim{it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_confirm_password),true)
                false
            }
            binding.etPassword.text.toString().trim{it <= ' '} != binding.etConfirmPassword.text.toString()
                .trim{it <= ' '} -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_password_and_confirm_password_mismatch),true)
                false
            }
            else -> {
                // showErrorSnackBar(resources.getString(R.string.registery_successful),false)
                true
            }
        }
    }

    private fun goToSaveImageActivity(){
        val i = Intent(this@RegisterActivity, SaveImageActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
    }

    private fun registerUser(){
        // Check with validate function if the entries are valid or not
        if(validateRegisterDetails()){
            showProgressDialog(resources.getString(R.string.please_wait))
            val name: String = binding.etFirstName.text.toString().trim{it <= ' '}
            val last_name: String = binding.etLastName.text.toString().trim {it <= ' '}
            val email: String = binding.etEmail.text.toString().trim{it <= ' '}
            val phone: String = binding.etPhone.text.toString().trim{it <= ' '}
            val password: String = binding.etPassword.text.toString().trim{it <= ' '}
            // Create an instance and create register a user with email and password
            val user = User(
                name = name,
                lastname = last_name,
                email = email,
                phone = phone,
                password = password
            )
            usersProvider.register(user)?.enqueue(object: Callback<ResponseHttp>{
                override fun onResponse(
                    call: Call<ResponseHttp>,
                    response: Response<ResponseHttp>
                ) {

                    if(response.body()?.isSuccess == true){
                        saveUserInSession(response.body()?.data.toString())
                        goToSaveImageActivity()
                    }
                    Toast.makeText(this@RegisterActivity,response.body()?.message,Toast.LENGTH_LONG).show()
                    Log.d(TAG,"Response: ${response}")
                    Log.d(TAG,"Body: ${response.body()}")
                    hideProgressDialog()
                }
                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    Log.d(TAG,"Se produjo un error ${t.message}")
                    Toast.makeText(this@RegisterActivity,"Se produjo un error ${t.message}",Toast.LENGTH_LONG).show()
                    hideProgressDialog()
                }
            })
        }

    }

}