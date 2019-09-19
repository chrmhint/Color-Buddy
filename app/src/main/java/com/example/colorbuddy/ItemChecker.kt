package com.example.colorbuddy

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_item_checker.*


class ItemChecker : AppCompatActivity() {

    private val IMAGE_CAPTURE_CODE = 1001
    val PERMISSION_CODE = 1000

    var imageSRC: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_checker)

        //added setOnClickListener to btnCapture that called
        //dispatchTakePictureIntent(), which handles all the camera stuff
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
                    //weird warning here
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
    //@SuppressLint("NewApi")
    fun readImage(bm: Bitmap) {

        /*
        val startWidth = (bm.width - (bm.width * .8))
        val endWidth = (bm.width - (bm.width * .2))
        val startHeight = (bm.height - (bm.height * .8))
        val endHeight = (bm.height - (bm.height * .2))
        */

        val pixel = bm.getPixel(0, 0)
        btnCapture.setBackgroundColor(pixel)
/*
        var pixels = IntArray(1000){i -> -1}
        var pixel_nums = IntArray(1000) { i -> 0}
        var color: Int
        var pos: Int
        var currentIndex = 0

        for (w in 1 until bm.width) {
            for (h in 0 until bm.height) {
                color = bm.getPixel(w, h)

                if (pixels.contains(color)) {
                    pos = pixels.binarySearch(color)
                    pixel_nums[pos]++

                } else
                    pixels[currentIndex] = color
                    currentIndex++
            }

            //change button color to most prominent color in image
            pos = pixel_nums.binarySearch(pixel_nums.max())

            if(pixels.isEmpty()){
                btnCapture.setBackgroundColor(0)
            }
            else {
                pixels[0]?.let { btnCapture.setBackgroundColor(it) }
            }

        }
*/


    }

}
//display prominent colors



//check for matches







