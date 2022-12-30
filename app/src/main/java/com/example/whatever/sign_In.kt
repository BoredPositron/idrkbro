package com.example.whatever

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.database.DatabaseErrorHandler
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class sign_In : AppCompatActivity() {
    private lateinit var codeBox: EditText
    private lateinit var logIn: Button
    private lateinit var dbRef: DatabaseReference
    private lateinit var dbRef2: DatabaseReference
    private lateinit var dbRef3: DatabaseReference
    private lateinit var sharedPreferences: SharedPreferences
    var prefs_key = "prefs"
    var route_key = "route"
    var codee = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        codeBox = findViewById(R.id.cde)
        logIn = findViewById(R.id.signIn)
        dbRef = FirebaseDatabase.getInstance().getReference("Carers")
        dbRef2 = FirebaseDatabase.getInstance().getReference("Buses")
        dbRef3 = FirebaseDatabase.getInstance().getReference("Attendance")
            .child(LocalDate.now().toString())
        val carerCodes = mutableListOf<Carer>()
        val access = mutableListOf<String>()
        sharedPreferences = getSharedPreferences(prefs_key, Context.MODE_PRIVATE)
        codee = sharedPreferences.getString(route_key, "").toString()
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (carer in snapshot.children) {
                    val currentCarer = carer.getValue(Carer::class.java)
                    carerCodes.add(currentCarer!!)
                    access.add(currentCarer.code!!.toString())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        logIn.setOnClickListener {
            val carerCode = codeBox.text.toString()
            if (carerCode in access) {
                if (carerCodes[access.indexOf(carerCode)].access == true) {
                    val intent = Intent(this, idrk::class.java)
                    startActivity(intent)
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    val editor: SharedPreferences.Editor = sharedPreferences.edit()
                    editor.putString(route_key, carerCodes[access.indexOf(carerCode)].routeNumber)
                    editor.apply()
                    codeBox.text.clear()
                    dbRef2.child(carerCodes[access.indexOf(carerCode)].routeNumber.toString())
                        .setValue(
                            Bus(
                                carerCodes[access.indexOf(carerCode)].routeNumber.toString(),
                                carerCodes[access.indexOf(carerCode)].name.toString(),
                                null,
                                null,
                            )
                        )
                } else {
                    Toast.makeText(this, "You dont have access!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Invalid Code", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
