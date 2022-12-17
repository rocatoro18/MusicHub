package com.rocatoro.musichub.models

import com.google.gson.Gson

class VentaExterna(
    val id: String,
    val no_orden: String,
    val monto_total: String,
    val no_art: Int,
    val cantidad_producto: Int,
    val no_producto: Int,
    val nombre_producto: String,
    val descuento_producto: String,
    val no_serie_producto: String,
    val precio_producto: Double,
) {

    fun toJson(): String{
        return Gson().toJson(this)
    }

    override fun toString(): String {
        return "VentaExterna(id='$id', no_orden='$no_orden', monto_total='$monto_total', no_art=$no_art, cantidad_producto=$cantidad_producto, no_producto=$no_producto, nombre_producto='$nombre_producto', descuento_producto='$descuento_producto', no_serie_producto='$no_serie_producto', precio_producto=$precio_producto)"
    }
}