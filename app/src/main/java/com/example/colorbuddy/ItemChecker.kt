package com.example.colorbuddy

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import android.graphics.drawable.BitmapDrawable
import android.graphics.Bitmap
import android.graphics.Color
import android.widget.ImageView
import com.example.colorbuddy.Groups.EXTRA_GROUP_NAME
import com.example.colorbuddy.Groups.NewItemActivity
import kotlin.math.roundToInt
import com.example.colorbuddy.R
import com.example.colorbuddy.adapters.EXTRA_ITEM_TYPE
import com.example.colorbuddy.classes.Item
import kotlinx.android.synthetic.main.activity_item_checker.*

const val EXTRA_COLOR1:String = "EXTRA_COLOR_1"
const val EXTRA_COLOR2:String = "EXTRA_COLOR_2"
const val EXTRA_COLOR3:String = "EXTRA_COLOR_3"
const val EXTRA_COLOR4:String = "EXTRA_COLOR_4"
const val EXTRA_COLOR5:String = "EXTRA_COLOR_5"
const val EXTRA_IMG_SRC:String = "EXTRA_IMG_SRC"

//on confirm, move to newItemActivity with hexString info
class ItemChecker : AppCompatActivity() {


    private val IMAGE_CAPTURE_CODE = 1001
    val PERMISSION_CODE = 1000

    var imageSRC: Uri? = null
    var hexStrings = arrayListOf<String>("FFFFFF", "FFFFFF", "FFFFFF", "FFFFFF", "FFFFFF")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_checker)

        val groupName = intent.getStringExtra("EXTRA_GROUP_NAME")
        val itemType = intent.getStringExtra("EXTRA_ITEM_TYPE")

        //take picture
        btnCapture.setOnClickListener {
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

            }
        }

        //confirm -> move to NewItemActivity
        btnConfirm.setOnClickListener {
            val intent = Intent(this, NewItemActivity::class.java)
            intent.putExtra(EXTRA_GROUP_NAME,groupName)
            intent.putExtra(EXTRA_ITEM_TYPE,itemType)
            intent.putExtra(EXTRA_COLOR1, hexStrings[0])
            intent.putExtra(EXTRA_COLOR2, hexStrings[1])
            intent.putExtra(EXTRA_COLOR3, hexStrings[2])
            intent.putExtra(EXTRA_COLOR4, hexStrings[3])
            intent.putExtra(EXTRA_COLOR5, hexStrings[4])
            intent.putExtra(EXTRA_IMG_SRC, imageSRC)

            startActivityForResult(intent,1)
        }


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

    //request permission
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
            imageView2.setImageURI(imageSRC)

            //convert image to bitmap to read pixel data
            val bm = getBitmap(imageView2)
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
            //remove alpha
            hexStrings[e] = hexStrings[e].substring(2)
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
