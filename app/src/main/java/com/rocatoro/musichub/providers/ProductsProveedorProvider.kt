package com.rocatoro.musichub.providers

import android.util.Log
import com.rocatoro.musichub.api.ApiRoutes
import com.rocatoro.musichub.models.*
import com.rocatoro.musichub.routes.CategoriesRoutes
import com.rocatoro.musichub.routes.ProductsProveedorRoutes
import com.rocatoro.musichub.routes.ProductsRoutes
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import java.io.File

class ProductsProveedorProvider(val token: String) {

    private var productsProveedorRoutes: ProductsProveedorRoutes? = null
    val TAG = "ProductsProvProvider"
    init {
        val api = ApiRoutes()
        productsProveedorRoutes = api.getProductosProveedorRoutes(token)
        Log.d(TAG, token)
    }


    /*
    fun findByCategory(idCategory: String): Call<ArrayList<Product>>?{
        return  productsRoutes?.findByCategory(idCategory,token)
    }
    */

    fun create(producto: ProductProveedor): Call<ResponseHttp>? {

        //val requestBody = RequestBody.create(MediaType.parse("text/plain"),producto.toJson())
        Log.d(TAG,producto.toJson())

        //return productsProveedorRoutes?.create(p","total":1.0)
        return productsProveedorRoutes?.create(producto)

    }

}