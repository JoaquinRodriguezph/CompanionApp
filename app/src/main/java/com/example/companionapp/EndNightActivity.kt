package com.example.companionapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class EndNightActivity : AppCompatActivity() {
    private val playerTurnCounts = mutableMapOf<String, Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_end_night)

        val currentNight = intent.getIntExtra("currentNight", 1)
        val players = intent.getStringArrayListExtra("players") ?: arrayListOf()
        val colors = intent.getStringArrayListExtra("colors") ?: arrayListOf()

        // Retrieve escaped player items (scores)
        val escapedPlayerItems = intent.getSerializableExtra("escapedPlayerItems") as? HashMap<String, Int> ?: hashMapOf()
        val passedTurnCounts = intent.getSerializableExtra("playerTurnCounts") as? HashMap<String, Int> ?: hashMapOf()
        playerTurnCounts.putAll(passedTurnCounts)

        // Generate and display ranks
        val rankTextView = findViewById<TextView>(R.id.rank_text)

        if (escapedPlayerItems.isNotEmpty()) {
            val comparator = Comparator<Map.Entry<String, Int>> { entry1, entry2 ->
                val score1 = entry1.value
                val score2 = entry2.value
                val turnCount1 = passedTurnCounts[entry1.key] ?: 0
                val turnCount2 = passedTurnCounts[entry2.key] ?: 0

                //Returns score placement of two different scores sorted in descending order
                if (score1 != score2) {
                    return@Comparator score2 - score1
                } else { // If there is a tie, returns score placement sorted by turns in ascending order
                    return@Comparator turnCount1 - turnCount2
                }
            }

            // Sort players
            val rankedPlayers = escapedPlayerItems.entries.sortedWith(comparator)

            // Display text rankings
            val rankDisplay = rankedPlayers.joinToString(separator = "\n") { (player, score) ->
                "$player: $score pts (took ${passedTurnCounts[player] ?: 0} turns)"
            }

            rankTextView.text = rankDisplay
        } else {
            rankTextView.text = "No scores to display."
        }

        // Configure the action button
        val nextButton = findViewById<Button>(R.id.action_button)
        if (currentNight < 3) {
            nextButton.text = "Next Night"
            nextButton.setOnClickListener {
                // Move to the next night
                val intent = Intent(this, ItemSelectionForNightActivity::class.java).apply {
                    putExtra("currentNight", currentNight + 1) // Increment night
                    putStringArrayListExtra("players", ArrayList(players)) // Pass players
                    putStringArrayListExtra("colors", ArrayList(colors))   // Pass
                    putExtra("escapedPlayerItems", HashMap(escapedPlayerItems)) // Pass scores
                    putExtra("playerTurnCounts", HashMap(playerTurnCounts)) // Pass turn counts
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
