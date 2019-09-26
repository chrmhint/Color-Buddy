package com.example.colorbuddy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import com.example.colorbuddy.classes.Group
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_add_group.*

class AddGroupActivity : AppCompatActivity() {

    private lateinit var groupName: EditText
    private lateinit var groupTypeSwitch: Switch
    private lateinit var ref: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_group)

        ref = FirebaseDatabase.getInstance().getReference("Groups")
        groupName = findViewById(R.id.addGroupName)
        groupTypeSwitch = findViewById(R.id.groupTypeSwitch)

        btnAddGroup.setOnClickListener {
            addGroup(ref,groupName, groupTypeSwitch)
        }
    }

    private fun addGroup(ref: DatabaseReference,name: EditText,type: Switch){
        var mName = name.text

        if(mName.isEmpty()){
            name.error = "Please enter the name of a Group"
            return
        }

        var gID = ref.push().key
        var group = Group(gID.toString(),mName.toString(),"","","","","","")
        ref.child(gID.toString()).setValue(group).addOnCompleteListener {
            Toast.makeText(applicationContext, "Group saved successfully", Toast.LENGTH_LONG).show()
        }
        finish()
    }
}
