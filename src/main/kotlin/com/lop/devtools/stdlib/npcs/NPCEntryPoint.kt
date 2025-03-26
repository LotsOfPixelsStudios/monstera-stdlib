package com.lop.devtools.stdlib.npcs

import com.lop.devtools.monstera.addon.Addon
import com.lop.devtools.monstera.files.getResource

/**
 *
 * @sample sampleNPC
 */
fun npc(name: String, displayName: String, addon: Addon, data: NPC.() -> Unit) {
    NPC(name, displayName, addon).apply(data).build()
}

fun sampleNPC() {
    npc("npc", "NPC", Addon.active!!) {
        scale = 1f
        walkingSpeed = 0.23f

        invincible = false
        avoidSun = false
        avoidBlocks = arrayListOf("minecraft:stone")


        //Can also be called at the same time.
        //Skin and geometry will be randomly picked when the entity is spawned
        addTexture(getResource("steve_npc.png"), GeoType.STEVE)
        addTextures(getResource("general/npcs/alex_skins"), GeoType.ALEX)

        behaviour {
            components {
                //variant and skinID are reserved for geometry and textures
                markVariant = 2
            }
        }
        resource {
            animation(getResource("npc/npc.animations.json"))
        }
        icon {
            eggByFile(getResource("npcs/spawn_icon.png"))
        }
    }
}
