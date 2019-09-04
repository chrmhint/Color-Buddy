package com.example.colorbuddy

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.core.view.get
import androidx.core.view.size
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_item.*
import kotlinx.android.synthetic.main.activity_item.button
import kotlinx.android.synthetic.main.activity_wardrobe.*

class wardrobeActivity : AppCompatActivity() {
    private lateinit var wardrobeList: ListView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wardrobe)

        wardrobeList = findViewById(R.id.wardrobeListView)
        val wardrobes: Array<String> = arrayOf("Fancy","School","Casual","Gym")
        val listItems = arrayOfNulls<String>(wardrobes.size)
        for(i in 0 until wardrobes.size){
            val wardrobe = wardrobes[i]
            listItems[i] = wardrobe
        }
        val adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,listItems)
        wardrobeList.adapter = WardrobeListAdapter(this, wardrobes)

        button.setOnClickListener{
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
    }

    private class WardrobeListAdapter(context: Context,wardrobes: Array<String>): BaseAdapter() {

        private val mContext: Context
        private val mWardrobes: Array<String>

        init{
            this.mContext = context
            this. mWardrobes = wardrobes
        }
        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {

            val wardrobeLayoutInflater = LayoutInflater.from(mContext)
            val wardrobe_row = wardrobeLayoutInflater.inflate(R.layout.row_wardrobe, p2, false)
            val wardrobeName = wardrobe_row.findViewById<TextView>(R.id.wardrobeName)
            wardrobeName.text = mWardrobes[p0]
            return wardrobe_row
        }

        override fun getItem(p0: Int): Any {
            return ""
        }

        override fun getItemId(p0: Int): Long {
            return p0.toLong()
        }

        override fun getCount(): Int {
            return mWardrobes.size
        }

    }
}
