package com.example.colorbuddy

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_room_items.*

class RoomItemsActivity : AppCompatActivity() {

    private lateinit var paletteView: LinearLayout
    private lateinit var itemsView: RecyclerView
    private lateinit var itemList: MutableList<Item>
    private lateinit var ref: DatabaseReference
    private lateinit var refItems: DatabaseReference



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_items)

        ref = FirebaseDatabase.getInstance().getReference("Rooms")
        refItems = ref.child("Items")
        itemList = mutableListOf()
        itemsView = findViewById(R.id.recyclerView_items)


        refItems.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()){
                    itemList.clear()
                    for(w in p0.children){
                        val item = w.getValue(Item::class.java)
                        itemList.add(item!!)
                    }

                    itemsView.layoutManager = LinearLayoutManager(applicationContext)
                    itemsView.adapter = ItemAdapter(itemList)
                }
            }
        })





        //btnAddItem.setOnClickListener()
        //add item to item list for room

        //btnDeleteItem.setOnClickListener()
        //remove item from item list for room
    }
}
