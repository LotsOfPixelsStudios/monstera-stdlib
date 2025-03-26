package com.lop.devtools.stdlib.packaging.marketing

import com.lop.devtools.monstera.Config
import java.io.File

interface PackMarketing {
    val properties: Config

    /**
     * the internal build/tmp directory
     */
    val targetDir: File

    /**
     * the screenshots in the correct format
     */
    val unsafeScreenShots: ArrayList<File>

    /**
     * if addRaw() was called
     */
    val other: ArrayList<File>

    /**
     * the partner art
     */
    var partnerArt: File

    /**
     * the key art
     */
    var keyArt: File

    /**
     * add a screenshot to the marketing files
     */
    fun addScreenShot(file: File, index: Int = unsafeScreenShots.size + 1)

    /**
     * rawly add a picture, dev has to ensure the naming conventions matches
     */
    fun addRaw(file: File)

    /**
     * copy the validated files into a directory
     *
     * @param dir the 'Marketing Art' directory or the parent of the 'Marketing Art' directory
     */
    fun buildTo(dir: File)
}