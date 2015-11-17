package me.tomassetti.civs.logic

import me.tomassetti.civs.model.Band
import me.tomassetti.civs.model.Population
import org.worldengine.world.World
import java.util.*


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

public data class DeathsAndGrowths(val deaths: GenderData, val growth: GenderData)

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



private fun genericDeathsAndGrowth(prosperity: Float, male: Int, female: Int, random: Random, deathFactor: Float, growthFactor: Float) : DeathsAndGrowths {
    if (male < 0) {
        throw IllegalArgumentException("Male " + male)
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
