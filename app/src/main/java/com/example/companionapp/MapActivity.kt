package com.example.companionapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MapActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        val players = intent.getStringArrayListExtra("players") ?: arrayListOf("Player 1", "Player 2", "Player 3", "Player 4")
        val colors = intent.getStringArrayListExtra("colors") ?: arrayListOf("Red", "Blue", "Green", "Yellow")

        val rootView = findViewById<View>(android.R.id.content)
        rootView.setOnClickListener {
            val nightIntent = Intent(this, NightScreenActivity::class.java)
            nightIntent.putStringArrayListExtra("players", players)
            nightIntent.putStringArrayListExtra("colors", colors)
            startActivity(nightIntent)
        }
    }
}
