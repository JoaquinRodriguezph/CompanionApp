package com.example.companionapp

import android.os.Bundle
import android.util.DisplayMetrics
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


        // Get playerItems from intent
        val playerItems = intent.getSerializableExtra("playerItems") as? HashMap<String, ArrayList<String>>

        // Initialize the grid if not already done
        MapManager.instance.initializeGrid(playerItems)


        // Render the grid
        val gridView: GridView = findViewById(R.id.mapGridView)

        gridView.post {
            adjustGridView(gridView)
        }
        Log.d("MapPopUpActivity", "Player Items: $playerItems")
        // Pass playerItems to the GridAdapter
        val gridAdapter = GridAdapter(this, MapManager.instance.getGrid(), playerItems ?: hashMapOf())
        gridView.adapter = gridAdapter
    }

    private fun adjustGridView(gridView: GridView) {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)

        val screenWidth = displayMetrics.widthPixels
        val screenHeight = displayMetrics.heightPixels

        val numColumns = 15 // Fixed number of columns
        val tileSize = if (resources.configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE) {
            // For landscape, adjust to fit within height while keeping tiles square
            Math.min(screenWidth / numColumns, screenHeight / numColumns)
        } else {
            // For portrait, fit within width
            screenWidth / numColumns
        }

        gridView.numColumns = numColumns
        gridView.columnWidth = tileSize
        gridView.stretchMode = GridView.STRETCH_COLUMN_WIDTH
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed() // Go back to the previous activity
        return true
    }
}
