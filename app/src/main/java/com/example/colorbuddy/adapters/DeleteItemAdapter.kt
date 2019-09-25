package com.example.colorbuddy.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.colorbuddy.R
import com.example.colorbuddy.classes.Item
import kotlinx.android.synthetic.main.item_row_delete.view.*

class DeleteItemAdapter(val itemList: MutableList<Item>): RecyclerView.Adapter<DeleteItemsViewHolder>(){
    private val mItems = itemList

    override fun getItemCount(): Int {
        return mItems.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeleteItemsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.item_row_delete,parent,false)
        return DeleteItemsViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: DeleteItemsViewHolder, position: Int) {
        val item = mItems[position]
        holder.view.itemType.text = item.itemName
        holder.view.itemDescription.text = item.itemDescript
    }
}

class DeleteItemsViewHolder(val view: View): RecyclerView.ViewHolder(view){

}