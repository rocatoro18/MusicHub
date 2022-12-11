package com.rocatoro.musichub.providers

import android.util.Log
import com.rocatoro.musichub.api.ApiRoutes
import com.rocatoro.musichub.models.Address
import com.rocatoro.musichub.models.Category
import com.rocatoro.musichub.models.ResponseHttp
import com.rocatoro.musichub.models.User
import com.rocatoro.musichub.routes.AddressRoutes
import com.rocatoro.musichub.routes.CategoriesRoutes
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import java.io.File

class AddressProvider(val token: String) {

    private var addressRoutes: AddressRoutes? = null
    val TAG = "AddressProvider"
    init {
        val api = ApiRoutes()
        addressRoutes = api.getAddressRoutes(token)
        Log.d(TAG, token)
    }


    fun getAddress(id_user: String): Call<ArrayList<Address>>?{
        return  addressRoutes?.getAddress(id_user,token)
    }


    fun create(address: Address): Call<ResponseHttp>? {
        return addressRoutes?.create(address,token)
    }

}