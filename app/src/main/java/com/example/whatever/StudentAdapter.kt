package com.example.whatever

import android.content.Context
import android.os.strictmode.WebViewMethodCalledOnWrongThreadViolation
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import org.w3c.dom.Text
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class StudentAdapter
    (
    val context: Context,
    val StudentList: ArrayList<Student>,
    val dbRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("Attendance")
        .child(LocalDate.now().toString()),
    val dbRef2: DatabaseReference = FirebaseDatabase.getInstance().getReference("Buses")
) :
    RecyclerView.Adapter<StudentAdapter.StudentDataHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentDataHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.student, parent, false)
        return StudentDataHolder(view)
    }

    override fun onBindViewHolder(holder: StudentDataHolder, position: Int) {
        val currentStudent = StudentList[position]
        var nextStudent = ""
        try {
            nextStudent = StudentList[position + 1].Name.toString()
        } catch (e: java.lang.IndexOutOfBoundsException) {
            nextStudent = "School"
        }
        holder.con.visibility = View.GONE
        holder.grade.text = "Grade: N/A"
        holder.studentName.text = currentStudent.Name
        holder.outBus.visibility = View.GONE
        holder.undo.visibility = View.GONE
        holder.undo1.visibility = View.GONE
        holder.ot.setOnClickListener {
            holder.con.visibility = View.VISIBLE
            holder.yes.setOnClickListener {
                holder.con.visibility = View.GONE
                dbRef.child(currentStudent.RouteNo.toString())
                    .child(currentStudent.PickupNo.toString())
                    .setValue(
                        StudentInOut(
                            name = currentStudent.Name.toString(),
                            morningAttendance = false,
                            inBus = false,
                        )
                    ).addOnSuccessListener {
                        dbRef2.child(currentStudent.RouteNo.toString()).child("previousStop")
                            .setValue(currentStudent.Name.toString())
                        dbRef2.child(currentStudent.RouteNo.toString()).child("nextStop")
                            .setValue(nextStudent)
                        holder.inBus.visibility = View.GONE
                        holder.ot.visibility = View.GONE
                        dbRef.child(currentStudent.RouteNo.toString()).child("Morning Count")
                            .setValue(ServerValue.increment(0))
                        holder.con.visibility = View.GONE
                        holder.undo1.visibility = View.VISIBLE
                    }
            }
        }
        holder.undo1.setOnClickListener {
            holder.con.visibility = View.VISIBLE
            holder.yes.setOnClickListener {
                holder.con.visibility = View.GONE
                dbRef.child(currentStudent.RouteNo.toString())
                    .child(currentStudent.PickupNo.toString())
                    .setValue(
                        StudentInOut()
                    ).addOnSuccessListener {
                        dbRef2.child(currentStudent.RouteNo.toString()).child("previousStop")
                            .setValue(currentStudent.Name.toString())
                        dbRef2.child(currentStudent.RouteNo.toString()).child("nextStop")
                            .setValue(nextStudent)
                        holder.inBus.visibility = View.VISIBLE
                        holder.ot.visibility = View.VISIBLE
                        holder.undo1.visibility = View.GONE
                        dbRef.child(currentStudent.RouteNo.toString()).child("Morning Count")
                            .setValue(ServerValue.increment(0))
                    }
            }
        }
        holder.undo.setOnClickListener {
            holder.con.visibility = View.VISIBLE
            holder.yes.setOnClickListener {
                holder.con.visibility = View.GONE   
                dbRef.child(currentStudent.RouteNo.toString())
                    .child(currentStudent.PickupNo.toString())
                    .setValue(
                        StudentInOut()
                    ).addOnSuccessListener {
                        dbRef2.child(currentStudent.RouteNo.toString()).child("previousStop")
                            .setValue(currentStudent.Name.toString())
                        dbRef2.child(currentStudent.RouteNo.toString()).child("nextStop")
                            .setValue(nextStudent)
                        holder.inBus.visibility = View.VISIBLE
                        holder.ot.visibility = View.VISIBLE
                        holder.undo.visibility = View.GONE
                        dbRef.child(currentStudent.RouteNo.toString()).child("Morning Count")
                            .setValue(ServerValue.increment(-1))
                    }
            }
        }
        holder.no.setOnClickListener {
            holder.con.visibility = View.GONE
            Toast.makeText(context, "Ok!", Toast.LENGTH_SHORT).show()
        }
        holder.inBus.setOnClickListener {
            holder.con.visibility = View.VISIBLE
            holder.yes.setOnClickListener {
                holder.con.visibility = View.GONE
                dbRef.child(currentStudent.RouteNo.toString())
                    .child(currentStudent.PickupNo.toString())
                    .setValue(
                        StudentInOut(
                            inTime = LocalTime.now().toString(),
                            morningAttendance = true,
                            inBus = true,
                            name = currentStudent.Name
                        )
                    ).addOnSuccessListener {
                        dbRef2.child(currentStudent.RouteNo.toString()).child("previousStop")
                            .setValue(currentStudent.Name.toString())
                        dbRef2.child(currentStudent.RouteNo.toString()).child("nextStop")
                            .setValue(nextStudent)
                        holder.inBus.visibility = View.GONE
                        holder.ot.visibility = View.GONE
                        dbRef.child(currentStudent.RouteNo.toString()).child("Morning Count")
                            .setValue(ServerValue.increment(1))
                        holder.con.visibility = View.GONE
                        holder.undo.visibility = View.VISIBLE
                    }
            }

        }
        dbRef.child(currentStudent.RouteNo.toString()).child(currentStudent.PickupNo.toString())
            .child("inBus").get().addOnSuccessListener {
                if (it.value == true) {
                    holder.inBus.visibility = View.GONE
                    holder.con.visibility = View.GONE
                    holder.ot.visibility = View.GONE
                    holder.undo.visibility = View.VISIBLE
                } else if (it.value == false) {
                    holder.inBus.visibility = View.GONE
                    holder.con.visibility = View.GONE
                    holder.ot.visibility = View.GONE
                    holder.undo1.visibility = View.VISIBLE
                } else {
                    holder.inBus.visibility = View.VISIBLE
                    holder.con.visibility = View.GONE
                    holder.ot.visibility = View.VISIBLE
                    holder.undo.visibility = View.GONE
                }
            }
    }

    override fun getItemCount(): Int {
        return StudentList.size
    }

    class StudentDataHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val studentName = itemView.findViewById<TextView>(R.id.student_Name)
        val inBus = itemView.findViewById<Button>(R.id.studentIn)
        val outBus = itemView.findViewById<Button>(R.id.studentOut)
        val yes = itemView.findViewById<Button>(R.id.Yes)
        val no = itemView.findViewById<Button>(R.id.No)
        val con = itemView.findViewById<CardView>(R.id.confirmDialogue)
        val grade = itemView.findViewById<TextView>(R.id.grade)
        val ot = itemView.findViewById<Button>(R.id.ot)
        val undo = itemView.findViewById<ImageButton>(R.id.undo)
        val undo1 = itemView.findViewById<ImageButton>(R.id.undo1)
    }
}