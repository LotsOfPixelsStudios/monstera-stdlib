package com.lop.devtools.stdlib.player.res

import com.lop.devtools.monstera.addon.Addon
import com.lop.devtools.monstera.files.res.entities.ResEntity
import java.io.File

@Suppress("unused", "MemberVisibilityCanBePrivate")
class PlayerResTextures(val addon: Addon, private val entity: ResEntity) {
    var clean: Boolean = false

    var skipDefault: Boolean = false
    var skipCape: Boolean = false

    private val textures: ArrayList<Pair<String, String>> = arrayListOf()

    fun addTexture(name: String, path: String) {
        textures.add(name to path)
    }

    fun addTexture(file: File, name: String = file.name.removeSuffix(".png")) {
        file.copyTo(
            addon.config.paths.resTextures.resolve("monstera").resolve("player").resolve(file.name).toFile(),
            overwrite = true
        )
        addTexture(name, "textures/monstera/player/${file.name.removeSuffix(".png")}")
    }

    fun build(): ArrayList<Pair<String, String>> {
        if (clean)
            return textures
        if (!skipDefault)
            addTexture("default", "textures/entity/steve")
        if (!skipCape)
            addTexture("cape", "textures/entity/cape_invisible")
        entity.description {
            textures.forEach {
                texture(it.first, it.second)
            }
        }
        return textures
    }
}