package com.lop.devtools.stdlib.packaging.world

import com.lop.devtools.monstera.Config
import java.io.File
import java.util.UUID

interface WorldPacker {
    val properties: Config

    fun generateWorldManifest(
        targetDir: File,
        version: MutableList<Int> = properties.version,
        baseGameVersion: MutableList<Int> = properties.targetMcVersion,
        lockTemplateOptions: Boolean = true,
        uuid: UUID = properties.worldUUID,
        modUuid: UUID = properties.worldModuleUUID,
    )

    fun generateWorldTexts(
        targetDir: File,
        title: String = properties.projectName,
        description: String = properties.description
    )

    fun generateWorldDependencies(
        targetDir: File,
        behUUID: UUID = properties.behUUID,
        behVersion: MutableList<Int> = properties.version,
        resUUID: UUID = properties.resUUID,
        resVersion: MutableList<Int> = properties.version,
    )
}