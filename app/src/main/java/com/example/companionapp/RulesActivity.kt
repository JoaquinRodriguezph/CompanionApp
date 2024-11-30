package com.example.companionapp

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat

class RulesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rules)

        // Enable the back button in the toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Game Rules"

        // Set the FAQ content
        val rulesTextView: TextView = findViewById(R.id.rules_placeholder)
        rulesTextView.text = HtmlCompat.fromHtml(getFAQs(), HtmlCompat.FROM_HTML_MODE_LEGACY)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed() // Go back to the previous activity
        return true
    }

    // Function to return the FAQs as a formatted string
    private fun getFAQs(): String {
        return """
        <b>How many players can play?</b><br>
        3 or 4 players<br><br>
        
        <b>How do I win the game?</b><br>
        The player with the most points at the end of 3 nights is declared the winner. Points are earned by successfully stealing items listed on your checklist.<br><br>
        
        <b>What happens if 2 players tie in points at the end of the game?</b><br>
        The companion app determines the ranking based on the fewest turns used throughout the game.<br><br>
        
        <b>Can multiple players start at the same location?</b><br>
        Yes, players can overlap and start at the same corner of the board.<br><br>
        
        <b>Do I have to steal items in the exact order of my "To Steal List"?</b><br>
        No, items can be stolen in any order, as long as they are on your checklist and you meet the energy requirements to steal.<br><br>
        
        <b>Can I skip my turn if I don’t want to move or take any action?</b><br>
        You can choose to skip actions or movements, but rolling the dice at the start of your turn is mandatory.<br><br>
        
        <b>What happens if I run out of energy during my turn?</b><br>
        If you run out of energy, you cannot take further actions or move until your next turn when you roll for new energy.<br><br>
        
        <b>What does the companion app do?</b><br>
        The app handles various aspects of the game:<br>
        - Map generation for each night.<br>
        - Checklist input and tracking.<br>
        - Automatic tiebreaker calculations.<br><br>
        
        <b>Can I play an action card on the same turn I steal an item?</b><br>
        Yes, you can take up to 2 actions on your turn. For example, you can move, steal an item, and then play an action card if you have enough energy. You may also move as much as you want as long as you have sufficient energy throughout your turn since moving is not counted as an action.<br><br>
        
        <b>Can action cards target players who have already escaped the house?</b><br>
        Some action cards, like Slash, cannot target escaped players. Be sure to read each card’s rules carefully.<br><br>
        
        <b>What happens when the Snitch card is played?</b><br>
        The night ends immediately, and any players still inside the house are arrested (even the card user), preventing them from escaping or completing further actions for that night unless they use a disable card.<br><br>
        
        <b>What is the energy bank used for?</b><br>
        The energy bank allows you to store leftover energy (maximum of 12) for use in future turns, providing flexibility for strategic planning.<br><br>
        
        <b>How is my steal checklist formed?</b><br>
        At the start of each night, you refresh your hands with 4 random item cards and 3 action cards. Those 4 item cards makeup your steal checklist.<br><br>
        
        <b>Can I lose the game entirely?</b><br>
        No, even if you are arrested or perform poorly, you remain in the game for all three nights. However, players with the most points at the end of the game win.
    """.trimIndent()
    }
}
