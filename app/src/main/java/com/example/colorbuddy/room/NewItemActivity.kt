package com.example.colorbuddy.room

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.colorbuddy.R
import com.example.colorbuddy.adapters.EXTRA_ROOM_NAME
import com.example.colorbuddy.classes.Item
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_new_item.*

class NewItemActivity : AppCompatActivity() {

    private lateinit var ref: DatabaseReference
    private lateinit var roomRef: DatabaseReference
    private lateinit var roomId: String
    private lateinit var itemId: String
    private lateinit var itemName: String
    private lateinit var itemDesc: String
    private lateinit var itemPri: String
    private lateinit var itemSec: String
    private lateinit var roomName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_item)



        btnAdd.setOnClickListener{

            addItem()
        }



    }

    fun addItem(){
        roomName = intent.getStringExtra("EXTRA_ROOM_NAME")
        itemName = itemType.text.toString()
        itemDesc = itemDescription.text.toString()
        itemPri = itemPrimary.text.toString()
        itemSec = itemSecondary.text.toString()
        ref = FirebaseDatabase.getInstance().getReference("Items")
        itemId = ref.push().key!!
        val item = Item(
            roomName,
            itemId,
            itemName,
            itemDesc,
            itemPri,
            itemSec
        )

        ref.child(itemId).setValue(item)

        val intent = Intent(this, RoomItemsActivity::class.java)
        intent.putExtra(EXTRA_ROOM_NAME,roomName)
        startActivity(intent)

    }
}
