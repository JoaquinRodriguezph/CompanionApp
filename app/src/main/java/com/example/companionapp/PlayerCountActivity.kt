package com.example.companionapp

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class PlayerCountActivity : AppCompatActivity() {

    private val selectedColors = mutableMapOf<Int, String>() // Map player index to selected color
    private val availableColors = mutableListOf("Red", "Blue", "Green", "Yellow")
    private val playerNames = listOf("Player 1", "Player 2", "Player 3", "Player 4") // Default player names
    private val selectedPlayers = mutableListOf<String>() // List of selected players

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player_count)

        val checkboxes = listOf(
            findViewById<CheckBox>(R.id.checkbox1),
            findViewById<CheckBox>(R.id.checkbox2),
            findViewById<CheckBox>(R.id.checkbox3),
            findViewById<CheckBox>(R.id.checkbox4)
        )

        val confirmButton = findViewById<Button>(R.id.confirm_button)

        checkboxes.forEachIndexed { index, checkbox ->
            checkbox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    val playerName = playerNames[index]
                    showColorPicker(index, checkbox)
                    selectedPlayers.add(playerName)
                } else {
                    val playerName = playerNames[index]
                    selectedPlayers.remove(playerName)
                    val removedColor = selectedColors.remove(index)
                    removedColor?.let { availableColors.add(it) }
                    checkbox.setBackgroundColor(Color.TRANSPARENT)
                }
                updateConfirmButtonState(confirmButton)
            }
        }

        confirmButton.setOnClickListener {
            if (selectedPlayers.size >= 3) {
                // Proceed to item selection
                val intent = Intent(this, ItemSelectionActivity::class.java).apply {
                    putStringArrayListExtra("players", ArrayList(selectedPlayers))
                    putStringArrayListExtra("colors", ArrayList(selectedColors.values))
                }
                startActivity(intent)
            } else {
                Toast.makeText(this, "At least 3 players must be selected!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showColorPicker(playerIndex: Int, checkbox: CheckBox) {
        val colorOptions = availableColors.toTypedArray()

        if (colorOptions.isEmpty()) {
            Toast.makeText(this, "No colors available!", Toast.LENGTH_SHORT).show()
            checkbox.isChecked = false
            return
        }

        AlertDialog.Builder(this)
            .setTitle("Pick a color")
            .setItems(colorOptions) { _, which ->
                val selectedColor = colorOptions[which]
                selectedColors[playerIndex] = selectedColor
                availableColors.remove(selectedColor)
                checkbox.setBackgroundColor(getColorFromName(selectedColor))
            }
            .setOnCancelListener {
                checkbox.isChecked = false
            }
            .show()
    }

    private fun getColorFromName(colorName: String): Int {
        return when (colorName) {
            "Red" -> Color.RED
            "Blue" -> Color.BLUE
            "Green" -> Color.GREEN
            "Yellow" -> Color.YELLOW
            else -> Color.TRANSPARENT
        }
    }

    private fun updateConfirmButtonState(confirmButton: Button) {
        confirmButton.isEnabled = selectedPlayers.size >= 3
    }
}
