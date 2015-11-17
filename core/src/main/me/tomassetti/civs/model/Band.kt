package me.tomassetti.civs.model

import java.util.ArrayList

public data class Population(val childrenMale: Int, val childrenFemale: Int,
                             val adultMale: Int, val adultFemale: Int,
                             val oldMale: Int, val oldFemale: Int) {
    fun total() : Int = childrenMale + childrenFemale + adultMale + adultFemale + oldMale + oldFemale
    fun extinguished() : Boolean = total() == 0
}

public data class WorldSize(val width: Int, val height: Int)

public data class Position(val x: Int, val y:Int, val worldSize:WorldSize) {
    fun around(includeSelf:Boolean) : List<Position> {
        val res = ArrayList<Position>()
        res.add(move(this, -1, -1))
        res.add(move(this,  0, -1))
        res.add(move(this,  1, -1))
        res.add(move(this, -1,  0))
        if (includeSelf) {
            res.add(this)
        }
        res.add(move(this,  1,  0))
        res.add(move(this, -1,  1))
        res.add(move(this,  0,  1))
        res.add(move(this,  1,  1))
        return res
    }
}

public class Band(val id:Int, val name:String, var position:Position, var population:Population) {

    fun move(dx: Int, dy: Int) {
        position = move(position, dx, dy)
    }

    override fun equals(other: Any?): Boolean {
        if (other is Band) {
            return other.id == this.id
        } else {
            return false
        }
    }

    override fun hashCode(): Int {
        return id
    }

    override fun toString(): String {
        return "Band " + name
    }

    fun isAlive(): Boolean {
        return !population.extinguished()
    }
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