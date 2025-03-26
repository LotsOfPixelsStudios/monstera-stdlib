package com.lop.devtools.stdlib.packaging.skin

import com.lop.devtools.monstera.Config
import java.awt.image.BufferedImage
import java.io.File
import java.util.*

interface PackSkins {
    val properties: Config
    val unsafeSkins: ArrayList<File>
    var unsafeManifestFile: File
    var unsafeSkinDefFile: File
    var unsafeTextDir: File

    /**
     * add a skin to the build files
     */
    fun addSkin(file: File, autoCorrect: Boolean)

    /**
     * manually validate a skin
     *
     * @param file the skin file to validate, if the file suffix is _a.png it's handled like an alex skin, if the suffix
     * is _s it's a steve skin, if no suffix matches it will be handled like a steve skin
     * @param autoCorrect indicates if the invisible uv pixels should be cleared
     * @return true if the skin was
     */
    fun validateSkin(file: File, autoCorrect: Boolean): Boolean

    /**
     * checks the image constrain of a given image
     *
     * @param file the image
     * @param model the model to check against
     * @param autoCorrect indicates if the invisible uv pixels should be cleared
     */
    fun checkImageConstrain(file: File, model: PlayerModel, autoCorrect: Boolean): Boolean

    /**
     * check and fill the area within an image
     *
     * @param x1 form
     * @param x2 to
     * @param y1 form
     * @param y2 to
     * @param skinIm the image
     * @param autoCorrect indicates if the invisible uv pixels should be cleared
     */
    fun checkInvisibleArea(
        x1: Int,
        x2: Int,
        y1: Int,
        y2: Int,
        skinIm: BufferedImage,
        autoCorrect: Boolean
    ): Boolean

    /**
     * generate the manifest
     *
     * @return the manifest.json
     */
    fun generateManifest(
        authors: ArrayList<String> = arrayListOf("Timolia Creative"),
        uuid: UUID = UUID.nameUUIDFromBytes(("${properties.projectName}-skin${properties.uuidSalt}".toByteArray())),
        uuidMod: UUID = UUID.nameUUIDFromBytes(("${properties.projectName}-skinMod${properties.uuidSalt}".toByteArray())),
    ): File

    /**
     * generate the skins.json
     *
     * @return the skins.json file
     */
    fun generateSkinsJSON(
        serializeName: String = properties.namespace,
        localizationName: String = properties.namespace,
        skins: ArrayList<File> = unsafeSkins
    ): File
}