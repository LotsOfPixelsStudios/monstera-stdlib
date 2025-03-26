package com.lop.devtools.stdlib.furnitures.furniturepresets.lamp

import com.lop.devtools.stdlib.furnitures.Furniture
import com.lop.devtools.stdlib.furnitures.furniturepresets.lamp.util.LightSource
import com.lop.devtools.stdlib.furnitures.furniturepresets.lamp.util.LightState

interface LampFurniture: Furniture {
    var lightSwitchOnInteract: Boolean
    var initialLightState: LightState
    var hangingLamp: Boolean

    fun lightSource(data: LightSource.() -> Unit)
    fun buildLamp()
}