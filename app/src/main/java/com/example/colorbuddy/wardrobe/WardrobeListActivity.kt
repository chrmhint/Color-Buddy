package com.example.colorbuddy.wardrobe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.colorbuddy.R
import com.example.colorbuddy.adapters.WardrobeAdapter
import com.example.colorbuddy.classes.Wardrobe
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_wardrobe.*

class WardrobeListActivity : AppCompatActivity() {
    private lateinit var wardrobeView: RecyclerView
    private lateinit var wardrobeName: EditText
    private lateinit var wardrobeList: MutableList<Wardrobe>
    private lateinit var ref: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wardrobe)

        ref = FirebaseDatabase.getInstance().getReference("Wardrobes")
        wardrobeList = mutableListOf()
        wardrobeView = findViewById(R.id.recyclerView_wardrobe)
        wardrobeName = findViewById(R.id.rowName)


        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    wardrobeList.clear()
                    for (w in p0.children) {
                        val wardrobe = w.getValue(Wardrobe::class.java)
                        wardrobeList.add(wardrobe!!)
                    }

                    wardrobeView.layoutManager = LinearLayoutManager(applicationContext)
                    wardrobeView.adapter =
                        WardrobeAdapter(wardrobeList)
                }
            }

        })

        addWardrobeButton.setOnClickListener {
            addWardrobe()
        }

    }


    private fun addWardrobe() {
        val name = wardrobeName.text.toString()

        if (name.isEmpty()) {
            wardrobeName.error = "Please enter a wardrobe name"
            return
        }

        val wardrobeId = ref.push().key

        val wardrobe = Wardrobe(wardrobeId.toString(), name)

        ref.child(wardrobeId.toString()).setValue(wardrobe).addOnCompleteListener {
            Toast.makeText(applicationContext, "Wardrobe saved successfully", Toast.LENGTH_LONG)
                .show()
        }

    }
}


