package com.example.colorbuddy

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlinx.android.synthetic.main.activity_item.*
import kotlinx.android.synthetic.main.activity_item.button
import kotlinx.android.synthetic.main.activity_room.*
import kotlinx.android.synthetic.main.activity_wardrobe.*

class roomActivity : AppCompatActivity() {
    private lateinit var roomsList: ListView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room)

        roomsList = findViewById(R.id.roomsListView)
        var rooms = arrayOf("Bedroom","Living Room","Master Bathroom","Den","Dining Room","Kitchen", "Garage", "Patio")
        val listItems = arrayOfNulls<String>(rooms.size)
        for(i in 0 until rooms.size){
            val room = rooms[i]
            listItems[i] = room
        }
        val adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,listItems)
        roomsList.adapter = RoomListAdapter(this, rooms)

        button.setOnClickListener{
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

    }
    private class RoomListAdapter(context: Context, rooms: Array<String>): BaseAdapter() {

        private val mContext: Context
        private val mRooms: Array<String>

        init{
            this.mContext = context
            this. mRooms = rooms
        }
        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {

            val roomLayoutInflater = LayoutInflater.from(mContext)
            val room_row = roomLayoutInflater.inflate(R.layout.row_wardrobe, p2, false)
            val roomName = room_row.findViewById<TextView>(R.id.wardrobeName)
            roomName.text = mRooms[p0]
            return room_row
        }

        override fun getItem(p0: Int): Any {
            return ""
        }

        override fun getItemId(p0: Int): Long {
            return p0.toLong()
        }

        override fun getCount(): Int {
            return mRooms.size
        }

    }


}
