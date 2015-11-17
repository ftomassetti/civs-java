package me.tomassetti.civs.simulation

import me.tomassetti.civs.model.Band
import org.worldengine.world.World
import java.util.*
import java.util.concurrent.locks.ReentrantLock
import kotlin.*
import kotlin.concurrent.*

object Simulation {
    var turn: Int = 0
    private var bands = ArrayList<Band>()
    var world : World? = null
    var random : Random = Random(1)
    private var nextBandId : Int = 0
    private val bandsLock = ReentrantLock()

    fun nextTurn() {
        turn += 1
    }

    fun bandsCopy() : List<Band> {
        bandsLock.lock()
        val copy = ArrayList(bands)
        bandsLock.unlock()
        return copy
    }

    fun nBandsAlive() : Int {
        bandsLock.lock()
        var t = 0
        bands.forEach { b -> if (b.isAlive()) t++ }
        bandsLock.unlock()
        return t
    }

    fun totalPopulation() : Int {
        var t = 0
        bandsLock.lock()
        bands.forEach { b -> t += b.population.total() }
        bandsLock.unlock()
        return t
    }

    fun addBand(band : Band) {
        bandsLock.lock()
        bands.add(band)
        bandsLock.unlock()
    }

    fun nextBand(): Int {
        nextBandId++
        return nextBandId
    }

}