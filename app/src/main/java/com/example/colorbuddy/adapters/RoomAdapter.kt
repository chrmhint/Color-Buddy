package com.example.colorbuddy.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.colorbuddy.R
import com.example.colorbuddy.classes.Room
import com.example.colorbuddy.room.RoomItemsActivity
import kotlinx.android.synthetic.main.group_row.view.*

const val EXTRA_ROOM_NAME = "EXTRA_ROOM_NAME"
const val EXTRA_ROOM_ID = "EXTRA_ROOM_ID"

class RoomAdapter(roomList: MutableList<Room>): RecyclerView.Adapter<RoomViewHolder>(){

    private val mRooms = roomList



    //number of Items
    override fun getItemCount(): Int {
        return mRooms.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.group_row,parent,false)
        return RoomViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        val room = mRooms[position]
        holder.view.rowTitle.text = room.name
    }

}

class RoomViewHolder(val view: View): RecyclerView.ViewHolder(view){

    init {
        view.btnRow.setOnClickListener{
            val intent = Intent(view.context, RoomItemsActivity::class.java)
            intent.putExtra(EXTRA_ROOM_NAME, view.rowTitle.text)
            view.context.startActivity(intent)
        }
    }

}