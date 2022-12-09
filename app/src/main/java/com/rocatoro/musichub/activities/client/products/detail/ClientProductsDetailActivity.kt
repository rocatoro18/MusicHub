package com.rocatoro.musichub.activities.client.products.detail

import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rocatoro.musichub.R
import com.rocatoro.musichub.models.Product
import com.rocatoro.musichub.utils.SharedPref
import kotlinx.android.synthetic.main.activity_client_products_detail.*
import org.w3c.dom.Text

class ClientProductsDetailActivity : AppCompatActivity() {

    val TAG = "ProductsDetail"
    var toolbar: Toolbar? = null

    var product: Product? = null
    val gson = Gson()

    var imageSlider: ImageSlider? = null
    var textViewName: TextView? = null
    var textViewDescription: TextView? = null
    var textViewPrice: TextView? = null
    var textViewCounter: TextView? = null
    var imageViewAdd: ImageView? = null
    var imageViewRemove: ImageView? = null
    var buttonAdd: Button? = null

    var counter = 1
    var productPrice = 0.00

    var sharedPref: SharedPref? = null
    var selectedProducts = ArrayList<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_products_detail)
        toolbar = findViewById(R.id.toolbar)
        toolbar?.title = "Detalles Producto"
        toolbar?.setTitleTextColor(ContextCompat.getColor(this,R.color.white))
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        product = gson.fromJson(intent.getStringExtra("product"),Product::class.java)

        sharedPref = SharedPref(this)

        imageSlider = findViewById(R.id.imageslider)
        textViewName = findViewById(R.id.textview_name)
        textViewDescription = findViewById(R.id.textview_description)
        textViewPrice = findViewById(R.id.textview_price)
        textViewCounter = findViewById(R.id.textview_counter)
        imageViewAdd = findViewById(R.id.imageview_add)
        imageViewRemove = findViewById(R.id.imageview_remove)
        buttonAdd = findViewById(R.id.btn_add_product)

        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel(product?.image1,ScaleTypes.CENTER_CROP))
        imageList.add(SlideModel(product?.image2,ScaleTypes.CENTER_CROP))
        imageList.add(SlideModel(product?.image3,ScaleTypes.CENTER_CROP))

        imageSlider?.setImageList(imageList)
        textViewName?.text = product?.name
        textViewDescription?.text = product?.description
        textViewPrice?.text = "${product?.price}$"


        imageViewAdd?.setOnClickListener { addItem() }
        imageViewRemove?.setOnClickListener { removeItem() }

        buttonAdd?.setOnClickListener { addToBag() }

        getProductsFromSharedPref()

    }

    private fun addToBag(){

        val index = getIndexOf(product?.id!!) // INDEX OF PRODUCTS IF EXISTS IN SHARED PREF
        //Log.d(TAG,"INDEX: ${index}")
        if(index == -1){ // THIS PRODUCT DOESN'T EXIST SO FAR ON SHARED PREF
            //Log.d(TAG,"PRODUCT QUANTITY: ${product?.quantity}")
            if(product?.quantity == null){
                //Log.d(TAG,"ENTRO AL NO")
                product?.quantity = 1
                //Log.d(TAG,"PRODUCT QUANTITY DESPUES: ${product?.quantity}")
            }
            selectedProducts.add(product!!)
        }
        else { // PRODUCT ALREADY EXISTS ON SHARED PREF WE MUST EDIT THE QUANTITY
            //Log.d(TAG,"ENTRO AL SI")
            selectedProducts[index].quantity = counter
        }
        //Log.d(TAG,"No entro en ninguno")
        //selectedProducts.add(product!!)
        sharedPref?.save("order",selectedProducts)
        Toast.makeText(this,"Producto agregado",Toast.LENGTH_LONG).show()

    }

    private fun getProductsFromSharedPref(){

        if(!sharedPref?.getData("order").isNullOrBlank()){ // EXISTS ORDER IN SHARE PREFERENCES
            val type = object: TypeToken<ArrayList<Product>>(){}.type
            selectedProducts = gson.fromJson(sharedPref?.getData("order"),type)

            val index = getIndexOf(product?.id!!)

            if (index != -1){

                product?.quantity = selectedProducts[index].quantity

                //Desde la BD asignamos la cantidad al textview
                    counter = product?.quantity!!
                    textViewCounter?.text = "$counter"
                    //textViewCounter?.text = "${product?.quantity}"

                    productPrice = product?.price!! * product?.quantity!!
                    textViewPrice?.text = "${productPrice}$"

                buttonAdd?.setText("EDITAR PRODUCTO")
                buttonAdd?.backgroundTintList = ColorStateList.valueOf(Color.RED)

            }
            for(p in selectedProducts){
                Log.d(TAG,"Shared pref: $p")
            }

        }

    }

    // SEARCH IF A PRODUCT EXIST IN SHARED PREF FOR ONLY EDIT THE QUANTITY OF SELECTED PRODUCT
    private fun getIndexOf(idProduct: String): Int{
        var pos = 0

        for (p in selectedProducts){
            if(p.id == idProduct){
                return pos
            }
            pos++
        }
        return -1
    }

    private fun addItem(){
        counter++
        productPrice = product?.price!! * counter
        product?.quantity = counter
        textview_counter.text = "${product?.quantity}"
        textViewPrice?.text = "${productPrice}$"
    }

    private fun removeItem(){
        if (counter > 1){
            counter--
            productPrice = product?.price!! * counter
            product?.quantity = counter
            textview_counter.text = "${product?.quantity}"
            textViewPrice?.text = "${productPrice}$"
        }
    }

}