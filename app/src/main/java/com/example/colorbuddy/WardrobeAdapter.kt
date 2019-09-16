package com.example.colorbuddy

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.group_row.view.*

class WardrobeAdapter(wardrobeList: MutableList<Wardrobe>): RecyclerView.Adapter<WardrobeViewHolder>(){

    private val mWardrobes = wardrobeList

    //number of Items
    override fun getItemCount(): Int {
        return mWardrobes.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WardrobeViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.group_row,parent,false)
        return WardrobeViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: WardrobeViewHolder, position: Int) {
        val room = mWardrobes[position]
        holder.view.rowTitle.text = room.name
    }



}

class WardrobeViewHolder(val view: View): RecyclerView.ViewHolder(view){

    init {
        view.btnRow.setOnClickListener{
            val intent = Intent(view.context, RoomItemsActivity::class.java)
            view.context.startActivity(intent)
        }
    }
}