package com.rocatoro.musichub.activities.client.payments.create

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.rocatoro.musichub.R
import com.rocatoro.musichub.activities.client.payments.list.ClientPaymentListActivity
import com.rocatoro.musichub.models.Payment
import com.rocatoro.musichub.models.ResponseHttp
import com.rocatoro.musichub.models.User
import com.rocatoro.musichub.providers.PaymentProvider
import com.rocatoro.musichub.utils.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClientPaymentCreateActivity : AppCompatActivity() {

    val TAG = "ClientPaymentCreate"

    var toolbar: Toolbar? = null

    var paymentProvider: PaymentProvider? = null

    var sharedPref: SharedPref? = null
    var user: User? = null
    val gson = Gson()

    var editTextAccountNo: EditText? = null
    var editTextDate: EditText? = null
    var editTextCVV: EditText? = null
    var btnCreatePayment: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_payment_create)

        toolbar = findViewById(R.id.toolbar)
        toolbar?.title = "Agregar un método de pago"
        toolbar?.setTitleTextColor(ContextCompat.getColor(this,R.color.white))
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        sharedPref = SharedPref(this)
        getUserFromSession()

        paymentProvider = PaymentProvider(user?.sessionToken!!)

        editTextAccountNo = findViewById(R.id.et_account_no)
        editTextDate = findViewById(R.id.et_date)
        editTextCVV = findViewById(R.id.et_cvv)
        btnCreatePayment = findViewById(R.id.btn_create_payment)

        btnCreatePayment?.setOnClickListener { createPayment() }


    }

    private fun createPayment(){
        val accountNo = editTextAccountNo?.text.toString()
        val date = editTextDate?.text.toString()
        val cvv = editTextCVV?.text.toString()

        if(isValidForm(accountNo,date,cvv)){
            val modelPayment = Payment(
                id_user = user?.id!!,
                account_no = accountNo,
                expiry = date,
                cvv = cvv
            )

            paymentProvider?.create(modelPayment)?.enqueue(object: Callback<ResponseHttp>{
                override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>
                ) {
                    if(response.body() != null){
                        Toast.makeText(this@ClientPaymentCreateActivity,response.body()!!.message,Toast.LENGTH_LONG).show()
                        goToPaymentList()
                    }
                    else {
                        Toast.makeText(this@ClientPaymentCreateActivity,"Ocurrió un error en la petición",Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    Toast.makeText(this@ClientPaymentCreateActivity,"Error: ${t.message}",Toast.LENGTH_LONG).show()
                }
            })

        }



    }

    private fun goToPaymentList(){
        val i = Intent(this,ClientPaymentListActivity::class.java)
        startActivity(i)
    }

    private fun isValidForm(accountNo: String, date: String, cvv: String): Boolean{

        if(accountNo.isNullOrBlank()){
            Toast.makeText(this,"Ingrese el número de su tarjeta", Toast.LENGTH_LONG).show()
            return false
        }
        if(date.isNullOrBlank()){
            Toast.makeText(this,"Ingrese la fecha de vencimiento", Toast.LENGTH_LONG).show()
            return false
        }
        if(cvv.isNullOrBlank()){
            Toast.makeText(this,"Ingrese el CVV", Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }

    private fun getUserFromSession(){

        val gson = Gson()

        if(!sharedPref?.getData("user").isNullOrBlank()){
            // IF USER EXIST IN SESSION
            user = gson.fromJson(sharedPref?.getData("user"), User::class.java)
            Log.d(TAG,"Usuario: $user")
        }

    }

}