package me.tomassetti.civs.logic

import me.tomassetti.civs.model.Population

interface SubstainmentActivity {
    fun prosperity(biome: Biome, population: Population) : Float

    fun crowdingPenalty(population: Population) : Float

    fun considerSubstaintability(prosperity: Float, population: Population): Float
}

object huntingAndGathering : SubstainmentActivity {

    override fun crowdingPenalty(population: Population) : Float = ((population.total()/40.0 - 1.0)/2.5).toFloat()

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
            val crowdingPenalty = crowdingPenalty(population)
            val res = prosperity - crowdingPenalty
            return res
        }
    }
}
