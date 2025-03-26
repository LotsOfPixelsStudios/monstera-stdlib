package com.lop.devtools.stdlib.templateworld

import java.io.File

@Suppress("unused", "MemberVisibilityCanBePrivate")
fun File.modifyTemplateWorldName(worldName: String): File {
    return modifyWorldName(this, worldName)
}

/**
 * @param worldDir has to be the template world provided within the addon template
 * @param worldName the new world name, max 13 characters! (maybe changed if needed)
 */
fun modifyWorldName(worldDir: File, worldName: String): File {
    //copy world
    val targetDir = File("build/tmp/world")
    targetDir.deleteRecursively()
    targetDir.mkdirs()

    worldDir.copyRecursively(targetDir)

    //replace level name in levelname.txt

    File(targetDir, "levelname.txt").writeText(worldName)

    //replace level name (hardcoded to "TemplateWorld")
    val levelDat = File(targetDir, "level.dat")
    if (!levelDat.exists()) return targetDir

    val levelDatBytes = levelDat.readBytes()

    val newNameBytes = worldName.toByteArray()

    for (i in 0..12) {
        try {
            levelDatBytes[515 + i] = newNameBytes[i]
        } catch (e: IndexOutOfBoundsException) {
            levelDatBytes[515 + i] = 0.toByte()
        }
    }

    levelDat.writeBytes(levelDatBytes)
    return targetDir
}
