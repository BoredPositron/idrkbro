package com.example.whatever

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import java.time.LocalDate

class MainActivity : AppCompatActivity() {
    private lateinit var students: RecyclerView
    private lateinit var studentAdapt: StudentAdapter
    private lateinit var studentList: ArrayList<Student>
    private lateinit var dbRef: DatabaseReference
    private lateinit var dbRef2: DatabaseReference
    private lateinit var dbRef3: DatabaseReference
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var nextStop: TextView
    private lateinit var presC: TextView
    var pref_key = "prefs"
    var route_key = "route"
    var route = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        nextStop = findViewById(R.id.nextStop)
        sharedPreferences = getSharedPreferences(pref_key, Context.MODE_PRIVATE)
        route = sharedPreferences.getString(route_key, null)!!
        students = findViewById(R.id.StudentList)
        dbRef = FirebaseDatabase.getInstance().getReference("Students").child(route)
        dbRef2 = FirebaseDatabase.getInstance().getReference("Attendance")
            .child(LocalDate.now().toString()).child(route)
        dbRef3 = FirebaseDatabase.getInstance().getReference("Buses").child(route)
        presC = findViewById(R.id.presentCount)
        studentList = ArrayList()
        studentAdapt = StudentAdapter(this, studentList)
        var presentCount = "0"
        var absentCount = "0"
        students.layoutManager = LinearLayoutManager(this)
        students.adapter = studentAdapt
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                studentList.clear()
                for (name in snapshot.children) {
                    val currentStudent = name.getValue(Student::class.java)
                    studentList.add(currentStudent!!)
                    absentCount += 1
                }
                studentAdapt.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
        dbRef3.child("nextStop").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                nextStop.text = "Next Stop : ${snapshot.value.toString()}"
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        dbRef2.child("Morning Count").addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                presentCount = snapshot.value.toString()
                presC.text = "Present: ${presentCount}"
            }   

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}