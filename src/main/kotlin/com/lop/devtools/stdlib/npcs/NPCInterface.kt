package com.lop.devtools.stdlib.npcs

import com.lop.devtools.monstera.addon.Addon
import com.lop.devtools.monstera.addon.entity.behaviour.BehaviourEntity
import com.lop.devtools.monstera.addon.entity.resource.ResourceEntity
import com.lop.devtools.monstera.files.res.entities.comp.ResEntitySpawnEgg
import java.io.File

interface NPCInterface {
    var name: String
    var displayName: String
    val addon: Addon

    var scale : Float
    var walkingSpeed: Float

    var invincible: Boolean

    var avoidSun: Boolean
    var avoidBlocks: ArrayList<String>



    fun addTexture(file: File, geometry: GeoType)

    fun addTextures(textureFolder: File, geometry: GeoType)

    fun behaviour(behavior: BehaviourEntity.() -> Unit)

    fun resource(resource: ResourceEntity.() -> Unit)

    fun icon(data: ResEntitySpawnEgg.() -> Unit)

    fun ResEntitySpawnEgg.eggByFile(file: File) {
        eggByFile(file, addon)
    }
}