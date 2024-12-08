package com.example.companionapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MapActivity : AppCompatActivity() {
    private var currentRound = 1
    private val playerTurnCounts = hashMapOf<String, Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)


        // Retrieve data from the Intent
        val players = intent.getStringArrayListExtra("players")
        val colors = intent.getStringArrayListExtra("colors")
        val playerItems = intent.getSerializableExtra("playerItems") as? HashMap<String, ArrayList<String>>
        val escapedPlayerItems = intent.getSerializableExtra("escapedPlayerItems") as? HashMap<String, Int> ?: hashMapOf()
        val passedTurnCounts = intent.getSerializableExtra("playerTurnCounts") as? HashMap<String, Int> ?: hashMapOf()
        currentRound = intent.getIntExtra("currentRound", 1)
        val currentNight = intent.getIntExtra("currentNight", 1)
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

        val rootView = findViewById<View>(android.R.id.content)
        rootView.setOnClickListener {
            // Launch NightScreenActivity on click
            val nightIntent = Intent(this, NightScreenActivity::class.java).apply {
                putStringArrayListExtra("players", ArrayList(selectedPlayers)) // Pass selected players
                putStringArrayListExtra("colors", ArrayList(selectedColors))  // Pass corresponding colors
                putExtra("playerItems", selectedPlayerItems)
                putExtra("escapedPlayerItems", HashMap(escapedPlayerItems)) // Pass scores
                putExtra("playerTurnCounts", HashMap(playerTurnCounts)) // Pass turn count
                putExtra("currentNight", intent.getIntExtra("currentNight", 1)) // Increment night count// Pass items for each player
            }
            startActivity(nightIntent)
        }
        Log.d("MapActivity", "LaunchingActivity Player Items: $playerItems")
        // Call MapPopUpActivity during initialization
        //launchMapPopUpActivity()
        if (currentNight>1){
            MapManager.instance.regenerateDoorsAndItems(playerItems)
        }

        try {
            val mapPopUpIntent = Intent(this, MapPopUpActivity::class.java).apply {
                putExtra("playerItems", playerItems)
            }
            startActivity(mapPopUpIntent)
        } catch (e: Exception) {
            Log.e("MapActivity", "Error launching MapPopUpActivity: ${e.message}")
            e.printStackTrace()
        }
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
