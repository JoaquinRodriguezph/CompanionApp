package com.example.companionapp

import android.content.Intent
import android.os.Bundle
import android.widget.CheckBox
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class PlayerCountActivity : AppCompatActivity() {

    private lateinit var confirmButton: Button
    private lateinit var checkboxes: List<CheckBox>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player_count)

        // Initialize views
        confirmButton = findViewById(R.id.confirm_button)
        checkboxes = listOf(
            findViewById(R.id.checkbox1),
            findViewById(R.id.checkbox2),
            findViewById(R.id.checkbox3),
            findViewById(R.id.checkbox4)
        )

        // Track changes to checkboxes
        checkboxes.forEach { checkbox ->
            checkbox.setOnCheckedChangeListener { _, _ -> updateConfirmButtonState() }
        }

        // Confirm button click
        confirmButton.setOnClickListener {
            // Go to the next screen (empty activity for now)
            val intent = Intent(this, MapActivity::class.java)
            startActivity(intent)
        }
    }

    private fun updateConfirmButtonState() {
        // Count the number of checked checkboxes
        val checkedCount = checkboxes.count { it.isChecked }

        // Enable the confirm button if at least 3 checkboxes are checked
        confirmButton.isEnabled = checkedCount >= 3
    }
}
