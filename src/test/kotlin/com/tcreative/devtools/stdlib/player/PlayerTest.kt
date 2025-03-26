package com.tcreative.devtools.stdlib.player

import com.lop.devtools.monstera.addon.addon
import com.lop.devtools.monstera.addon.molang.Query
import com.lop.devtools.monstera.addon.testAddon
import com.lop.devtools.stdlib.player.player
import java.io.File
import kotlin.io.path.exists
import kotlin.test.Test

internal class PlayerTest {

    @Test
    fun test() {
        addon("test") {
            buildToMcFolder = true
            player {
                playerBeh {
                    modifyBehComponents {
                        properties {
                            bool("test") {
                                default(false)
                                clientSync = true
                            }
                        }
                        components {
                            markVariant = 1
                        }

                        componentGroup("a") {
                            markVariant = 2
                        }

                        event("test_event") {
                            setProperty("test", true, this@addon)
                        }
                    }

                    animController("testing") {
                        state("default") {
                            onEntry = arrayListOf("/say init test")
                            transition("test_state", Query.True )
                        }
                        state("test_state") {
                            onEntry = arrayListOf("/say initialized")
                            transition("test", Query.property("test"))
                        }
                        state("test") {
                            onEntry = arrayListOf("/say property test")
                        }

                    }
                }
                playerRes {
                    modifyGeometries {
                        addGeometry("test")
                    }

                    modifyRenderController {
                        skipThirdPerson = true
                        addCustomRenderController(
                            "third_person",
                            "!variable.is_first_person && !variable.map_face_icon"
                        )
                    }
                }
            }

            //clean
            File("build" + File.separator + "development_behavior_packs").deleteRecursively()
            File("build" + File.separator + "development_resource_packs").deleteRecursively()
        }
    }

    @Test
    fun emptyFileTest() {
        addon("test") {
            player {  }
        }
    }
}