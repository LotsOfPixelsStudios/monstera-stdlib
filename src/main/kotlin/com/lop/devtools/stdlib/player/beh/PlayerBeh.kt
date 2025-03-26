package com.lop.devtools.stdlib.player.beh

import com.lop.devtools.monstera.addon.Addon
import com.lop.devtools.monstera.addon.entity.Entity
import com.lop.devtools.monstera.addon.entity.behaviour.BehaviourEntity
import com.lop.devtools.monstera.files.animcontroller.AnimationControllers
import com.lop.devtools.monstera.files.beh.animations.BehAnimation
import com.lop.devtools.monstera.files.beh.animations.BehAnimations
import com.lop.devtools.monstera.files.beh.entitiy.BehEntity
import com.lop.devtools.monstera.files.getVersionAsString
import kotlin.io.path.Path

@Suppress("unused", "MemberVisibilityCanBePrivate")
class PlayerBeh(
    addon: Addon,
    entData: Entity.Data
) : BehaviourEntity(entData) {
    val unsafePlayerComponents = PlayerBehComponents(addon, unsafeRawEntity)

    fun modifyBehComponents(data: PlayerBehComponents.() -> Unit) {
        unsafePlayerComponents.apply(data)
    }

    override fun build() {
        unsafePlayerComponents.buildComponents()
        unsafePlayerComponents.buildComponentGroups()
        unsafePlayerComponents.buildEvents()
        super.build()
    }
}