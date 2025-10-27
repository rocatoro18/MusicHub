package com.rocatoro.musichub.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rocatoro.musichub.R
import com.rocatoro.musichub.databinding.ActivityForgotPasswordBinding
//import kotlinx.android.synthetic.main.activity_forgot_password.*

class ForgotPasswordActivity : BaseActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        //setContentView(R.layout.activity_forgot_password)
        setContentView(binding.root)

        setupActionBar()

    }

    private fun setupActionBar(){
        setSupportActionBar(binding.toolbarForgotPasswordActivity)

        val actionBar = supportActionBar
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24)
        }
        binding.toolbarForgotPasswordActivity.setNavigationOnClickListener { onBackPressed() }

        binding.btnSubmit.setOnClickListener {
            val email: String = binding.etEmailForgotPw.text.toString().trim { it <= ' ' }
            if(email.isEmpty()){
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email),true)
            } else {
                showProgressDialog(resources.getString(R.string.please_wait))

            }
        }

    }

}