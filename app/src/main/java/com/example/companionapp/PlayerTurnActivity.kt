package com.example.companionapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class PlayerTurnActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player_turn)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("PlayerTurnPrefs", Context.MODE_PRIVATE)

        // Get player data from the intent
        val playerName = intent.getStringExtra("playerName") ?: "Player"
        val playerItems = intent.getStringArrayListExtra("playerItems") ?: arrayListOf()
        val players = intent.getStringArrayListExtra("players") ?: arrayListOf()
        val colors = intent.getStringArrayListExtra("colors") ?: arrayListOf()
        val playerItemsMap = intent.getSerializableExtra("playerItemsMap") as? HashMap<String, ArrayList<String>>
        val escapedPlayerItems = intent.getSerializableExtra("escapedPlayerItems") as? HashMap<String, Int> ?: hashMapOf()
        val currentNight = intent.getIntExtra("currentNight", 1)
        // Calculate score for the player
        val totalPoints = calculateScore(playerItems)
        escapedPlayerItems[playerName] = totalPoints

        println("Player Name: $playerName")
        println("Player Items: $playerItems")
        println("Player Score: $totalPoints")
        println("Updated Escaped Players: $escapedPlayerItems")

        // Set player title
        val playerTitle = findViewById<TextView>(R.id.player_title)
        playerTitle.text = playerName

        // Track selected items
        val selectedItems = mutableListOf<String>()

        // Display items as checkboxes
        val itemContainer = findViewById<LinearLayout>(R.id.item_container)
        playerItems.forEach { item ->
            val checkBox = CheckBox(this)
            checkBox.text = item
            checkBox.isChecked = getCheckboxState(playerName, item) // Restore checkbox state
            if (checkBox.isChecked) {
                selectedItems.add(item) // Add pre-checked items to selectedItems
            }
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                saveCheckboxState(playerName, item, isChecked) // Save checkbox state
                if (isChecked) {
                    selectedItems.add(item)
                } else {
                    selectedItems.remove(item)
                }
            }
            itemContainer.addView(checkBox)
        }

        // Escape button
        findViewById<Button>(R.id.escape_button).setOnClickListener {
            val totalPoints = calculateScore(selectedItems)
            escapedPlayerItems[playerName] = totalPoints // Record escaped player's score
            println("Updated escapedPlayerItems: $escapedPlayerItems")


            val intent = Intent(this, NightScreenActivity::class.java).apply {
                putStringArrayListExtra("players", ArrayList(players))
                putStringArrayListExtra("colors", ArrayList(colors))
                putExtra("playerItems", HashMap(playerItemsMap))
                putExtra("escapedPlayerItems", HashMap(escapedPlayerItems))
                putExtra("currentNight", currentNight)
            }
            startActivity(intent)
            finish()
        }

        // End Turn button remains unchanged
        findViewById<Button>(R.id.end_turn_button).setOnClickListener {
            val intent = Intent(this, NightScreenActivity::class.java).apply {
                putStringArrayListExtra("players", ArrayList(players))
                putStringArrayListExtra("colors", ArrayList(colors))
                putExtra("playerItems", HashMap(playerItemsMap))
                putExtra("escapedPlayerItems", HashMap(escapedPlayerItems))
                putExtra("currentNight", currentNight)
            }
            startActivity(intent)
            finish()
        }
    }

    // Save checkbox state to SharedPreferences
    private fun saveCheckboxState(playerName: String, item: String, isChecked: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("${playerName}_$item", isChecked)
        editor.apply()
    }

    // Retrieve checkbox state from SharedPreferences
    private fun getCheckboxState(playerName: String, item: String): Boolean {
        return sharedPreferences.getBoolean("${playerName}_$item", false)
    }

    // Calculate score for selected items
    private fun calculateScore(items: List<String>): Int {
        val itemScores = mapOf(
            // Bronze
            "Bronze: Toothbrush" to 1, "Bronze: Mug" to 3, "Bronze: Umbrella" to 2, "Bronze: Slippers" to 1,
            "Bronze: Sunglasses" to 3, "Bronze: Saltshaker" to 1,
            // Silver
            "Silver: Console" to 6, "Silver: Sneakers" to 5, "Silver: Telephone" to 5, "Silver: Autograph" to 6,
            "Silver: Fancy Underwear" to 4, "Silver: Watch" to 4, "Silver: Gold Earrings" to 5,
            // Gold
            "Gold: Gold Bars" to 10, "Gold: Guitar" to 8, "Gold: TV" to 9, "Gold: Smart Fridge" to 10,
            "Gold: Vinyl Record" to 7, "Gold: Massage Chair" to 9, "Gold: Painting" to 7
        )
        val total = items.sumOf { itemScores[it] ?: 0 }
        println("Calculating score for items: $items -> Total: $total")
        return total
    }
}
