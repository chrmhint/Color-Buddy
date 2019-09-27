package com.example.colorbuddy.Groups

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
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
    private lateinit var c1: MutableList<String>
    private lateinit var c2: MutableList<String>
    private lateinit var c3: MutableList<String>
    private lateinit var c4: MutableList<String>
    private lateinit var c5: MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_items)

        paletteLayout = findViewById(R.id.paletteLayout)
        itemView = findViewById(R.id.recyclerView_items)
        ref = FirebaseDatabase.getInstance().getReference("Items")
        items = mutableListOf()
        c1 = mutableListOf()
        c2 = mutableListOf()
        c3 = mutableListOf()
        c4 = mutableListOf()
        c5 = mutableListOf()
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
                    for(c in items){
                        c1.add(c.c1)
                        c2.add(c.c2)
                        c3.add(c.c3)
                        c4.add(c.c4)
                        c5.add(c.c5)
                    }

                    val param = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        1.0f
                    )
                    if(c1.isNotEmpty())
                        groupC1.setBackgroundColor(Color.parseColor(c1[0]))
                    groupC1.layoutParams = param
                    if(c2.isNotEmpty())
                        groupC2.setBackgroundColor(Color.parseColor(c2[0]))
                    groupC2.layoutParams = param
                    if(c3.isNotEmpty())
                        groupC3.setBackgroundColor(Color.parseColor(c3[0]))
                    groupC3.layoutParams = param
                    if(c4.isNotEmpty())
                        groupC4.setBackgroundColor(Color.parseColor(c4[0]))
                    groupC4.layoutParams = param
                    if(c5.isNotEmpty())
                        groupC5.setBackgroundColor(Color.parseColor(c5[0]))
                        groupC5.layoutParams = param

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

        fun groupColors(){

            for(c in items){
                c1.add(c.c1)
                c2.add(c.c2)
                c3.add(c.c3)
                c4.add(c.c4)
                c5.add(c.c5)
            }

            val param = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT,
                1.0f
            )
            if(c1.isNotEmpty())
                groupC1.setBackgroundColor(Color.parseColor(c1[0]))
                groupC1.layoutParams = param
            if(c2.isNotEmpty())
                groupC2.setBackgroundColor(Color.parseColor(c2[0]))
                groupC2.layoutParams = param
            if(c3.isNotEmpty())
                groupC3.setBackgroundColor(Color.parseColor(c3[0]))
                groupC3.layoutParams = param
            if(c4.isNotEmpty())
                groupC4.setBackgroundColor(Color.parseColor(c4[0]))
                groupC4.layoutParams = param
            if(c5.isNotEmpty())
                groupC5.setBackgroundColor(Color.parseColor(c5[0]))
                groupC5.layoutParams = param

        }

        groupColors()
    }
}
