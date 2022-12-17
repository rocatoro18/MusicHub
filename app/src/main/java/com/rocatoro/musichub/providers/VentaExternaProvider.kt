package com.rocatoro.musichub.providers

import android.util.Log
import com.rocatoro.musichub.api.ApiRoutes
import com.rocatoro.musichub.models.Category
import com.rocatoro.musichub.models.ResponseHttp
import com.rocatoro.musichub.models.User
import com.rocatoro.musichub.models.VentaExterna
import com.rocatoro.musichub.routes.CategoriesRoutes
import com.rocatoro.musichub.routes.VentaExternaRoutes
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import java.io.File

class VentaExternaProvider(val token: String) {

    private var ventaExternaRoutes: VentaExternaRoutes? = null
    val TAG = "VentaExternaProvider"
    init {
        val api = ApiRoutes()
        ventaExternaRoutes = api.getVentaExternaRoutes(token)
        Log.d(TAG, token)
    }

    fun getAll(): Call<ArrayList<VentaExterna>>?{
        return  ventaExternaRoutes?.getAll(token)
    }


    //fun create(file: File, category: Category): Call<ResponseHttp>? {
      //  val reqFile = RequestBody.create(MediaType.parse("image/*"),file)
       // val image = MultipartBody.Part.createFormData("image",file.name,reqFile)
       // val requestBody = RequestBody.create(MediaType.parse("text/plain"),category.toJson())

        //return categoriesRoutes?.create(image,requestBody,token)
    //}

}