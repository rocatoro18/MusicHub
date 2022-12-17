package com.rocatoro.musichub.fragments.musichub

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import com.rocatoro.musichub.R
import com.rocatoro.musichub.activities.BaseActivity
import com.rocatoro.musichub.activities.MusicHubStore.ventaexterna.RegistroVentaExternaActivity
import com.rocatoro.musichub.activities.adapters.CategoriesAdapter
import com.rocatoro.musichub.models.*
import com.rocatoro.musichub.providers.CategoriesProvider
import com.rocatoro.musichub.providers.ProductsProveedorProvider
import com.rocatoro.musichub.providers.ProductsProvider
import com.rocatoro.musichub.utils.SharedPref

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.time.LocalDate
import java.time.LocalDateTime

class MusicHubProductProveedorFragment : Fragment() {

    val TAG = "ProductProvFragment"

    var myView: View? = null
    var editTextName: EditText? = null
    var editTextPrice: EditText? = null
    var editTextQuantity: EditText? = null


    var buttonCreate: Button? = null
    var buttonVentaExterna: Button? = null

    var user: User? = null
    var sharedPref: SharedPref? = null

    var productsProveedorProvider: ProductsProveedorProvider? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_music_hub_product_proveedor, container, false)

        editTextName = myView?.findViewById(R.id.et_name)
        editTextPrice = myView?.findViewById(R.id.et_price)
        editTextQuantity = myView?.findViewById(R.id.et_quantity)

        buttonCreate = myView?.findViewById(R.id.btn_create)
        buttonVentaExterna = myView?.findViewById(R.id.btn_venta_externa)

        buttonVentaExterna?.setOnClickListener { goToRegVentaExterna() }

        buttonCreate?.setOnClickListener {
            createProduct()
        }

        sharedPref = SharedPref(requireActivity())

        getUserFromSession()

        productsProveedorProvider = ProductsProveedorProvider(user?.sessionToken!!)



        return myView
    }

    private fun goToRegVentaExterna(){
        val i = Intent(requireContext(),RegistroVentaExternaActivity::class.java)
        startActivity(i)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createProduct(){
        val name = editTextName?.text.toString()
        val priceText = editTextPrice?.text.toString()
        val quantityText = editTextQuantity?.text.toString()
        //val current = LocalDate.now()
        val current = LocalDateTime.now()
        val files = ArrayList<File>()

        if (isValidForm(name,priceText,quantityText)){
            val productoModel = ProductProveedor(name,priceText.toDouble(),quantityText.toInt(),priceText.toDouble() * quantityText.toInt(),current.toString())
            //ProgressDialogFragment.showProgressBar(requireActivity())

            productsProveedorProvider?.create(productoModel)?.enqueue(object: Callback<ResponseHttp>{
                override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>
                ) {
                    //ProgressDialogFragment.hideProgressBar(requireActivity())
                    Log.d(TAG,"Response: $response")
                    Log.d(TAG,"Response: ${response.body()}")

                    if(response.body() != null){
                        Toast.makeText(requireContext(),"Se ha registrado con éxito su producto",Toast.LENGTH_LONG).show()
                        resetForm()
                    }else{
                        Toast.makeText(requireContext(),"No se pudo registrar su producto",Toast.LENGTH_LONG).show()
                        resetForm()
                    }
                }

                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    //ProgressDialogFragment.hideProgressBar(requireActivity())
                    Log.d(TAG,"Error: ${t.message}")
                    Toast.makeText(requireContext(),"Error: ${t.message}",Toast.LENGTH_LONG).show()
                }
            })

        }

    }

    private fun resetForm() {
        editTextName?.setText("")
        editTextPrice?.setText("")
        editTextQuantity?.setText("")


    }

    private fun isValidForm(nombre: String, precio: String, cantidad: String): Boolean{

        if(nombre.isNullOrBlank()){
            Toast.makeText(requireContext(),"Ingrese el nombre del producto",Toast.LENGTH_LONG).show()
            return false
        }
        if(precio.isNullOrBlank()){
            Toast.makeText(requireContext(),"Ingrese el precio del producto",Toast.LENGTH_LONG).show()
            return false
        }
        if(cantidad.isNullOrBlank()){
            Toast.makeText(requireContext(),"Ingrese la cantidad del producto",Toast.LENGTH_LONG).show()
            return false
        }

        return true
    }

    private fun getUserFromSession(){

        val gson = Gson()

        if(!sharedPref?.getData("user").isNullOrBlank()){
            // IF USER EXIST IN SESSION
            user = gson.fromJson(sharedPref?.getData("user"),User::class.java)
            Log.d(TAG,"Usuario: $user")
        }

    }

}