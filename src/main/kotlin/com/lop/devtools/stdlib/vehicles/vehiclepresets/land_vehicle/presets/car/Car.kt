package com.lop.devtools.stdlib.vehicles.vehiclepresets.land_vehicle.presets.car

import com.lop.devtools.stdlib.vehicles.shared.Engine

interface Car {

    var wheelSize : Float
    fun engine(engineType: Engine.()->Unit)
    fun build()
}