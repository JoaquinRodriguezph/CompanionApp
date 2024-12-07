package com.example.companionapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MapActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        // Retrieve data from the Intent
        val players = intent.getStringArrayListExtra("players")
        val colors = intent.getStringArrayListExtra("colors")
        val playerItems = intent.getSerializableExtra("playerItems") as? HashMap<String, ArrayList<String>>

        // Validate and log data
        if (players == null || colors == null || playerItems == null) {
            Log.e("MapActivity", "Missing data! Falling back to defaults.")
        } else {
            Log.d("MapActivity", "Players: $players")
            Log.d("MapActivity", "Colors: $colors")
            Log.d("MapActivity", "Player Items: $playerItems")
        }

        // Prepare data for NightScreenActivity
        val selectedPlayers = players ?: arrayListOf()
        val selectedColors = colors ?: arrayListOf()
        val selectedPlayerItems = playerItems ?: hashMapOf()

        val nextNightButton = findViewById<Button>(R.id.next_night_button)
        nextNightButton.setOnClickListener {
            // Launch NightScreenActivity on click
            val nightIntent = Intent(this, NightScreenActivity::class.java).apply {
                putStringArrayListExtra("players", ArrayList(selectedPlayers)) // Pass selected players
                putStringArrayListExtra("colors", ArrayList(selectedColors))  // Pass corresponding colors
                putExtra("playerItems", selectedPlayerItems)                  // Pass items for each player
            }
            startActivity(nightIntent)
        }

        // Call MapPopUpActivity during initialization
        launchMapPopUpActivity()
    }

    /**
     * Launches MapPopUpActivity as a separate activity
     */
    private fun launchMapPopUpActivity() {
        try {
            val mapPopUpIntent = Intent(this, MapPopUpActivity::class.java)
            startActivity(mapPopUpIntent)
        } catch (e: Exception) {
            Log.e("MapActivity", "Error launching MapPopUpActivity: ${e.message}")
            e.printStackTrace()
        }
    }

}
