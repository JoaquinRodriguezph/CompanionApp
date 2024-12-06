package com.example.companionapp

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView

class GridAdapter(private val context: Context, private val grid: Array<Array<Tile>>) : BaseAdapter() {
    override fun getCount(): Int = grid.size * grid[0].size

    override fun getItem(position: Int): Tile {
        val row = position / grid[0].size
        val col = position % grid[0].size
        return grid[row][col]
    }

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val tile = getItem(position)
        val imageView = convertView as? ImageView ?: ImageView(context)
        imageView.layoutParams = ViewGroup.LayoutParams(100, 100) // Adjust size as needed

        imageView.setImageResource(
            when {
                tile.hasDoor -> R.drawable.door_icon
                tile.type == TileType.WALL -> R.drawable.wall_icon
                tile.item != null -> R.drawable.item_icon
                else -> R.drawable.walkable_icon
            }
        )

        return imageView
    }
}
