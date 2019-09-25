package com.example.colorbuddy.Groups

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.core.view.children
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.colorbuddy.R
import com.example.colorbuddy.adapters.DeleteItemAdapter
import com.example.colorbuddy.classes.Item
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_delete_items.*
import kotlinx.android.synthetic.main.item_row_delete.view.*

class DeleteItemsActivity : AppCompatActivity() {

    private lateinit var paletteView: LinearLayout
    private lateinit var itemsView: RecyclerView
    private lateinit var items: MutableList<Item>
    private lateinit var ref: DatabaseReference
    private lateinit var groupName: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete_items)

        ref = FirebaseDatabase.getInstance().getReference("Items")
        items = mutableListOf()
        itemsView = findViewById(R.id.recyclerView_delete_items)
        groupName = intent.getStringExtra("EXTRA_GROUP_NAME")

        this.title = groupName

        ref.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    items.clear()
                    for(i in p0.children){
                        val item = i.getValue(Item::class.java)
                        if(item?.groupName == groupName){
                            items.add(item)
                        }
                    }
                    itemsView.layoutManager = LinearLayoutManager(applicationContext)
                    itemsView.adapter = DeleteItemAdapter(items)
                }
            }
        })

        btnDeleteItem.setOnClickListener {
            deleteItems()
        }
    }

    private fun deleteItems(){
        val itemsToDelete = mutableListOf<Item>()
        var i = 0
        for (item in itemsView.children){
            if(item.checkBox.isChecked){
                itemsToDelete.add(items[i])
            }
            i++
        }
        for(item in itemsToDelete){
            val dref = FirebaseDatabase.getInstance().getReference("Items").child(item.itemId)
            dref.removeValue()
        }

        finish()
    }
}
