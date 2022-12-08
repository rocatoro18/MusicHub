package com.rocatoro.musichub.providers

import android.util.Log
import com.rocatoro.musichub.api.ApiRoutes
import com.rocatoro.musichub.models.Category
import com.rocatoro.musichub.models.ResponseHttp
import com.rocatoro.musichub.models.User
import com.rocatoro.musichub.routes.CategoriesRoutes
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import java.io.File

class CategoriesProvider(val token: String) {

    private var categoriesRoutes: CategoriesRoutes? = null
    val TAG = "CategoriesProvider"
    init {
        val api = ApiRoutes()
        categoriesRoutes = api.getCategoriesRoutes(token)
        Log.d(TAG, token)
    }

    fun getAll(): Call<ArrayList<Category>>?{
        return  categoriesRoutes?.getAll(token)
    }

    fun create(file: File, category: Category): Call<ResponseHttp>? {
        val reqFile = RequestBody.create(MediaType.parse("image/*"),file)
        val image = MultipartBody.Part.createFormData("image",file.name,reqFile)
        val requestBody = RequestBody.create(MediaType.parse("text/plain"),category.toJson())

        return categoriesRoutes?.create(image,requestBody,token)

    }

}