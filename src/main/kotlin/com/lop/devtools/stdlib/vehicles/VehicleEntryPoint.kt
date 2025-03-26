package com.lop.devtools.stdlib.vehicles

import com.lop.devtools.monstera.addon.Addon
import com.lop.devtools.stdlib.vehicles.vehiclepresets.land_vehicle.presets.car.GenericCar
import jdk.jshell.spi.ExecutionControl.NotImplementedException

fun vehicle(name: String, displayName : String, addon: Addon, data : GenericVehicle.() -> Unit) {
    GenericVehicle(name, displayName, addon).apply(data).build()
}


fun car(name: String, displayName : String, addon: Addon, data : GenericCar.() -> Unit) {
    GenericCar(name, displayName, addon).apply(data).build()
}


fun bicycle(name: String, displayName : String, addon: Addon, data : GenericVehicle.() -> Unit) {
    throw NotImplementedException("bicycles aren't implemented yet.")
    GenericVehicle(name, displayName, addon).apply(data).build()
}


fun waterVehicle(name: String, displayName : String, addon: Addon, data : GenericVehicle.() -> Unit) {
    throw NotImplementedException("water vehicles aren't implemented yet.")

    GenericVehicle(name, displayName, addon).apply(data).build()
}


fun plane(name: String, displayName : String, addon: Addon, data : GenericVehicle.() -> Unit) {
    throw NotImplementedException("planes aren't implemented yet.")

    GenericVehicle(name, displayName, addon).apply(data).build()
}

fun helicopter(name: String, displayName : String, addon: Addon, data : GenericVehicle.() -> Unit) {
    throw NotImplementedException("helicopters aren't implemented yet.")

    GenericVehicle(name, displayName, addon).apply(data).build()
}