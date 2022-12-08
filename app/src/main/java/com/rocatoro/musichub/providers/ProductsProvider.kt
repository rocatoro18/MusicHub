package com.rocatoro.musichub.providers

import android.util.Log
import com.rocatoro.musichub.api.ApiRoutes
import com.rocatoro.musichub.models.Category
import com.rocatoro.musichub.models.Product
import com.rocatoro.musichub.models.ResponseHttp
import com.rocatoro.musichub.models.User
import com.rocatoro.musichub.routes.CategoriesRoutes
import com.rocatoro.musichub.routes.ProductsRoutes
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import java.io.File

class ProductsProvider(val token: String) {

    private var productsRoutes: ProductsRoutes? = null
    val TAG = "ProductsProvider"
    init {
        val api = ApiRoutes()
        productsRoutes = api.getProductsRoutes(token)
        Log.d(TAG, token)
    }


    fun findByCategory(idCategory: String): Call<ArrayList<Product>>?{
        return  productsRoutes?.findByCategory(idCategory,token)
    }


    fun create(files: List<File>, product: Product): Call<ResponseHttp>? {

        val images = arrayOfNulls<MultipartBody.Part>(files.size)

        for(i in 0 until files.size){
            val reqFile = RequestBody.create(MediaType.parse("image/*"),files[i])
            images[i] = MultipartBody.Part.createFormData("image",files[i].name,reqFile)
        }

        val requestBody = RequestBody.create(MediaType.parse("text/plain"),product.toJson())

        return productsRoutes?.create(images,requestBody,token)

    }

}