package com.rocatoro.musichub.providers

import com.rocatoro.musichub.api.ApiRoutes
import com.rocatoro.musichub.models.ResponseHttp
import com.rocatoro.musichub.models.User
import com.rocatoro.musichub.routes.UsersRoutes
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import java.io.File

class UsersProvider(val token: String? = null) {

    private var usersRoutes: UsersRoutes? = null
    private var usersRoutesToken: UsersRoutes? = null

    init {
        val api = ApiRoutes()
        usersRoutes = api.getUsersRoutes()

        if (token != null){
            usersRoutesToken = api.getUsersRoutesWithToken(token!!)
        }

    }

    fun register(user: User): Call<ResponseHttp>?{
        return usersRoutes?.register(user)
    }

    fun login(email: String, password: String): Call<ResponseHttp>?{
        return usersRoutes?.login(email,password)
    }

    fun update(file: File, user: User): Call<ResponseHttp>? {
        val reqFile = RequestBody.create(MediaType.parse("image/*"),file)
        val image = MultipartBody.Part.createFormData("image",file.name,reqFile)
        val requestBody = RequestBody.create(MediaType.parse("text/plain"),user.toJson())

        return usersRoutesToken?.update(image,requestBody,token!!)

    }

}