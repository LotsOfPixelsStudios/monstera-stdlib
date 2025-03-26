package com.tcreative.devtools.stdlib.npc

import com.lop.devtools.monstera.addon.addon
import com.lop.devtools.monstera.config
import com.lop.devtools.monstera.files.getResource
import com.lop.devtools.stdlib.npcs.GeoType
import com.lop.devtools.stdlib.npcs.npc
import kotlin.test.Test

internal class NPCTest {

    //@Test
    fun test() {
        addon(config("test") {
            projectShort = "ts"
        }) {
            npc("test", "Test", this) {
                addTexture(getResource("npcs/skins/Astrid.png"), GeoType.ALEX)
                addTextures(getResource("npcs/skins/steve"), GeoType.STEVE)
            }
        }

        //clean
        java.io.File("build" + java.io.File.separator + "development_behavior_packs").deleteRecursively()
        java.io.File("build" + java.io.File.separator + "development_resource_packs").deleteRecursively()
    }
}