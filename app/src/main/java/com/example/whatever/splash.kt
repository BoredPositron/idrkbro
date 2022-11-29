package com.example.whatever

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class splash : AppCompatActivity() {
    private lateinit var skl: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        skl = findViewById(R.id.sklogo)
        skl.alpha = 0f
        skl.animate().setDuration(1500).alpha(1f).withEndAction{
            val intent = Intent(this, sign_In::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }
}