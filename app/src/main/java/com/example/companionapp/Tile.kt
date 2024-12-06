package com.example.companionapp

data class Tile(
    val x: Int,
    val y: Int,
    var type: TileType,
    var hasDoor: Boolean = false,
    var item: String? = null
)

enum class TileType {
    WALL, UNPLACABLE, WALKABLE
}
