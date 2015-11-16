package me.tomassetti.civs.logic

import me.tomassetti.civs.model.*
import com.github.excaliburHisSheath.NameGenerator
import java.util.HashSet
import java.util.Random
import java.io.File
import java.util.ArrayList
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException
import org.worldengine.world.World

fun namesGenerator() : NameGenerator {
    val file = File("/home/federico/repos/namegen-data/personnames/Italian_male.txt")
    val samples = ArrayList<String>()

    val reader = BufferedReader(FileReader(file))
    reader.lines()
            .map { line -> java.lang.String(line).trim() } //{ line -> java.lang.String(line.trim()).toString() }
            .filter { line -> !java.lang.String(line).isEmpty() }
            .forEach { line -> samples.add(line) }
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
    val bands = ArrayList<Band>()
    for (id in 1..nbands) {
        val initialPosition = getRandomLand(world, random, positionsOccupied)
        var name = java.lang.String(nameGenerator.generateName())
        name = java.lang.String(java.lang.String(name.substring(0, 1)).toUpperCase() + name.substring(1))
        val initialPopulation = Population(5, 5, 8, 8, 2, 2)
        val nameK : String = "" + name
        bands.add(Band(id, nameK, initialPosition, initialPopulation))
    }
    return bands
}

interface  Biome {
    fun name() : String
}

object ocean : Biome {
    override fun name() : String = "ocean"
}

object polarDesert : Biome {
    override fun name() : String = "polar desert"
}

object borealDesert : Biome {
    override fun name() : String = "boreal desert"
}

object borealDryScrub : Biome {
    override fun name() : String = "boreal dry scrub"
}

object borealMoistForest : Biome {
    override fun name() : String = "boreal moist forest"
}

object borealRainForest : Biome {
    override fun name() : String = "boreal rain forest"
}

object borealWetForest : Biome {
    override fun name() : String = "boreal wet forest"
}

object ice : Biome {
    override fun name() : String = "ice"
}

object subtropicalDesert : Biome {
    override fun name() : String = "subtropical desert"
}

object subtropicalDesertScrub : Biome {
    override fun name() : String = "subtropical desert scrub"
}

object subtropicalDryForest : Biome {
    override fun name() : String = "subtropical dry forest"
}

object subtropicalMoistForest : Biome {
    override fun name() : String = "subtropical moist forest"
}

object subtropicalRainForest : Biome {
    override fun name() : String = "subtropical rain forest"
}

object subtropicalThornWoodland : Biome {
    override fun name() : String = "subtropical thorn woodland"
}

object subtropicalWetForest : Biome {
    override fun name() : String = "subtropical wet forest"
}

object subpolarDryTundra : Biome {
    override fun name(): String = "subpolar dry tundra"
}

object subpolarMoistTundra : Biome {
    override fun name(): String = "subpolar moist tundra"
}

object subpolarRainTundra : Biome {
    override fun name(): String = "subpolar rain tundra"
}

object subpolarWetTundra : Biome {
    override fun name(): String = "subpolar wet tundra"
}

object coolTemperateDesert : Biome {
    override fun name(): String = "cool temperate desert"
}
object coolTemperateDesertScrub : Biome {
    override fun name(): String = "cool temperate desert scrub"
}
object coolTemperateMoistForest : Biome {
    override fun name(): String = "cool temperate moist forest"
}
object coolTemperateRainForest : Biome {
    override fun name(): String = "cool temperate rain forest"
}
object coolTemperateSteppe : Biome {
    override fun name(): String = "cool temperate steppe"
}
object coolTemperateWetForest : Biome {
    override fun name(): String = "cool temperate wet forest"
}

object tropicalDesert : Biome {
    override fun name(): String = "tropical desert"
}

object tropicalDesertScrub : Biome {
    override fun name(): String = "tropical desert scrub"
}

object tropicalDryForest : Biome {
    override fun name(): String = "tropical dry forest"
}

object tropicalMoistForest : Biome {
    override fun name(): String = "tropical moist forest"
}

object tropicalRainForest : Biome {
    override fun name(): String = "tropical rain forest"
}

object tropicalThornWoodland : Biome {
    override fun name(): String = "tropical thorn woodland"
}

object tropicalVeryDryForest : Biome {
    override fun name(): String = "tropical very dry forest"
}

object tropicalWetForest : Biome {
    override fun name(): String = "tropical wet forest"
}

object warmTemperateDesert : Biome {
    override fun name(): String = "warm temperate desert"
}

object warmTemperateDesertScrub : Biome {
    override fun name(): String = "warm temperate desert scrub"
}

object warmTemperateDryForest : Biome {
    override fun name(): String = "warm temperate dry forest"
}

object warmTemperateMoistForest : Biome {
    override fun name(): String = "warm temperate moist forest"
}

object warmTemperateRainForest : Biome {
    override fun name(): String = "warm temperate rain forest"
}

object warmTemperateThornScrub : Biome {
    override fun name(): String = "warm temperate thorn scrub"
}

object warmTemperateWetForest : Biome {
    override fun name(): String = "warm temperate wet forest"
}

fun isLand(position: Position, world: World) : Boolean {
    val biomeName : java.lang.String = java.lang.String(world.biome.get(world.height - 1 - position.y/3).get(position.x/3))
    return !biomeName.toLowerCase().equals("ocean")
}

fun biomeAt(position: Position, world: World) : Biome {
    val biomeName = world.biome.get(world.height - 1 - position.y/3).get(position.x/3)
    val allBiomes = ArrayList<Biome>()
    allBiomes.add(ice)
    allBiomes.add(polarDesert)
    allBiomes.add(subpolarDryTundra)
    allBiomes.add(subpolarMoistTundra)
    allBiomes.add(subpolarRainTundra)
    allBiomes.add(subpolarWetTundra)
    allBiomes.add(borealDesert)
    allBiomes.add(borealDryScrub)
    allBiomes.add(borealMoistForest)
    allBiomes.add(borealRainForest)
    allBiomes.add(borealWetForest)
    allBiomes.add(coolTemperateDesert)
    allBiomes.add(coolTemperateDesertScrub)
    allBiomes.add(coolTemperateMoistForest)
    allBiomes.add(coolTemperateRainForest)
    allBiomes.add(coolTemperateSteppe)
    allBiomes.add(coolTemperateWetForest)
    allBiomes.add(subtropicalDesert)
    allBiomes.add(subtropicalDesertScrub)
    allBiomes.add(subtropicalDryForest)
    allBiomes.add(subtropicalMoistForest)
    allBiomes.add(subtropicalRainForest)
    allBiomes.add(subtropicalThornWoodland)
    allBiomes.add(subtropicalWetForest)
    allBiomes.add(tropicalDesert)
    allBiomes.add(tropicalDesertScrub)
    allBiomes.add(tropicalDryForest)
    allBiomes.add(tropicalMoistForest)
    allBiomes.add(tropicalRainForest)
    allBiomes.add(tropicalThornWoodland)
    allBiomes.add(tropicalVeryDryForest)
    allBiomes.add(tropicalWetForest)
    allBiomes.add(warmTemperateDesert)
    allBiomes.add(warmTemperateDesertScrub)
    allBiomes.add(warmTemperateDryForest)
    allBiomes.add(warmTemperateMoistForest)
    allBiomes.add(warmTemperateRainForest)
    allBiomes.add(warmTemperateThornScrub)
    allBiomes.add(warmTemperateWetForest)

    for (biome in allBiomes) {
        if (biome.name().equals(biomeName)) {
            return biome
        }
    }
    throw RuntimeException(biomeName)
}

public fun determineNewPosition(band: Band, world: World, random: Random) : Position {
    val positions = band.position.around(true)
    val cleanPositions = ArrayList<Position>()
    for (p in positions) {
        if (isLand(p, world)) {
            cleanPositions.add(p)
        }
    }
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

public fun updatePopulation(band: Band, world: World, random: Random) {
    var prosperity = huntingAndGathering.prosperity(biomeAt(band.position, world))
    prosperity *= (random.nextFloat() - 0.5f) / 10.0f
    band.population = calcPopulationGivenProsperity(prosperity, band.population, random)
}

public data class GenderData(val male:Int, val female:Int)

fun newBirths(prosperity: Float, population: Population, random: Random) : GenderData {
    var newBirthsM = 0
    var newBirthsF = 0
    for (i in 1..population.adultFemale) {
        if (random.nextFloat() < adultMaleFactor(population) * prosperity * 0.3f) {
            if (random.nextFloat() < 0.5f) {
                newBirthsM += 1
            } else {
                newBirthsF += 1
            }
        }
    }
    return GenderData(newBirthsM, newBirthsF)
}

fun calcPopulationGivenProsperity(prosperity: Float, population: Population, random: Random) : Population {
    val (newBirthsM, newBirthsF) = newBirths(prosperity, population, random)
    var chMdeaths = 0
    var chMgrowth = 0
    for (i in 1..population.childrenMale) {
        if (random.nextFloat() * random.nextFloat() > prosperity * 1.00f) {
            chMdeaths += 1
        } else if (random.nextFloat() > 0.5f) {
            chMgrowth += 1
        }
    }
    var chFdeaths = 0
    var chFgrowth = 0
    for (i in 1..population.childrenFemale) {
        if (random.nextFloat() * random.nextFloat() > prosperity * 1.00f) {
            chFdeaths += 1
        } else if (random.nextFloat() > 0.5f) {
            chFgrowth += 1
        }
    }
    var adMdeaths = 0
    var adMgrowth = 0
    for (i in 1..population.adultMale) {
        if (random.nextFloat() * random.nextFloat() > prosperity * 1.15f) {
            adMdeaths += 1
        } else if (random.nextFloat() > 0.3f) {
            adMgrowth += 1
        }
    }
    var adFdeaths = 0
    var adFgrowth = 0
    for (i in 1..population.adultFemale) {
        if (random.nextFloat() * random.nextFloat() > prosperity * 1.15f) {
            adFdeaths += 1
        } else if (random.nextFloat() > 0.3f) {
            adFgrowth += 1
        }
    }
    var oldMdeaths = 0
    for (i in 1..population.oldMale) {
        if (random.nextFloat() * random.nextFloat() > prosperity * 0.60f) {
            oldMdeaths += 1
        }
    }
    var oldFdeaths = 0
    for (i in 1..population.oldFemale) {
        if (random.nextFloat() * random.nextFloat() > prosperity * 0.60f) {
            oldFdeaths += 1
        }
    }
    val newPopulation = Population(
            population.childrenMale + newBirthsM - chMdeaths - chMgrowth,
            population.childrenFemale + newBirthsF - chFdeaths - chFgrowth,
            population.adultMale + chMgrowth - adMdeaths - adMgrowth,
            population.adultFemale + chFgrowth - adFdeaths - adFgrowth,
            population.oldFemale + adFgrowth - oldFdeaths,
            population.oldFemale + adFgrowth - oldFdeaths
    )
    return newPopulation
}

interface SubstainmentActivity {
    fun prosperity(biome: Biome) : Float
}

object huntingAndGathering : SubstainmentActivity {
    override fun prosperity(biome: Biome) : Float {
        return when (biome) {
            ocean -> 0.0f
            ice -> 0.2f
            polarDesert -> 0.22f
            subpolarDryTundra -> 0.25f
            subpolarMoistTundra -> 0.3f
            subpolarRainTundra -> 0.34f
            subpolarWetTundra -> 0.38f
            borealDesert -> 0.2f
            borealDryScrub -> 0.3f
            borealMoistForest -> 0.5f
            borealRainForest ->  0.65f
            borealWetForest -> 0.8f
            coolTemperateDesert -> 0.25f
            coolTemperateDesertScrub -> 0.35f
            coolTemperateMoistForest -> 0.85f
            coolTemperateRainForest -> 0.9f
            coolTemperateSteppe -> 0.8f
            coolTemperateWetForest -> 0.8f
            subtropicalDesert -> 0.3f
            subtropicalDesertScrub -> 0.4f
            subtropicalDryForest -> 0.5f
            subtropicalMoistForest -> 0.7f
            subtropicalRainForest -> 0.8f
            subtropicalThornWoodland -> 0.8f
            subtropicalWetForest -> 0.9f
            tropicalDesert -> 0.33f
            tropicalDesertScrub -> 0.33f
            tropicalDryForest -> 0.5f
            tropicalMoistForest -> 0.7f
            tropicalRainForest -> 0.8f
            tropicalThornWoodland -> 0.35f
            tropicalVeryDryForest -> 0.4f
            tropicalWetForest -> 0.9f
            warmTemperateDesert -> 0.35f
            warmTemperateDesertScrub -> 0.42f
            warmTemperateDryForest -> 0.55f
            warmTemperateMoistForest -> 0.6f
            warmTemperateRainForest -> 0.78f
            warmTemperateThornScrub -> 0.50f
            warmTemperateWetForest -> 0.83f
            else -> throw RuntimeException(biome.name())
        }
    }
}