package com.lop.devtools.stdlib.blockbench.parse.geo

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import com.lop.devtools.monstera.files.File
import com.lop.devtools.monstera.files.res.geo.BedrockGeometry
import com.lop.devtools.monstera.files.res.geo.data.GeometryData
import com.lop.devtools.monstera.files.res.geo.data.bones.Cube
import com.lop.devtools.monstera.files.res.geo.data.bones.GeoBone
import com.lop.devtools.monstera.files.res.geo.data.bones.Locator
import com.lop.devtools.monstera.files.res.geo.data.description.GeometryDescription
import com.lop.devtools.stdlib.blockbench.filestruc.BlBase
import com.lop.devtools.stdlib.blockbench.filestruc.data.BlOutline
import org.slf4j.LoggerFactory
import java.io.File
import java.util.*
import kotlin.math.*

class BlockBenchGeoParser(private val base: BlBase, private val debugOutput: Boolean) {
    companion object {
        @Suppress("MemberVisibilityCanBePrivate")
        var tmpDir = File("build", "tmp", "blockbench", "geos")
        private val logger = LoggerFactory.getLogger("bbmodel parser")

        fun getGeometry(base: BlBase, debugOutput: Boolean = false): File {
            val tmpDirID = File(
                tmpDir,
                base.modelIdentifier.replace("\\s".toRegex(), "").replace(":", "")
            )
            tmpDirID.mkdirs()

            val target = File(tmpDirID, "${base.modelIdentifier.replace(":", "")}.geo.json")
            if (!target.exists()) {
                if (debugOutput)
                    logger.info("geo tmp file ${target.path} created!")
                target.createNewFile()
            }

            val gson = GsonBuilder()
                .setPrettyPrinting()
                .create()
            val parser = BlockBenchGeoParser(base, debugOutput)

            if (debugOutput)
                logger.info("Created geo parser, parsing ...")

            val geo = BedrockGeometry(
                "1.12.0",
                arrayListOf(
                    parser.getGeoData()
                )
            )

            if (debugOutput)
                logger.info("(${base.name}) Parsing compete, writing to tmp file ...")
            target.writeText(gson.toJson(geo))
            return target
        }
    }

    private val gson = Gson()

    private val description = GeometryDescription(
        identifier = "geometry.${base.modelIdentifier}",
        textureWidth = base.resolution.width,
        textureHeight = base.resolution.height
    )
        get() {
            if (debugOutput)
                logger.info("(${base.name}) Parsing description ...")

            val box = calculateVisibleBox()
            field.visibleBoundsWidth = box.first
            field.visibleBoundsHeight = box.second
            field.visibleBoundsOffset = arrayListOf(0f, box.third, 0f)
            return field
        }

    private val boneParser = BoneParser(base)

    //new
    fun getGeoData(): GeometryData {
        return GeometryData(
            description,
            boneParser.compileGroups()
        )
    }

    private fun calculateVisibleBox(): Triple<Float, Float, Float> {
        val allElmX = arrayListOf<Float>()
        val allElmY = arrayListOf<Float>()
        val allElmZ = arrayListOf<Float>()

        base.elements.forEach { element ->
            element.from?.let {
                allElmX.add(it[0])
                allElmY.add(it[1])
                allElmZ.add(it[2])
            }
            element.to?.let {
                allElmX.add(it[0])
                allElmY.add(it[1])
                allElmZ.add(it[2])
            }
        }

        //width
        //look for the furthest element that needs rendering
        val radius = max(
            max(allElmX.max(), -allElmX.min()),
            max(allElmZ.max(), -allElmZ.min())
        )

        val width = ceil((radius * 2) / 16)

        //height

        @Suppress("UNUSED_VARIABLE")
        val visBoxDataWidth = base.visibleBox?.get(0) ?: 0f
        val visBoxDataHeight = base.visibleBox?.get(1) ?: 0f
        val visBoxDataOffset = base.visibleBox?.get(2) ?: 0f

        val yMin = min(
            floor(allElmY.min() / 16),
            (visBoxDataOffset - visBoxDataHeight / 2)
        )

        val yMax = max(
            ceil(allElmY.max() / 16),
            (visBoxDataOffset + visBoxDataHeight / 2)
        )

        val height = yMax - yMin

        //offset
        val offset = (yMax + yMin) / 2

        return Triple(
            width,
            height,
            offset
        )
    }

    inner class BoneParser(private val base: BlBase) {
        private val cubeParser = CubeParser(base)
        val bones: ArrayList<GeoBone> = arrayListOf()

        private fun collectLooseElements() {
            val elements = arrayListOf<UUID>()
            val bone = GeoBone(
                name = "root",
                pivot = arrayListOf(0f, 0f, 0f),
                cubes = arrayListOf()
            )
            base.outliner.forEach {
                try {
                    val uuid = gson.fromJson(it, UUID::class.java)
                    elements.add(uuid)
                } catch (_: Exception) {
                }
            }
            val cubes = elements.filter { getBlElement(it).type == "cube" }
            val locators = elements.filter { getBlElement(it).type == "locator" }

            cubes.forEach { target ->
                bone.cubes!!.add(cubeParser.parse(target))
            }
            locators.forEach { target ->
                val loc = cubeParser.parseLocator(target)
                bone.locators = (bone.locators ?: mutableMapOf()).apply {
                    put(loc.first, loc.second)
                }
            }
            if (elements.size > 0) {
                bones.add(bone)
            }

            if (debugOutput)
                logger.info("(${base.name}) Parsing root level cubes found: ${elements.size}")
        }

        private fun getBlElement(uuid: UUID) = base.elements.first { it.uuid == uuid }

        fun compileGroups(): ArrayList<GeoBone> {
            if (debugOutput)
                logger.info("(${base.name}) Parsing bones ...")

            collectLooseElements()

            base.outliner.forEach {
                try {
                    val group = gson.fromJson(it, BlOutline::class.java)
                    compileGroup(group)
                } catch (e: Exception) {
                    if (debugOutput)
                        logger.error("(${base.name}) Failed to parse group '$it'")
                }
            }
            if (debugOutput)
                logger.info("(${base.name}) Parsed ${bones.size} bones!")
            return bones
        }

        private fun compileGroup(group: BlOutline, parent: String? = null) {
            val cubes = arrayListOf<Cube>()
            val locators: MutableMap<String, Locator> = mutableMapOf()
            group.children?.forEach {
                try {
                    //just cubes
                    val str = gson.fromJson(it, String::class.java)
                    val uuid = UUID.fromString(str)
                    val ele = getBlElement(uuid)
                    if (ele.type == "cube")
                        cubes.add(cubeParser.parse(uuid))
                    else if (ele.type == "locator") {
                        val loc = cubeParser.parseLocator(uuid)
                        locators[loc.first] = loc.second
                    }

                } catch (e: JsonSyntaxException) {
                    //child bone
                    compileGroup(gson.fromJson(it, BlOutline::class.java), group.name)
                }
            }
            bones.add(mapGroupToBone(group, parent, cubes, locators))
        }

        private fun mapGroupToBone(
            group: BlOutline,
            parent: String?,
            cubes: ArrayList<Cube>,
            locators: MutableMap<String, Locator>
        ): GeoBone {
            return GeoBone(
                name = group.name,
                parent = parent,
                pivot = mapPivot(group.origin),
                rotation = mapRotation(group.rotation),
                cubes = cubes,
                locators = locators
            )
        }

        //######################################################################
        //#########                      CANCER!!                      #########
        //######################################################################

        @Suppress("DuplicatedCode") //let this duplicate as it's too important here
        private fun mapPivot(pivot: ArrayList<Float>?): ArrayList<Float>? {
            if (pivot.isNullOrEmpty())
            //irrelevant for map
                return null
            else {
                if (pivot.all { it == 0f })
                    return pivot //irrelevant for map

                //map pivot
                val newPivot = arrayListOf(0f, 0f, 0f)

                newPivot[0] = pivot[0] * -1
                newPivot[1] = pivot[1]
                newPivot[2] = pivot[2]

                return newPivot
            }
        }

        private fun mapRotation(rotation: ArrayList<Float>?): ArrayList<Float>? {
            return if (rotation.isNullOrEmpty() || rotation.all { it == 0f })
            //irrelevant for map
                null
            else {
                //map pivot
                val newRotation = arrayListOf(0f, 0f, 0f)

                newRotation[0] = rotation[0] * -1
                newRotation[1] = rotation[1] * -1
                newRotation[2] = rotation[2]

                newRotation
            }
        }

        //################################ END ##################################
    }

    inner class CubeParser(private val base: BlBase) {

        fun parseLocator(uuid: UUID): Pair<String, Locator> {
            with(base.elements.first { it.uuid == uuid }) {
                var rot: MutableList<Number>? = arrayListOf()
                rot?.addAll(mapRotation(rotation) ?: listOf())
                var off: MutableList<Number>? = arrayListOf()

                position?.let {
                    off?.add(it[0] * -1)
                    off?.add(it[1])
                    off?.add(it[2])
                }

                if (off.isNullOrEmpty())
                    off = null
                if (rot.isNullOrEmpty())
                    rot = null

                return (name ?: "loc") to Locator(
                    offset = off,
                    rotation = rot
                )
            }
        }

        fun parse(uuid: UUID): Cube {
            with(base.elements.first { it.uuid == uuid }) {
                val size = mapSize(from, to)

                return Cube(
                    origin = mapOrigin(from, size),
                    size = size,
                    rotation = mapRotation(rotation),
                    uv = uvOffset ?: arrayListOf(0f, 0f),
                    mirror = mirrorUv,
                    pivot = mapPivot(origin, rotation),
                    inflate = inflate
                )
            }
        }

        //######################################################################
        //#########                      CANCER!!                      #########
        //######################################################################

        private fun mapOrigin(origin: ArrayList<Float>?, size: ArrayList<Float>?): ArrayList<Float>? {
            if (origin.isNullOrEmpty() || size.isNullOrEmpty())
            //irrelevant for map
                return null
            else {
                if (origin.all { it == 0f })
                    return origin //irrelevant for map

                //map pivot
                val newOrigin = arrayListOf(0f, 0f, 0f)

                newOrigin[0] = -(origin[0] + size[0])
                newOrigin[1] = origin[1]
                newOrigin[2] = origin[2]

                return newOrigin
            }
        }

        @Suppress("DuplicatedCode") //let this duplicate as it's too important here
        private fun mapSize(from: ArrayList<Float>?, to: ArrayList<Float>?): ArrayList<Float>? {
            var size: ArrayList<Float>? = null
            if (from != null && to != null) {
                size = arrayListOf(
                    roundFloat(abs(from[0] - to[0])),
                    roundFloat(abs(from[1] - to[1])),
                    roundFloat(abs(from[2] - to[2]))
                )
            }
            return size
        }

        private fun mapRotation(rotation: ArrayList<Float>?): ArrayList<Float>? {
            if (rotation.isNullOrEmpty())
            //irrelevant for map
                return null
            else {
                if (rotation.all { it == 0f })
                    return null //irrelevant for map

                //map pivot
                val newRotation = arrayListOf(0f, 0f, 0f)

                newRotation[0] = roundFloat(rotation[0] * -1)
                newRotation[1] = roundFloat(rotation[1] * -1)
                newRotation[2] = roundFloat(rotation[2])

                return newRotation
            }
        }

        @Suppress("DuplicatedCode") //let this duplicate as it's too important here
        private fun mapPivot(pivot: ArrayList<Float>?, rotation: ArrayList<Float>?): ArrayList<Float>? {
            if (pivot.isNullOrEmpty() || rotation.isNullOrEmpty())
            //irrelevant for map
                return null
            else {
                if (pivot.all { it == 0f })
                    return pivot //irrelevant for map

                //map pivot
                val newPivot = arrayListOf(0f, 0f, 0f)

                newPivot[0] = roundFloat(pivot[0] * -1)
                newPivot[1] = roundFloat(pivot[1])
                newPivot[2] = roundFloat(pivot[2])

                return newPivot
            }
        }

        //################################ END ##################################
    }

    private fun roundFloat(data: Float) = (data * 10000.0).roundToInt() / 10000.0f
}
