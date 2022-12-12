package com.rocatoro.musichub.providers

import android.util.Log
import com.rocatoro.musichub.api.ApiRoutes
import com.rocatoro.musichub.models.*
import com.rocatoro.musichub.routes.AddressRoutes
import com.rocatoro.musichub.routes.CategoriesRoutes
import com.rocatoro.musichub.routes.OrdersRoutes
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import java.io.File

class OrdersProvider(val token: String) {

    private var orderRoutes: OrdersRoutes? = null
    val TAG = "OrderProvider"
    init {
        val api = ApiRoutes()
        orderRoutes = api.getOrdersRoutes(token)
        Log.d(TAG, token)
    }


    fun getOrdersByStatus(status: String): Call<ArrayList<Order>>?{
        return  orderRoutes?.getOrdersByStatus(status, token)
    }

    fun getOrdersByClientAndStatus(id_client: String,status: String): Call<ArrayList<Order>>?{
        return  orderRoutes?.getOrdersByClientAndStatus(id_client,status,token)
    }


    fun create(order: Order): Call<ResponseHttp>? {
        return orderRoutes?.create(order,token)
    }

}