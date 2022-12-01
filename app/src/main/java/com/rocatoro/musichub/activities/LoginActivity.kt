package com.rocatoro.musichub.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import com.rocatoro.musichub.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity(), View.OnClickListener {
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

    private fun loginInRegisteredUser(){
        if(validateLoginDetails()){
            // Show the progress dialog
            showProgressDialog(resources.getString(R.string.please_wait))
            // Get the text from editText and trim the space
            val email = et_email.text.toString().trim{ it <= ' '}
            val password = et_password.text.toString().trim{ it <= ' '}
            // Log-In using Firebase Authentication

        }
    }

}