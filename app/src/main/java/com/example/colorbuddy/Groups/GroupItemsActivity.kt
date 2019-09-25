package com.example.colorbuddy.Groups

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.colorbuddy.R
import com.example.colorbuddy.adapters.EXTRA_ITEM_TYPE
import com.example.colorbuddy.adapters.ItemAdapter
import com.example.colorbuddy.classes.Item
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_group_items.*

const val EXTRA_GROUP_NAME:String = "EXTRA_GROUP_NAME"

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class GroupItemsActivity : AppCompatActivity() {

    private lateinit var paletteLayout: LinearLayout
    private lateinit var itemView: RecyclerView
    private lateinit var items: MutableList<Item>
    private lateinit var itemType: String
    private lateinit var ref: DatabaseReference
    private lateinit var groupName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_items)

        itemView = findViewById(R.id.recyclerView_items)
        ref = FirebaseDatabase.getInstance().getReference("Items")
        items = mutableListOf()
        itemType = intent.getStringExtra("EXTRA_ITEM_TYPE")
        groupName = intent.getStringExtra("EXTRA_GROUP_NAME")

        this.title = groupName

        ref.addValueEventListener(object : ValueEventListener{
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
                    itemView.layoutManager = LinearLayoutManager(applicationContext)
                    itemView.adapter = ItemAdapter(items)
                }
            }
        })

        btnItemAdd.setOnClickListener {
            val intent = Intent(this,NewItemActivity::class.java)
            intent.putExtra(EXTRA_GROUP_NAME,groupName)
            intent.putExtra(EXTRA_ITEM_TYPE,itemType)
            startActivityForResult(intent,1)
        }

        btnItemDelete.setOnClickListener {
            val intent = Intent(this,DeleteItemsActivity::class.java)
            intent.putExtra(EXTRA_GROUP_NAME,groupName)
            startActivityForResult(intent,1)
        }

    }
}
