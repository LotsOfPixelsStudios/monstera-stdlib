package com.lop.devtools.stdlib.player

import com.lop.devtools.monstera.MonsteraLoggerContext
import com.lop.devtools.monstera.addon.Addon
import com.lop.devtools.monstera.addon.entity.Entity
import com.lop.devtools.monstera.addon.sound.unsafeApplySoundData
import com.lop.devtools.stdlib.player.beh.PlayerBeh
import com.lop.devtools.stdlib.player.res.PlayerRes

fun Addon.player(data: Player.() -> Unit): Player {
    MonsteraLoggerContext.setEntity("player")
    val player = ((entities["player"] ?: Player(this)) as Player).apply(data)
    entities["player"] = player
    MonsteraLoggerContext.clear()
    return player
}

class Player(addon: Addon) : Entity(addon, "player") {
    override val unsafeBehaviourEntity = PlayerBeh(addon, entData = data)
    override val unsafeResourceEntity = PlayerRes(addon, entData = data)

    private var buildRes = false
    private var buildBeh = false

    fun playerBeh(entity: PlayerBeh.() -> Unit) {
        buildBeh = true
        unsafeBehaviourEntity.apply(entity)
    }

    fun playerRes(entity: PlayerRes.() -> Unit) {
        buildRes = true
        unsafeResourceEntity.apply(entity)
    }

    override fun build() {
        if (buildBeh)
            unsafeBehaviourEntity.build()
        if (buildRes)
            unsafeResourceEntity.build()

        if (data.sounds.isNotEmpty()) {
            addon.unsafeApplySoundData(data.sounds, getIdentifier())
        }
    }
}