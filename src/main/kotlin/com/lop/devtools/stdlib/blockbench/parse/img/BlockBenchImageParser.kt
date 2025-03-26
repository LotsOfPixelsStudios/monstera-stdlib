package com.lop.devtools.stdlib.blockbench.parse.img

import com.lop.devtools.monstera.files.File
import com.lop.devtools.stdlib.blockbench.filestruc.BlBase
import java.io.ByteArrayInputStream
import java.io.File
import java.util.*
import javax.imageio.ImageIO
import kotlin.collections.ArrayList

object BlockBenchImageParser {
    val tmpDir = File("build", "tmp", "blockbench", "textures")

    fun getTextures(base: BlBase): ArrayList<File> {
        val targetTextures = arrayListOf<File>()

        val tmpDirID = File(tmpDir, base.modelIdentifier.replace("\\s".toRegex(), ""))
        tmpDirID.deleteRecursively()
        tmpDirID.mkdirs()

        base.textures.forEachIndexed { index, it ->
            val target = File(tmpDirID, base.modelIdentifier + "_$index" + ".png")
            val str = it.source?.removePrefix("data:image/png;base64,")
            val imageBytes = Base64.getDecoder().decode(str)
            val imag = ImageIO.read(ByteArrayInputStream(imageBytes))
            ImageIO.write(imag, "png", target)

            targetTextures.add(target)
        }

        return targetTextures
    }
}