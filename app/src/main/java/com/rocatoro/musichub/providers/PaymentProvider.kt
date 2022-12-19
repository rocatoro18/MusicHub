package com.rocatoro.musichub.providers

import android.util.Log
import com.rocatoro.musichub.api.ApiRoutes
import com.rocatoro.musichub.models.*
import com.rocatoro.musichub.routes.AddressRoutes
import com.rocatoro.musichub.routes.CategoriesRoutes
import com.rocatoro.musichub.routes.PaymentRoutes
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import java.io.File

class PaymentProvider(val token: String) {

    private var paymentRoutes: PaymentRoutes? = null
    val TAG = "PaymentProvider"
    init {
        val api = ApiRoutes()
        paymentRoutes = api.getPaymentRoutes(token)
        Log.d(TAG, token)
    }



    fun getPayment(id_user: String): Call<ArrayList<Payment>>?{
        return  paymentRoutes?.getPayment(id_user,token)
    }



    fun create(payment: Payment): Call<ResponseHttp>? {
        return paymentRoutes?.create(payment,token)
    }

}