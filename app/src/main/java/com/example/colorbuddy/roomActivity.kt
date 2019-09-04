package com.example.colorbuddy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_item.*
import kotlinx.android.synthetic.main.activity_item.button
import kotlinx.android.synthetic.main.activity_room.*
import kotlinx.android.synthetic.main.activity_wardrobe.*

class roomActivity : AppCompatActivity() {
    var listOfRoom = arrayOf("Bedroom","Living Room","Master Bathroom","Den","Dining Room","Kitchen")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room)
        button.setOnClickListener{
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }


    }


}
