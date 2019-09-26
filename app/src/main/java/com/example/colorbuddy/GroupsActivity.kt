package com.example.colorbuddy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.colorbuddy.adapters.GroupAdapter
import com.example.colorbuddy.classes.Group
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_groups.*
import kotlinx.android.synthetic.main.activity_total_items.*

class GroupsActivity : AppCompatActivity() {

    private lateinit var groupsView: RecyclerView
    private lateinit var groupName: TextView
    private lateinit var groupSwitch: Switch
    private lateinit var groupList: MutableList<Group>
    private lateinit var ref: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_groups)

        ref = FirebaseDatabase.getInstance().getReference("Groups")
        groupsView = findViewById(R.id.recyclerView_groups)
        groupName = findViewById(R.id.groupTitle)
        groupSwitch = findViewById(R.id.groupSwitch)
        groupList = mutableListOf()

        groupSwitch.setOnCheckedChangeListener { _, b ->
            if(listSwitch.isChecked){
                loadWardrobes(groupList)
            }
            else{
                loadRooms()
            }
        }

        ref.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    groupList.clear()
                    for(g in p0.children){
                        val group = g.getValue(Group::class.java)
                        groupList.add(group!!)
                    }
                }
                groupsView.layoutManager = LinearLayoutManager(applicationContext)
                groupsView.adapter = GroupAdapter(groupList)
            }
        })


        btnGroupAdd.setOnClickListener {
            val intent = Intent(this,AddGroupActivity::class.java)
            startActivityForResult(intent,1)
        }
    }

    private fun loadWardrobes(groupList: MutableList<Group>){
        var wardrobeList: MutableList<Group>? = null
        for(g in groupList){

            if(g.groupType=="Wardrobe") {
                wardrobeList!!.add(g)
            }
        }

        groupsView.layoutManager = LinearLayoutManager(applicationContext)
        groupsView.adapter = GroupAdapter(wardrobeList!!)
    }

    private fun loadRooms(){

    }
}
