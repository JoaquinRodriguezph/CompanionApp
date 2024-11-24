import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.companionapp.R

class PlayerAdapter(
    private val players: List<String>,
    private val colors: List<String>,
    private val playerItems: MutableMap<String, ArrayList<String>>,
    private val items: Map<String, List<String>>, // Map of available items by category
    private val onItemSelected: () -> Unit // Callback to trigger when items are updated
) : RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.player_item, parent, false)
        return PlayerViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        val playerName = players[position]
        val playerColor = colors[position]

        holder.bind(playerName, playerColor) {
            // Show item selection dialog for this player
            showItemSelectionDialog(holder.itemView.context, playerName)
        }
    }

    override fun getItemCount(): Int = players.size

    private fun showItemSelectionDialog(context: Context, playerName: String) {
        val selectedItems = playerItems[playerName] ?: ArrayList()
        val allItems = items.flatMap { (category, itemList) ->
            itemList.map { "$category: $it" }
        }.toTypedArray()

        val checkedItems = BooleanArray(allItems.size) { selectedItems.contains(allItems[it]) }

        AlertDialog.Builder(context)
            .setTitle("Select 4 items for $playerName")
            .setMultiChoiceItems(allItems, checkedItems) { _, which, isChecked ->
                if (isChecked) {
                    if (selectedItems.size < 4) {
                        selectedItems.add(allItems[which])
                    } else {
                        Toast.makeText(context, "You can only select 4 items!", Toast.LENGTH_SHORT).show()
                        checkedItems[which] = false // Uncheck if limit exceeded
                    }
                } else {
                    selectedItems.remove(allItems[which])
                }
            }
            .setPositiveButton("Confirm") { _, _ ->
                if (selectedItems.size == 4) {
                    playerItems[playerName] = selectedItems
                    onItemSelected()
                } else {
                    Toast.makeText(context, "$playerName must pick exactly 4 items!", Toast.LENGTH_SHORT).show()
                }
            }
            .setCancelable(false)
            .show()
    }

    class PlayerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val playerNameView: TextView = itemView.findViewById(R.id.player_name)
        private val playerColorBox: View = itemView.findViewById(R.id.player_color_box)

        fun bind(playerName: String, playerColor: String, onClick: () -> Unit) {
            playerNameView.text = playerName
            playerColorBox.setBackgroundColor(getColorFromName(playerColor)) // Set box color dynamically
            itemView.setOnClickListener { onClick() }
        }

        private fun getColorFromName(colorName: String): Int {
            return when (colorName) {
                "Red" -> android.graphics.Color.RED
                "Blue" -> android.graphics.Color.BLUE
                "Green" -> android.graphics.Color.GREEN
                "Yellow" -> android.graphics.Color.YELLOW
                else -> android.graphics.Color.TRANSPARENT
            }
        }
    }

}
