package org.worldengine.world

import org.msgpack.core.MessagePack
import org.msgpack.value.impl.ImmutableDoubleValueImpl
import java.io.File
import java.io.FileInputStream
import java.util.*

public fun loadFromMsgPack(file: File) : World {
    val unpacker = MessagePack.newDefaultUnpacker(FileInputStream(file))
    var name = "Unknown"
    var width = 0
    var height = 0
    var biome : MutableList<List<String>> = ArrayList()
    var elevation : MutableList<List<Float>> = ArrayList()
    while (unpacker.hasNext()) {
        val v = unpacker.unpackValue().asMapValue()
        val dataMap = v.map()
        for (key in dataMap.keys) {
            when (key.asRawValue().asString()) {
                "name" -> name = dataMap.get(key)!!.asRawValue().asString()
                "width" -> width = dataMap.get(key)!!.asIntegerValue().asInt()
                "height" -> height = dataMap.get(key)!!.asIntegerValue().asInt()
                "biome" -> {
                    val a = dataMap.get(key)!!.asArrayValue()
                    for (rowData in a) {
                        val row : MutableList<String> = ArrayList()
                        for (el in rowData.asArrayValue()) {
                            row.add(el.asRawValue().asString())
                        }
                        biome.add(row)
                    }
                }
                "elevation" ->  {
                    val a = dataMap.get(key)!!.asArrayValue()
                    for (rowData in a) {
                        val row : MutableList<Float> = ArrayList()
                        for (el in rowData.asArrayValue()) {
                            row.add((el as ImmutableDoubleValueImpl).toFloat())
                        }
                        elevation.add(row)
                    }
                }
            }
        }
    }
    return World(name, width, height, elevation, biome)
}

public class WorldFileMsgPackLoader {
    companion object {

        public fun main(args: Array<String>) {
            val file = File("/home/federico/repos/worldengine/seed_888.world")
            val world = loadFromMsgPack(file)
        }
    }

}

fun main(args: Array<String>) = WorldFileMsgPackLoader.main(args)
