package com.rocatoro.musichub.models

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

class PaymentITHBank(
    @SerializedName("CuentaOrigen") val CuentaOrigen: String,
    @SerializedName("FechaVencimientoP1") val FechaVencimientoP1: String,
    @SerializedName("FechaVencimientoP2") val FechaVencimientoP2: String,
    @SerializedName("Token") val Token: String,
    @SerializedName("CuentaDestino") val CuentaDestino: String,
    @SerializedName("Monto") val Monto: String,
) {



    fun toJson(): String{
        return Gson().toJson(this)
    }

    override fun toString(): String {
        return "PaymentITHBank(CuentaOrigen='$CuentaOrigen', FechaVencimientoP1='$FechaVencimientoP1', FechaVencimientoP2='$FechaVencimientoP2', Token='$Token', CuentaDestino='$CuentaDestino', Monto='$Monto')"
    }

}