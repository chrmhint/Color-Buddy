package com.example.colorbuddy.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.colorbuddy.R
import com.example.colorbuddy.classes.Clothes
import kotlinx.android.synthetic.main.item_row_delete.view.*

class DeleteClothesAdapter(val clothesList: MutableList<Clothes>): RecyclerView.Adapter<DeleteClothesViewHolder>() {

    private val mClothes = clothesList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeleteClothesViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.item_row_delete,parent,false)
        return DeleteClothesViewHolder(cellForRow)
    }

    override fun getItemCount(): Int {
        return mClothes.size
    }

    override fun onBindViewHolder(holder: DeleteClothesViewHolder, position: Int) {
        val clothes = mClothes[position]
        holder.view.itemType.text = clothes.clothesType
        holder.view.itemDescription.text = clothes.clothesDescript
    }
}

class DeleteClothesViewHolder(val view: View): RecyclerView.ViewHolder(view){

}