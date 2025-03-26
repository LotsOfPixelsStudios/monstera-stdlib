package com.lop.devtools.stdlib.blockbench

import com.lop.devtools.monstera.addon.entity.resource.ResourceEntity
import com.lop.devtools.monstera.addon.molang.Molang
import java.io.File

/**
 * load textures, animations and geometries from a blockbench file (.bbmodel)
 *
 * ```
 * loadBlockbenchFile(getResource("entities/heli.bbmodel"), { Query.variant }) {
 *     //requires the file to be a .bbmodel instead of a .json file
 *     ignoreFileExtension = true
 * }
 * ```
 */
fun ResourceEntity.loadBlockbenchFile(file: File, textureQuery: () -> Molang, config: BlockBenchReader.() -> Unit = {}) {
    loadBlockbenchFile(file, textureQuery().data, config)
}

/**
 * load textures, animations and geometries from a blockbench file (.bbmodel)
 *
 * ```
 * loadBlockbenchFile(getResource("entities/heli.bbmodel"), Query.variant) {
 *     //requires the file to be a .bbmodel instead of a .json file
 *     ignoreFileExtension = true
 * }
 * ```
 */
fun ResourceEntity.loadBlockbenchFile(file: File, textureQuery: Molang, config: BlockBenchReader.() -> Unit = {}) {
    loadBlockbenchFile(file, textureQuery.data, config)
}

/**
 * load textures, animations and geometries from a blockbench file (.bbmodel)
 *
 * ```
 * loadBlockbenchFile(getResource("entities/heli.bbmodel"), Query.variant) {
 *     //requires the file to be a .bbmodel instead of a .json file
 *     ignoreFileExtension = true
 * }
 * ```
 */
fun ResourceEntity.loadBlockbenchFile(file: File, textureQuery: String = "0", config: BlockBenchReader.() -> Unit = {}) {
    val reader = BlockBenchReader.create(file)
    reader.apply(config)

    val textures = reader.getTextures()
    if (textures.size == 1 && !reader.skipTextures) {
        textureLayer(textures[0])
    } else if(!reader.skipTextures) {
        textureLayer(textures, textureQuery)
    }

    if(!reader.skipGeometry)
        geometryLayer(reader.getGeometry())

    val anims = reader.getAnimations()
    if (anims != null && !reader.skipAnimations)
        animation(anims)
}