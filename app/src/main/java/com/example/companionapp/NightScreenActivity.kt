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

    private var currentRound = 1
    private val playerTurnCounts = mutableMapOf<String, Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_night_screen)

        // Retrieve data
        val players = intent.getStringArrayListExtra("players") ?: arrayListOf()
        val colors = intent.getStringArrayListExtra("colors") ?: arrayListOf()
        val playerItems = intent.getSerializableExtra("playerItems") as? HashMap<String, ArrayList<String>>
        val escapedPlayerItems = intent.getSerializableExtra("escapedPlayerItems") as? HashMap<String, Int> ?: hashMapOf()
        val passedTurnCounts = intent.getSerializableExtra("playerTurnCounts") as? HashMap<String, Int> ?: hashMapOf()
        currentRound = intent.getIntExtra("currentRound", 1)

        playerTurnCounts.putAll(passedTurnCounts)

        // Update night title and round display
        val nightTitle = findViewById<TextView>(R.id.night_title)
        nightTitle.text = "Night ${intent.getIntExtra("currentNight", 1)}"

        val roundDisplay = findViewById<TextView>(R.id.round_display)
        roundDisplay.text = "Round $currentRound"

        // Populate players dynamically
        val playerContainer = findViewById<LinearLayout>(R.id.player_container)
        playerContainer.removeAllViews()

        val playerStatus = intent.getSerializableExtra("playerStatus") as? HashMap<String, Boolean> ?: hashMapOf()
        players.forEach { playerStatus.putIfAbsent(it, false) }

        for (i in players.indices) {
            val playerView = TextView(this)
            val turnCount = playerTurnCounts[players[i]] ?: 0
            val score = escapedPlayerItems[players[i]] ?: 0
            playerView.text = "${players[i]} - Score: $score, Turns: $turnCount"
            playerView.setBackgroundColor(Color.parseColor(getColorFromName(colors[i])))
            playerView.setTextColor(Color.BLACK)
            playerView.textSize = 18f
            playerView.setPadding(16, 16, 16, 16)

            if (playerStatus[players[i]] == true || currentRound > 7) {
                playerView.isEnabled = false
            } else {
                playerView.setOnClickListener {
                    val intent = Intent(this, PlayerTurnActivity::class.java).apply {
                        putExtra("playerName", players[i])
                        putStringArrayListExtra("playerItems", playerItems?.get(players[i]) ?: arrayListOf())
                        putStringArrayListExtra("players", ArrayList(players))
                        putStringArrayListExtra("colors", ArrayList(colors))
                        putExtra("playerItemsMap", HashMap(playerItems))
                        putExtra("escapedPlayerItems", HashMap(escapedPlayerItems))
                        putExtra("playerStatus", HashMap(playerStatus))
                        putExtra("playerTurnCounts", HashMap(playerTurnCounts))
                        putExtra("currentNight", intent.getIntExtra("currentNight", 1))
                        putExtra("currentRound", currentRound)
                    }
                    startActivity(intent)
                    finish()
                }
            }
            playerContainer.addView(playerView)
        }

        // End Round button
        val endRoundButton = findViewById<Button>(R.id.end_round_button)
        endRoundButton.setOnClickListener {
            if (currentRound < 7) {
                currentRound++
                val intent = Intent(this, NightScreenActivity::class.java).apply {
                    putStringArrayListExtra("players", ArrayList(players))
                    putStringArrayListExtra("colors", ArrayList(colors))
                    putExtra("playerItems", HashMap(playerItems))
                    putExtra("escapedPlayerItems", HashMap(escapedPlayerItems))
                    putExtra("playerStatus", HashMap(playerStatus))
                    putExtra("playerTurnCounts", HashMap(playerTurnCounts))
                    putExtra("currentNight", intent.getIntExtra("currentNight", 1))
                    putExtra("currentRound", currentRound)
                }
                startActivity(intent)
                finish()
            }
        }

        if (currentRound >= 7) {
            endRoundButton.isEnabled = false
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

        // End Night button
        findViewById<Button>(R.id.end_night_button).setOnClickListener {
            val intent = Intent(this, EndNightActivity::class.java).apply {
                putExtra("currentNight", intent.getIntExtra("currentNight", 1))
                putStringArrayListExtra("players", ArrayList(players))
                putStringArrayListExtra("colors", ArrayList(colors))
                putExtra("escapedPlayerItems", HashMap(escapedPlayerItems)) //score
                putExtra("playerTurnCounts", HashMap(playerTurnCounts)) //turns
            }
            startActivity(intent)
            finish()
        }
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
