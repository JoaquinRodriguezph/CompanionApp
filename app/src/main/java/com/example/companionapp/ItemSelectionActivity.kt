package com.example.companionapp

import PlayerAdapter
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ItemSelectionActivity : AppCompatActivity() {

    private val items = mutableMapOf(
        "Bronze" to mutableListOf("Toothbrush", "Mug", "Umbrella", "Slippers", "Sunglasses", "Saltshaker"),
        "Silver" to mutableListOf("Console", "Sneakers", "Telephone", "Autograph", "Fancy Underwear", "Watch", "Gold Earrings"),
        "Gold" to mutableListOf("Gold Bars", "Guitar", "TV", "Smart Fridge", "Vinyl Record", "Massage Chair", "Painting")
    )

    private lateinit var players: List<String>
    private lateinit var colors: List<String>
    private val playerItems = mutableMapOf<String, ArrayList<String>>() // Map player name to selected items

    private lateinit var confirmButton: Button
    private lateinit var playerRecyclerView: RecyclerView
    private lateinit var playerAdapter: PlayerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_selection)

        // Retrieve data from the Intent
        players = intent.getStringArrayListExtra("players") ?: emptyList()
        colors = intent.getStringArrayListExtra("colors") ?: emptyList()

        if (players.isEmpty() || colors.isEmpty() || players.size != colors.size) {
            Toast.makeText(this, "Error: Missing player data!", Toast.LENGTH_SHORT).show()
            finish()
            return
        }


        confirmButton = findViewById(R.id.confirm_button)
        playerRecyclerView = findViewById(R.id.player_recycler_view)

        setupRecyclerView()
        updateConfirmButtonState()

        confirmButton.setOnClickListener {
            proceedToMapActivity()
        }
    }

    private fun setupRecyclerView() {
        playerRecyclerView.layoutManager = LinearLayoutManager(this)
        playerAdapter = PlayerAdapter(
            players,
            colors,
            playerItems,
            items // Pass items map here
        ) { updateConfirmButtonState() }
        playerRecyclerView.adapter = playerAdapter
    }

    private fun updateConfirmButtonState() {
        // Enable confirm button only if all players have selected 4 items
        confirmButton.isEnabled = playerItems.size == players.size && playerItems.values.all { it.size == 4 }
    }

    private fun proceedToMapActivity() {
        val intent = Intent(this, MapActivity::class.java).apply {
            putStringArrayListExtra("players", ArrayList(players)) // Pass players
            putStringArrayListExtra("colors", ArrayList(colors))   // Pass colors
            putExtra("playerItems", HashMap(playerItems))          // Pass playerItems
        }
        startActivity(intent)
        finish()
    }
}
