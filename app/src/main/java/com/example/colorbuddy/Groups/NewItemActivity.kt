package com.example.colorbuddy.Groups

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.DialogInterface
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
import androidx.appcompat.app.AlertDialog
import androidx.core.graphics.red
import androidx.core.net.toUri
import com.example.colorbuddy.R
import com.example.colorbuddy.classes.Item
import com.example.colorbuddy.ItemChecker
import com.example.colorbuddy.classes.Group
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_item_checker.*
import kotlinx.android.synthetic.main.activity_new_item.*
import java.lang.Float.parseFloat
import java.lang.Long.parseLong

import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.abs
import kotlin.math.roundToInt

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class NewItemActivity : AppCompatActivity() {

    private lateinit var ref: DatabaseReference
    private lateinit var groupName: String
    private lateinit var groupID: String
    private lateinit var groupItems: MutableList<Group>
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

        btnRetry.setOnClickListener {
            openCamera()
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
        val startWidth = (bm.width - (bm.width * .8)).roundToInt()
        val endWidth = (bm.width - (bm.width * .2)).roundToInt()
        val startHeight = (bm.height - (bm.height * .8)).roundToInt()
        val endHeight = (bm.height - (bm.height * .2)).roundToInt()

        //collect pixel information and build map of color -> color appearances
        var pixels = mutableMapOf(0 to 0)
        var color = 0

        //within subsection of picture, take every 100th pixel
        for (w in startWidth until endWidth step 50) {
            for (h in startHeight until endHeight step 50) {
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
                var oldKey = 0

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

        //check for matches

        //if(findMatches(pixel_list)){
            //show matches were found
            var test:String = findMatches(pixel_list)
            val builder = AlertDialog.Builder(this)
            builder.setTitle(test)
            builder.setMessage("Congratulations, this item matches your colllection!")
            builder.setNeutralButton("OK", DialogInterface.OnClickListener{ dialog, id ->
                dialog.cancel()
            })
            builder.show()
        //}
    /*
        else{
            val builder = AlertDialog.Builder(this)
            builder.setTitle("No Matches Found!")
            builder.setMessage("Sorry, no matches found.")
            builder.setNeutralButton("OK", DialogInterface.OnClickListener{ dialog, id ->
                dialog.cancel()
            })
            builder.show()
        }
*/

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

    //round each pixel
    private fun posterizePixel(p: Int): Int {
        if( p < 20)
            return 0
        if (p < 40)
            return 25
        if(p < 60)
            return 40
        if (p < 80)
            return 60
        if(p < 100)
            return 80
        if (p < 120)
            return 100
        if(p < 140)
            return 120
        if (p < 160)
            return 140
        if(p < 180)
            return 160
        if(p < 200)
            return 180
        if(p < 220)
            return 200
        if(p < 240)
            return 220

        return 255

    }

    //returns true if new color is within 45 degrees of compared color
    //30 degrees because your eyes lie to you
    //+15 degrees to account for picture and lighting quality
    private fun isMonochromatic(newHue: Float, comparedHex: String): Boolean{

        //convert hex string to ARGB
        val comparedARGB = Color.toArgb(parseLong(comparedHex.substring(1)))

        //convert to HSV -> hue is HSV[0]
        val HSV = FloatArray(3)
        Color.colorToHSV(comparedARGB, HSV)

        if(abs(newHue-HSV[0]) <= 45)
            return true

        return false
    }


    //returns true if the color 180 degrees from newHue is within same hue slice as compared hue
    private fun isComplimentary(newHue: Float, comparedHex: String): Boolean{

        //convert hex string to ARGB
        val comparedARGB = Color.toArgb(parseLong(comparedHex.substring(1)))

        //convert to HSV -> hue is HSV[0]
        val HSV = FloatArray(3)
        Color.colorToHSV(comparedARGB, HSV)


        if(newHue <= 180)
            return isMonochromatic((newHue + 180), comparedHex)

        return isMonochromatic(newHue - 180, comparedHex)

    }


    //returns true if 2 hues are in the same color range
    private fun isSameHue(hue1: Float, hue2: Float): Boolean{

        //red
        if(hue1 <= 60f && hue2 <= 60f)
            return true

        //yellow
        if(hue1 <= 120f && hue2 <= 120f)
            return true

        //green
        if(hue1 <= 180f && hue2 <= 180f)
            return true

        //cyan
        if(hue1 <= 240f && hue2 <= 240f)
            return true

        //blue
        if(hue1 <= 300f && hue2 <= 300f)
            return true

        //magenta
        if(hue1 > 300f && hue2 > 300f)
            return true

        return false
    }


    //supposed to return Boolean -> returns String now to see what's in groupHex
    private fun findMatches(pixelList: ArrayList<Int>) : String{

        var groupHex = mutableListOf<String>()
        var HSV = FloatArray(3)
        groupItems = mutableListOf()
        var ref = FirebaseDatabase.getInstance().getReference("Groups")
        groupName = intent.getStringExtra("EXTRA_GROUP_NAME")
        //for each color in the new item, compare to a color in group palette


        //get color from group palette
        ref.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    for(i in p0.children){
                        val group = i.getValue(Group::class.java)
                        if(group?.groupName == groupName){
                            //groupID = i.ref.key!!
                            groupHex.add(group.c1)
                            groupHex.add(group.c2)
                            groupHex.add(group.c3)
                            groupHex.add(group.c4)
                            groupHex.add(group.c5)
                        }

                    }
                }
            }
        })

        /*
            //for each new color, check against each prom. color in group
            for(i in 0..4) {
                Color.colorToHSV(pixelList[i], HSV)

                for(k in 1..5) {
                    //check if any monochromatic/complimentary matches are likely
                    if (isMonochromatic(HSV[i], groupHex[k]) || isComplimentary((HSV[i]), groupHex[k]))
                        return true
                }

            }
*/
        return groupHex[1]

    }
}
