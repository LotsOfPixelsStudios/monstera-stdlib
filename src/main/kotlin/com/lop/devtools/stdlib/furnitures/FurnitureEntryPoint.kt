package com.lop.devtools.stdlib.furnitures

import com.lop.devtools.monstera.addon.Addon
import com.lop.devtools.monstera.files.getResource
import com.lop.devtools.stdlib.furnitures.furniturepresets.GenericFurniture
import com.lop.devtools.stdlib.furnitures.furniturepresets.lamp.GenericLamp
import com.lop.devtools.stdlib.furnitures.furniturepresets.lamp.util.LightState
import com.lop.devtools.stdlib.furnitures.furniturepresets.seat.GenericSeatingFurniture
import com.lop.devtools.stdlib.furnitures.furniturepresets.storage.GenericStorageFurniture
import com.lop.devtools.stdlib.furnitures.shared.FurnitureDropBehaviour

/**
 * Note: you have access to behaviour {} and resource {} but it might be beneficial to code the entire entity yourself
 * if you have to use these functions
 *
 * @sample furnitureSample
 */
fun furniture(name: String, displayName: String, addon: Addon, data: GenericFurniture.() -> Unit) {
    GenericFurniture(name, displayName, addon).apply(data).build()
}

/**
 * Note: you have access to behaviour {} and resource {} but it might be beneficial to code the entire entity yourself
 * if you have to use these functions
 *
 * @sample lampSpecSample
 * @sample furnitureSample
 */
fun lamp(name: String, displayName: String, addon: Addon, data: GenericLamp.() -> Unit) {
    GenericLamp(name, displayName, addon).apply(data).buildLamp()
}

/**
 * @sample sampleSeat
 * @sample furnitureSample
 */
fun seatingFurniture(name: String, displayName: String, addon: Addon, data: GenericSeatingFurniture.() -> Unit) {
    GenericSeatingFurniture(name, displayName, addon).apply(data).buildSeatingFurniture()
}

fun storageFurniture(name: String, displayName: String, addon: Addon, data: GenericStorageFurniture.() -> Unit) {
    GenericStorageFurniture(name, displayName, addon).apply(data).buildStorageFurniture()
}

private fun furnitureSample() {
    furniture("vase", "Vase", Addon.active!!) {
        texture = getResource("vase/vase.png")
        geometry = getResource("vase/vase.geo.json")

        height = 0.7f
        width = 0.3f
        scale = 1f

        autoRotationAdjustment = false
        dropBehaviour = FurnitureDropBehaviour.CUSTOM_LOOT

        behaviour {
            components {
                markVariant = 2
            }
        }
        resource {
            animation(getResource("vase/vase.animations.json"))
        }
        icon {
            eggByFile(getResource("vase/item.png"), addon)
        }
        breakable {
            animation("break", brokeAnimName = "broke")
            //or
            animation("break", despawnDelay = 2f)

            loot(1f) {
                //loot table
                pool { }
            }
            onBreak = arrayListOf("/say breaking")
        }
    }
}

private fun lampSpecSample() {
    lamp("vase", "Vase", Addon.active!!) {
        lightSwitchOnInteract = true
        initialLightState = LightState.OFF
        hangingLamp = false

        lightSource {
            x = 1f
            y = 0f
            z = 0f
            brightness = 15
        }
    }
}

private fun sampleSeat() {
    seatingFurniture("vase", "Vase", Addon.active!!) {
        addSeat(Triple(1f, 2, 3f))
    }
}
