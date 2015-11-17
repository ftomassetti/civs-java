package me.tomassetti.civs.simulation

import me.tomassetti.civs.model.Band
import org.worldengine.world.World
import java.util.*
import kotlin.*

object Simulation {
    var turn: Int = 0
    var bands = ArrayList<Band>()
    var world : World? = null

    fun nextTurn() {
        turn += 1
    }

    fun nBandsAlive() : Int {
        var t = 0
        bands.forEach { b -> if (b.isAlive()) t++ }
        return t
    }

    fun totalPopulation() : Int {
        var t = 0
        bands.forEach { b -> t += b.population.total() }
        return t
    }

}