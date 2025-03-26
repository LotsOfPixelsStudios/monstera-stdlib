package com.lop.devtools.stdlib.player.res

import com.lop.devtools.monstera.addon.Addon
import com.lop.devtools.monstera.addon.entity.getGeoId
import com.lop.devtools.monstera.files.getUniqueFileName
import com.lop.devtools.monstera.files.res.entities.ResEntity
import java.io.File

@Suppress("unused", "MemberVisibilityCanBePrivate")
class PlayerResGeometries(private val addon: Addon, private val entity: ResEntity) {
    var clean: Boolean = false

    var skipDefault: Boolean = false
    var skipCape: Boolean = false

    fun addGeometry(name: String, id: String = "geometry.$name") {
        entity.description {
            geometry(name, id)
        }
    }

    fun addGeometry(file: File, name: String = file.name.removeSuffix(".png")) {
        val target = addon.config.paths.resModels.resolve("entity").resolve(getUniqueFileName(file)).toFile()

        file.copyTo(
            target,
            overwrite = true
        )
        addGeometry(name, getGeoId(file))
    }

    fun build() {
        if (!skipDefault)
            addGeometry("default", "geometry.humanoid.custom")
        if (!skipCape)
            addGeometry("cape", "geometry.cape")
    }
}