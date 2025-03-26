package com.lop.devtools.stdlib.packaging.behres

import com.lop.devtools.monstera.Config
import com.lop.devtools.monstera.files.File
import java.io.File

interface BehResPacker {
    val properties: Config
    val sourceDir: File

    fun changeTitle(title: String, textFile: File = File(sourceDir.absolutePath, "texts", "en_US.lang"))
    fun changeDescription(description: String, textFile: File = File(sourceDir.absolutePath, "texts", "en_US.lang"))

    fun buildTo(dir: File)
}