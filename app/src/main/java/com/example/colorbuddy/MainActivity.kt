package com.example.colorbuddy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        itemButton.setOnClickListener{
            val intent = Intent(this,itemActivity::class.java)
            startActivity(intent)
        }
        wardrobeButton.setOnClickListener{
            val intent = Intent(this,wardrobeActivity::class.java)
            startActivity(intent)
        }
        roomsButton.setOnClickListener{
            val intent = Intent(this,roomActivity::class.java)
            startActivity(intent)
        }

    }

}
