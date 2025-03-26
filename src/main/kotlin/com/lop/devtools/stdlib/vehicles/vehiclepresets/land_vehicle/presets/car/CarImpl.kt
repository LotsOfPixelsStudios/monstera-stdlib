package com.lop.devtools.stdlib.vehicles.vehiclepresets.land_vehicle.presets.car

import com.lop.devtools.monstera.addon.Addon
import com.lop.devtools.monstera.addon.dev.ResourceLoader
import com.lop.devtools.monstera.addon.molang.Query
import com.lop.devtools.monstera.addon.molang.eq
import com.lop.devtools.monstera.files.getResource
import com.lop.devtools.monstera.files.res.sounds.ResSoundDef
import com.lop.devtools.monstera.files.res.sounds.SoundCategory
import com.lop.devtools.monstera.files.res.sounds.SoundEvent
import com.lop.devtools.stdlib.vehicles.VehicleImpl
import com.lop.devtools.stdlib.vehicles.shared.Engine
import com.lop.devtools.stdlib.vehicles.shared.EngineType
import java.io.File
import kotlin.math.ln

abstract class CarImpl(
    name: String,
    displayName: String,
    addon: Addon
)

    : Car, VehicleImpl(name, displayName, addon) {

    private var engine = Engine()
    override var wheelSize = 20f

    override fun engine(engineType: Engine.() -> Unit) {
        engine.apply(engineType)
    }

    override fun build() {
        with(addon) {
            if (engine.engineType != EngineType.NONE) {
                sounds {
                    soundsDefinitions {
                        if (!File("${addon.config.resPath}/sounds/monstera/monstera_std-lib_car_engine_drive.ogg").exists())
                            ResourceLoader.loadResourceWithStreamTo("vehicles/engine_sfx/monstera_std-lib_car_engine_drive.ogg", File("${addon.config.resPath}/sounds/monstera/monstera_std-lib_car_engine_drive.ogg"))
                        if (!File("${addon.config.resPath}/sounds/monstera/monstera_std-lib_car_engine_drive_1.ogg").exists())
                            ResourceLoader.loadResourceWithStreamTo("vehicles/engine_sfx/monstera_std-lib_car_engine_drive_1.ogg", File("${addon.config.resPath}/sounds/monstera/monstera_std-lib_car_engine_drive_1.ogg"))
                        if (!File("${addon.config.resPath}/sounds/monstera/monstera_std-lib_car_engine_start.ogg").exists())
                            ResourceLoader.loadResourceWithStreamTo("vehicles/engine_sfx/monstera_std-lib_car_engine_start.ogg", File("${addon.config.resPath}/sounds/monstera/monstera_std-lib_car_engine_start.ogg"))
                        if (!File("${addon.config.resPath}/sounds/monstera/monstera_std-lib_car_engine_stop.ogg").exists())
                            ResourceLoader.loadResourceWithStreamTo("vehicles/engine_sfx/monstera_std-lib_car_engine_stop.ogg", File("${addon.config.resPath}/sounds/monstera/monstera_std-lib_car_engine_stop.ogg"))
                        if (!File("${addon.config.resPath}/sounds/monstera/monstera_std-lib_electric_motor.ogg").exists())
                            ResourceLoader.loadResourceWithStreamTo("vehicles/engine_sfx/monstera_std-lib_electric_motor.ogg", File("${addon.config.resPath}/sounds/monstera/monstera_std-lib_electric_motor.ogg"))

                        soundDefinitions["${addon.config.namespace}.vehicle.drive"] = ResSoundDef().apply {
                            category = SoundCategory.NEUTRAL
                            sound {
                                name = "sounds/monstera/monstera_std-lib_car_engine_drive" }
                            sound { name = "sounds/monstera/monstera_std-lib_car_engine_drive_1" }
                        }
                        soundDefinitions["${addon.config.namespace}.vehicle.start_engine"] = ResSoundDef().apply {
                            sound { name = "sounds/monstera/monstera_std-lib_car_engine_start" }
                        }
                        soundDefinitions["${addon.config.namespace}.vehicle.stop_engine"] = ResSoundDef().apply {
                            sound { name = "sounds/monstera/monstera_std-lib_car_engine_stop" }
                        }
                        soundDefinitions["${addon.config.namespace}.electric_vehicle.drive"] = ResSoundDef().apply {
                            sound{ name = "sounds/monstera/monstera_std-lib_electric_motor" }
                        }
                    }

                    categorySounds {
                        entitySounds {
                            soundEventGroup("${addon.config.namespace}:$name") {
                                event(SoundEvent.STEP) {
                                    volume(1f)
                                    pitch(engine.enginePitch)
                                    sound("${addon.config.namespace}.vehicle.drive")
                                }
                            }
                        }
                    }
                }
            }
        }
        super.resource {
            if (!File("${addon.config.resPath}/animations/monstera.std-lib.vehicle.animation.json").exists())
                ResourceLoader.loadResourceWithStreamTo(
                    "vehicles/monstera.std-lib.vehicle.animation.json",
                    File("${addon.config.resPath}/animations/monstera.std-lib.vehicle.animation.json")
                )

            animation("driving", "animation.monstera.std-lib.vehicle.driving")
            components {
                scripts {
                    preAnimationEntry("variable.wheel_size = ${(1-(ln((wheelSize+1))/6.5))*1.5};")
                    preAnimationEntry("variable.driving_speed = ${Query.modifiedDistanceMoved};")

                    animate("driving")
                }
            }
        }
        super.behaviour {
            if (engine.engineType == EngineType.NORMAL) {
                animController("$name.vehicle.sfx") {

                    state("default") {
                        transition("start_engine", Query.hasRider)
                    }
                    state("start_engine") {
                        onEntry = arrayListOf(
                            "/playsound ${addon.config.namespace}.vehicle.start_engine @a ~ ~ ~ 0.5 ${engine.enginePitch}"
                        )
                        transition("stop_engine", Query.hasRider eq 0)
                    }
                    state("stop_engine") {
                        onEntry = arrayListOf(
                            "/playsound ${addon.config.namespace}.vehicle.stop_engine @a ~ ~ ~ 0.5 ${engine.enginePitch}"
                        )
                        transition("default", Query.True)
                    }

                }
            }
        }
        super.build()
    }
}