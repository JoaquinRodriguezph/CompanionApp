package com.example.companionapp

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.ImageView

class GridAdapter(
    private val context: Context,
    private val grid: Array<Array<Tile>>,
    private val playerItems: HashMap<String, ArrayList<String>> // Pass playerItems here
) : BaseAdapter() {

    private val itemDrawables = mapOf(
        "Autograph" to R.drawable.autograph,
        "Console" to R.drawable.console,
        "Gold Earrings" to R.drawable.earrings,
        "Guitar" to R.drawable.electricguitar,
        "Fancy Underwear" to R.drawable.fancy_underwear,
        "Gold Bars" to R.drawable.goldbars,
        "Massage Chair" to R.drawable.massagechair,
        "Mug" to R.drawable.mug,
        "Painting" to R.drawable.painting,
        "Saltshaker" to R.drawable.saltshaker,
        "Slippers" to R.drawable.slippers,
        "Smart Fridge" to R.drawable.smartfridge,
        "Sneakers" to R.drawable.sneakers,
        "Sunglasses" to R.drawable.sunglasses,
        "Telephone" to R.drawable.telephone,
        "Toothbrush" to R.drawable.toothbrush,
        "TV" to R.drawable.tv,
        "Umbrella" to R.drawable.umbrella,
        "Vinyl Record" to R.drawable.vinyl,
        "Watch" to R.drawable.watch
    )

    init {
        // Debug: Print playerItems
        Log.d("ItemIntent", "Player Items: $playerItems")

        // Flatten and shuffle items
        val allItems = playerItems.values.flatten().shuffled()

        // Debug: Print the flattened list of all items
        Log.d("ItemIntent", "Shuffled Items: $allItems")

        var itemIndex = 0

//        for (row in grid) {
//            for (tile in row) {
//                if (itemIndex < allItems.size) {
//                    tile.item = allItems[itemIndex]
//                    Log.w("ItemIntent", "Placing: ${tile.item}")
//                    itemIndex++
//                } else {
//                    // Debug: Log if the grid has more tiles than items
//                    Log.w("GridAdapter", "Ran out of items to assign. Extra tiles remain unassigned.")
//                    break
//                }
//            }
//        }
    }

    override fun getCount(): Int = grid.size * grid[0].size

    override fun getItem(position: Int): Tile {
        val row = position / grid[0].size
        val col = position % grid[0].size
        return grid[row][col]
    }

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val tile = getItem(position)
        val imageView = convertView as? ImageView ?: ImageView(context).apply {
            scaleType = ImageView.ScaleType.CENTER_CROP
        }

        val gridView = parent as GridView
        val tileSize = gridView.columnWidth
        imageView.layoutParams = ViewGroup.LayoutParams(tileSize, tileSize)

        // Set the appropriate image based on the tile's state
        imageView.setImageResource(
            when {
                tile.hasDoor -> R.drawable.door_icon
                tile.type == TileType.WALL -> R.drawable.wall_icon
                tile.item != null -> itemDrawables[tile.item] ?: R.drawable.item_icon // Display item if present
                else -> R.drawable.walkable_icon
            }
        )

        imageView.setBackgroundColor(android.graphics.Color.parseColor("#7f6669"))
        // Debug: Log the item assigned to the tile
        Log.d("GridAdapter", "Tile at position $position: item=${tile.item}, type=${tile.type}, hasDoor=${tile.hasDoor}")

        return imageView
    }
}
