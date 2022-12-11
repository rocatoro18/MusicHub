package com.rocatoro.musichub.models

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

class Address(
    @SerializedName("id") val id: String? = null,
    @SerializedName("id_user") val id_user: String,
    @SerializedName("address") val address: String,
    @SerializedName("postal_code") val postal_code: String,
    @SerializedName("phone") val phone: String,
) {
    override fun toString(): String {
        return "Address(id=$id, id_user='$id_user', address='$address', postal_code='$postal_code', phone='$phone')"
    }

    fun toJson(): String{
        return Gson().toJson(this)
    }

}