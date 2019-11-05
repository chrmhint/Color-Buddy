package com.example.colorbuddy.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import com.example.colorbuddy.R
import com.example.colorbuddy.classes.Group
import kotlinx.android.synthetic.main.group_check_row.view.*




class GroupCheckAdapter(val groupList: MutableList<Group>):RecyclerView.Adapter<GroupCheckViewHolder>(){

    private val mGroups = groupList
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupCheckViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.group_check_row,parent,false)
        return GroupCheckViewHolder(cellForRow)
    }

    override fun getItemCount(): Int {
        return mGroups.size
    }

    override fun onBindViewHolder(holder: GroupCheckViewHolder, position: Int) {
        val group = mGroups[position]
        holder.view.groupCheckbtn.text = group.groupName
    }
}

class GroupCheckViewHolder(val view: View):RecyclerView.ViewHolder(view) {

}
