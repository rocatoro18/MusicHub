package com.rocatoro.musichub.models

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

class ProductProveedor(
    @SerializedName("nombre") var nombre: String,
    @SerializedName("importe") var importe: Double,
    @SerializedName("cantidadUnidad") var cantidadUnidad: Int,
    @SerializedName("total") var total: Double,
    @SerializedName("fecha") var fecha: String,
    @SerializedName("idProveedor") val idProveedor: Int = 30,
    @SerializedName("descripcion") var descripcion: String = "Sin descripción",

) {
    fun toJson(): String {
        return  Gson().toJson(this)
    }

    override fun toString(): String {
        return "ProductProveedor(nombre='$nombre', importe=$importe, cantidadUnidad=$cantidadUnidad, total=$total, fecha='$fecha', idProveedor=$idProveedor, descripcion='$descripcion')"
    }


}