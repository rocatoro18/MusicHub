package com.rocatoro.musichub.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Im
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import com.rocatoro.musichub.R
import com.rocatoro.musichub.activities.client.home.ClientHomeActivity
import com.rocatoro.musichub.models.ResponseHttp
import com.rocatoro.musichub.models.User
import com.rocatoro.musichub.providers.UsersProvider
import com.rocatoro.musichub.utils.SharedPref
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class SaveImageActivity : AppCompatActivity() {

    val TAG = "SaveImageActivity"

    var circleImageUser: CircleImageView? = null
    var buttonNext: Button? = null
    var buttonConfirm: Button? = null

    private var imageFile: File? = null

    var usersProvider: UsersProvider? = null

    var user: User? = null

    var sharedPref: SharedPref? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save_image)

        sharedPref = SharedPref(this)

        getUserFromSession()

        usersProvider = UsersProvider(user?.sessionToken)

        circleImageUser = findViewById(R.id.circleimage_user)
        buttonNext = findViewById(R.id.btn_next)
        buttonConfirm = findViewById(R.id.btn_confirm)

        circleImageUser?.setOnClickListener {
            selectImage()
        }

        buttonConfirm?.setOnClickListener {
            saveImage()
        }

        buttonNext?.setOnClickListener {
            goToClientHome()
        }

    }

    private fun saveUserInSession(data: String){
        val gson = Gson()
        val user = gson.fromJson(data,User::class.java)
        sharedPref?.save("user",user)
        goToClientHome()

    }

    private fun saveImage(){

        if(imageFile != null && user != null) {
            usersProvider?.update(imageFile!!,user!!)?.enqueue(object: Callback<ResponseHttp> {
                override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {
                    Log.d(TAG,"Response: $response")
                    Log.d(TAG,"Body: ${response.body()}")

                    if(response.body()?.isSuccess == true){
                        saveUserInSession(response.body()?.data.toString())
                    } else {
                        Toast.makeText(this@SaveImageActivity,"${response.body()?.message}",Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    Log.d(TAG,"Error: ${t.message}")
                    Toast.makeText(this@SaveImageActivity,"Error: ${t.message}",Toast.LENGTH_LONG).show()
                }

            })
        }
        else {
            Toast.makeText(this@SaveImageActivity,"La imagen no puede ser nula ni " +
                    "tampoco los datos de sesión de usuario",Toast.LENGTH_LONG).show()
        }

    }

    private fun getUserFromSession(){

        val gson = Gson()

        if(!sharedPref?.getData("user").isNullOrBlank()){
            // IF USER EXIST IN SESSION
            user = gson.fromJson(sharedPref?.getData("user"), User::class.java)
        }

    }

    private fun goToClientHome(){
        val i = Intent(this@SaveImageActivity, ClientHomeActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
    }

    private val startImageForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result: ActivityResult ->
        val resultCode = result.resultCode
        val data = result.data

        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            val fileUri = data?.data!!

            imageFile = File(fileUri.path) // FILE THAT WE WILL SAVE AS IMAGE ON SERVER
            circleImageUser?.setImageURI(fileUri)
        }
        else if (resultCode == ImagePicker.RESULT_ERROR){
            Toast.makeText(this, ImagePicker.getError(data),Toast.LENGTH_LONG).show()
        }
        else {
            Toast.makeText(this, "La tarea se ha cancelado",Toast.LENGTH_LONG).show()
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