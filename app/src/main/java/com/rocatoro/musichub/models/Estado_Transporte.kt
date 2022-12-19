package com.rocatoro.musichub.models

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

class Estado_Transporte(
    @SerializedName("numero_venta") val numero_venta: Int? = null,
    @SerializedName("numero_guia") val numero_guia: Int? = null,
    @SerializedName("estado_transporte") val estado_transporte: String? = null,
    ){

    fun toJson(): String{
        return Gson().toJson(this)
    }

    override fun toString(): String {
        return "Estado_Transporte(numero_venta=$numero_venta, numero_guia=$numero_guia, estado_transporte=$estado_transporte)"
    }


}