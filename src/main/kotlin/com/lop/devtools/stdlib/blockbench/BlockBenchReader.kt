package com.lop.devtools.stdlib.blockbench

import com.google.gson.Gson
import com.lop.devtools.stdlib.blockbench.filestruc.BlBase
import com.lop.devtools.stdlib.blockbench.parse.anim.BlockBenchAnimParser
import com.lop.devtools.stdlib.blockbench.parse.geo.BlockBenchGeoParser
import com.lop.devtools.stdlib.blockbench.parse.img.BlockBenchImageParser
import org.slf4j.LoggerFactory
import java.io.File

class BlockBenchReader(private val file: File) {
    companion object {
        fun create(file: File): BlockBenchReader {
            return BlockBenchReader(file)
        }

        fun create(file: File, data: BlockBenchReader.() -> Unit) {
            BlockBenchReader(file).apply(data)
        }
    }

    val unsafe = Unsafe()
    var ignoreFileExtension: Boolean = false
    var debugOutput: Boolean = false
    private val gson: Gson = Gson()
    private val logger = LoggerFactory.getLogger("bbmodel parser")

    var skipAnimations: Boolean = false
    var skipGeometry: Boolean = false
    var skipTextures: Boolean = false

    inner class Unsafe {
        val base: BlBase by lazy {
            if(!ignoreFileExtension && !file.name.endsWith(".bbmodel")) {
                logger.warn("Loading Blockbench file with wrong extension")
            }
            gson.fromJson(file.readText(), BlBase::class.java)
        }
    }

    fun getTextures(): ArrayList<File> {
        return BlockBenchImageParser.getTextures(unsafe.base)
    }

    fun getGeometry(): File {
        return BlockBenchGeoParser.getGeometry(unsafe.base)
    }

    fun getAnimations(): File? {
        if(unsafe.base.animations == null)
            return null
        return BlockBenchAnimParser.getAnimation(unsafe.base)
    }
}