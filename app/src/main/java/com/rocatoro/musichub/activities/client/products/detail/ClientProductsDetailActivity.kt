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
import com.rocatoro.musichub.databinding.ActivityClientProductsDetailBinding
import com.rocatoro.musichub.models.Product
import com.rocatoro.musichub.models.ProductToTransport
import com.rocatoro.musichub.utils.SharedPref
//import kotlinx.android.synthetic.main.activity_client_products_detail.*
import org.w3c.dom.Text

class ClientProductsDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityClientProductsDetailBinding

    val TAG = "ProductsDetail"
    //var toolbar: Toolbar? = null

    var product: Product? = null
    //var productqToTransport: ProductToTransport? = null
    val gson = Gson()

    //var imageSlider: ImageSlider? = null
    //var textViewName: TextView? = null
    //var textViewDescription: TextView? = null
    //var textViewPrice: TextView? = null
    //var textViewCounter: TextView? = null
    //var imageViewAdd: ImageView? = null
    //var imageViewRemove: ImageView? = null
    //var buttonAdd: Button? = null

    var counter = 1
    var productPrice = 0.00

    var sharedPref: SharedPref? = null
    var selectedProducts = ArrayList<Product>()
    var selectedProductsToTransport = ArrayList<ProductToTransport>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClientProductsDetailBinding.inflate(layoutInflater)
        //setContentView(R.layout.activity_client_products_detail)
        setContentView(binding.root)
        //toolbar = findViewById(R.id.toolbar)
        //toolbar?.title = "Detalles Producto"
        //toolbar?.setTitleTextColor(ContextCompat.getColor(this,R.color.white))
        //setSupportActionBar(toolbar)
        //supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupToolbar()

        product = gson.fromJson(intent.getStringExtra("product"),Product::class.java)

        sharedPref = SharedPref(this)

        //imageSlider = findViewById(R.id.imageslider)
        //textViewName = findViewById(R.id.textview_name)
        //textViewDescription = findViewById(R.id.textview_description)
        //textViewPrice = findViewById(R.id.textview_price)
        //textViewCounter = findViewById(R.id.textview_counter)
        //imageViewAdd = findViewById(R.id.imageview_add)
        //imageViewRemove = findViewById(R.id.imageview_remove)
        //buttonAdd = findViewById(R.id.btn_add_product)

        //val imageList = ArrayList<SlideModel>()
        //imageList.add(SlideModel(product?.image1,ScaleTypes.CENTER_CROP))
        //imageList.add(SlideModel(product?.image2,ScaleTypes.CENTER_CROP))
        //imageList.add(SlideModel(product?.image3,ScaleTypes.CENTER_CROP))

        //imageSlider?.setImageList(imageList)
        //textViewName?.text = product?.name
        //textViewDescription?.text = product?.description
        //textViewPrice?.text = "${product?.price}$"


        //imageViewAdd?.setOnClickListener { addItem() }
        //imageViewRemove?.setOnClickListener { removeItem() }

        //buttonAdd?.setOnClickListener { addToBag() }
        setupUI()
        getProductsFromSharedPref()

    }

    private fun setupToolbar() {
        val toolbar = binding.includeToolbar.toolbar
        toolbar.title = "Detalles Producto"
        toolbar.setTitleTextColor(ContextCompat.getColor(this,R.color.white))
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupUI() {
        val imageList = ArrayList<SlideModel>()
        product?.let {
            imageList.add(SlideModel(it.image1, ScaleTypes.CENTER_CROP))
            imageList.add(SlideModel(it.image2, ScaleTypes.CENTER_CROP))
            imageList.add(SlideModel(it.image3, ScaleTypes.CENTER_CROP))
        }

        binding.imageslider.setImageList(imageList)
        binding.textviewName.text = product?.name
        binding.textviewDescription.text = product?.description
        binding.textviewPrice.text = "${product?.price}$"

        binding.imageviewAdd.setOnClickListener { addItem() }
        binding.imageviewRemove.setOnClickListener { removeItem() }
        binding.btnAddProduct.setOnClickListener { addToBag() }
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
                    binding.textviewCounter.text = "$counter"
                    //textViewCounter?.text = "${product?.quantity}"

                    productPrice = product?.price!! * product?.quantity!!
                    binding.textviewPrice.text = "${productPrice}$"

                //buttonAdd?.setText("EDITAR PRODUCTO")
                binding.btnAddProduct.text = "EDITAR PRODUCTO"
                binding.btnAddProduct.backgroundTintList = ColorStateList.valueOf(Color.RED)

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
        binding.textviewCounter.text = "${product?.quantity}"
        binding.textviewPrice.text = "${productPrice}$"
    }

    private fun removeItem(){
        if (counter > 1){
            counter--
            productPrice = product?.price!! * counter
            product?.quantity = counter
            binding.textviewCounter.text = "${product?.quantity}"
            binding.textviewPrice.text = "${productPrice}$"
        }
    }

}