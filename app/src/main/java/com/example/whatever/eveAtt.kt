package com.example.whatever

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class eveAtt : AppCompatActivity() {
    private lateinit var nxt: Button
    private lateinit var attLst: RecyclerView
    private lateinit var studList: ArrayList<Student>
    private lateinit var adapt: eveAttAdapt
    private lateinit var dbRef: DatabaseReference
    private lateinit var dbRef2: DatabaseReference
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var presentCount: TextView
    var pref_key = "prefs"
    var route_key = "route"
    var route = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eve_att)
        sharedPreferences = getSharedPreferences(pref_key, Context.MODE_PRIVATE)
        route = sharedPreferences.getString(route_key, null)!!
        nxt = findViewById(R.id.nextpage)
        presentCount = findViewById(R.id.PresentCount)
        dbRef = FirebaseDatabase.getInstance().getReference("Students").child(route)
        dbRef2 = FirebaseDatabase.getInstance().getReference("Attendance").child(route)
        nxt.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        attLst = findViewById(R.id.attendanceList)
        studList = ArrayList()
        adapt = eveAttAdapt(this, studList)
        attLst.layoutManager =LinearLayoutManager(this)
        attLst.adapter = adapt

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                studList.clear()
                for (name in snapshot.children) {
                    val currentStudent = name.getValue(Student::class.java)
                    studList.add(currentStudent!!)
                }
                adapt.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
        dbRef2.child("Evening Count").addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                presentCount.text = snapshot.value.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}