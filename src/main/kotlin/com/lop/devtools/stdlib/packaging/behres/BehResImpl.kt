package com.lop.devtools.stdlib.packaging.behres

import com.lop.devtools.monstera.Config
import java.io.File

open class BehResImpl(override val properties: Config, override val sourceDir: File) : BehResPacker {
    override fun changeTitle(title: String, textFile: File) {
        var text = ""
        if (textFile.exists()) {
            text = textFile.readText()
            if (text.contains("pack\\.name=.*(\\r\\n|\\r|\\n)".toRegex())) {
                text = text.replace("pack\\.name=.*(\\r\\n|\\r|\\n)".toRegex(), "pack.name=$title\n")
                textFile.writeText(text)
                return
            }
        } else {
            textFile.parentFile.mkdirs()
            textFile.createNewFile()
        }

        text = if (text.endsWith("\n")) {
            "${text}pack.name=$title\n"
        } else {
            "$text\npack.name=$title\n"
        }
        textFile.writeText(text)
    }

    override fun changeDescription(description: String, textFile: File) {
        var text = ""
        if (textFile.exists()) {
            text = textFile.readText()
            if (text.contains("pack\\.description=.*(\\r\\n|\\r|\\n)".toRegex())) {
                text = text.replace("pack\\.description=.*(\\r\\n|\\r|\\n)".toRegex(), "pack.name=$description\n")
                textFile.writeText(text)
                return
            }
        } else {
            textFile.parentFile.mkdirs()
            textFile.createNewFile()
        }

        text = if (text.endsWith("\n")) {
            "${text}pack.name=$description\n"
        } else {
            "$text\npack.name=$description\n"
        }
        textFile.writeText(text)
    }

    override fun buildTo(dir: File) {
        sourceDir.copyRecursively(dir, true)
    }
}