package com.lop.devtools.stdlib.packaging

import com.lop.devtools.monstera.addon.Addon
import com.lop.devtools.monstera.files.getResource
import org.slf4j.LoggerFactory

/**
 * ```
 * world = getResource("world/my_world")
 * baseGameVersion = arrayListOf(1, 19, 0)
 * lockTemplateOptions = true
 * addBehaviorPack {    //default
 *      title = "only the best name"
 *      description = "my custom description that modifies what in prop is defined"
 * }
 * addResourcePack {    //default
 *      title = "this name is for the resource pack only"
 * }
 * addStoreArt {}
 * addSkinPack {}
 * addMarketing {}
 * ```
 */
fun Addon.packaging(data: Packer.() -> Unit) {
    val logger = LoggerFactory.getLogger("Packager")

    val packaging = PackerImpl(config)
    packaging.baseGameVersion = ArrayList(config.targetMcVersion)

    packaging.addBehaviorPack {  }
    packaging.addResourcePack {  }
    packaging.world = config.world

    packaging.addStoreArt {
        try {
            getResource("package/Store-Art").walk().forEach {
                if (it.endsWith(".jpg"))
                    addRaw(it)
            }
        } catch (e: Exception) {
            logger.info("Can't find Store Art")
        }
    }
    packaging.addMarketing {
        try {
            getResource("package/Marketing-Art").walk().forEach {
                if (it.endsWith(".jpg"))
                    addRaw(it)
            }
        } catch (e: Exception) {
            logger.info("Can't find Marketing Art")
        }
    }
    packaging.apply(data).build()
}

/**
 * ```
 * world = getResource("world/my_world")
 * baseGameVersion = arrayListOf(1, 19, 0)
 * lockTemplateOptions = true
 * addBehaviorPack {
 *      packName = "only the best name"
 *      packDescription = "my custom description that modifies what in prop is defined"
 * }
 * addResourcePack {
 *      packName = "this name is for the resource pack only"
 * }
 * addStoreArt {
 *      dir = getResource("package/Store-Art")
 * }
 * addSkinPack(false)
 * addMarketing {
 *      dir = getResource("package/Marketing-Art")
 * }
 * ```
 */
fun Addon.packagingWithoutDefaults(data: Packer.() -> Unit) {
    PackerImpl(config).apply(data).build()
}