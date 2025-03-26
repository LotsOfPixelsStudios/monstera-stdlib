package com.lop.devtools.stdlib.packaging.world

import com.google.gson.GsonBuilder
import com.lop.devtools.monstera.Config
import com.lop.devtools.monstera.files.lang.LangFileBuilder
import java.io.File
import java.util.*

class WorldPackerImpl(override val properties: Config): WorldPacker {
    override fun generateWorldManifest(
        targetDir: File,
        version: MutableList<Int>,
        baseGameVersion: MutableList<Int>,
        lockTemplateOptions: Boolean,
        uuid: UUID,
        modUuid: UUID
    ) {
        val manifestData = mutableMapOf(
            "header" to mutableMapOf<String, Any>(
                "name" to "pack.name",
                "description" to "pack.description",
                "version" to version,
                "uuid" to uuid,
                "base_game_version" to baseGameVersion,
                "lock_template_options" to lockTemplateOptions
            ),
            "modules" to arrayListOf<Any>(
                mutableMapOf<String, Any>(
                    "version" to version,
                    "type" to "world_template",
                    "uuid" to modUuid
                )
            ),
            "format_version" to 2
        )

        val raw = GsonBuilder().setPrettyPrinting().create().toJson(manifestData)
        val target = File(targetDir, "manifest.json")
        target.createNewFile()
        target.writeText(raw)
    }

    override fun generateWorldTexts(targetDir: File, title: String, description: String) {
        LangFileBuilder()
            .config { textDir = targetDir }
            .add("pack.name", title)
            .add("pack.description", description)
            .build()
    }

    override fun generateWorldDependencies(
        targetDir: File,
        behUUID: UUID,
        behVersion: MutableList<Int>,
        resUUID: UUID,
        resVersion: MutableList<Int>
    ) {
        val gson = GsonBuilder().setPrettyPrinting().create()
        val dependOnBehPack = gson.toJson(
            arrayListOf(
                mutableMapOf(
                    "pack_id" to behUUID,
                    "version" to behVersion
                )
            )
        )
        val dependOnResPack = gson.toJson(
            arrayListOf(
                mutableMapOf(
                    "pack_id" to resUUID,
                    "version" to resVersion
                )
            )
        )
        File(targetDir, "world_behavior_packs.json").createNewFile()
        File(targetDir, "world_resource_packs.json").createNewFile()
        File(targetDir, "world_behavior_packs.json").writeText(dependOnBehPack)
        File(targetDir, "world_resource_packs.json").writeText(dependOnResPack)
    }
}