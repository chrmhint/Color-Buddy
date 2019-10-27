package com.example.colorbuddy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.colorbuddy.Groups.GroupsActivity
import com.example.colorbuddy.Inventory.TotalItemsActivity
import com.example.colorbuddy.classes.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.IllegalArgumentException



class MainActivity : AppCompatActivity() {
    val REQUEST_IMAGE_CAPTURE = 1
    private lateinit var mAuth: FirebaseAuth
    private lateinit var ref: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mAuth = FirebaseAuth.getInstance()
        val mUser = mAuth.currentUser
        ref = FirebaseDatabase.getInstance().getReference("/Users")

        ref.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    for(u in p0.children){
                        var user = u.getValue(User::class.java)
                        if(user!!.userID != mUser!!.uid){
                            val newUser = User(mUser.uid)
                            ref.child(newUser.userID).setValue(newUser)
                        }
                    }
                }
            }

        })





        itemButton.setOnClickListener{
            val intent = Intent(this,TotalItemsActivity::class.java)
            startActivity(intent)
        }
        roomsButton.setOnClickListener{
            val intent = Intent(this,GroupsActivity::class.java)
            startActivity(intent)
        }
        signoutButton.setOnClickListener{
            signout()
        }



    }

    private fun signout(){
        mAuth.signOut()
        val intent = Intent(this,LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
