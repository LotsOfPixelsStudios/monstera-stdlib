package com.lop.devtools.stdlib.packaging

import com.lop.devtools.monstera.Config
import com.lop.devtools.stdlib.packaging.behres.BehResPacker
import com.lop.devtools.stdlib.packaging.marketing.PackMarketing
import com.lop.devtools.stdlib.packaging.skin.PackSkins
import com.lop.devtools.stdlib.packaging.store.PackStoreArt
import com.lop.devtools.stdlib.packaging.world.WorldPacker
import java.io.File

interface Packer {
    val properties: Config
    val buildDir: File
    val targetFile: File

    var unsafeWorldPacker: WorldPacker

    /**
     * the name of the final zip
     */
    var packageName: String

    /**
     * the world to package
     */
    var world: File

    /**
     * the base game version for the manifest
     */
    var baseGameVersion: ArrayList<Int>

    /**
     * lock the settings in the settings menu
     */
    var lockTemplateOptions: Boolean

    /**
     * set the beh pack obj
     */
    var unsafeBehPack: BehResPacker

    /**
     * set the res pack obj
     */
    var unsafeResPack: BehResPacker

    /**
     * add the behaviour pack and modify the title and description
     *
     * ```
     * changeTitle()
     * changeDescription()
     * ```
     */
    fun addBehaviorPack(data: BehResPacker.() -> Unit)

    /**
     * add the resource pack and modify the title and description
     *
     * ```
     * changeTitle()
     * changeDescription()
     * ```
     */
    fun addResourcePack(data: BehResPacker.() -> Unit)

    /**
     * set the skin pack obj
     */
    var unsafePackSkins: PackSkins

    /**
     * set the marketing obj
     */
    var unsafePackMarketing: PackMarketing

    /**
     * set the store obj
     */
    var unsafePackStore: PackStoreArt

    /**
     * add skins to a skin pack, files should end with .png
     */
    fun addSkinPack(data: PackSkins.() -> Unit)

    /**
     * add marketing pictures, files should end with .jpg
     */
    fun addMarketing(data: PackMarketing.() -> Unit)

    /**
     * add store art pictures, files should end with .jpg
     *
     * ```
     * addScreenshot(file: File)
     * addPanorama(file: File)
     * addThumbnail(file: File)
     * addRaw(file: File)
     * ```
     */
    fun addStoreArt(data: PackStoreArt.() -> Unit)

    /**
     * build the zip
     *
     * @param targetZip the zip file, file does not have to exist, will be overwritten
     */
    fun build(targetZip: File = targetFile)
}