package com.example.whatever

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import java.time.LocalDate

class StudentAdapter(val context: Context, val StudentList: ArrayList<Student>, val dbRef: DatabaseReference = FirebaseDatabase.getInstance().getReference(LocalDate.now().toString())): RecyclerView.Adapter<StudentAdapter.StudentDataHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentDataHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.student, parent, false)
        return StudentDataHolder(view)
    }

    override fun onBindViewHolder(holder: StudentDataHolder, position: Int) {
        val currentStudent = StudentList[position]
        dbRef.child(currentStudent.RouteNo.toString()).child(currentStudent.PickupNo.toString()).child(currentStudent.Name.toString()).child("In Bus").addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.value.toString() == "True"){
                    holder.studentIn.isEnabled = false
                    holder.studentOut.isEnabled = true
                }else if (snapshot.value.toString() == "False"){
                    holder.studentOut.isEnabled = false
                    holder.studentIn.isEnabled = true
                }else{
                    holder.studentOut.isEnabled = false
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        holder.studentIn.setOnClickListener {
            dbRef.child(currentStudent.RouteNo.toString()).child(currentStudent.PickupNo.toString()).child(currentStudent.Name.toString()).child("In Bus").setValue("True").addOnSuccessListener {
                Toast.makeText(context, "Student in bus!", Toast.LENGTH_SHORT).show()
            }
        }
        holder.studentOut.setOnClickListener {
            dbRef.child(currentStudent.RouteNo.toString()).child(currentStudent.PickupNo.toString()).child(currentStudent.Name.toString()).child("In Bus").setValue("False").addOnSuccessListener {
                Toast.makeText(context, "Student ded", Toast.LENGTH_SHORT).show()
            }
        }
        holder.studentName.text = currentStudent.Name.toString()
    }

    override fun getItemCount(): Int {
        return StudentList.size
    }
    class StudentDataHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val studentName = itemView.findViewById<TextView>(R.id.student_Name)
        val studentIn = itemView.findViewById<Button>(R.id.stdnt_In)
        val studentOut = itemView.findViewById<Button>(R.id.stdnt_Out)
    }
}