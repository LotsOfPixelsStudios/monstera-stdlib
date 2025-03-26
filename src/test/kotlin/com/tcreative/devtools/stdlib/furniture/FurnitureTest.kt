package com.tcreative.devtools.stdlib.furniture

import com.lop.devtools.monstera.addon.addon
import com.lop.devtools.stdlib.furnitures.furniture
import com.lop.devtools.stdlib.furnitures.furniturepresets.lamp.util.LightState
import com.lop.devtools.stdlib.furnitures.lamp
import com.lop.devtools.stdlib.furnitures.shared.FurnitureDropBehaviour
import org.junit.jupiter.api.Test

internal class FurnitureTest {

    //@Test
    fun furnitureTest() {
        addon("test") {
            lamp("test_furniture", "Test Furniture", this) {
                autoRotationAdjustment = true
                dropBehaviour = FurnitureDropBehaviour.CAN_PICKUP
                lightSource {
                    x = 0f
                    y = 0f
                    z = 0f
                    brightness = 15
                }
                initialLightState = LightState.ON
            }
        }

        //clean
        java.io.File("build" + java.io.File.separator + "development_behavior_packs").deleteRecursively()
        java.io.File("build" + java.io.File.separator + "development_resource_packs").deleteRecursively()
    }
}