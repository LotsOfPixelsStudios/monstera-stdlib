package com.lop.devtools.stdlib.packaging.skin

import com.google.gson.GsonBuilder
import com.lop.devtools.monstera.Config
import com.lop.devtools.monstera.files.File
import com.lop.devtools.monstera.files.lang.LangFileBuilder
import org.slf4j.LoggerFactory
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import java.util.*
import javax.imageio.ImageIO

class PackSkinImpl(override val properties: Config) : PackSkins {
    override val unsafeSkins: ArrayList<File> = arrayListOf()
    override var unsafeManifestFile: File = File("")
    override var unsafeSkinDefFile: File = File("")
    override var unsafeTextDir: File = File("")

    private val logger = LoggerFactory.getLogger("Packager")

    override fun addSkin(file: File, autoCorrect: Boolean) {
        val buildFile = file.copyTo(File("build", "skin_pack", file.name), true)
        validateSkin(buildFile, autoCorrect)
        unsafeSkins.add(buildFile)
    }

    override fun validateSkin(file: File, autoCorrect: Boolean): Boolean {
        if (!file.name.endsWith(".png")) {
            logger.warn("Wrong file suffix of file '${file.name}'! Should be '.png'")
            return false
        }

        return if (file.name.endsWith("_a.png", true)) {
            checkImageConstrain(file, PlayerModel.ALEX, autoCorrect)
        } else {
            checkImageConstrain(file, PlayerModel.STEVE, autoCorrect)
        }
    }

    override fun checkImageConstrain(file: File, model: PlayerModel, autoCorrect: Boolean): Boolean {
        val image = ImageIO.read(file)
        if (image.width != image.height) {
            logger.warn("Wrong file format: width '${image.width}' != height '${image.height}'")
            return false
        }

        val scale = (image.width / 8)
        val scaleHalf = scale / 2
        val scaleQuarter = scale / 4

        println(scale)

        //head left
        val check1 = checkInvisibleArea(
            x1 = 0,
            x2 = scale - 1,
            y1 = 0,
            y2 = scale - 1,
            image,
            autoCorrect
        )
        //head right + layer left
        val check2 = checkInvisibleArea(
            x1 = scale * 3,
            x2 = scale * 5 - 1,
            y1 = 0,
            y2 = scale - 1,
            image,
            autoCorrect
        )
        //head layer right
        val check3 = checkInvisibleArea(
            x1 = scale * 7,
            x2 = image.width - 1,
            y1 = 0,
            y2 = scale - 1,
            image,
            autoCorrect
        )
        //arm top left
        val check4 = checkInvisibleArea(
            x1 = 0,
            x2 = scaleHalf - 1,
            y1 = scaleHalf * 4,
            y2 = scaleHalf * 5 - 1,
            image,
            autoCorrect
        )
        //arm top middle left
        val check5 = checkInvisibleArea(
            x1 = scaleHalf * 3,
            x2 = scaleHalf * 5 - 1,
            y1 = scaleHalf * 4,
            y2 = scaleHalf * 5 - 1,
            image,
            autoCorrect
        )
        //arm top middle right
        val check6 = checkInvisibleArea(
            x1 = scaleHalf * 9,
            x2 = scaleHalf * 11 - 1,
            y1 = scaleHalf * 4,
            y2 = scaleHalf * 5 - 1,
            image,
            autoCorrect
        )
        //arm top right
        val check7 = checkInvisibleArea(
            x1 = scaleHalf * 13,
            x2 = image.width - 1,
            y1 = scaleHalf * 4,
            y2 = scaleHalf * 5 - 1,
            image,
            autoCorrect
        )
        //right big box
        val check8 = checkInvisibleArea(
            x1 = scaleHalf * 14,
            x2 = image.width - 1,
            y1 = scale * 2,
            y2 = scale * 6 - 1,
            image,
            autoCorrect
        )
        //arm middle left
        val check9 = checkInvisibleArea(
            x1 = 0,
            x2 = scaleHalf - 1,
            y1 = scaleHalf * 8,
            y2 = scaleHalf * 9 - 1,
            image,
            autoCorrect
        )
        //arm top middle left
        val check10 = checkInvisibleArea(
            x1 = scaleHalf * 3,
            x2 = scaleHalf * 5 - 1,
            y1 = scaleHalf * 8,
            y2 = scaleHalf * 9 - 1,
            image,
            autoCorrect
        )
        //arm top middle right
        val check11 = checkInvisibleArea(
            x1 = scaleHalf * 9,
            x2 = scaleHalf * 11 - 1,
            y1 = scaleHalf * 8,
            y2 = scaleHalf * 9 - 1,
            image,
            autoCorrect
        )
        //arm middle right
        val check12 = checkInvisibleArea(
            x1 = scaleHalf * 13,
            x2 = image.width - 1,
            y1 = scaleHalf * 8,
            y2 = scaleHalf * 9 - 1,
            image,
            autoCorrect
        )
        //arm bottom left
        val check13 = checkInvisibleArea(
            x1 = 0,
            x2 = scaleHalf - 1,
            y1 = scaleHalf * 12,
            y2 = scaleHalf * 13 - 1,
            image,
            autoCorrect
        )
        //arm bottom middle left
        val check14 = checkInvisibleArea(
            x1 = scaleHalf * 3,
            x2 = scaleHalf * 5 - 1,
            y1 = scaleHalf * 12,
            y2 = scaleHalf * 13 - 1,
            image,
            autoCorrect
        )
        //arm bottom middle right
        val check15 = checkInvisibleArea(
            x1 = scaleHalf * 11,
            x2 = scaleHalf * 13 - 1,
            y1 = scaleHalf * 12,
            y2 = scaleHalf * 13 - 1,
            image,
            autoCorrect
        )
        //arm bottom middle
        val check16 = checkInvisibleArea(
            x1 = scaleHalf * 7,
            x2 = scaleHalf * 9 - 1,
            y1 = scaleHalf * 12,
            y2 = scaleHalf * 13 - 1,
            image,
            autoCorrect
        )
        //arm bottom right
        val check17 = checkInvisibleArea(
            x1 = scaleHalf * 15,
            x2 = image.width - 1,
            y1 = scaleHalf * 12,
            y2 = scaleHalf * 13 - 1,
            image,
            autoCorrect
        )

        ImageIO.write(image, "png", file)

        if (model == PlayerModel.STEVE)
            return check1
                    && check2
                    && check3
                    && check4
                    && check5
                    && check6
                    && check7
                    && check8
                    && check9
                    && check10
                    && check11
                    && check12
                    && check13
                    && check14
                    && check15
                    && check16
                    && check17

        //arm top right
        val check24 = checkInvisibleArea(
            x1 = scaleQuarter * 25,
            x2 = scaleQuarter * 26 - 1,
            y1 = scaleHalf * 4,
            y2 = scaleHalf * 5 - 1,
            image,
            autoCorrect
        )
        //big box right
        val check18 = checkInvisibleArea(
            x1 = scaleQuarter * 27,
            x2 = scaleQuarter * 28 - 1,
            y1 = scale * 2,
            y2 = scale * 6 - 1,
            image,
            autoCorrect
        )
        //arm middle right
        val check19 = checkInvisibleArea(
            x1 = scaleQuarter * 25,
            x2 = scaleQuarter * 26 - 1,
            y1 = scaleHalf * 8,
            y2 = scaleHalf * 9 - 1,
            image,
            autoCorrect
        )
        //arm bottom right
        val check20 = checkInvisibleArea(
            x1 = scaleQuarter * 29,
            x2 = scaleQuarter * 30 - 1,
            y1 = scaleHalf * 12,
            y2 = scaleHalf * 13 - 1,
            image,
            autoCorrect
        )
        //arm bottom middle-right
        val check21 = checkInvisibleArea(
            x1 = scaleQuarter * 21,
            x2 = scaleQuarter * 23 - 1,
            y1 = scaleHalf * 12,
            y2 = scaleHalf * 13 - 1,
            image,
            autoCorrect
        )
        //leg bottom middle-right
        val check22 = checkInvisibleArea(
            x1 = scaleQuarter * 23,
            x2 = scaleQuarter * 24 - 1,
            y1 = scaleHalf * 13,
            y2 = image.height - 1,
            image,
            autoCorrect
        )
        //leg bottom right
        val check23 = checkInvisibleArea(
            x1 = image.width - scaleQuarter,
            x2 = image.width - 1,
            y1 = scaleHalf * 13,
            y2 = image.height - 1,
            image,
            autoCorrect
        )

        ImageIO.write(image, "png", file)

        return check1
                && check2
                && check3
                && check4
                && check5
                && check6
                && check7
                && check8
                && check9
                && check10
                && check11
                && check12
                && check13
                && check14
                && check15
                && check16
                && check17
                && check18
                && check19
                && check20
                && check21
                && check22
                && check23
                && check24

    }

    override fun checkInvisibleArea(
        x1: Int,
        x2: Int,
        y1: Int,
        y2: Int,
        skinIm: BufferedImage,
        autoCorrect: Boolean
    ): Boolean {
        var check = true
        for (x in x1..x2) {
            for (y in y1..y2) {
                if (Color(skinIm.getRGB(x, y)).alpha != 0) {
                    check = false
                    if (autoCorrect) {
                        skinIm.setRGB(x, y, 0)
                    }
                }
            }
        }
        return check
    }

    override fun generateManifest(authors: ArrayList<String>, uuid: UUID, uuidMod: UUID): File {
        val map = mutableMapOf(
            "format_version" to 1,
            "header" to mutableMapOf(
                "name" to "pack.name",
                "description" to "pack.description",
                "uuid" to uuid,
                "version" to properties.version
            ),
            "modules" to arrayListOf(
                mutableMapOf(
                    "type" to "skin_pack",
                    "uuid" to uuidMod,
                    "version" to properties.version
                )
            ),
            "metadata" to mutableMapOf(
                "authors" to authors
            )
        )

        unsafeManifestFile = File("build", "package", "skin_pack", "manifest.json")
        unsafeManifestFile.parentFile.mkdirs()
        val gson = GsonBuilder().setPrettyPrinting().create()
        unsafeManifestFile.createNewFile()
        unsafeManifestFile.writeText(gson.toJson(map))

        return unsafeManifestFile
    }

    override fun generateSkinsJSON(serializeName: String, localizationName: String, skins: ArrayList<File>): File {
        unsafeTextDir = File("build", "package", "skin_pack", "texts")
        val langBuilder = LangFileBuilder().config { textDir = unsafeTextDir }

        val content = mutableMapOf(
            "skins" to skins.map {
                val fancyName = it.name
                    .removeSuffix(".png")
                    .removeSuffix("_s")
                    .removeSuffix("_a")
                    .replace("_", " ")

                langBuilder.add("skin.${properties.namespace}.${it.name.removeSuffix(".png")}", fancyName)

                if (it.name.endsWith("_a.png"))
                    mutableMapOf(
                        "localization_name" to it.name.removeSuffix(".png"),
                        "geometry" to "geometry.humanoid.customSlim",
                        "texture" to it.name,
                        "type" to "paid"
                    )
                else
                    mutableMapOf(
                        "localization_name" to it.name.removeSuffix(".png"),
                        "geometry" to "geometry.humanoid.custom",
                        "texture" to it.name,
                        "type" to "paid"
                    )
            },
            "serialize_name" to properties.namespace,
            "localization_name" to properties.namespace
        )
        langBuilder.build()
        unsafeSkinDefFile = File("build", "package", "skin_pack", "skins.json")

        if (!unsafeSkinDefFile.exists())
            unsafeSkinDefFile.createNewFile()
        val gson = GsonBuilder().setPrettyPrinting().create()
        unsafeSkinDefFile.writeText(gson.toJson(content))
        return unsafeSkinDefFile
    }
}