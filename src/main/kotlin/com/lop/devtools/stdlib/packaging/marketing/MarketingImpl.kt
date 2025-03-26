package com.lop.devtools.stdlib.packaging.marketing

import com.lop.devtools.monstera.Config
import org.slf4j.LoggerFactory
import java.io.File

abstract class MarketingImpl(override val properties: Config) : PackMarketing {
    override val unsafeScreenShots: ArrayList<File> = arrayListOf()
    override val other: ArrayList<File> = arrayListOf()

    private val logger = LoggerFactory.getLogger("Packager")

    override var keyArt: File = File("")
        set(value) {
            val target = File(targetDir, "${properties.projectShort.uppercase()}_MarketingKeyArt.jpg")
            value.copyTo(target, true)
            field = target
        }
    override var partnerArt: File = File("")
        set(value) {
            val target = File(targetDir, "${properties.projectShort.uppercase()}_PartnerArt.jpg")
            value.copyTo(target, true)
            field = target
        }

    override fun addScreenShot(file: File, index: Int) {
        if (!file.name.endsWith(".jpg")) {
            logger.warn("File '${file.absolutePath}' is not the correct type (.jpg)")
            return
        }
        val targetFile = File(targetDir, "${properties.projectShort.uppercase()}_MarketingScreenshot_$index.jpg")
        file.copyTo(targetFile, true)
        unsafeScreenShots.add(targetFile)
    }

    override fun addRaw(file: File) {
        other.add(file)
    }

    override fun buildTo(dir: File) {
        var target = File(dir, "Marketing Art")

        if (dir.exists() && dir.isDirectory && dir.name == "Marketing Art") {
            target = dir
        }
        target.mkdirs()

        unsafeScreenShots.forEach {
            it.copyTo(File(target, it.name), true)
        }
        other.forEach {
            it.copyTo(File(target, it.name), true)
        }
    }
}