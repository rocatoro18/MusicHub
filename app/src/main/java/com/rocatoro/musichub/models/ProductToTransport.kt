package com.rocatoro.musichub.models

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

class ProductToTransport(
    @SerializedName("id") val id: String? = null,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("image1") val image1: String? = null,
    @SerializedName("image2") val image2: String? = null,
    @SerializedName("image3") val image3: String? = null,
    @SerializedName("id_category") val idCategory: String,
    @SerializedName("price") val price: Double,
    @SerializedName("stock") val stock: Int,
    @SerializedName("quantity") var quantity: Int? = null
) {
    fun toJson(): String {
        return  Gson().toJson(this)
    }

    override fun toString(): String {
        return "$name"
    }


}