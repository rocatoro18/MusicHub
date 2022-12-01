package com.rocatoro.musichub.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rocatoro.musichub.R
import kotlinx.android.synthetic.main.activity_forgot_password.*

class ForgotPasswordActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        setupActionBar()

    }

    private fun setupActionBar(){
        setSupportActionBar(toolbar_forgot_password_activity)

        val actionBar = supportActionBar
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24)
        }
        toolbar_forgot_password_activity.setNavigationOnClickListener { onBackPressed() }

        btn_submit.setOnClickListener {
            val email: String = et_email_forgot_pw.text.toString().trim { it <= ' ' }
            if(email.isEmpty()){
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email),true)
            } else {
                showProgressDialog(resources.getString(R.string.please_wait))

            }
        }

    }

}