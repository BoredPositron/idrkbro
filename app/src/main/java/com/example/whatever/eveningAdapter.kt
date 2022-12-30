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
import java.time.LocalTime

class eveningAdapter(
    val context: Context,
    val studentList: ArrayList<Student>,
    val dbRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("Attendance")
        .child(LocalDate.now().toString()),
    val dbRef2: DatabaseReference = FirebaseDatabase.getInstance().getReference("Buses")
) : RecyclerView.Adapter<eveningAdapter.StudHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.student, parent, false)
        return StudHolder(view)
    }

    override fun onBindViewHolder(holder: StudHolder, position: Int) {
        val currentStudent = studentList.reversed()[position]
        var nextStudent = ""
        try {
            nextStudent = studentList.reversed()[position + 1].Name.toString()
        } catch (e: java.lang.IndexOutOfBoundsException) {
            nextStudent = "School"
        }
        holder.studentName.text = currentStudent.Name
        holder.con.visibility = View.GONE
        holder.undo.visibility = View.GONE
        holder.ot.visibility = View.GONE
        holder.undo1.visibility = View.GONE
        holder.outBus.setOnClickListener {
            holder.con.visibility = View.VISIBLE
            holder.yes.setOnClickListener {
                holder.con.visibility = View.GONE
                holder.studentCard.setCardBackgroundColor(Color.GRAY)
                dbRef.child(currentStudent.Route.toString())
                    .child(currentStudent.Pickup.toString()).child("inBus").setValue(false)
                    .addOnSuccessListener {
                        dbRef.child(currentStudent.Route.toString())
                            .child(currentStudent.Pickup.toString()).child("outTime")
                            .setValue(LocalTime.now().toString())
                        holder.outBus.visibility = View.GONE
                        dbRef2.child(currentStudent.Route.toString()).child("nextStop")
                            .setValue(nextStudent)
                        dbRef2.child(currentStudent.Route.toString()).child("previousStop")
                            .setValue(currentStudent.Name)
                        holder.undo.visibility = View.VISIBLE
                        holder.inBus.visibility = View.GONE
                        Toast.makeText(context, "Task completed!", Toast.LENGTH_SHORT).show()
                    }
            }
        }
        holder.undo.setOnClickListener {
            holder.con.visibility = View.VISIBLE
            holder.yes.setOnClickListener {
                holder.con.visibility = View.GONE
                holder.undo.visibility = View.GONE
                holder.studentCard.setCardBackgroundColor(Color.parseColor("#3a3b3c"))
                dbRef.child(currentStudent.Route.toString())
                    .child(currentStudent.Pickup.toString()).child("inBus").setValue(true)
                    .addOnSuccessListener {
                        holder.outBus.visibility = View.VISIBLE
                        Toast.makeText(context, "Task completed successfully!", Toast.LENGTH_SHORT)
                            .show()
                        holder.undo.visibility = View.GONE
                        dbRef.child(currentStudent.Route.toString())
                            .child(currentStudent.Pickup.toString()).child("outTime")
                            .setValue(null)
                    }
            }
        }
        dbRef.child(currentStudent.Route.toString()).child(currentStudent.Pickup.toString())
            .child("inBus").get().addOnSuccessListener {
                if (it.value == true) {
                    holder.studentCard.setCardBackgroundColor(Color.parseColor("#3a3b3c"))
                    holder.undo.visibility = View.GONE
                    holder.outBus.visibility = View.VISIBLE
                    holder.inBus.visibility = View.GONE
                    holder.undo1.visibility = View.GONE
                } else if (it.value == false) {
                    holder.studentCard.setCardBackgroundColor(Color.GRAY)
                    holder.undo.visibility = View.VISIBLE
                    holder.outBus.visibility = View.GONE
                    holder.inBus.visibility = View.GONE
                    holder.undo1.visibility = View.GONE
                } else {
                    holder.studentCard.setCardBackgroundColor(Color.parseColor("#3a3b3c"))
                    holder.undo.visibility = View.GONE
                    holder.outBus.visibility = View.VISIBLE
                    holder.undo1.visibility = View.GONE
                    holder.inBus.visibility = View.GONE
                }
            }
        holder.no.setOnClickListener {
            Toast.makeText(context, "Ok!", Toast.LENGTH_SHORT).show()
            holder.con.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return studentList.size
    }

    class StudHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val studentName = itemView.findViewById<TextView>(R.id.student_Name)
        val inBus = itemView.findViewById<Button>(R.id.studentIn)
        val outBus = itemView.findViewById<Button>(R.id.studentOut)
        val yes = itemView.findViewById<Button>(R.id.Yes)
        val no = itemView.findViewById<Button>(R.id.No)
        val con = itemView.findViewById<CardView>(R.id.confirmDialogue)
        val ot = itemView.findViewById<Button>(R.id.ot)
        val undo = itemView.findViewById<ImageButton>(R.id.undo)
        val undo1 = itemView.findViewById<ImageButton>(R.id.undo1)
        val studentCard = itemView.findViewById<CardView>(R.id.studentCard)
    }
}