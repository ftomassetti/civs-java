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
    var prosperity = huntingAndGathering.prosperity(biomeAt(band.position, world), band.population)
    var adaptedProsperity = huntingAndGathering.considerSubstaintability(prosperity, band.population)
    band.population = calcPopulationGivenProsperity(adaptedProsperity, band.population, random)
}

public data class GenderData(val male:Int, val female:Int) {
    fun total() : Int = male + female
}

fun newBirths(prosperity: Float, population: Population, random: Random) : GenderData {
    var natalityFactor = 3.4 * adultMaleFactor(population) * population.adultFemale * ((prosperity + 6.0)/7)
    var newBirthsTotal = Math.max(0, intRandomValue(random, natalityFactor, 0.55))
    var newBirthsM = Math.max(0, Math.min(newBirthsTotal, intRandomValue(random, newBirthsTotal / 2.0, 0.3)))
    var newBirthsF = newBirthsTotal - newBirthsM
    return GenderData(newBirthsM, newBirthsF)
}

public data class DeathsAndGrowths(val deaths:GenderData, val growth:GenderData)

private fun mostExtreme(values : List<Float>) : Float {
    if (values.size == 0) {
        throw IllegalArgumentException()
    } else if (values.size == 1) {
        return values.get(0)
    } else {
        val other = mostExtreme(values.subList(1, values.size))
        val first = values.get(0)
        if (Math.abs(first - 0.5f) > Math.abs(other - 0.5f)) {
            return first
        } else {
            return other
        }
    }
}

private fun randomNumber(size: Int, random: Random) : Float {
    if (size >= 15) {
        return (random.nextFloat() + random.nextFloat() + random.nextFloat()) / 3.0f
    } else if (size >= 7) {
        return (random.nextFloat() + random.nextFloat()) / 2.0f
    } else if (size >= 4){
        return random.nextFloat()
    } else if (size >= 2){
        val values = ArrayList<Float>()
        values.add(random.nextFloat())
        values.add(random.nextFloat())
        return mostExtreme(values)
    } else {
        val values = ArrayList<Float>()
        values.add(random.nextFloat())
        values.add(random.nextFloat())
        values.add(random.nextFloat())
        return mostExtreme(values)
    }
}

private fun genericDeaths(prosperity: Float, male: Int, female: Int, random: Random, deathFactor: Float) : GenderData {
    if (male < 0) {
        throw IllegalArgumentException()
    }
    if (female < 0) {
        throw IllegalArgumentException()
    }
    var correctedDeathFactor = deathFactor * ((3.0 + (1.0 - prosperity))/4.0)
    val chMdeaths = Math.max(0, Math.min(male, intRandomValue(random, correctedDeathFactor * male.toDouble(), 1.0)))
    val chFdeaths = Math.max(0, Math.min(female, intRandomValue(random, correctedDeathFactor * female.toDouble(), 1.0)))
    return GenderData(chMdeaths, chFdeaths)
}

private fun round(v:Float, r:Random) : Int {
    val base : Int = Math.ceil(v.toDouble()).toInt()
    val rest : Float = v - base
    if (r.nextFloat() < rest) {
        return base + 1
    } else {
        return base
    }
}

private fun round(v:Double, r:Random) : Int {
    val base : Int = Math.ceil(v.toDouble()).toInt()
    val rest : Double = v - base
    if (r.nextDouble() < rest) {
        return base + 1
    } else {
        return base
    }
}

private fun genericDeathsAndGrowth(prosperity: Float, male: Int, female: Int, random: Random, deathFactor: Float, growthFactor: Float) : DeathsAndGrowths {
    if (male < 0) {
        throw IllegalArgumentException()
    }
    if (female < 0) {
        throw IllegalArgumentException()
    }
    val (chMdeaths, chFdeaths) = genericDeaths(prosperity, male, female, random, deathFactor)
    if (chMdeaths < 0) {
        throw RuntimeException()
    }
    if (chFdeaths < 0) {
        throw RuntimeException()
    }

    val chMgrowth = Math.min(male - chMdeaths, Math.max(0, intRandomValue(random, growthFactor * (male - chMdeaths).toDouble(), 0.65)))
    val chFgrowth = Math.min(female - chFdeaths, Math.max(0, intRandomValue(random, growthFactor * (female - chFdeaths).toDouble(), 0.65)))
    return DeathsAndGrowths(GenderData(chMdeaths, chFdeaths), GenderData(chMgrowth, chFgrowth))
}

fun childrenDeathsAndGrowth(prosperity: Float, population: Population, random: Random) : DeathsAndGrowths {
    return genericDeathsAndGrowth(prosperity, population.childrenMale, population.childrenFemale, random, 0.28f, 0.5f)
}

fun adultsDeathsAndGrowth(prosperity: Float, population: Population, random: Random) : DeathsAndGrowths {
    return genericDeathsAndGrowth(prosperity, population.adultMale, population.adultFemale, random, 0.155f, 0.3f)
}

fun oldDeaths(prosperity: Float, population: Population, random: Random) : GenderData {
    return genericDeaths(prosperity, population.oldMale, population.oldFemale, random, 0.285f)
}

fun randomValue(random: Random, mean: Double, variance: Double) : Double {
    return random.nextGaussian() * variance * mean + mean
}

fun intRandomValue(random: Random, mean: Double, variance: Double) : Int {
    return round(randomValue(random, mean, variance), random)
}

fun calcPopulationGivenProsperity(prosperity: Float, population: Population, random: Random) : Population {
    val (newBirthsM, newBirthsF) = newBirths(prosperity, population, random)
    val (chDeaths, chGrowth) = childrenDeathsAndGrowth(prosperity, population, random)
    val (chMdeaths, chFdeaths) = chDeaths
    val (chMgrowth, chFgrowth) = chGrowth
    val (adDeaths, adGrowth) = adultsDeathsAndGrowth(prosperity, population, random)
    val (adMdeaths, adFdeaths) = adDeaths
    val (adMgrowth, adFgrowth) = adGrowth
    var (oldMdeaths, oldFdeaths) = oldDeaths(prosperity, population, random)

    if (population.childrenMale - chMdeaths - chMgrowth < 0) {
        throw RuntimeException("Children male: " + population.childrenMale +", deaths " + chMdeaths + ", growth " + chMgrowth)
    }
    if (population.childrenFemale - chFdeaths - chFgrowth < 0) {
        throw RuntimeException("Children female: " + population.childrenFemale +", deaths " + chFdeaths + ", growth " + chFgrowth)
    }
    if (newBirthsM < 0) {
        throw RuntimeException()
    }
    if (newBirthsF < 0) {
        throw RuntimeException()
    }

    val newChm = population.childrenMale + newBirthsM - chMdeaths - chMgrowth
    val newChf = population.childrenFemale + newBirthsF - chFdeaths - chFgrowth
    val newAdm = population.adultMale + chMgrowth - adMdeaths - adMgrowth
    val newAdf = population.adultFemale + chFgrowth - adFdeaths - adFgrowth
    val newOlm = population.oldMale + adMgrowth - oldMdeaths
    val newOlf = population.oldFemale + adFgrowth - oldFdeaths

    if (newChm < 0) {
        throw RuntimeException()
    }
    if (newChf < 0) {
        throw RuntimeException()
    }
    if (newAdm < 0) {
        throw RuntimeException()
    }
    if (newAdf < 0) {
        throw RuntimeException()
    }
    if (newOlm < 0) {
        throw RuntimeException()
    }
    if (newOlf < 0) {
        throw RuntimeException()
    }

    val newPopulation = Population(newChm, newChf, newAdm, newAdf, newOlm, newOlf)
    return newPopulation
}

interface SubstainmentActivity {
    fun prosperity(biome: Biome, population: Population) : Float

    fun considerSubstaintability(prosperity: Float, population: Population): Float
}

object huntingAndGathering : SubstainmentActivity {
    override fun prosperity(biome: Biome, population: Population) : Float {
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

    override fun considerSubstaintability(prosperity: Float, population: Population): Float {
        val total = population.total()
        if (total < 40) {
            return prosperity
        } else {
            val crowdingPenalty = ((total/40.0 - 1.0)/2.5).toFloat()
            val res = prosperity - crowdingPenalty
            System.out.println("Prosperity " + prosperity +", total " + total +", res -> " + res)
            return res
        }
    }
}