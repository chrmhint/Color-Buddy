package com.example.colorbuddy.Groups

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.Images.Media.getBitmap
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.net.toUri
import com.example.colorbuddy.R
import com.example.colorbuddy.classes.Item
import com.example.colorbuddy.ItemChecker
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_item_checker.*
import kotlinx.android.synthetic.main.activity_new_item.*

import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class NewItemActivity : AppCompatActivity() {

    private lateinit var ref: DatabaseReference
    private lateinit var groupName: String
    private lateinit var itemId: String
    private lateinit var itemType: String
    private lateinit var itemName: String
    private lateinit var itemDescript: String
    private lateinit var itemPalette: LinearLayout
    private lateinit var c1: String
    private lateinit var c2: String
    private lateinit var c3: String
    private lateinit var c4: String
    private lateinit var c5: String
    private val IMAGE_CAPTURE_CODE = 1001
    val PERMISSION_CODE = 1000

    var imageSRC: Uri? = null
    var hexStrings = arrayListOf<String>("#FFFFFF", "#FFFFFF", "#FFFFFF", "#FFFFFF", "#FFFFFF")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_item)

        itemPalette = findViewById(R.id.newItemPalette)

        btnNewItem.setOnClickListener {
            addItem()
        }


        //take picture

        if (checkSelfPermission(Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_DENIED ||
            checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_DENIED
        ) {
            //permission denied
            val permission =
                arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            //show popup
            requestPermissions(permission, PERMISSION_CODE)
        } else {
            //permission granted
            openCamera()

            val param = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT,
                1.0f
            )

            newItemC1.setBackgroundColor(Color.parseColor(hexStrings[0]))
            newItemC1.layoutParams = param
            newItemC2.setBackgroundColor(Color.parseColor(hexStrings[1]))
            newItemC2.layoutParams = param
            newItemC3.setBackgroundColor(Color.parseColor(hexStrings[2]))
            newItemC3.layoutParams = param
            newItemC4.setBackgroundColor(Color.parseColor(hexStrings[3]))
            newItemC4.layoutParams = param
            newItemC5.setBackgroundColor(Color.parseColor(hexStrings[4]))
            newItemC5.layoutParams = param

        }




    }

    private fun addItem(){
        groupName = intent.getStringExtra("EXTRA_GROUP_NAME")
        itemType = intent.getStringExtra("EXTRA_ITEM_TYPE")
        itemName = newItemName.text.toString()
        itemDescript = newItemDescription.text.toString()

        c1 = hexStrings[0]
        c2 = hexStrings[1]
        c3 = hexStrings[2]
        c4 = hexStrings[3]
        c5 = hexStrings[4]

        ref = FirebaseDatabase.getInstance().getReference("Items")
        itemId = ref.push().key!!
        val item = Item(groupName,itemId,itemType,itemName,itemDescript,c1,c2,c3,c4,c5)
        ref.child(itemId).setValue(item)

        finish()
    }

    //allow user to take a picture
    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Item")
        values.put(MediaStore.Images.Media.DESCRIPTION, "Camera")
        imageSRC = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        //camera intent
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageSRC)
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    //permission granted
                    openCamera()
                } else {
                    //permission not granted
                    Toast.makeText(this, "Permission Denied.", Toast.LENGTH_SHORT)
                }
            }
        }
    }

    //capture image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(resultCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            //set image to ImageViewer
            imageView.setImageURI(imageSRC)

            //convert image to bitmap to read pixel data
            val bm = getBitmap(imageView)
            bm?.let { readImage(it) }
        }


    }

    //convert image to bitmap
    fun getBitmap(imageView2: ImageView): Bitmap? {
        return (imageView2.drawable as BitmapDrawable).bitmap
    }

    //find prominent colors
    fun readImage(bm: Bitmap) {

        //bounds of color search
        val startWidth: Int = (bm.width - (bm.width * .8)).roundToInt()
        val endWidth = (bm.width - (bm.width * .2)).roundToInt()
        val startHeight = (bm.height - (bm.height * .8)).roundToInt()
        val endHeight = (bm.height - (bm.height * .2)).roundToInt()


        //collect pixel information and build map of color -> color appearances
        var pixels = mutableMapOf(0 to 0)
        var color = 0

        //within subsection of picture, take every 100th pixel
        for (w in startWidth until endWidth step 100) {
            for (h in startHeight until endHeight step 100) {
                //argb
                color = bm.getPixel(w, h)

                //separate each pixel
                val A = color shr 24 and 0xff // or color >>> 24
                var R = color shr 16 and 0xff
                var G = color shr 8 and 0xff
                var B = color and 0xff

                //posterize color
                R = posterizePixel(R)
                G = posterizePixel(G)
                B = posterizePixel(B)

                var post = Color.argb(A, R, G, B)

                //find prominent colors
                if (pixels.containsKey(post)) {
                    val value: Int = pixels.getValue(post)
                    pixels.replace(post, value, value + 1)

                }

                else
                    pixels[post] = 1
            }

        }


        //convert map to list of most frequent colors
        val filtered_pixels = pixels.filterValues { it >= 10 }
        val pixel_list = ArrayList(filtered_pixels.keys)


        //get hex strings
        for(e in 0..4){
            hexStrings[e] = Integer.toHexString(pixel_list[e])

            hexStrings[e] = hexStrings[e].toUpperCase(Locale.US)

            //remove alpha
            hexStrings[e] = hexStrings[e].substring(2)

            //add #
            hexStrings[e] = "#" + hexStrings[e]
        }


    }

    //round each pixel
    fun posterizePixel(p: Int): Int {
        if (p < 5)
            return 0
        if( p < 20)
            return 15
        if (p < 40)
            return 25
        if(p < 55)
            return 40
        if (p < 70)
            return 50
        if(p < 85)
            return 70
        if(p < 100)
            return 85
        if(p < 115)
            return 100
        if (p < 130)
            return 115
        if(p < 140)
            return 120
        if (p < 160)
            return 140
        if(p < 200)
            return 160
        if(p < 250)
            return 200

        return 250
    }


}
