package com.rocatoro.musichub.fragments.musichub

import android.app.Activity
import android.content.Intent
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
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import com.rocatoro.musichub.R
import com.rocatoro.musichub.activities.adapters.CategoriesAdapter
import com.rocatoro.musichub.models.Category
import com.rocatoro.musichub.models.Product
import com.rocatoro.musichub.models.ResponseHttp
import com.rocatoro.musichub.models.User
import com.rocatoro.musichub.providers.CategoriesProvider
import com.rocatoro.musichub.providers.ProductsProvider
import com.rocatoro.musichub.utils.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class MusicHubProductFragment : Fragment() {

    val TAG = "ProductFragment"

    var myView: View? = null
    var editTextName: EditText? = null
    var editTextDescription: EditText? = null
    var editTextPrice: EditText? = null
    var editTextStock: EditText? = null

    var imageViewProduct1: ImageView? = null
    var imageViewProduct2: ImageView? = null
    var imageViewProduct3: ImageView? = null

    var buttonCreate: Button? = null

    var imageFile1: File? = null
    var imageFile2: File? = null
    var imageFile3: File? = null
    var spinnerCategories: Spinner? = null

    var categoriesProvider: CategoriesProvider? = null
    var user: User? = null
    var sharedPref: SharedPref? = null
    var categories = ArrayList<Category>()

    var idCategory = ""

    var productsProvider: ProductsProvider? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_music_hub_product, container, false)

        editTextName = myView?.findViewById(R.id.et_name)
        editTextDescription = myView?.findViewById(R.id.et_description)
        editTextPrice = myView?.findViewById(R.id.et_price)
        editTextStock = myView?.findViewById(R.id.et_stock)

        imageViewProduct1 = myView?.findViewById(R.id.imageview_image1)
        imageViewProduct2 = myView?.findViewById(R.id.imageview_image2)
        imageViewProduct3 = myView?.findViewById(R.id.imageview_image3)

        spinnerCategories = myView?.findViewById(R.id.spinner_categories)

        buttonCreate = myView?.findViewById(R.id.btn_create)

        buttonCreate?.setOnClickListener {
            createProduct()
        }

        imageViewProduct1?.setOnClickListener { selectImage(101) }

        imageViewProduct2?.setOnClickListener { selectImage(102) }

        imageViewProduct3?.setOnClickListener { selectImage(103) }

        sharedPref = SharedPref(requireActivity())

        getUserFromSession()

        categoriesProvider = CategoriesProvider(user?.sessionToken!!)
        productsProvider = ProductsProvider(user?.sessionToken!!)

        getCategories()

        return myView
    }

    private fun createProduct(){
        val name = editTextName?.text.toString()
        val description = editTextDescription?.text.toString()
        val priceText = editTextPrice?.text.toString()
        val stockText = editTextStock?.text.toString()

        val files = ArrayList<File>()

        if (isValidForm(name,description,priceText,stockText)){
            val product = Product(
                name = name,
                description = description,
                price = priceText.toDouble(),
                stock = stockText.toInt(),
                idCategory = idCategory
            )
            files.add(imageFile1!!)
            files.add(imageFile2!!)
            files.add(imageFile3!!)

            productsProvider?.create(files,product)?.enqueue(object: Callback<ResponseHttp>{
                override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>
                ) {
                    Log.d(TAG,"Response: $response")
                    Log.d(TAG,"Response: ${response.body()}")

                    Toast.makeText(requireContext(),response.body()?.message,Toast.LENGTH_LONG).show()

                }

                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    Log.d(TAG,"Error: ${t.message}")
                    Toast.makeText(requireContext(),"Error: ${t.message}",Toast.LENGTH_LONG).show()
                }
            })

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {

            val fileUri = data?.data

            if(requestCode == 101){
                //Toast.makeText(requireContext(),"Si entro",Toast.LENGTH_LONG).show()
                imageFile1 = File(fileUri?.path) // EL ARCHIVO QUE VAMOS A GUARDAR COMO IMAGEN EN EL SERVIDOR
                imageViewProduct1?.setImageURI(fileUri)
            }
            else if (requestCode == 102){
                //Toast.makeText(requireContext(),"Si entro",Toast.LENGTH_LONG).show()
                imageFile2 = File(fileUri?.path) // EL ARCHIVO QUE VAMOS A GUARDAR COMO IMAGEN EN EL SERVIDOR
                imageViewProduct2?.setImageURI(fileUri)
            }
            else if (requestCode == 103){
                imageFile3 = File(fileUri?.path) // EL ARCHIVO QUE VAMOS A GUARDAR COMO IMAGEN EN EL SERVIDOR
                imageViewProduct3?.setImageURI(fileUri)
            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    private fun selectImage(requestCode: Int){
        ImagePicker.with(this)
            .crop()	    			//Crop image(Optional), Check Customization for more option
            .compress(1024)			//Final image size will be less than 1 MB(Optional)
            .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
            .start(requestCode)
    }

    private fun isValidForm(name: String, description: String, price: String, stock: String): Boolean{

        if(name.isNullOrBlank()){
            Toast.makeText(requireContext(),"Ingrese el nombre del producto",Toast.LENGTH_LONG).show()
            return false
        }
        if(description.isNullOrBlank()){
            Toast.makeText(requireContext(),"Ingrese la descripción del producto",Toast.LENGTH_LONG).show()
            return false
        }
        if(price.isNullOrBlank()){
            Toast.makeText(requireContext(),"Ingrese el precio del producto",Toast.LENGTH_LONG).show()
            return false
        }
        if(stock.isNullOrBlank()){
            Toast.makeText(requireContext(),"Ingrese la cantidad del producto",Toast.LENGTH_LONG).show()
            return false
        }
        if (imageFile1 == null){
            Toast.makeText(requireContext(),"Seleccione la imagen 1",Toast.LENGTH_LONG).show()
            return false
        }
        if (imageFile2 == null){
            Toast.makeText(requireContext(),"Seleccione la imagen 2",Toast.LENGTH_LONG).show()
            return false
        }
        if (imageFile3 == null){
            Toast.makeText(requireContext(),"Seleccione la imagen 3",Toast.LENGTH_LONG).show()
            return false
        }
        if (idCategory.isNullOrBlank()){
            Toast.makeText(requireContext(),"Seleccione una categoría para el producto",Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }

    private fun getCategories(){
        categoriesProvider?.getAll()?.enqueue(object: Callback<ArrayList<Category>> {
            override fun onResponse(
                call: Call<ArrayList<Category>>,
                response: Response<ArrayList<Category>>
            ) {

                if(response.body() != null){
                    categories = response.body()!!

                    val arrayAdapter = ArrayAdapter<Category>(requireActivity(),android.R.layout.simple_dropdown_item_1line,
                    categories)
                    spinnerCategories?.adapter = arrayAdapter
                    spinnerCategories?.onItemSelectedListener = object:AdapterView.OnItemSelectedListener{
                        override fun onItemSelected(adapterview: AdapterView<*>?, view: View?, position: Int, l: Long
                        ) {
                            idCategory = categories[position].id!!
                            Log.d(TAG,"Id category: $idCategory")
                        }

                        override fun onNothingSelected(p0: AdapterView<*>?) {

                        }

                    }
                }

            }

            override fun onFailure(call: Call<ArrayList<Category>>, t: Throwable) {
                Log.d(TAG,"Error: ${t.message}")
                Toast.makeText(requireContext(),"Error: ${t.message}",Toast.LENGTH_LONG).show()
            }
        })
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