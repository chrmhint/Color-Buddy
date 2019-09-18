package com.example.colorbuddy.wardrobe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.colorbuddy.R
import com.example.colorbuddy.adapters.ClothesAdapter
import com.example.colorbuddy.adapters.EXTRA_WARDROBE_NAME
import com.example.colorbuddy.adapters.ItemAdapter
import com.example.colorbuddy.classes.Clothes
import com.example.colorbuddy.room.NewItemActivity
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_wardrobe_items.*

class WardrobeItemsActivity : AppCompatActivity() {

    private lateinit var paletteView: LinearLayout
    private lateinit var clothesView: RecyclerView
    private lateinit var clothes: MutableList<Clothes>
    private lateinit var ref: DatabaseReference
    private lateinit var refClothes: DatabaseReference
    private lateinit var wardrobeName: String
    private lateinit var roomId: String



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wardrobe_items)

        ref = FirebaseDatabase.getInstance().getReference("Clothes")
        clothes = mutableListOf()
        clothesView = findViewById(R.id.recylcerView_clothes)
        wardrobeName = intent.getStringExtra("EXTRA_WARDROBE_NAME")


        this.title = wardrobeName



        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()){
                    clothes.clear()
                    for(w in p0.children){
                        val item = w.getValue(Clothes::class.java)
                        if( item?.wardrobeId == wardrobeName) {
                            clothes.add(item!!)
                        }
                    }

                    clothesView.layoutManager = LinearLayoutManager(applicationContext)
                    clothesView.adapter = ClothesAdapter(clothes)
                }
            }
        })







        btnAddClothes.setOnClickListener{
            val intent = Intent(this, NewClothesActivity::class.java)
            intent.putExtra(EXTRA_WARDROBE_NAME, wardrobeName)
            startActivity(intent)
        }


        //btnDeleteItem.setOnClickListener()
        //remove item from item list for room
    }
}

