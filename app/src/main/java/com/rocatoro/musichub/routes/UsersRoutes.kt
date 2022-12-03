package com.rocatoro.musichub.routes

import com.rocatoro.musichub.models.ResponseHttp
import com.rocatoro.musichub.models.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface UsersRoutes {

    @POST("users/create")
    fun register(@Body user:User): Call<ResponseHttp>

}