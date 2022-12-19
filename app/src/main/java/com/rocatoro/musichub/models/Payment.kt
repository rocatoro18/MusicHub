package com.rocatoro.musichub.models

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

class Payment(
    @SerializedName("id") val id: String? = null,
    @SerializedName("id_user") val id_user: String,
    @SerializedName("account_no") val account_no: String,
    @SerializedName("expiry") val expiry: String,
    @SerializedName("cvv") val cvv: String,
) {

    fun toJson(): String{
        return Gson().toJson(this)
    }

    override fun toString(): String {
        return "Payment(id=$id, id_user='$id_user', account_no=$account_no, expiry='$expiry', cvv=$cvv)"
    }

}