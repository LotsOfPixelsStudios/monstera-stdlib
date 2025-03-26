package com.lop.devtools.stdlib.packaging.skin

import com.google.gson.GsonBuilder
import com.lop.devtools.monstera.Config
import com.lop.devtools.monstera.files.File
import com.lop.devtools.monstera.files.getResource
import com.lop.devtools.monstera.files.lang.LangFileBuilder
import org.slf4j.LoggerFactory
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

fun addSkins(validate: Boolean = true, properties: Config, skinResourceDir: File = getResource("skin_pack")) {
    val skins = arrayListOf<MutableMap<String, Any>>()
    val logger = LoggerFactory.getLogger("Packager")

    val langBuilder = LangFileBuilder().config { textDir = File("build", "skin_pack", "texts") }
    langBuilder.add("skinpack.${properties.namespace}", properties.projectName)

    skins.apply {
        skinResourceDir
            .walk()
            .forEach {
                if (it.name.endsWith(".png")) {
                    val newFile = it.copyTo(File("build", "skin_pack", File.separator + it.name))
                    val id = it.name.removeSuffix(".png")

                    //alex/slim skin
                    if (id.endsWith("_a")) {
                        if (validate)
                            checkSkinImageConstrains(newFile, PlayerModel.ALEX)
                        add(
                            mutableMapOf(
                                "localization_name" to id,
                                "geometry" to "geometry.humanoid.customSlim",
                                "texture" to it.name,
                                "type" to "paid"
                            )
                        )
                        //steve skin
                    } else if (id.endsWith("_s")) {
                        if (validate)
                            checkSkinImageConstrains(newFile, PlayerModel.STEVE)
                        add(
                            mutableMapOf(
                                "localization_name" to id,
                                "geometry" to "geometry.humanoid.custom",
                                "texture" to it.name,
                                "type" to "paid"
                            )
                        )
                    } else {
                        logger.warn("Filename does not match Suffix '_s' or '_a' of file ${it.name} (skipping)")
                        return
                    }

                    val fancyName = id
                        .removeSuffix("_s")
                        .removeSuffix("_a")
                        .replace("_", " ")

                    langBuilder.add("skin.${properties.namespace}.$id", fancyName)

                } else if (it.name != ".gitkeep" && it.name != "skin_pack") {
                    logger.warn("Skin not accepted ${it.name} (wrong suffix, should be .png)")
                }
            }
    }

    val content = mutableMapOf(
        "skins" to skins,
        "serialize_name" to properties.namespace,
        "localization_name" to properties.namespace
    )

    val skinDefFile = File("build", "skin_pack", "skins.json")

    skinDefFile.createNewFile()
    val gson = GsonBuilder().setPrettyPrinting().create()
    skinDefFile.writeText(gson.toJson(content))
    langBuilder.build()
}

/**
 * the skin has to be invisible at parts that are not visible within minecraft
 */
fun checkSkinImageConstrains(skin: File, type: PlayerModel) {
    val logger = LoggerFactory.getLogger("Packager")
    val skinIm = ImageIO.read(skin)
    var warnDev = false
    //all invisible spaces on the steve skin are also invisible on the alex skin
    //Steve

    //row 1
    //head 1
    if (fillColor(0, 7, 0, 7, skinIm))
        warnDev = true
    //head 2 + layer head 1
    if (fillColor(24, 39, 0, 7, skinIm))
        warnDev = true
    //layer head 2
    if (fillColor(56, 63, 0, 7, skinIm))
        warnDev = true

    //row 2
    //body 1
    if (fillColor(0, 3, 16, 19, skinIm))
        warnDev = true
    //body 2
    if (fillColor(12, 19, 16, 19, skinIm))
        warnDev = true
    //body 3
    if (fillColor(36, 43, 16, 19, skinIm))
        warnDev = true
    //body 4
    if (fillColor(52, 63, 16, 19, skinIm))
        warnDev = true
    //row 3
    if (fillColor(56, 63, 20, 31, skinIm))
        warnDev = true

    //row 4
    //leg 1
    if (fillColor(0, 3, 32, 35, skinIm))
        warnDev = true
    //leg 2
    if (fillColor(12, 19, 32, 35, skinIm))
        warnDev = true
    //leg 3
    if (fillColor(36, 43, 32, 35, skinIm))
        warnDev = true
    //leg 4
    if (fillColor(52, 63, 32, 35, skinIm))
        warnDev = true

    //row 5
    if (fillColor(56, 63, 36, 47, skinIm))
        warnDev = true

    //row 6
    //arm 1
    if (fillColor(0, 3, 47, 51, skinIm))
        warnDev = true
    //arm 2
    if (fillColor(12, 19, 47, 51, skinIm))
        warnDev = true
    //arm 3
    if (fillColor(28, 35, 47, 51, skinIm))
        warnDev = true
    //arm 4
    if (fillColor(44, 51, 47, 51, skinIm))
        warnDev = true
    //arm 5
    if (fillColor(60, 63, 47, 51, skinIm))
        warnDev = true

    //alex
    if (type == PlayerModel.ALEX) {
        if (fillColor(50, 51, 16, 19, skinIm))
            warnDev = true
        if (fillColor(50, 51, 32, 35, skinIm))
            warnDev = true

        if (fillColor(54, 55, 20, 31, skinIm))
            warnDev = true
        if (fillColor(54, 55, 36, 47, skinIm))
            warnDev = true

        if (fillColor(58, 59, 48, 51, skinIm))
            warnDev = true
        if (fillColor(42, 43, 48, 51, skinIm))
            warnDev = true

        if (fillColor(46, 47, 52, 63, skinIm))
            warnDev = true
        if (fillColor(62, 63, 52, 63, skinIm))
            warnDev = true
    }

    if (warnDev)
        logger.warn("Modified ${skin.name}")

    ImageIO.write(skinIm, "png", skin)
}

private fun fillColor(x1: Int, x2: Int, y1: Int, y2: Int, skinIm: BufferedImage): Boolean {
    var warnDev = false
    for (x in x1..x2) {
        for (y in y1..y2) {
            if (Color(skinIm.getRGB(x, y)).alpha != 0) {
                warnDev = true
                skinIm.setRGB(x, y, 0)
            }
        }
    }
    return warnDev
}

enum class PlayerModel {
    STEVE,
    ALEX
}