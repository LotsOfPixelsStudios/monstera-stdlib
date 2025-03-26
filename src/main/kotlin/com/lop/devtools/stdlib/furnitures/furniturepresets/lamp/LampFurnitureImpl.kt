package com.lop.devtools.stdlib.furnitures.furniturepresets.lamp

import com.lop.devtools.monstera.addon.Addon
import com.lop.devtools.monstera.addon.molang.Query
import com.lop.devtools.monstera.addon.molang.and
import com.lop.devtools.stdlib.furnitures.FurnitureImpl
import com.lop.devtools.stdlib.furnitures.furniturepresets.lamp.util.LightSource
import com.lop.devtools.stdlib.furnitures.furniturepresets.lamp.util.LightState

abstract class LampFurnitureImpl(
    name: String,
    displayName: String,
    addon: Addon
) : LampFurniture, FurnitureImpl(
    name, displayName, addon
) {
    override var lightSwitchOnInteract: Boolean = false

    /**
     * lamp is off when [Query.isBaby] is true
     */
    override var initialLightState: LightState = LightState.ON

    /**
     * disables gravity
     */
    override var hangingLamp: Boolean = false

    private var lightSource: LightSource = LightSource()

    //modify behaviour and set light with anim controller
    override fun buildLamp() {
        super.behaviour {
            animController("$name.light_placer") {
                initialState = "initial"
                state("initial") {
                    transition("place_light", Query.isBaby)
                }
                state("place_light") {
                    onEntry = arrayListOf(
                        "/setblock ~${lightSource.x} ~${lightSource.y} ~${lightSource.z} light_block [\"block_light_level\" = ${lightSource.brightness}]"
                    )
                    transition("remove_light_on_despawn", Query.property("despawn"))
                    transition("remove_light", !Query.isAlive)
                    transition("remove_light", Query.isTransforming)
                    transition("remove_light", !Query.isBaby)
                    transition("remove_light_on_falling", !Query.isOnGround)
                }
                state("remove_light") {
                    onEntry = arrayListOf(
                        "/setblock ~${lightSource.x} ~${lightSource.y} ~${lightSource.z} air"
                    )
                    transition("place_light", Query.isBaby)
                }
                state("remove_light_on_despawn") {
                    onEntry = arrayListOf(
                        "/setblock ~${lightSource.x} ~${lightSource.y} ~${lightSource.z} air"
                    )
                }
                state("remove_light_on_falling") {
                    onEntry = arrayListOf(
                        "/setblock ~${lightSource.x + 0.2} ~${lightSource.y + 0.2} ~${lightSource.z + 0.2} air",
                    )
                    transition("initial", Query.isBaby and Query.isOnGround)
                }
            }

            if (hangingLamp) {
                components {
                    physics {
                        hasGravity = false
                    }
                }
            }

            if (lightSwitchOnInteract) {
                componentGroup("light_switch_on") {
                    isBaby { }
                    interact {
                        interaction {
                            onInteract {
                                event = "light_switch_off"
                            }
                            playSound = "random.click"
                        }
                    }
                }
                componentGroup("light_switch_off") {
                    interact {
                        interaction {
                            playSound = "random.click"
                            onInteract {
                                event = "light_switch_on"
                            }
                        }
                    }
                }
            }
            if (initialLightState == LightState.ON) {
                componentGroup("on_without_interact") {
                    isBaby { }
                }
            }

            eventSpawned {
                if (initialLightState == LightState.OFF) {
                    if (lightSwitchOnInteract) {
                        add { componentGroups("light_switch_off") }
                    }
                } else {
                    if (lightSwitchOnInteract) {
                        add { componentGroups("light_switch_on") }
                    } else {
                        add { componentGroups("on_without_interact") }
                    }
                }
            }
            if (lightSwitchOnInteract) {
                event("light_switch_on") {
                    add { componentGroups("light_switch_on") }
                    remove { componentGroups("light_switch_off") }
                }
                event("light_switch_off") {
                    add { componentGroups("light_switch_off") }
                    remove { componentGroups("light_switch_on") }
                }
            }
        }
        super.build()
    }

    override fun lightSource(data: LightSource.() -> Unit) {
        lightSource = LightSource().apply(data)
    }
}