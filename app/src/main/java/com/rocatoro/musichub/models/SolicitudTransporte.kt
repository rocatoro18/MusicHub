package com.rocatoro.musichub.models

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

class SolicitudTransporte(
    @SerializedName("numeroVenta") val numeroVenta: Int,
    @SerializedName("productos") val productos: String,
    @SerializedName("nombreDestinatario") val nombreDestinatario: String,
    @SerializedName("direccionDestino") val direccionDestino: String,
    @SerializedName("fechaEntrega") val fechaEntrega: Int,
    @SerializedName("idCliente") val idCliente: Int,
    ){



    fun toJson(): String{
        return Gson().toJson(this)
    }

    override fun toString(): String {
        return "SolicitudTransporte(numeroVenta=$numeroVenta, productos='$productos', nombreDestinatario='$nombreDestinatario', direccionDestino='$direccionDestino', fechaEntrega=$fechaEntrega, idCliente=$idCliente)"
    }


}