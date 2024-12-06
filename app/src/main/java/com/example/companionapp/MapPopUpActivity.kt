package com.example.companionapp

import android.os.Bundle
import android.util.Log
import android.widget.GridView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class MapPopUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_popup)

        // Set up the Toolbar as the ActionBar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Enable the back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Map"

        // Initialize the grid if not already done
        MapManager.instance.initializeGrid()

        // Render the grid
        val gridView: GridView = findViewById(R.id.mapGridView)
        val gridAdapter = GridAdapter(this, MapManager.instance.getGrid())
        gridView.adapter = gridAdapter
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed() // Go back to the previous activity
        return true
    }
}
