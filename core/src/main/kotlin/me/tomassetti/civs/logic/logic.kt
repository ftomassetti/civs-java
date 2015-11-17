package me.tomassetti.civs.logic

import me.github.excaliburHisSheath.NameGenerator
import me.tomassetti.civs.model.Band
import me.tomassetti.civs.model.Population
import me.tomassetti.civs.model.Position
import me.tomassetti.civs.model.WorldSize
import me.tomassetti.civs.simulation.Simulation
import org.worldengine.world.World
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.util.*
import me.tomassetti.civs.logic.*
import java.util.stream.Stream

fun namesGenerator() : NameGenerator {
    val file = File("/home/federico/repos/namegen-data/personnames/Italian_male.txt")
    val samples = ArrayList<String>()

    val reader = BufferedReader(FileReader(file))
    reader.lines().forEach { line -> samples.add(line) }
    val nameGenerator = NameGenerator(samples)
    return nameGenerator
}

fun getRandomLand(world: World, random: Random, positionsOccupied: MutableSet<Position>): Position {
    val x = random.nextInt(world.width)
    val y = random.nextInt(world.height)
    if (java.lang.String(world.biome.get(world.height - 1 - y).get(x)).toLowerCase() == "ocean") {
        return getRandomLand(world, random, positionsOccupied)
    } else {
        val worldSize = WorldSize(world.width * 3, world.height * 3)
        val p = Position(x * 3 + 1, y * 3 + 1, worldSize)
        if (positionsOccupied.contains(p)) {
            return getRandomLand(world, random, positionsOccupied)
        } else {
            positionsOccupied.add(p)
            return p
        }
    }
}

public fun createBands(world: World, nbands:Int) : List<Band> {
    val nameGenerator = namesGenerator()
    val positionsOccupied = HashSet<Position>()
    val random = Random(1)
    for (count in 1..nbands) {
        val initialPosition = getRandomLand(world, random, positionsOccupied)
        var name = java.lang.String(nameGenerator.generateName())
        name = java.lang.String(java.lang.String(name.substring(0, 1)).toUpperCase() + name.substring(1))
        val initialPopulation = Population(5, 5, 8, 8, 2, 2)
        val nameK : String = "" + name
        Simulation.addBand(Band(Simulation.nextBand(), nameK, initialPosition, initialPopulation))
    }
    return Simulation.bandsCopy()
}

private fun reachablePositions(band: Band, includeCurrent: Boolean) : List<Position> {
    val positions = band.position.around(includeCurrent)
    val cleanPositions = ArrayList<Position>()
    for (p in positions) {
        if (isLand(p, Simulation.world!!)) {
            cleanPositions.add(p)
        }
    }
    return cleanPositions
}

public fun determineNewPosition(band: Band, world: World, random: Random) : Position {
    val cleanPositions = reachablePositions(band, true)
    return cleanPositions.get(random.nextInt(cleanPositions.size))
}

fun adultMaleFactor(population: Population) : Float {
    val th = population.adultFemale * 1.2f
    if (population.adultMale >= th) {
        return 1.0f
    } else {
        return population.adultMale / th
    }
}

fun decideIfSplit(band : Band) : Boolean {
    // It can split only if there is a possible destination which is free
    if (reachablePositions(band, false).isEmpty()) {
        return false
    }

    val penalty = huntingAndGathering.crowdingPenalty(band.population)
    val probSplitting = (penalty - 0.5) / 5.0
    return Simulation.random.nextFloat() < probSplitting
}

fun split(band : Band) : Band {
    val poss = reachablePositions(band, false)
    val random = Simulation.random
    val positionNewBand = poss.get(random.nextInt(poss.size))

    val newChM = Math.max(0, Math.min(band.population.childrenMale, intRandomValue(random, band.population.childrenMale/2.0, 1.0)))
    val newChF = Math.max(0, Math.min(band.population.childrenFemale, intRandomValue(random, band.population.childrenFemale/2.0, 1.0)))
    val newAdM = Math.max(0, Math.min(band.population.adultMale, intRandomValue(random, band.population.adultMale/2.0, 1.0)))
    val newAdF = Math.max(0, Math.min(band.population.adultFemale, intRandomValue(random, band.population.adultFemale/2.0, 1.0)))
    val newOlM = Math.max(0, Math.min(band.population.oldMale, intRandomValue(random, band.population.oldMale/2.0, 1.0)))
    val newOlF = Math.max(0, Math.min(band.population.oldFemale, intRandomValue(random, band.population.oldFemale/2.0, 1.0)))
    val oldPopulation = Population(
            band.population.childrenMale - newChM, band.population.childrenFemale - newChF,
            band.population.adultMale - newAdM, band.population.adultFemale - newAdF,
            band.population.oldMale - newOlM, band.population.oldFemale - newOlF)
    val newPopulation = Population(newChM, newChF, newAdM, newAdF, newOlM, newOlF)
    band.population = oldPopulation
    val newName = namesGenerator().generateName()
    val newBand = Band(Simulation.nextBand(), newName, positionNewBand, newPopulation)
    return newBand
}