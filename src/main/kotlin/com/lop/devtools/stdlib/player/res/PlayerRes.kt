package com.lop.devtools.stdlib.player.res

import com.lop.devtools.monstera.addon.Addon
import com.lop.devtools.monstera.addon.entity.Entity
import com.lop.devtools.monstera.addon.entity.resource.ResourceEntity

@Suppress("unused", "MemberVisibilityCanBePrivate")
class PlayerRes(addon: Addon, entData: Entity.Data) : ResourceEntity(entData) {
    var enableAttachable: Boolean = true

    var playerAnims = PlayerResAnimations(addon, unsafeRawEntity)
    var playerTextures = PlayerResTextures(addon, unsafeRawEntity)
    var playerGeos = PlayerResGeometries(addon, unsafeRawEntity)
    var playerScripts = PlayerResScripts(unsafeRawEntity)
    var playerRenderControllers = PlayerResRenderControllers(addon, unsafeRawEntity)

    fun modifyAnimations(data: PlayerResAnimations.() -> Unit) {
        playerAnims.apply(data)
    }

    fun modifyTextures(data: PlayerResTextures.() -> Unit) {
        playerTextures.apply(data)
    }

    fun modifyGeometries(data: PlayerResGeometries.() -> Unit) {
        playerGeos.apply(data)
    }

    fun modifyScripts(data: PlayerResScripts.() -> Unit) {
        playerScripts.apply(data)
    }

    fun modifyRenderController(data: PlayerResRenderControllers.() -> Unit) {
        playerRenderControllers.apply(data)
    }

    override fun build() {
        unsafeRawEntity.description {
            identifier = "minecraft:player"
            material("default", "entity_alphatest")
            material("cape", "entity_alphatest")
            material("animated", "player_animated")
            this.enableAttachables = this@PlayerRes.enableAttachable
        }
        playerTextures.build()
        playerGeos.build()
        playerScripts.build()
        playerAnims.build()
        playerRenderControllers.build()

        super.build()
    }
}