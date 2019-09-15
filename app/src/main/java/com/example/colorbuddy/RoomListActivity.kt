package com.example.colorbuddy

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_room.*

class RoomListActivity : AppCompatActivity() {

    private lateinit var roomView: ListView
    private lateinit var roomName: EditText
    private lateinit var roomList: MutableList<Room>
    private lateinit var ref: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room)


        ref = FirebaseDatabase.getInstance().getReference("Rooms")
        roomView = findViewById(R.id.roomView)
        roomName = findViewById(R.id.addRoomName)
        roomList = mutableListOf()


        ref.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                //Not implementing currently
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0!!.exists()){
                    //clear list of rooms for refresh
                    roomList.clear()
                    //add rooms to list
                    for(w in p0.children){
                        val wardrobe = w.getValue(Room::class.java)
                        this@RoomListActivity.roomList.add(wardrobe!!)
                    }
                    val adapter = RoomListAdapter(applicationContext, R.layout.group_row, roomList)
                    roomView.adapter = adapter
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

    private class RoomListAdapter(context: Context, val layoutResId: Int, val roomList: List<Room>): ArrayAdapter<Room>(context,layoutResId,roomList){

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val layoutInflater: LayoutInflater = LayoutInflater.from(context)
            val view: View = layoutInflater.inflate(layoutResId,null)
            val textViewName = view.findViewById<TextView>(R.id.rowTextView)
            val room = roomList[position]

            textViewName.text = room.name

            return view
        }


    }


    private class Room (val roomId: String, val name: String){
        constructor(): this("","")
    }
}
