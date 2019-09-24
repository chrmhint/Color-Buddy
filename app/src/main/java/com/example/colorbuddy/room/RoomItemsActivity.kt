package com.example.colorbuddy.room

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.colorbuddy.R
import com.example.colorbuddy.adapters.EXTRA_ROOM_NAME
import com.example.colorbuddy.adapters.ItemAdapter
import com.example.colorbuddy.classes.Item
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_room_items.*



class RoomItemsActivity : AppCompatActivity() {

    private lateinit var paletteView: LinearLayout
    private lateinit var itemsView: RecyclerView
    private lateinit var items: MutableList<Item>
    private lateinit var ref: DatabaseReference
    private lateinit var refItems: DatabaseReference
    private lateinit var roomName: String
    private lateinit var roomId: String



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_items)

        ref = FirebaseDatabase.getInstance().getReference("Items")
        items = mutableListOf()
        itemsView = findViewById(R.id.recylcerView_items)
        roomName = intent.getStringExtra("EXTRA_ROOM_NAME")


        this.title = roomName



        ref.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()){
                    items.clear()
                    for(w in p0.children){
                        val item = w.getValue(Item::class.java)
                        if( item?.roomId == roomName) {
                            items.add(item!!)
                        }
                    }

                    itemsView.layoutManager = LinearLayoutManager(applicationContext)
                    itemsView.adapter = ItemAdapter(items)
                }
            }
        })







        btnAddItem.setOnClickListener{
            val intent = Intent(this, NewItemActivity::class.java)
            intent.putExtra(EXTRA_ROOM_NAME, roomName)
            startActivityForResult(intent,1)
        }


        btnDeleteItem.setOnClickListener{

        }
    }


}
