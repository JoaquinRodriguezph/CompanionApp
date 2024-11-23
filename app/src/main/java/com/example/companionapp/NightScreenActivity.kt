package com.example.companionapp

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class NightScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_night_screen)

        val players = intent.getStringArrayListExtra("players") ?: arrayListOf()
        val colors = intent.getStringArrayListExtra("colors") ?: arrayListOf()
        val currentNight = intent.getIntExtra("currentNight", 1) // Default to night 1

        // Update night title dynamically
        val nightTitle = findViewById<TextView>(R.id.night_title)
        nightTitle.text = "Night $currentNight"

        // Populate players and their colors
        val playerContainer = findViewById<LinearLayout>(R.id.player_container)
        playerContainer.removeAllViews() // Clear any existing views (precautionary)

        for (i in players.indices) {
            val playerView = TextView(this)
            playerView.text = players[i]
            playerView.setBackgroundColor(Color.parseColor(getColorFromName(colors[i])))
            playerView.setTextColor(Color.BLACK) // Ensure text is visible
            playerView.textSize = 18f
            playerView.setPadding(16, 16, 16, 16)
            playerContainer.addView(playerView)
        }

        // Handle End Night button
        findViewById<Button>(R.id.end_night_button).setOnClickListener {
            val intent = Intent(this, EndNightActivity::class.java).apply {
                putExtra("currentNight", currentNight) // Pass the current night to EndNightActivity
                putStringArrayListExtra("players", ArrayList(players)) // Pass players
                putStringArrayListExtra("colors", ArrayList(colors))   // Pass colors
            }
            startActivity(intent)
            finish() // Close this activity to prevent going back to it
        }
    }

    private fun getColorFromName(colorName: String): String {
        return when (colorName) {
            "Red" -> "#FF0000"
            "Blue" -> "#0000FF"
            "Green" -> "#008000"
            "Yellow" -> "#FFFF00"
            else -> "#FFFFFF" // Default to white if color is unknown
        }
    }
}
