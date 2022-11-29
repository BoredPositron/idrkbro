package com.example.whatever

import android.content.Context
import android.os.strictmode.WebViewMethodCalledOnWrongThreadViolation
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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
        val currentStudent =
            if (LocalDateTime.now().hour > 13) StudentList.reversed()[position] else StudentList[position]
        var nextStudent = ""
        try {
            nextStudent =
                if (LocalDateTime.now().hour > 13) StudentList.reversed()[position + 1].Name.toString() else StudentList[position + 1].Name.toString()
        } catch (e: java.lang.IndexOutOfBoundsException) {
            nextStudent = "School"
        }
        holder.con.visibility = View.GONE
        holder.grade.text = "Grade: N/A"
        dbRef
        holder.no.setOnClickListener {
            Toast.makeText(context, "Ok!", Toast.LENGTH_SHORT).show()
        }
        dbRef.child(currentStudent.RouteNo.toString()).child(currentStudent.PickupNo.toString())
            .child("inBus")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.value == true) {
                        holder.outBus.visibility = View.VISIBLE
                        holder.inBus.visibility = View.GONE
                        holder.outBus.setOnClickListener {
                            holder.con.visibility = View.VISIBLE
                            holder.yes.setOnClickListener {
                                holder.con.visibility = View.GONE
                                dbRef2.child(currentStudent.RouteNo.toString())
                                    .child("previousStop")
                                    .setValue(currentStudent.Name)
                                dbRef2.child(currentStudent.RouteNo.toString()).child("nextStop")
                                    .setValue(nextStudent)
                                dbRef.child(currentStudent.RouteNo.toString())
                                    .child(currentStudent.PickupNo.toString())
                                    .child("inBus").setValue(false).addOnSuccessListener {
                                        Toast.makeText(
                                            context,
                                            "Student Out of Bus!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                dbRef.child(currentStudent.RouteNo.toString()).child("Morning Count").setValue(ServerValue.increment(-1))
                            }
                            holder.no.setOnClickListener {
                                Toast.makeText(context, "Ok!", Toast.LENGTH_SHORT).show()
                                holder.con.visibility = View.GONE
                            }
                        }
                    } else if (snapshot.value == false) {
                        holder.inBus.visibility = View.VISIBLE
                        holder.outBus.visibility = View.GONE
                        holder.inBus.setOnClickListener {
                            holder.con.visibility = View.VISIBLE
                            holder.yes.setOnClickListener {
                                holder.con.visibility = View.GONE
                                dbRef.child(currentStudent.RouteNo.toString()).child("Morning Count").setValue(ServerValue.increment(1))
                                dbRef2.child(currentStudent.RouteNo.toString())
                                    .child("previousStop")
                                    .setValue(currentStudent.Name)
                                dbRef2.child(currentStudent.RouteNo.toString()).child("nextStop")
                                    .setValue(nextStudent)
                                dbRef.child(currentStudent.RouteNo.toString())
                                    .child(currentStudent.PickupNo.toString())
                                    .setValue(
                                        StudentInOut
                                            (
                                            currentStudent.Name.toString(),
                                            "${LocalDateTime.now().hour}:${LocalDateTime.now().minute}:${LocalDateTime.now().second}",
                                            true,
                                            true
                                        )
                                    )
                                    .addOnSuccessListener {
                                        Toast.makeText(
                                            context,
                                            "Student in bus!",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                    }
                            }
                            holder.no.setOnClickListener {
                                Toast.makeText(context, "Ok!", Toast.LENGTH_SHORT).show()
                                holder.con.visibility = View.GONE
                            }
                        }

                    } else {
                        holder.outBus.visibility = View.GONE
                        holder.inBus.visibility = View.VISIBLE
                        holder.inBus.setOnClickListener {
                            holder.con.visibility = View.VISIBLE
                            holder.yes.setOnClickListener {
                                dbRef.child(currentStudent.RouteNo.toString()).child("Morning Count").setValue(ServerValue.increment(1))
                                dbRef2.child(currentStudent.RouteNo.toString())
                                    .child("previousStop")
                                    .setValue(currentStudent.Name)
                                dbRef2.child(currentStudent.RouteNo.toString()).child("nextStop")
                                    .setValue(nextStudent)
                                holder.con.visibility = View.GONE
                                dbRef.child(currentStudent.RouteNo.toString())
                                    .child(currentStudent.PickupNo.toString())
                                    .setValue(
                                        StudentInOut
                                            (
                                            currentStudent.Name.toString(),
                                            "${LocalDateTime.now().hour}:${LocalDateTime.now().minute}:${LocalDateTime.now().second}",
                                            true,
                                            true
                                        )
                                    )
                                    .addOnSuccessListener {
                                        Toast.makeText(
                                            context,
                                            "Student in bus!",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                    }
                            }
                            holder.no.setOnClickListener {
                                Toast.makeText(context, "Ok!", Toast.LENGTH_SHORT).show()
                                holder.con.visibility = View.GONE
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        holder.ot.setOnClickListener {
            holder.con.visibility = View.VISIBLE
            holder.yes.setOnClickListener {
                dbRef2.child(currentStudent.RouteNo.toString())
                    .child("previousStop")
                    .setValue(currentStudent.Name)
                dbRef2.child(currentStudent.RouteNo.toString()).child("nextStop")
                    .setValue(nextStudent).addOnSuccessListener {
                        holder.con.visibility = View.GONE
                    }
            }
        }
        holder.studentName.text = currentStudent.Name.toString()
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
    }
}