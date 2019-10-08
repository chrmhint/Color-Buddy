package com.example.colorbuddy.Inventory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.colorbuddy.R
import com.example.colorbuddy.adapters.ItemAdapter
import com.example.colorbuddy.classes.Item
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_total_items.*

class TotalItemsActivity : AppCompatActivity() {

    private lateinit var totalItemsView: RecyclerView
    private lateinit var itemRef: DatabaseReference
    private lateinit var clothesList: MutableList<Item>
    private lateinit var itemList: MutableList<Item>
    private lateinit var items: MutableList<Item>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_total_items)

        totalItemsView = recyclerView_total_items
        itemRef = FirebaseDatabase.getInstance().getReference("Items")
        items = mutableListOf()
        clothesList = mutableListOf()
        itemList = mutableListOf()

        titleTextView.text = "Clothes"

        itemRef.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()){
                    items.clear()
                    for(w in p0.children){
                        val item = w.getValue(Item::class.java)
                        if(item!!.itemType == "Clothes") {
                            clothesList.add(item!!)
                        }
                        items.add(item)
                    }
                    totalItemsView.layoutManager = LinearLayoutManager(applicationContext)
                    totalItemsView.adapter = ItemAdapter(clothesList)
                }
            }
        })

        listSwitch.setOnCheckedChangeListener { _, b ->
            if(listSwitch.isChecked){
                loadItems()
            }
            else{
                loadClothes()
            }
        }

    }

    private fun loadClothes() {
        titleTextView.text = getString(R.string.Clothes)
        clothesList.clear()
        for(g in items){
            if(g.itemType=="Clothing") {
                clothesList.add(g)
            }
        }
        totalItemsView.layoutManager = LinearLayoutManager(applicationContext)
        totalItemsView.adapter = ItemAdapter(clothesList)
    }


    private fun loadItems(){
        titleTextView.text = getString(R.string.Items)
        itemList.clear()
        for(g in items){
            if(g.itemType=="Item") {
                itemList.add(g)
            }
        }
        totalItemsView.layoutManager = LinearLayoutManager(applicationContext)
        totalItemsView.adapter = ItemAdapter(itemList)
    }
}
