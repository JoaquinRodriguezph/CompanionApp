package com.example.companionapp

import android.util.Log
import kotlin.math.log
import kotlin.random.Random

class MapManager private constructor() {
    private val grid: Array<Array<Tile>> = Array(12) { x -> Array(15) { y -> Tile(x, y, TileType.UNPLACABLE) } }
    private var isInitialized = false

    companion object {
        val instance: MapManager by lazy { MapManager() }
    }

    fun initializeGrid(playerItems: HashMap<String, ArrayList<String>>?) {
        Log.d("MapManager", "initializeGrid() called")
        if (isInitialized) return // Prevent reinitialization

        // Define rooms and walls
        val room1 = listOf(Pair(0, 10), Pair(1, 10), Pair(2, 10), Pair(3, 10), Pair(3, 11), Pair(3, 12), Pair(3, 13), Pair(3, 14))
        val room2 = listOf(Pair(8, 10), Pair(8, 11), Pair(8, 12), Pair(8, 13), Pair(8, 14), Pair(9, 10), Pair(10, 10), Pair(11, 10))
        val livingRoom = listOf(Pair(5, 0), Pair(5, 1), Pair(5, 2), Pair(5, 3), Pair(5, 4), Pair(5, 5), Pair(5, 6), Pair(5, 7), Pair(5, 8), Pair(6, 8), Pair(7, 8), Pair(8, 8), Pair(9, 8))
        val cr = listOf(Pair(10, 6), Pair(10, 7), Pair(10, 8), Pair(11, 6), Pair(11, 8))

        val rooms = listOf(room1, room2, livingRoom, cr)

        // Mark walls
        rooms.forEach { room ->
            room.forEach { (x, y) -> grid[x][y].type = TileType.WALL }
        }

        // Randomly place doors
        rooms.forEach { room ->
            val doorTile = room.random()
            grid[doorTile.first][doorTile.second].hasDoor = true
        }

        // Mark walkable tiles
        grid.forEach { row ->
            row.forEach { tile ->
                if (tile.type == TileType.UNPLACABLE) {
                    tile.type = TileType.WALKABLE
                }
            }
        }

        // Spawn items
        spawnItems(playerItems)

        isInitialized = true
    }

    private fun spawnItems(playerItems: HashMap<String, ArrayList<String>>?) {

        val walkableTiles = grid.flatten().filter { it.type == TileType.WALKABLE && !it.hasDoor }
        var i = 0
        val allItems = playerItems?.values?.flatten()?.shuffled()?.map { item ->
            // Use regex to extract the portion after the colon and space
            item.replace(".*?:\\s*(.*)".toRegex(), "$1")
        } ?: listOf()  // Safe call and fallback to empty list

        walkableTiles.shuffled().take(16).forEach { tile ->
            if (i < allItems.size) {
                tile.item = allItems[i]
                Log.d("MapManager", "item=${tile.item}")
                i++
            }
        }
    }

    public fun regenerateDoorsAndItems(playerItems: HashMap<String, ArrayList<String>>?) {
        Log.d("MapManager", "regenerateDoorsAndItems() called")

        // Clear existing door positions
        grid.forEach { row ->
            row.forEach { tile ->
                if (tile.hasDoor) {
                    tile.hasDoor = false
                }
            }
        }

        // Define rooms
        val room1 = listOf(Pair(0, 10), Pair(1, 10), Pair(2, 10), Pair(3, 10), Pair(3, 11), Pair(3, 12), Pair(3, 13), Pair(3, 14))
        val room2 = listOf(Pair(8, 10), Pair(8, 11), Pair(8, 12), Pair(8, 13), Pair(8, 14), Pair(9, 10), Pair(10, 10), Pair(11, 10))
        val livingRoom = listOf(Pair(5, 0), Pair(5, 1), Pair(5, 2), Pair(5, 3), Pair(5, 4), Pair(5, 5), Pair(5, 6), Pair(5, 7), Pair(5, 8), Pair(6, 8), Pair(7, 8), Pair(8, 8), Pair(9, 8))
        val cr = listOf(Pair(10, 6), Pair(10, 7), Pair(10, 8), Pair(11, 6), Pair(11, 8))

        val rooms = listOf(room1, room2, livingRoom, cr)

        // Randomly place new doors
        rooms.forEach { room ->
            val doorTile = room.random()
            grid[doorTile.first][doorTile.second].hasDoor = true
        }

        // Clear existing items
        grid.forEach { row ->
            row.forEach { tile ->
                tile.item = null
            }
        }

        // Spawn new items
        spawnItems(playerItems)
    }

    fun getGrid(): Array<Array<Tile>> = grid
}
