package me.tomassetti.civs.logic

import me.tomassetti.civs.model.Position
import org.worldengine.world.World
import java.util.*

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
