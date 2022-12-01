package com.rocatoro.musichub.activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowInsets
import android.view.WindowManager
import com.rocatoro.musichub.R
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
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

        tv_login.setOnClickListener {
            //val intent = Intent(this@RegisterActivity,LoginActivity::class.java)
            //startActivity(intent)
            //finish()
            onBackPressed()
        }

        btn_register.setOnClickListener{
            registerUser()
        }

    }

    private fun setupActionBar(){
        setSupportActionBar(toolbar_register_activity)

        val actionBar = supportActionBar
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24)
        }
        toolbar_register_activity.setNavigationOnClickListener { onBackPressed() }
    }

    // A function to validate the entries of new user.
    private fun validateRegisterDetails(): Boolean{
        return when {
            TextUtils.isEmpty(et_first_name.text.toString().trim{it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_first_name),true)
                false
            }
            TextUtils.isEmpty(et_last_name.text.toString().trim{it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_last_name),true)
                false
            }
            TextUtils.isEmpty(et_email.text.toString().trim{it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email),true)
                false
            }
            TextUtils.isEmpty(et_password.text.toString().trim{it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password),true)
                false
            }
            TextUtils.isEmpty(et_confirm_password.text.toString().trim{it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_confirm_password),true)
                false
            }
            et_password.text.toString().trim{it <= ' '} != et_confirm_password.text.toString()
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

    private fun registerUser(){
        // Check with validate function if the entries are valid or not
        if(validateRegisterDetails()){
            showProgressDialog(resources.getString(R.string.please_wait))
            val email: String = et_email.text.toString().trim{it <= ' '}
            val password: String = et_password.text.toString().trim{it <= ' '}
            // Create an instance and create register a user with email and password

        }

    }

}