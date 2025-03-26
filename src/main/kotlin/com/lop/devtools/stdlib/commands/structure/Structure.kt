package com.lop.devtools.stdlib.commands.structure

import com.lop.devtools.monstera.addon.Addon
import java.io.File

class Structure {

    /**
     * ```
     * load(getResource("myStructure.mcstructure"), Triple(0, 100, 0)) {
     *      rotation = Rotation.ZERO
     *      mirror = Mirror.NONE
     *      animation: Animation = Animation.NONE
     *      includeEntities = true
     *      includeBlocks = true
     *      waterLogged = true
     *      integrity = 1f
     *      seed = ""
     * }
     * ```
     */
    fun load(
        file: File? = null,
        name: String? = file?.name?.removeSuffix(".mcstructure"),
        to: Triple<Int, Int, Int>,
        data: StructureLoad.() -> Unit
    ): String {
        if (file == null && name == null)
            throw IllegalArgumentException("Invalid Arguments, at least a name or file has to be given")
        file?.copyTo(Addon.active!!.config.behPath.resolve("structures").resolve("$name.mcstructure").toFile(), true)
        return "structure load $name ${to.first} ${to.second} ${to.third} ${StructureLoad().apply(data).getData()}"
    }

    fun delete(name: String): String {
        return "structure delete $name"
    }

    /**
     * ```
     * save("my_struck", Triple(0, 100, 0), Triple(10, 105, 0)) {
     *      includeEntities = true
     *      saveMode = SaveMode.MEMORY
     *      includeBlocks = true
     * }
     * ```
     */
    fun save(
        name: String,
        from: Triple<Int, Int, Int>,
        to: Triple<Int, Int, Int>,
        data: StructureSave.() -> Unit
    ): String {
        return "structure save $name ${from.first} ${from.second} ${from.third} ${to.first} ${to.second} ${to.third} ${
            StructureSave().apply(data).getData()
        }"
    }

    enum class Rotation(private val data: String) {
        ZERO("0_degrees"),
        ONE_EIGHTY("180_degrees"),
        ZWO_SEVENTY("270_degrees"),
        NINETY("90_degrees");

        override fun toString(): String {
            return data
        }
    }

    enum class Mirror(private val data: String) {
        NONE("none"),
        X("x"),
        XZ("xz"),
        Z("z");

        override fun toString(): String {
            return data
        }
    }

    enum class Animation(private val data: String) {
        NONE(""),
        BLOCK_BY_BLOCK("block_by_block"),
        LAYER_BY_LAYER("layer_by_layer");

        override fun toString(): String {
            return data
        }
    }

    inner class StructureLoad {
        var rotation: Rotation = Rotation.ZERO
        var mirror: Mirror = Mirror.NONE
        var animation: Animation = Animation.NONE
        var includeEntities: Boolean = true
        var includeBlocks: Boolean = true
        var waterLogged: Boolean = true
        var integrity: Float? = null
        var seed: String? = null

        fun getData(): String {
            var dataStr = ""
            dataStr += rotation.toString()
            dataStr += " $mirror"
            if (animation != Animation.NONE)
                dataStr += " $animation"
            dataStr += " $includeEntities"
            dataStr += " $includeBlocks"
            dataStr += " $waterLogged"
            if (integrity != null)
                dataStr += " $integrity"
            if (seed != null)
                dataStr += " $seed"
            return dataStr
        }
    }

    enum class SaveMode(private val data: String) {
        DISK("disk"),
        MEMORY("memory");

        override fun toString(): String {
            return data
        }
    }

    inner class StructureSave {
        var includeEntities: Boolean = true
        var saveMode: SaveMode = SaveMode.MEMORY
        var includeBlocks: Boolean = true

        fun getData(): String {
            return "$includeEntities $saveMode $includeBlocks"
        }
    }
}