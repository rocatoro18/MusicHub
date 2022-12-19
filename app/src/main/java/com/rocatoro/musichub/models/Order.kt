package com.rocatoro.musichub.models

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

class Order(
    @SerializedName("id") val id: String? = null,
    @SerializedName("id_client") val id_client: String,
    @SerializedName("id_delivery") var id_delivery: String? = null,
    @SerializedName("id_address") val id_address: String,
    @SerializedName("numero_venta") val numero_venta: Int,
    @SerializedName("id_payment") val id_payment: String,
    @SerializedName("status") val status: String? = null,
    @SerializedName("timestamp") val timestamp: String? = null,
    @SerializedName("products") val products: ArrayList<Product>,
    @SerializedName("client") val client: User? = null,
    @SerializedName("address") val address: Address? = null

    ){

    fun toJson(): String{
        return Gson().toJson(this)
    }

    override fun toString(): String {
        return "Order(id=$id, id_client='$id_client', id_delivery=$id_delivery, id_address='$id_address', numero_venta=$numero_venta, id_payment='$id_payment', status=$status, timestamp=$timestamp, products=$products, client=$client, address=$address)"
    }


}