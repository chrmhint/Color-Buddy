package com.example.colorbuddy.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.colorbuddy.Groups.GroupItemsActivity
import com.example.colorbuddy.R
import kotlinx.android.synthetic.main.group_row.view.*

const val EXTRA_GROUP_NAME = "EXTRA_GROUP_NAME"
const val EXTRA_ITEM_TYPE = "EXTRA_ITEM_TYPE"

class GroupAdapter(groupList: MutableList<com.example.colorbuddy.classes.Group>,itemType:String): RecyclerView.Adapter<GroupViewHolder>(){
    private val mGroups = groupList
    private val mItemType = itemType

    override fun getItemCount(): Int {
        return mGroups.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.group_row,parent,false)
        return GroupViewHolder(cellForRow,mItemType)
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        val group = mGroups[position]
        val param = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT,
            1.0f
        )
        holder.view.rowTitle.text = group.groupName
    }
}

class GroupViewHolder(val view: View, val type:String): RecyclerView.ViewHolder(view){
    init {
        view.btnRow.setOnClickListener {
            val intent = Intent(view.context, GroupItemsActivity::class.java)
            intent.putExtra(EXTRA_GROUP_NAME, view.rowTitle.text)
            intent.putExtra(EXTRA_ITEM_TYPE,type)
            view.context.startActivity(intent)
        }
    }
}




