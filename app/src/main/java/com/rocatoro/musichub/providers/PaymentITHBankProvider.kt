package com.rocatoro.musichub.providers

import android.util.Log
import com.rocatoro.musichub.api.ApiRoutes
import com.rocatoro.musichub.models.*
import com.rocatoro.musichub.routes.AddressRoutes
import com.rocatoro.musichub.routes.CategoriesRoutes
import com.rocatoro.musichub.routes.PaymentITHBankRoutes
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import java.io.File

class PaymentITHBankProvider(val token: String) {

    private var paymentITHBankRoutes: PaymentITHBankRoutes? = null
    val TAG = "PaymentITHBankProvider"
    init {
        val api = ApiRoutes()
        paymentITHBankRoutes = api.getPaymentITHBankRoutes(token)
        Log.d(TAG, token)
    }


    /*
    fun getAddress(id_user: String): Call<ArrayList<Address>>?{
        return  addressRoutes?.getAddress(id_user,token)
    }
    */

    fun createPaymentITHBank(paymentITHBank: PaymentITHBank): Call<ResponseHttp>? {
        return paymentITHBankRoutes?.createPayment(paymentITHBank.CuentaOrigen,paymentITHBank.FechaVencimientoP1,paymentITHBank.FechaVencimientoP2,
            paymentITHBank.Token,paymentITHBank.CuentaDestino,paymentITHBank.Monto,token)
    }

}