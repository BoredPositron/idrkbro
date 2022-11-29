package com.example.whatever

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import java.time.LocalDate

class eveAttAdapt(
    val context: Context,
    val StudentList: ArrayList<Student>,
    val dbRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("Attendance")
        .child(LocalDate.now().toString()),
) : RecyclerView.Adapter<eveAttAdapt.StudentData>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentData {
        val view: View = LayoutInflater.from(context).inflate(R.layout.attendance, parent, false)
        return StudentData(view)
    }

    override fun onBindViewHolder(holder: StudentData, position: Int) {
        val currentStudent = StudentList[position]
        holder.name.text = currentStudent.Name
        holder.consent.visibility = View.GONE
        var att = "false"
        holder.no.setOnClickListener {
            Toast.makeText(context, "Ok!", Toast.LENGTH_SHORT).show()
        }
        holder.att.setOnClickListener {
            holder.consent.visibility = View.VISIBLE
            holder.yes.setOnClickListener {
                dbRef.child(currentStudent.RouteNo.toString())
                    .child(currentStudent.PickupNo.toString())
                    .child("eveningAttendance").setValue(true).addOnSuccessListener {
                        Toast.makeText(context, "Task completed successfully!", Toast.LENGTH_SHORT)
                            .show()
                        holder.att.visibility = View.GONE
                        holder.consent.visibility = View.GONE
                        dbRef.child(currentStudent.RouteNo.toString()).child("Evening Count").setValue(ServerValue.increment(1))
                    }
            }
        }

        dbRef.child(currentStudent.RouteNo.toString()).child(currentStudent.PickupNo.toString())
            .child("eveningAttendance").get().addOnSuccessListener {
                if (it.value == true) {
                holder.att.visibility = View.GONE
                }
            }
    }


    override fun getItemCount(): Int {
        return StudentList.size
    }

    class StudentData(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.findViewById<TextView>(R.id.studentName)
        val att = itemView.findViewById<Button>(R.id.present)
        val consent = itemView.findViewById<CardView>(R.id.confirmDialogue)
        val yes = itemView.findViewById<Button>(R.id.Yes)
        val no = itemView.findViewById<Button>(R.id.No)
    }
}