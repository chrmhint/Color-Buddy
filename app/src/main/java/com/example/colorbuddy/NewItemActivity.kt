package com.example.colorbuddy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Toast
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
        val item = Item(roomName,itemId, itemName, itemDesc,itemPri, itemSec)

        ref.child(itemId).setValue(item)

    }
}
