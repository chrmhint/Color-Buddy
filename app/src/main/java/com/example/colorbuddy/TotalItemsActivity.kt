package com.example.colorbuddy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.colorbuddy.adapters.ClothesAdapter
import com.example.colorbuddy.adapters.ItemAdapter
import com.example.colorbuddy.classes.Clothes
import com.example.colorbuddy.classes.Item
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_total_items.*

class TotalItemsActivity : AppCompatActivity() {

    private lateinit var totalItemsView: RecyclerView
    private lateinit var clothes: MutableList<Clothes>
    private lateinit var itemRef: DatabaseReference
    private lateinit var clothesRef: DatabaseReference
    private lateinit var wardrobeName: String
    private lateinit var roomId: String
    private lateinit var  wardrobeId: String
    private lateinit var items: MutableList<Item>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_total_items)

        totalItemsView = recyclerView_total_items
        itemRef = FirebaseDatabase.getInstance().getReference("Items")
        clothesRef = FirebaseDatabase.getInstance().getReference("Clothes")

        items = mutableListOf()
        clothes = mutableListOf()
        titleTextView.text = R.string.items_text.toString()


        listSwitch.setOnCheckedChangeListener { _, b ->
            if(listSwitch.isChecked){
                loadClothes()
            }
            else{
                loadItems()
            }
        }

        clothesRef.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()){
                    clothes.clear()
                    for(w in p0.children){
                        val item = w.getValue(Clothes::class.java)
                        clothes.add(item!!)

                    }

                    totalItemsView.layoutManager = LinearLayoutManager(applicationContext)
                    totalItemsView.adapter = ClothesAdapter(clothes)
                }
            }
        })

        itemRef.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()){
                    items.clear()
                    for(w in p0.children){
                        val item = w.getValue(Item::class.java)
                        items.add(item!!)

                    }

                    totalItemsView.layoutManager = LinearLayoutManager(applicationContext)
                    totalItemsView.adapter = ItemAdapter(items)
                }
            }
        })


    }

    private fun loadClothes() {
        titleTextView.text = R.string.Clothes_text.toString()
        totalItemsView.layoutManager = LinearLayoutManager(applicationContext)
        totalItemsView.adapter = ClothesAdapter(clothes)
    }

    private fun loadItems(){
        titleTextView.text = R.string.items_text.toString()
        totalItemsView.layoutManager = LinearLayoutManager(applicationContext)
        totalItemsView.adapter = ItemAdapter(items)
    }
}
