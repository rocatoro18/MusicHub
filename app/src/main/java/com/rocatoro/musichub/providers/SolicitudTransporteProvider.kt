package com.rocatoro.musichub.providers

import android.util.Log
import com.rocatoro.musichub.api.ApiRoutes
import com.rocatoro.musichub.models.*
import com.rocatoro.musichub.routes.CategoriesRoutes
import com.rocatoro.musichub.routes.ProductsProveedorRoutes
import com.rocatoro.musichub.routes.ProductsRoutes
import com.rocatoro.musichub.routes.SolicitudTransporteRoutes
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import java.io.File

class SolicitudTransporteProvider(val token: String) {

    private var solicitudTransporteRoutes: SolicitudTransporteRoutes? = null
    val TAG = "SolicTransProvider"
    init {
        val api = ApiRoutes()
        solicitudTransporteRoutes = api.getSolicitudTransporteRoutes(token)
        Log.d(TAG, token)
    }


    /*
    fun findByCategory(idCategory: String): Call<ArrayList<Product>>?{
        return  productsRoutes?.findByCategory(idCategory,token)
    }
    */

    fun create(solicitud: SolicitudTransporte): Call<ResponseHttp>? {

        //val requestBody = RequestBody.create(MediaType.parse("text/plain"),producto.toJson())
        Log.d(TAG,solicitud.toJson())

        //return productsProveedorRoutes?.create(p","total":1.0)
        return solicitudTransporteRoutes?.create(solicitud)

    }

}