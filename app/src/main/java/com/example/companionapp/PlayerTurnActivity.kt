package com.example.companionapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class PlayerTurnActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var vibrator: Vibrator
    private lateinit var timerTextView: TextView
    private var remainingTime: Long = 15000 // 15 seconds in milliseconds
    private var countDownTimer: CountDownTimer? = null // Keep a reference to the timer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player_turn)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("PlayerTurnPrefs", Context.MODE_PRIVATE)
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        // Initialize the timer text view
        timerTextView = findViewById(R.id.timer_text)
        startCountdown()

        // Get player data from the intent
        val playerName = intent.getStringExtra("playerName") ?: "Player"
        val playerItems = intent.getStringArrayListExtra("playerItems") ?: arrayListOf()
        val players = intent.getStringArrayListExtra("players") ?: arrayListOf()
        val colors = intent.getStringArrayListExtra("colors") ?: arrayListOf()
        val playerItemsMap = intent.getSerializableExtra("playerItemsMap") as? HashMap<String, ArrayList<String>>
        val escapedPlayerItems = intent.getSerializableExtra("escapedPlayerItems") as? HashMap<String, Int> ?: hashMapOf()
        val playerTurnCounts = intent.getSerializableExtra("playerTurnCounts") as? HashMap<String, Int> ?: hashMapOf()
        val currentNight = intent.getIntExtra("currentNight", 1)
        val currentRound = intent.getIntExtra("currentRound", 1)
        val playerStatus = intent.getSerializableExtra("playerStatus") as? HashMap<String, Boolean> ?: hashMapOf()

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
            val previousScore = escapedPlayerItems[playerName] ?: 0
            escapedPlayerItems[playerName] = previousScore + totalPoints
            playerStatus[playerName] = true // Mark player as escaped
            // Increment turn count
            playerTurnCounts[playerName] = currentRound

            val intent = Intent(this, NightScreenActivity::class.java).apply {
                putStringArrayListExtra("players", ArrayList(players))
                putStringArrayListExtra("colors", ArrayList(colors))
                putExtra("playerItems", HashMap(playerItemsMap))
                putExtra("escapedPlayerItems", HashMap(escapedPlayerItems))
                putExtra("playerStatus", HashMap(playerStatus))
                putExtra("playerTurnCounts", HashMap(playerTurnCounts))
                putExtra("currentNight", currentNight)
                putExtra("currentRound", currentRound) // Pass the current round
            }
            startActivity(intent)
            finish()
        }

        // Enable the back button in the toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Map"

        // Handle back button press in toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun startCountdown() {
        countDownTimer = object : CountDownTimer(remainingTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsLeft = millisUntilFinished / 1000
                timerTextView.text = "Time Left: $secondsLeft"

                // Start vibration at the 10-second mark
                if (secondsLeft <= 5) {
                    vibrateIncreasingly()
                }
            }

            override fun onFinish() {
                // Time's up, navigate to NightTurnActivity
                navigateToNightScreen()
            }
        }
        .start()
    }

    private fun vibrateIncreasingly() {
        // Start vibration with increasing intensity
        if (vibrator.hasVibrator()) {
            val vibrationPattern = longArrayOf(0, 200, 100, 300, 100, 400) // Vibration starts from 200ms, increasing with intervals
            val repeat = -1 // Do not repeat after the final vibration
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createWaveform(vibrationPattern, repeat))
            } else {
                vibrator.vibrate(vibrationPattern, repeat)
            }
        }
    }

    // Cancel the timer when leaving the activity
    override fun onPause() {
        super.onPause()
        countDownTimer?.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.cancel() // Ensure the timer is canceled if the activity is destroyed
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
    override fun onBackPressed() {
        super.onBackPressed()
        navigateToNightScreen()
    }

    override fun onSupportNavigateUp(): Boolean {
        navigateToNightScreen()
        return true
    }

    private fun navigateToNightScreen() {
        val playerName = intent.getStringExtra("playerName") ?: "Player"
        val players = intent.getStringArrayListExtra("players") ?: arrayListOf()
        val colors = intent.getStringArrayListExtra("colors") ?: arrayListOf()
        val playerItemsMap = intent.getSerializableExtra("playerItemsMap") as? HashMap<String, ArrayList<String>>
        val escapedPlayerItems = intent.getSerializableExtra("escapedPlayerItems") as? HashMap<String, Int> ?: hashMapOf()
        val playerTurnCounts = intent.getSerializableExtra("playerTurnCounts") as? HashMap<String, Int> ?: hashMapOf()
        val currentNight = intent.getIntExtra("currentNight", 1)
        val currentRound = intent.getIntExtra("currentRound", 1)


        val intent = Intent(this, NightScreenActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT // Bring existing instance to front
            putStringArrayListExtra("players", ArrayList(players))
            putStringArrayListExtra("colors", ArrayList(colors))
            putExtra("playerItems", HashMap(playerItemsMap))
            putExtra("escapedPlayerItems", HashMap(escapedPlayerItems))
            putExtra("playerTurnCounts", HashMap(playerTurnCounts))
            putExtra("currentNight", currentNight)
            putExtra("currentRound", currentRound)
        }
        startActivity(intent)
        finish()
    }


}
