package com.rocatoro.musichub.fragments.client

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.rocatoro.musichub.R
import com.rocatoro.musichub.activities.LoginActivity
import com.rocatoro.musichub.activities.SelectRolesActivity
import com.rocatoro.musichub.models.User
import com.rocatoro.musichub.utils.SharedPref
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_client_profile.*

class ClientProfileFragment : Fragment() {

    val TAG = "ClientProfileFragment"

    var user: User? = null

    var sharedPref: SharedPref? = null
    var myView: View? = null

    var buttonSelectRol: Button? = null
    //var buttonUpdateProfile: Button? = null

    var circleImageUser: CircleImageView? = null
    var textViewName: TextView? = null
    var textViewEmail: TextView? = null
    var textViewPhone: TextView? = null

    var imageViewLogout: ImageView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_client_profile, container, false)

        sharedPref = SharedPref(requireActivity())

        getUserFromSession()

        buttonSelectRol = myView?.findViewById(R.id.btn_select_rol)
        //buttonUpdateProfile = myView?.findViewById(R.id.btn_update_profile)

        circleImageUser = myView?.findViewById(R.id.circleimage_user)

        textViewName = myView?.findViewById(R.id.textview_name)
        textViewEmail = myView?.findViewById(R.id.textview_email)
        textViewPhone = myView?.findViewById(R.id.textview_phone)

        imageViewLogout = myView?.findViewById(R.id.imageview_logout)

        buttonSelectRol?.setOnClickListener {
            goToSelectRol()
        }

        imageViewLogout?.setOnClickListener{
            logout()
        }

        textViewName?.text = "${user?.name} ${user?.lastname}"
        textViewEmail?.text = user?.email
        textViewPhone?.text = user?.phone

        if(!user?.image.isNullOrBlank()){
            Glide.with(requireContext()).load(user?.image).into(circleImageUser!!)
        }

        return myView
    }

    private fun getUserFromSession(){

        val gson = Gson()

        if(!sharedPref?.getData("user").isNullOrBlank()){
            // IF USER EXIST IN SESSION
            user = gson.fromJson(sharedPref?.getData("user"), User::class.java)
            Log.d(TAG,"Usuario: $user")
        }

    }

    private fun logout(){
        sharedPref?.remove("user")
        val i = Intent(requireContext(), LoginActivity::class.java)
        startActivity(i)
    }

    private fun goToSelectRol(){
        val i = Intent(requireContext(), SelectRolesActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
    }

}