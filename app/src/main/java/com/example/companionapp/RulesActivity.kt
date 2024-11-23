package com.example.companionapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class RulesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rules)

        // Enable the back button in the toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Game Rules"

        // Handle back button press in toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed() // Go back to the previous activity
        return true
    }
}
