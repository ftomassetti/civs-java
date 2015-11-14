package me.tomassetti.tilenav

import com.badlogic.gdx.maps.tiled.TiledMapTile

public data class WorldSize(width: Int, height: Int) {
    val width: Int = width
    val height: Int = height
}

public data class Position(x: Int, y:Int, worldSize:WorldSize) {
    val x = x
    val y = y
    val worldSize = worldSize
}

fun move(p: Position, dx: Int, dy: Int) : Position {
    var nx = (p.x + dx)
    while (nx < 0) {
        nx += p.worldSize.width
    }
    while (nx >= p.worldSize.width) {
        nx -= p.worldSize.width
    }
    var ny = (p.y + dy)
    while (ny < 0) {
        ny += p.worldSize.height
    }
    while (ny >= p.worldSize.height) {
        ny -= p.worldSize.height
    }
    return Position(nx, ny, p.worldSize)
}

public data class Band(position:Position) {
    var position = position

    fun move(dx: Int, dy: Int) {
        position = move(position, dx, dy)
    }
}
