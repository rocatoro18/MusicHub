package com.rocatoro.musichub.fragments.musichub

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import com.rocatoro.musichub.R
import com.rocatoro.musichub.models.Category
import com.rocatoro.musichub.models.ResponseHttp
import com.rocatoro.musichub.models.User
import com.rocatoro.musichub.providers.CategoriesProvider
import com.rocatoro.musichub.utils.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import kotlin.math.log


class MusicHubCategoryFragment : Fragment() {

    var TAG = "MusicHubCategoryFragment"
    var myView: View? = null
    var imageViewCategory: ImageView? = null
    var editTextCategory: EditText? = null
    var buttonCreate: Button? = null

    private var imageFile: File? = null

    private var categoriesProvider: CategoriesProvider? = null

    var sharedPref: SharedPref? = null
    var user: User? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_music_hub_category, container, false)

        sharedPref = SharedPref(requireActivity())

        imageViewCategory = myView?.findViewById(R.id.imageview_category)
        editTextCategory = myView?.findViewById(R.id.et_category)
        buttonCreate = myView?.findViewById(R.id.btn_create)

        imageViewCategory?.setOnClickListener {
            selectImage()
        }

        buttonCreate?.setOnClickListener {
            createCategory()
        }

        getUserFromSession()

        Log.d(TAG,user?.sessionToken!!)

        categoriesProvider = CategoriesProvider(user?.sessionToken!!)

        return myView
    }

    private fun createCategory(){
        val name = editTextCategory?.text.toString()
        Log.d(TAG,name)

        if(imageFile != null){

            val category = Category(name = name)
            Log.d(TAG,category.toString())
            Log.d(TAG,imageFile.toString())
            categoriesProvider?.create(imageFile!!,category)?.enqueue(object: Callback<ResponseHttp>{
                override fun onResponse(
                    call: Call<ResponseHttp>,
                    response: Response<ResponseHttp>
                ) {
                    Log.d(TAG,"RESPONSE: $response")
                    Log.d(TAG,"BODY: ${response.body()}")
                    Toast.makeText(requireContext(),response.body()?.message,Toast.LENGTH_LONG).show()
                    if (response.body()?.isSuccess == true){
                        clearForm()
                    }
                }

                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    Log.d(TAG,"Error: ${t.message}")
                    Toast.makeText(requireContext(),"Error: ${t.message}",Toast.LENGTH_LONG).show()
                }
            })

        }
        else {
            Toast.makeText(requireContext(),"Selecciona una imagen",Toast.LENGTH_LONG).show()
        }

    }

    private fun clearForm(){
        editTextCategory?.setText("")
        imageFile = null
        imageViewCategory?.setImageResource(R.drawable.ic_image)
    }

    private fun getUserFromSession(){

        val gson = Gson()

        if(!sharedPref?.getData("user").isNullOrBlank()){
            // IF USER EXIST IN SESSION
            user = gson.fromJson(sharedPref?.getData("user"), User::class.java)
            Log.d(TAG,"Usuario: $user")
        }

    }

    private val startImageForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result: ActivityResult ->

            val resultCode = result.resultCode
            val data = result.data

            if(resultCode == Activity.RESULT_OK){
                val fileUri = data?.data
                imageFile = File(fileUri?.path) // EL ARCHIVO QUE VAMOS A GUARDAR COMO IMAGEN EN EL SERVIDOR
                imageViewCategory?.setImageURI(fileUri)
            }
            else if (resultCode == ImagePicker.RESULT_ERROR){
                Toast.makeText(requireContext(),ImagePicker.getError(data),Toast.LENGTH_LONG).show()
            }
            else {
                Toast.makeText(requireContext(),"Tarea se canceló", Toast.LENGTH_LONG).show()
            }


        }

    private fun selectImage(){
        ImagePicker.with(this)
            .crop()	    			//Crop image(Optional), Check Customization for more option
            .compress(1024)			//Final image size will be less than 1 MB(Optional)
            .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
            .createIntent { intent ->
                startImageForResult.launch(intent)
            }
    }

}