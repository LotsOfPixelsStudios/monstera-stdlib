package com.lop.devtools.stdlib.packaging.store

import com.lop.devtools.monstera.Config
import java.io.File

interface PackStoreArt {
    val properties: Config
    val targetDir: File

    val unsafeScreenShots: ArrayList<File>
    val unsafePanorama: ArrayList<File>
    val unsafeThumbnail: ArrayList<File>
    val other: ArrayList<File>
    var packIcon: File?

    fun addScreenshot(file: File, index: Int = unsafeScreenShots.size)
    fun addPanorama(file: File, index: Int = unsafePanorama.size)
    fun addThumbnail(file: File, index: Int = unsafeThumbnail.size)

    fun addRaw(file: File)

    fun buildTo(dir: File)
}