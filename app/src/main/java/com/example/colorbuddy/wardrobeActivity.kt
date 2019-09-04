package com.example.colorbuddy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
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
        wardrobeList.adapter = adapter

        button.setOnClickListener{
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
    }
}
