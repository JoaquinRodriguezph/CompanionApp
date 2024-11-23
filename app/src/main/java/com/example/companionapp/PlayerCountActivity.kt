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
                    // Show color picker when a box is checked
                    showColorPicker(index, checkbox)
                } else {
                    // Remove player color selection if unchecked
                    val removedColor = selectedColors.remove(index)
                    removedColor?.let { availableColors.add(it) }
                    checkbox.setBackgroundColor(Color.TRANSPARENT)
                }
                // Update Confirm Button state
                updateConfirmButtonState(confirmButton)
            }
        }

        confirmButton.setOnClickListener {
            if (selectedColors.size >= 3) {
                // Prepare data for next activity
                val selectedPlayers = selectedColors.keys.map { playerNames[it] }
                val selectedPlayerColors = selectedColors.values.toList()

                // Proceed to the next activity
                val intent = Intent(this, MapActivity::class.java).apply {
                    putStringArrayListExtra("players", ArrayList(selectedPlayers))
                    putStringArrayListExtra("colors", ArrayList(selectedPlayerColors))
                }
                startActivity(intent)
            } else {
                Toast.makeText(this, "At least 3 players need to pick a color!", Toast.LENGTH_SHORT).show()
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

                // Assign color to player and remove from availability
                selectedColors[playerIndex] = selectedColor
                availableColors.remove(selectedColor)

                // Set the checkbox background to the selected color
                checkbox.setBackgroundColor(getColorFromName(selectedColor))

                // Update Confirm Button state
                updateConfirmButtonState(findViewById(R.id.confirm_button))
            }
            .setOnCancelListener {
                // Reset checkbox if dialog is canceled
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
        // Enable Confirm Button only if 3 or more players are checked and have colors
        confirmButton.isEnabled = selectedColors.size >= 3
    }
}
