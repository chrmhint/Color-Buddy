package com.example.colorbuddy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.item_row.view.*

class ItemAdapter(val itemList: MutableList<Item>): RecyclerView.Adapter<ItemViewHolder>() {

    private val mItems = itemList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.item_row,parent,false)
        return ItemViewHolder(cellForRow)
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = mItems[position]
        holder.view.itemType.text = item.itemName
        holder.view.itemDescription.text = item.itemDescript

    }
}

class ItemViewHolder(val view: View):RecyclerView.ViewHolder(view){

}