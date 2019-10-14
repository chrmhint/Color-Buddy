package com.example.colorbuddy.Groups

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.net.toUri
import com.example.colorbuddy.R
import com.example.colorbuddy.classes.Item
import com.example.colorbuddy.ItemChecker
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_new_item.*

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class NewItemActivity : AppCompatActivity() {

    private lateinit var ref: DatabaseReference
    private lateinit var groupName: String
    private lateinit var itemId: String
    private lateinit var itemType: String
    private lateinit var itemName: String
    private lateinit var itemDescript: String
    private lateinit var c1: String
    private lateinit var c2: String
    private lateinit var c3: String
    private lateinit var c4: String
    private lateinit var c5: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_item)

        val imageSRC: String = intent.getStringExtra("EXTRA_IMAGE_SRC")
        imageView.setImageURI(imageSRC.toUri())

        btnNewItem.setOnClickListener {
            addItem()
        }
    }

    private fun addItem(){
        groupName = intent.getStringExtra("EXTRA_GROUP_NAME")
        itemType = intent.getStringExtra("EXTRA_ITEM_TYPE")
        itemName = newItemName.text.toString()
        itemDescript = newItemDescription.text.toString()
        c1 = intent.getStringExtra("EXTRA_COLOR_1")
        c2 = intent.getStringExtra("EXTRA_COLOR_2")
        c3 = intent.getStringExtra("EXTRA_COLOR_3")
        c4 = intent.getStringExtra("EXTRA_COLOR_4")
        c5 = intent.getStringExtra("EXTRA_COLOR_5")
        ref = FirebaseDatabase.getInstance().getReference("Items")
        itemId = ref.push().key!!
        val item = Item(groupName,itemId,itemType,itemName,itemDescript,c1,c2,c3,c4,c5)
        ref.child(itemId).setValue(item)

        finish()
    }
}
