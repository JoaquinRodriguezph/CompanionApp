package com.example.companionapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class PlayerTurnActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player_turn)

        // Get player data from the intent
        val playerName = intent.getStringExtra("playerName") ?: "Player"
        val playerItems = intent.getStringArrayListExtra("playerItems") ?: arrayListOf()
        val players = intent.getStringArrayListExtra("players") ?: arrayListOf()
        val colors = intent.getStringArrayListExtra("colors") ?: arrayListOf()
        val playerItemsMap = intent.getSerializableExtra("playerItemsMap") as? HashMap<String, ArrayList<String>>
        val currentNight = intent.getIntExtra("currentNight", 1)

        // Set player title
        val playerTitle = findViewById<TextView>(R.id.player_title)
        playerTitle.text = playerName

        // Display items as checkboxes
        val itemContainer = findViewById<LinearLayout>(R.id.item_container)
        playerItems.forEach { item ->
            val checkBox = CheckBox(this)
            checkBox.text = item
            itemContainer.addView(checkBox)
        }

        // End Night button
        findViewById<Button>(R.id.end_night_button).setOnClickListener {
            val intent = Intent(this, NightScreenActivity::class.java).apply {
                putStringArrayListExtra("players", ArrayList(players))
                putStringArrayListExtra("colors", ArrayList(colors))
                putExtra("playerItems", HashMap(playerItemsMap))
                putExtra("currentNight", currentNight)
            }
            startActivity(intent)
            finish()
        }

        // End Turn button
        findViewById<Button>(R.id.end_turn_button).setOnClickListener {
            val intent = Intent(this, NightScreenActivity::class.java).apply {
                putStringArrayListExtra("players", ArrayList(players))
                putStringArrayListExtra("colors", ArrayList(colors))
                putExtra("playerItems", HashMap(playerItemsMap))
                putExtra("currentNight", currentNight)
            }
            startActivity(intent)
            finish()
        }
    }
}
