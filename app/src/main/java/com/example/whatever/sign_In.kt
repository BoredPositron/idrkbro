package com.example.whatever

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class sign_In : AppCompatActivity() {
    private lateinit var codeBox: EditText
    private lateinit var logIn: Button
    private lateinit var dbRef: DatabaseReference
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
        var carerCodes = mutableListOf<Carer>()
        val access = mutableListOf<String>()
        sharedPreferences = getSharedPreferences(prefs_key, Context.MODE_PRIVATE)
        codee = sharedPreferences.getString(route_key, "").toString()
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (carer in snapshot.children) {
                    val currentCarer = carer.getValue(Carer::class.java)
                    carerCodes.add(currentCarer!!)
                    access.add(currentCarer.Code!!)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        logIn.setOnClickListener {
            val carerCode = codeBox.text.toString()
            if (carerCodes[access.indexOf(carerCode)].Access == true) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                editor.putString(route_key, carerCodes[access.indexOf(carerCode)].RouteNumber)
                editor.apply()
            }else{
                Toast.makeText(this, "You dont have access!", Toast.LENGTH_SHORT).show()
            }

        }
    }
}
