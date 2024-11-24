package com.example.companionapp

import android.content.Context
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

        // Retrieve data
        val players = intent.getStringArrayListExtra("players") ?: arrayListOf()
        val colors = intent.getStringArrayListExtra("colors") ?: arrayListOf()
        val playerItems = intent.getSerializableExtra("playerItems") as? HashMap<String, ArrayList<String>>
        val currentNight = intent.getIntExtra("currentNight", 1)

        // Update night title
        val nightTitle = findViewById<TextView>(R.id.night_title)
        nightTitle.text = "Night $currentNight"

        // Populate players dynamically
        val playerContainer = findViewById<LinearLayout>(R.id.player_container)
        playerContainer.removeAllViews()

        for (i in players.indices) {
            val playerView = TextView(this)
            playerView.text = players[i]
            playerView.setBackgroundColor(Color.parseColor(getColorFromName(colors[i])))
            playerView.setTextColor(Color.WHITE)
            playerView.textSize = 18f
            playerView.setPadding(16, 16, 16, 16)

            // Set onClickListener for PlayerTurnActivity
            playerView.setOnClickListener {
                val intent = Intent(this, PlayerTurnActivity::class.java).apply {
                    putExtra("playerName", players[i])
                    putStringArrayListExtra("playerItems", playerItems?.get(players[i]) ?: arrayListOf())
                    putStringArrayListExtra("players", ArrayList(players))
                    putStringArrayListExtra("colors", ArrayList(colors))
                    putExtra("playerItemsMap", HashMap(playerItems))
                    putExtra("currentNight", currentNight)
                }
                startActivity(intent)
            }
            playerContainer.addView(playerView)
        }

        // End Night button clears SharedPreferences
        findViewById<Button>(R.id.end_night_button).setOnClickListener {
            clearCheckboxState()
            val intent = Intent(this, EndNightActivity::class.java).apply {
                putExtra("currentNight", currentNight)
                putStringArrayListExtra("players", ArrayList(players))
                putStringArrayListExtra("colors", ArrayList(colors))
            }
            startActivity(intent)
            finish()
        }

        // Buttons
        findViewById<Button>(R.id.calculator_button).setOnClickListener {
            startActivity(Intent(this, CalculatorActivity::class.java))
        }

        findViewById<Button>(R.id.question_button).setOnClickListener {
            startActivity(Intent(this, RulesActivity::class.java))
        }

        findViewById<Button>(R.id.map_button).setOnClickListener {
            startActivity(Intent(this, MapPopUpActivity::class.java))
        }
    }

    private fun clearCheckboxState() {
        val sharedPreferences = getSharedPreferences("PlayerTurnPrefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()
    }

    private fun getColorFromName(colorName: String): String {
        return when (colorName) {
            "Red" -> "#FF0000"
            "Blue" -> "#0000FF"
            "Green" -> "#008000"
            "Yellow" -> "#FFFF00"
            else -> "#FFFFFF"
        }
    }
}
