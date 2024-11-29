package com.example.companionapp

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class CalculatorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculator)

        // Enable the back button in the toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Calculator"

        // Input fields and buttons
        val input1 = findViewById<EditText>(R.id.input_number1)
        val input2 = findViewById<EditText>(R.id.input_number2)
        val addButton = findViewById<Button>(R.id.button_add)
        val subtractButton = findViewById<Button>(R.id.button_subtract)
        val resultText = findViewById<TextView>(R.id.result_text)

        // Add button click listener
        addButton.setOnClickListener {
            val num1 = input1.text.toString().toDoubleOrNull()
            val num2 = input2.text.toString().toDoubleOrNull()
            if (num1 != null && num2 != null) {
                val result = num1 + num2
                resultText.text = "Result: $result"
            } else {
                resultText.text = "Invalid input"
            }
        }

        // Subtract button click listener
        subtractButton.setOnClickListener {
            val num1 = input1.text.toString().toDoubleOrNull()
            val num2 = input2.text.toString().toDoubleOrNull()
            if (num1 != null && num2 != null) {
                val result = num1 - num2
                resultText.text = "Result: $result"
            } else {
                resultText.text = "Invalid input"
            }
        }
    }

    // Handle back button press in toolbar
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed() // Go back to the previous activity
        return true
    }
}
