package com.example.colorbuddy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.colorbuddy.room.ItemActivity
import com.example.colorbuddy.room.RoomListActivity
import com.example.colorbuddy.wardrobe.WardrobeListActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.IllegalArgumentException
import com.google.firebase.auth.FirebaseAuth
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.google.firebase.auth.FirebaseUser
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T





class MainActivity : AppCompatActivity(), View.OnClickListener {
    val REQUEST_IMAGE_CAPTURE = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        itemButton.setOnClickListener(this)
        wardrobeButton.setOnClickListener(this)
        roomsButton.setOnClickListener(this)

    }

    override fun onClick(view: View) {
        val intent = when (view.id){
            R.id.wardrobeButton -> Intent(this,
                WardrobeListActivity::class.java)
            R.id.roomsButton -> Intent(this, RoomListActivity::class.java)
            R.id.itemButton -> Intent(this, ItemActivity::class.java)
            else -> throw IllegalArgumentException("Undefined Button Pressed")
        }
        startActivity(intent)
    }
}
