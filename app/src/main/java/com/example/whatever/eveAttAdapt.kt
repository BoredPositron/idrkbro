package com.example.whatever

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
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
        holder.undo.visibility = View.GONE
        holder.no.setOnClickListener {
            Toast.makeText(context, "Ok!", Toast.LENGTH_SHORT).show()
            holder.consent.visibility = View.GONE
        }
        holder.att.setOnClickListener {
            holder.consent.visibility = View.VISIBLE
            holder.yes.setOnClickListener {
                dbRef.child(currentStudent.Route.toString())
                    .child(currentStudent.Pickup.toString())
                    .child("eveningAttendance").setValue(true).addOnSuccessListener {
                        Toast.makeText(context, "Task completed successfully!", Toast.LENGTH_SHORT)
                            .show()
                        holder.studentCard.setCardBackgroundColor(Color.GRAY)
                        holder.att.visibility = View.GONE
                        holder.undo.visibility = View.VISIBLE
                        holder.consent.visibility = View.GONE
                        dbRef.child(currentStudent.Route.toString()).child("EveningCount")
                            .setValue(ServerValue.increment(1))
                        dbRef.child(currentStudent.Route.toString())
                            .child(currentStudent.Pickup.toString()).child("inBus").setValue(true)
                    }
            }
        }
        holder.undo.setOnClickListener {
            holder.consent.visibility = View.VISIBLE
            holder.yes.setOnClickListener {
                dbRef.child(currentStudent.Route.toString())
                    .child(currentStudent.Pickup.toString())
                    .child("eveningAttendance").setValue(false).addOnSuccessListener {
                        Toast.makeText(context, "Task completed successfully!", Toast.LENGTH_SHORT)
                            .show()
                        holder.studentCard.setCardBackgroundColor(Color.parseColor("#3a3b3c"))
                        holder.att.visibility = View.VISIBLE
                        holder.undo.visibility = View.GONE
                        holder.consent.visibility = View.GONE
                        dbRef.child(currentStudent.Route.toString()).child("EveningCount")
                            .setValue(ServerValue.increment(-1))
                    }
            }
        }

        dbRef.child(currentStudent.Route.toString()).child(currentStudent.Pickup.toString())
            .child("eveningAttendance").get().addOnSuccessListener {
                if (it.value == true) {
                    holder.att.visibility = View.GONE
                    holder.studentCard.setCardBackgroundColor(Color.GRAY)
                    holder.undo.visibility = View.VISIBLE
                } else {
                    holder.att.visibility = View.VISIBLE
                    holder.studentCard.setCardBackgroundColor(Color.parseColor("#3a3b3c"))
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
        val undo = itemView.findViewById<ImageButton>(R.id.undo)
        val studentCard = itemView.findViewById<CardView>(R.id.studentCard)
    }
}