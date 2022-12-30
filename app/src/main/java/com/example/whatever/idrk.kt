package com.example.whatever

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import java.time.LocalDate
import java.time.LocalTime

class idrk : AppCompatActivity() {
    private lateinit var progressB: ProgressBar
    private lateinit var logo: ImageView
    private lateinit var dbRef: DatabaseReference
    private lateinit var sharedPreferences: SharedPreferences
    var prefKey = "prefs"
    var routeKey = "route"
    var route = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_idrk)
        sharedPreferences = getSharedPreferences(prefKey, Context.MODE_PRIVATE)
        route = sharedPreferences.getString(routeKey, null)!!
        logo = findViewById(R.id.logo1)
        progressB = findViewById(R.id.progB)
        dbRef = FirebaseDatabase.getInstance().getReference("Attendance")
            .child(LocalDate.now().toString()).child(route).child("eAt")
        progressB.max = 10000
        val currentProgress = 10000
        logo.alpha = 0f
        logo.animate().setDuration(2500).alpha(1f).withEndAction {
            getPage()
        }
        ObjectAnimator.ofInt(progressB, "progress", currentProgress).setDuration(2500).start()
    }

    private fun getPage() {
        if (LocalTime.now().hour > 13) {
            dbRef.get().addOnSuccessListener {
                if (it.value == true) {
                    val intent = Intent(this, eveningTrack::class.java)
                    startActivity(intent)
                } else {
                    val intent = Intent(this, eveAtt::class.java)
                    startActivity(intent)
                }
            }

        } else {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}