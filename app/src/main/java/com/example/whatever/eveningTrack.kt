package com.example.whatever

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class eveningTrack : AppCompatActivity() {
    private lateinit var students: RecyclerView
    private lateinit var studentList: ArrayList<Student>
    private lateinit var evenAdapter: eveningAdapter
    private lateinit var dbRef: DatabaseReference
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var nextStop: TextView
    private lateinit var dbRef2: DatabaseReference
    var pref_key = "prefs"
    var route_key = "route"
    var route = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_evening_track)

        sharedPreferences = getSharedPreferences(pref_key, Context.MODE_PRIVATE)
        route = sharedPreferences.getString(route_key, null)!!
        students = findViewById(R.id.studs)
        dbRef2 = FirebaseDatabase.getInstance().getReference("Buses").child(route)
        nextStop = findViewById(R.id.nextStop)
        students.layoutManager = LinearLayoutManager(this)
        studentList = ArrayList()
        evenAdapter = eveningAdapter(this, studentList)
        students.adapter = evenAdapter
        dbRef = FirebaseDatabase.getInstance().getReference("Students").child(route)
        dbRef.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (i in snapshot.children){
                    val currentStudent = i.getValue(Student::class.java)
                    studentList.add(currentStudent!!)
                }
                evenAdapter.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        dbRef2.child("nextStop").addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                nextStop.text = "Next Stop : ${snapshot.value.toString()}"
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}