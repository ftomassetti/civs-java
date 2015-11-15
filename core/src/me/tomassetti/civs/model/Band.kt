package me.tomassetti.civs.model

import com.badlogic.gdx.maps.tiled.TiledMapTile

public data class WorldSize(val width: Int, val height: Int)

public data class Position(val x: Int, val y:Int, val worldSize:WorldSize)

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

public data class Band(var position:Position, val name:String) {

    fun move(dx: Int, dy: Int) {
        position = move(position, dx, dy)
    }
}
