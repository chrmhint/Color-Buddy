package com.example.colorbuddy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.colorbuddy.Groups.GroupsActivity
import com.example.colorbuddy.Inventory.TotalItemsActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.IllegalArgumentException



class MainActivity : AppCompatActivity(), View.OnClickListener {
    val REQUEST_IMAGE_CAPTURE = 1
    private lateinit var mAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mAuth = FirebaseAuth.getInstance()



        itemButton.setOnClickListener(this)
        roomsButton.setOnClickListener(this)
        registerButton.setOnClickListener(this)

    }

    override fun onStart() {
        super.onStart()
        //Check if user is logged in
        var currentUser = mAuth.currentUser
    }



    override fun onClick(view: View) {
        val intent = when (view.id){
            R.id.roomsButton -> Intent(this, GroupsActivity::class.java)
            R.id.itemButton -> Intent(this, TotalItemsActivity::class.java)
            else -> throw IllegalArgumentException("Undefined Button Pressed")
        }
        startActivity(intent)
    }
}
