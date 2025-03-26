package com.tcreative.devtools.stdlib.blockbench

import com.lop.devtools.monstera.addon.addon
import com.lop.devtools.monstera.config
import com.lop.devtools.monstera.files.getResource
import com.lop.devtools.stdlib.blockbench.loadBlockbenchFile
import java.awt.Color

fun main() {
    addon(config("blockbench"){
        projectShort = "bl"
    }) {
        buildToMcFolder = false
        entity("test-inflate", "Test Entity") {
            resource {
                loadBlockbenchFile(getResource("bl/test_inflate.bbmodel"))
                components {
                    spawnEgg {
                        eggByColor(Color.CYAN, Color.BLACK)
                    }
                }
            }
            behaviour {
                components {
                    physics {  }
                }
            }
        }

        entity("test-mirror", "Test Entity") {
            resource {
                loadBlockbenchFile(getResource("bl/mirror/modern_table1.bbmodel"))
                components {
                    spawnEgg {
                        eggByColor(Color.CYAN, Color.BLACK)
                    }
                }
            }
            behaviour {
                components {
                    physics {  }
                }
            }
        }
    }
}