package com.example.colorbuddy.wardrobe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.colorbuddy.R
import com.example.colorbuddy.classes.Clothes
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_new_clothes.*

class NewClothesActivity : AppCompatActivity() {

    private lateinit var ref: DatabaseReference
    private lateinit var roomId: String
    private lateinit var clothesId: String
    private lateinit var clothesType: String
    private lateinit var clothesDesc: String
    private lateinit var clothesPri: String
    private lateinit var clothesSec: String
    private lateinit var wardrobeName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_clothes)
        btnAdd.setOnClickListener{

            addItem()
        }



    }

    fun addItem(){
        wardrobeName = intent.getStringExtra("EXTRA_WARDROBE_NAME")
        clothesType = clothesName.text.toString()
        clothesDesc = clothesDescription.text.toString()
        clothesPri = clothesPrimary.text.toString()
        clothesSec = clothesSecondary.text.toString()
        ref = FirebaseDatabase.getInstance().getReference("Clothes")
        clothesId = ref.push().key!!
        val clothes = Clothes(
            clothesId,clothesType, clothesDesc,clothesPri,clothesSec,wardrobeName
        )

        ref.child(clothesId).setValue(clothes)

        finish()

    }
}