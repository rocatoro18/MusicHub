package com.rocatoro.musichub.api

import com.rocatoro.musichub.models.Product
import com.rocatoro.musichub.routes.*
import retrofit2.Retrofit
import retrofit2.create

class ApiRoutes {

    //val API_URL = "http://192.168.1.5:3000/api/"
    val API_URL = "https://music-hub.herokuapp.com/api/"

    val API_URL_PROVEEDOR = "https://proveedores-api-production.up.railway.app/api/"

    val API_URL_TRANSPORTE = "https://transportesithapi-production.up.railway.app/api/"

    val API_URL_BANCO = "http://www.itbank.somee.com/api/"

    val retrofit = RetrofitClient()

    fun getUsersRoutes(): UsersRoutes {
        return retrofit.getClient(API_URL).create(UsersRoutes::class.java)
    }

    fun getUsersRoutesWithToken(token: String): UsersRoutes {
        return retrofit.getClientWithToken(API_URL,token).create(UsersRoutes::class.java)
    }

    fun getCategoriesRoutes(token: String): CategoriesRoutes {
        return retrofit.getClientWithToken(API_URL,token).create(CategoriesRoutes::class.java)
    }

    fun getVentaExternaRoutes(token: String): VentaExternaRoutes{
        return retrofit.getClientWithToken(API_URL,token).create(VentaExternaRoutes::class.java)
    }

    fun getProductsRoutes(token: String): ProductsRoutes {
        return retrofit.getClientWithToken(API_URL,token).create(ProductsRoutes::class.java)
    }

    fun getProductosProveedorRoutes(token: String): ProductsProveedorRoutes {
        return retrofit.getClientWithToken(API_URL_PROVEEDOR,token).create(ProductsProveedorRoutes::class.java)
    }

    fun getSolicitudTransporteRoutes(token: String): SolicitudTransporteRoutes {
        return retrofit.getClientWithToken(API_URL_TRANSPORTE,token).create(SolicitudTransporteRoutes::class.java)
    }

    fun getAddressRoutes(token: String): AddressRoutes {
        return retrofit.getClientWithToken(API_URL,token).create(AddressRoutes::class.java)
    }

    fun getPaymentITHBankRoutes(token: String): PaymentITHBankRoutes {
        return retrofit.getClientWithToken(API_URL_BANCO,token).create(PaymentITHBankRoutes::class.java)
    }

    fun getPaymentRoutes(token: String): PaymentRoutes {
        return retrofit.getClientWithToken(API_URL,token).create(PaymentRoutes::class.java)
    }

    fun getOrdersRoutes(token: String): OrdersRoutes {
        return retrofit.getClientWithToken(API_URL,token).create(OrdersRoutes::class.java)
    }

}