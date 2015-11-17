package me.tomassetti.civs.logic

import java.util.*

fun randomValue(random: Random, mean: Double, variance: Double) : Double {
    return random.nextGaussian() * variance * mean + mean
}

fun intRandomValue(random: Random, mean: Double, variance: Double) : Int {
    return round(randomValue(random, mean, variance), random)
}

private fun round(v:Float, r: Random) : Int {
    val base : Int = Math.ceil(v.toDouble()).toInt()
    val rest : Float = v - base
    if (r.nextFloat() < rest) {
        return base + 1
    } else {
        return base
    }
}

public fun round(v:Double, r: Random) : Int {
    val base : Int = Math.ceil(v.toDouble()).toInt()
    val rest : Double = v - base
    if (r.nextDouble() < rest) {
        return base + 1
    } else {
        return base
    }
}

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