package com.example.companionapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class EndNightActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_end_night)

        val currentNight = intent.getIntExtra("currentNight", 1)
        val players = intent.getStringArrayListExtra("players") ?: arrayListOf()
        val colors = intent.getStringArrayListExtra("colors") ?: arrayListOf()

        // Update rank placeholder text
        findViewById<TextView>(R.id.rank_text).text = "Rank is supposed to show here"

        val nextButton = findViewById<Button>(R.id.action_button)
        if (currentNight < 3) {
            nextButton.text = "Next Night"
            nextButton.setOnClickListener {
                // Move to the next night
                val intent = Intent(this, NightScreenActivity::class.java).apply {
                    putExtra("currentNight", currentNight + 1) // Increment night
                    putStringArrayListExtra("players", ArrayList(players)) // Pass players
                    putStringArrayListExtra("colors", ArrayList(colors))   // Pass colors
                }
                startActivity(intent)
                finish() // Close this activity to prevent going back to it
            }
        } else {
            // Final night, show "End Game"
            nextButton.text = "End Game"
            nextButton.setOnClickListener {
                // Exit the game
                finishAffinity() // Close all activities and exit
            }
        }
    }
}
