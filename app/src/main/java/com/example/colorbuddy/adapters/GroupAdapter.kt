package com.example.colorbuddy.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.colorbuddy.R
import kotlinx.android.synthetic.main.group_row.view.*
import java.security.acl.Group

class GroupAdapter(groupList: MutableList<com.example.colorbuddy.classes.Group>): RecyclerView.Adapter<GroupViewHolder>(){
    private val mGroups = groupList

    override fun getItemCount(): Int {
        return mGroups.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.group_row,parent,false)
        return GroupViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        val group = mGroups[position]
        holder.view.rowTitle.text = group.groupName
    }
}

class GroupViewHolder(val view: View): RecyclerView.ViewHolder(view){
}


