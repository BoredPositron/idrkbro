package com.example.whatever

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {
    private lateinit var students: RecyclerView
    private lateinit var studentAdapt: StudentAdapter
    private lateinit var studentList: ArrayList<Student>
    private lateinit var dbRef: DatabaseReference
    private lateinit var sharedPreferences: SharedPreferences
    var pref_key = "prefs"
    var code_key = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sharedPreferences = getSharedPreferences(pref_key, Context.MODE_PRIVATE)
        students = findViewById(R.id.StudentList)
        dbRef = FirebaseDatabase.getInstance().getReference("9")
        studentList = ArrayList()
        studentAdapt = StudentAdapter(this, studentList)
        students.layoutManager = LinearLayoutManager(this)
        students.adapter = studentAdapt

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                studentList.clear()
                for (name in snapshot.children) {
                    val currentStudent = name.getValue(Student::class.java)
                    studentList.add(currentStudent!!)
                }
                studentAdapt.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}