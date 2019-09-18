package com.example.colorbuddy.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.colorbuddy.R
import com.example.colorbuddy.classes.Clothes
import kotlinx.android.synthetic.main.item_row.view.*

class ClothesAdapter(val clothesList: MutableList<Clothes>): RecyclerView.Adapter<ClothesViewHolder>() {

    private val mClothes = clothesList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClothesViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.item_row,parent,false)
        return ClothesViewHolder(cellForRow)
    }

    override fun getItemCount(): Int {
        return mClothes.size
    }

    override fun onBindViewHolder(holder: ClothesViewHolder, position: Int) {
        val clothes = mClothes[position]
        holder.view.itemType.text = clothes.clothesType
        holder.view.itemDescription.text = clothes.clothesDescript

    }
}

class ClothesViewHolder(val view: View): RecyclerView.ViewHolder(view){

}
