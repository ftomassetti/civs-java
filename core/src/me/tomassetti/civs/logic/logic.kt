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
    BufferedReader(FileReader(file)).use { reader ->
        reader.lines()
                .map<String> { line -> line.trim() }
                .filter { line -> !line.isEmpty() }
                .forEach { line -> samples.add(line) }
    }
    val nameGenerator = NameGenerator(samples.toArray<String>(array<String>()))
    return nameGenerator
}

fun getRandomLand(world: World, random: Random, positionsOccupied: MutableSet<Position>): Position {
    val x = random.nextInt(world.width)
    val y = random.nextInt(world.height)
    if (world.biome.get(world.height - 1 - y).get(x).toLowerCase() == "ocean") {
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

fun createBands(world: World, nbands:Int) : List<Band> {
    val nameGenerator = namesGenerator()
    val positionsOccupied = HashSet<Position>()
    val random = Random(1)
    val bands = ArrayList<Band>()
    for (id in 1..nbands) {
        val initialPosition = getRandomLand(world, random, positionsOccupied)
        var name = nameGenerator.generateName()
        name = name.substring(0, 1).toUpperCase() + name.substring(1)
        val initialPopulation = Population(5, 5, 8, 8, 2, 2)
        bands.add(Band(id, name, initialPosition, initialPopulation))
    }
    return bands
}

trait Biome {
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
    return !world.biome.get(world.height - 1 - position.y/3).get(position.x/3).toLowerCase().equals("ocean")
}

fun biomeAt(position: Position, world: World) : Biome {
    val biomeName = world.biome.get(world.height - 1 - position.y/3).get(position.x/3)
    val allBiomes = array(ocean, ice, polarDesert,
            subpolarDryTundra, subpolarMoistTundra, subpolarRainTundra, subpolarWetTundra,
            borealDesert, borealDryScrub, borealMoistForest, borealRainForest, borealWetForest,
            coolTemperateDesert, coolTemperateDesertScrub, coolTemperateMoistForest, coolTemperateRainForest, coolTemperateSteppe, coolTemperateWetForest,
            subtropicalDesert, subtropicalDesertScrub, subtropicalDryForest, subtropicalMoistForest,
            subtropicalRainForest, subtropicalThornWoodland, subtropicalWetForest,
            tropicalDesert, tropicalDesertScrub, tropicalDryForest, tropicalMoistForest,
            tropicalRainForest, tropicalThornWoodland, tropicalVeryDryForest, tropicalWetForest,
            warmTemperateDesert, warmTemperateDesertScrub, warmTemperateDryForest, warmTemperateMoistForest,
            warmTemperateRainForest, warmTemperateThornScrub, warmTemperateWetForest)
    for (biome in allBiomes) {
        if (biome.name().equals(biomeName)) {
            return biome
        }
    }
    throw RuntimeException(biomeName)
}

fun determineNewPosition(band: Band, world: World, random: Random) : Position {
    val positions = band.position.around(true).filter { p -> isLand(p, world) }
    return positions.get(random.nextInt(positions.size()))
}

fun adultMaleFactor(population: Population) : Float {
    val th = population.adultFemale * 1.2f
    if (population.adultMale >= th) {
        return 1.0f
    } else {
        return population.adultMale / th
    }
}

fun updatePopulation(band: Band, world: World, random: Random) {
    var prosperity = huntingAndGathering.prosperity(biomeAt(band.position, world))
    prosperity *= (random.nextFloat() - 0.5f)/10.0f
    var newBirthsM = 0
    var newBirthsF = 0
    for (i in 1..band.population.adultFemale) {
        if (random.nextFloat() < adultMaleFactor(band.population) * prosperity * 0.3f) {
            if (random.nextFloat() < 0.5f) {
                newBirthsM += 1
            } else {
                newBirthsF += 1
            }
        }
    }
    var chMdeaths = 0
    var chMgrowth = 0
    for (i in 1..band.population.childrenMale) {
        if (random.nextFloat() * random.nextFloat() > prosperity * 1.00f) {
            chMdeaths += 1
        } else if (random.nextFloat() > 0.5f) {
            chMgrowth += 1
        }
    }
    var chFdeaths = 0
    var chFgrowth = 0
    for (i in 1..band.population.childrenFemale) {
        if (random.nextFloat() * random.nextFloat() > prosperity * 1.00f) {
            chFdeaths += 1
        } else if (random.nextFloat() > 0.5f) {
            chFgrowth += 1
        }
    }
    var adMdeaths = 0
    var adMgrowth = 0
    for (i in 1..band.population.adultMale) {
        if (random.nextFloat() * random.nextFloat() > prosperity * 1.15f) {
            adMdeaths += 1
        } else if (random.nextFloat() > 0.3f) {
            adMgrowth += 1
        }
    }
    var adFdeaths = 0
    var adFgrowth = 0
    for (i in 1..band.population.adultFemale) {
        if (random.nextFloat() * random.nextFloat() > prosperity * 1.15f) {
            adFdeaths += 1
        } else if (random.nextFloat() > 0.3f) {
            adFgrowth += 1
        }
    }
    var oldMdeaths = 0
    for (i in 1..band.population.oldMale) {
        if (random.nextFloat() * random.nextFloat() > prosperity * 0.60f) {
            oldMdeaths += 1
        }
    }
    var oldFdeaths = 0
    for (i in 1..band.population.oldFemale) {
        if (random.nextFloat() * random.nextFloat() > prosperity * 0.60f) {
            oldFdeaths += 1
        }
    }
    val newPopulation = Population(
            band.population.childrenMale + newBirthsM - chMdeaths - chMgrowth,
            band.population.childrenFemale + newBirthsF - chFdeaths - chFgrowth,
            band.population.adultMale + chMgrowth - adMdeaths - adMgrowth,
            band.population.adultFemale + chFgrowth - adFdeaths - adFgrowth,
            band.population.oldFemale + adFgrowth - oldFdeaths,
            band.population.oldFemale + adFgrowth - oldFdeaths
    )
    println("OLD POPULATION "+ band.population)
    println("NEW POPULATION "+ newPopulation)
    band.population = newPopulation
}

trait  SubstainmentActivity {
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