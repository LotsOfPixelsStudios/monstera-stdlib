package com.lop.devtools.stdlib.packaging.store

import com.lop.devtools.monstera.Config
import java.io.File

abstract class StoreArtImpl(override val properties: Config): PackStoreArt {
    override val unsafeScreenShots: ArrayList<File> = arrayListOf()
    override val unsafePanorama: ArrayList<File> = arrayListOf()
    override val unsafeThumbnail: ArrayList<File> = arrayListOf()
    override val other: ArrayList<File> = arrayListOf()
    override var packIcon: File? = null
        set(value) {
            val target = File(targetDir, "packicons.jpg")
            value?.copyTo(target, true)
            field = target
        }

    override fun addScreenshot(file: File, index: Int) {
        val target = File(targetDir, "${properties.projectShort.uppercase()}_screenshot_${index}.jpg")
        file.copyTo(target, true)
        unsafeScreenShots.add(target)
    }

    override fun addPanorama(file: File, index: Int) {
        val target = File(targetDir, "${properties.projectShort.uppercase()}_panorama_${index}.jpg")
        file.copyTo(target, true)
        unsafePanorama.add(target)
    }

    override fun addThumbnail(file: File, index: Int) {
        val target = File(targetDir, "${properties.projectShort.uppercase()}_Thumbnail_${index}.jpg")
        file.copyTo(target, true)
        unsafeThumbnail.add(target)
    }

    override fun addRaw(file: File) {
        file.copyTo(File(targetDir, file.name), true)
    }

    override fun buildTo(dir: File) {
        var target = File(dir, "Store Art")
        if (dir.exists() && dir.isDirectory && dir.name == "Store Art") {
            target = dir
        }
        target.mkdirs()

        unsafeScreenShots.forEach {
            it.copyTo(File(target, it.name), true)
        }
        unsafePanorama.forEach {
            it.copyTo(File(target, it.name), true)
        }
        unsafeThumbnail.forEach {
            it.copyTo(File(target, it.name), true)
        }
        other.forEach {
            it.copyTo(File(target, it.name), true)
        }
        packIcon?.let{ it.copyTo(File(target, it.name), true) }
    }
}