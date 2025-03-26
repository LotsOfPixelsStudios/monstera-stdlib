package com.tcreative.devtools.stdlib.vehicles

import com.lop.devtools.monstera.addon.addon
import com.lop.devtools.monstera.files.getResource
import com.lop.devtools.stdlib.templateworld.modifyTemplateWorldName
import com.lop.devtools.stdlib.vehicles.car
import org.junit.jupiter.api.Test

internal class VehicleTest {
    //@Test
    fun test() {
        addon("test")
        {
            buildToMcFolder =true
            config.world = getResource("world/template-world").modifyTemplateWorldName("vehicleLib")
            car("test", "Car 1", this) {
                geometry = getResource("vehicles/pick-up/pick-up.geo.json")
                texture = getResource("vehicles/pick-up/jeep_r_brown.png")
                width = 2.4f
                height = 2f

                engine {
                    enginePitch = 1.2f
                    engineType = com.lop.devtools.stdlib.vehicles.shared.EngineType.NORMAL
                }
                wheelSize = 40f

                addSeat(Triple(1,1,0))
            }
            car("test_2", "Car 2", this) {
                geometry = getResource("vehicles/pick-up/pick-up.geo.json")
                texture = getResource("vehicles/pick-up/jeep_r_brown.png")
                width = 2.4f
                height = 2f

                engine {
                    enginePitch = 1.2f
                    engineType = com.lop.devtools.stdlib.vehicles.shared.EngineType.NORMAL
                }

                addSeat(Triple(1, 10, 0))
            }
        }

        //clean
        //java.io.File("build" + java.io.File.separator + "development_behavior_packs").deleteRecursively()
        //java.io.File("build" + java.io.File.separator + "development_resource_packs").deleteRecursively()
    }
}