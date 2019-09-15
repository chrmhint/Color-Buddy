package com.example.colorbuddy

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_wardrobe.*
import kotlinx.android.synthetic.main.group_row.*

class WardrobeListActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var wardrobeView: ListView
    private lateinit var wardrobeName: EditText
    private lateinit var wardrobeList: MutableList<Wardrobe>
    private lateinit var ref: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wardrobe)

        ref = FirebaseDatabase.getInstance().getReference("Wardrobes")
        wardrobeList = mutableListOf()
        wardrobeView = findViewById(R.id.wardrobeListView)
        wardrobeName = findViewById(R.id.rowName)


        ref.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0!!.exists()){
                    wardrobeList.clear()
                    for(w in p0.children){
                        val wardrobe = w.getValue(Wardrobe::class.java)
                        wardrobeList.add(wardrobe!!)
                    }

                    val adapter = WardrobeListAdapter(applicationContext, R.layout.group_row, wardrobeList)
                    wardrobeView.adapter = adapter
                }
            }

        })

        addWardrobeButton.setOnClickListener{
            addWardrobe()
        }

    }

    override fun onClick(view: View?) {

    }

    private fun addWardrobe(){
        val name = wardrobeName.text.toString()

        if(name.isEmpty()){
            wardrobeName.error = "Please enter a wardrobe name"
            return
        }

        val wardrobeId = ref.push().key

        val wardrobe = Wardrobe(wardrobeId.toString(),name)

        ref.child(wardrobeId.toString()).setValue(wardrobe).addOnCompleteListener {
            Toast.makeText(applicationContext, "Wardrobe saved successfully",Toast.LENGTH_LONG).show()
        }

    }

    private class WardrobeListAdapter(context: Context, val layoutResId: Int, val wardrobeList: List<Wardrobe>): ArrayAdapter<Wardrobe>(context,layoutResId,wardrobeList) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val layoutInflater: LayoutInflater = LayoutInflater.from(context)
            val view: View = layoutInflater.inflate(layoutResId,null)
            val textViewName = view.findViewById<TextView>(R.id.rowTextView)
            val wardrobe = wardrobeList[position]

            textViewName.text = wardrobe.name

            return view
        }


    }
    private class Wardrobe(val wardrobeId: String, val name: String){
        constructor(): this("","")
    }
}