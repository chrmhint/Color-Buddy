package com.example.colorbuddy

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_room.*

class RoomListActivity : AppCompatActivity() {

    private lateinit var roomView: RecyclerView
    private lateinit var roomName: EditText
    private lateinit var roomList: MutableList<Room>
    private lateinit var ref: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room)


        ref = FirebaseDatabase.getInstance().getReference("Rooms")
        roomView = findViewById(R.id.recyclerView_room)
        roomName = findViewById(R.id.addRoomName)
        roomList = mutableListOf()

        roomView.layoutManager = LinearLayoutManager(this)
        roomView.adapter = RoomAdapter(roomList)

        ref.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()){
                    roomList.clear()
                    for(w in p0.children){
                        val room = w.getValue(Room::class.java)
                        roomList.add(room!!)
                    }

                    roomView.layoutManager = LinearLayoutManager(applicationContext)
                    roomView.adapter = RoomAdapter(roomList)
                }
            }

        })

        //call addRoom() when button is clicked
        addRoomButton.setOnClickListener{
            addRoom()
        }

    }

    private fun addRoom(){
        val name = roomName.text.toString()

        //check if name is empty
        if(name.isEmpty()){
            roomName.error = "Please enter the name of a room."
            return
        }

        //get reference from database
        val ref = FirebaseDatabase.getInstance().getReference("Rooms")
        val roomId = ref.push().key
        val room = Room(roomId.toString(),name)

        ref.child(roomId.toString()).setValue(room).addOnCompleteListener {
            Toast.makeText(applicationContext, "Room saved successfully",Toast.LENGTH_LONG).show()
        }
    }
}
