package com.example.colorbuddy

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_room.*

class roomActivity : AppCompatActivity() {

    private lateinit var roomView: ListView
    private lateinit var roomName: EditText
    private lateinit var roomList: MutableList<Room>
    private lateinit var ref: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room)


        ref = FirebaseDatabase.getInstance().getReference("Rooms")
        roomView = findViewById(R.id.roomsListView)
        roomName = findViewById(R.id.addRoomName)
        roomList = mutableListOf()


        ref.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0!!.exists()){
                    for(w in p0.children){
                        val wardrobe = w.getValue(Room::class.java)
                        roomList.add(wardrobe!!)
                    }

                    val adapter = RoomListAdapter(applicationContext, R.layout.row_wardrobe, roomList)
                    roomView.adapter = adapter
                }
            }

        })

        backButton.setOnClickListener{
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

        addRoomButton.setOnClickListener{
            addRoom()
        }

    }

    private fun addRoom(){
        val name = roomName.text.toString()

        if(name.isEmpty()){
            roomName.error = "Please enter the name of a room."
            return
        }

        val ref = FirebaseDatabase.getInstance().getReference("Rooms")
        val roomId = ref.push().key

        val room = Room(roomId.toString(),name)

        ref.child(roomId.toString()).setValue(room).addOnCompleteListener {
            Toast.makeText(applicationContext, "Room saved successfully",Toast.LENGTH_LONG)
        }
    }

    private class RoomListAdapter(context: Context, val layoutResId: Int, val roomList: List<Room>) : ArrayAdapter<Room>(context, layoutResId, roomList) {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val layoutInflater: LayoutInflater = LayoutInflater.from(context)
            val view: View = layoutInflater.inflate(layoutResId,null)

            val textViewName = view.findViewById<TextView>(R.id.rowName)

            val room = roomList[position]

            textViewName.text = room.name

            return view
        }
    }

    private class Room (val roomId: String, val name: String){
        constructor(): this("","")
    }
}
