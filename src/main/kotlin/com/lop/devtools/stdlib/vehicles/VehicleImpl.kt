package com.lop.devtools.stdlib.vehicles

import com.lop.devtools.monstera.addon.Addon
import com.lop.devtools.monstera.addon.dev.ResourceLoader
import com.lop.devtools.monstera.addon.entity.Entity
import com.lop.devtools.monstera.addon.entity.behaviour.BehaviourEntity
import com.lop.devtools.monstera.addon.entity.resource.ResourceEntity
import com.lop.devtools.monstera.files.beh.entitiy.data.DamageType
import com.lop.devtools.monstera.files.beh.entitiy.data.Subject
import com.lop.devtools.monstera.files.getResource
import com.lop.devtools.monstera.files.res.entities.comp.ResEntitySpawnEgg
import com.lop.devtools.stdlib.furnitures.shared.FurnitureDropBehaviour
import com.lop.devtools.stdlib.vehicles.shared.SteeringType
import java.awt.Color
import java.io.File

abstract class VehicleImpl(
    override var name: String,
    override var displayName: String,
    val addon: Addon
) : Vehicle {
    override var texture: File = ResourceLoader.getDefaultTexture()
    override var geometry: File = ResourceLoader.getDefaultModel()

    override var height: Float = 1f
    override var width: Float = 1f
    override var scale: Float = 1f

    override var steeringType: SteeringType = SteeringType.STANDARD_WASD
    override var speed: Float = 0.4f
    private val seats: ArrayList<Triple<Float, Float, Float>> = arrayListOf()

    override var spawnable: Boolean = true
    override var dropBehaviour: FurnitureDropBehaviour = FurnitureDropBehaviour.INVINCIBLE

    private var customSpawnEggApplied = false


    val unsafe = Unsafe()

    inner class Unsafe {
        var entity: Entity = Entity(addon, this@VehicleImpl.name, this@VehicleImpl.displayName)
        var canBreak: Boolean = false
    }

    init {
        with(unsafe.entity) {
            behaviour {
                components {
                    typeFamily{
                        family("vehicle")
                    }
                    physics {}
                    isStackable {}
                    conditionalBandwidthOptimization {}
                }
            }
            resource {
                components {
                    material = "parrot"
                }
            }
        }
    }

    override fun addSeat(coordinate: Triple<Number, Number, Number>) {
        seats.add(Triple(coordinate.first.toFloat(), coordinate.second.toFloat(), coordinate.third.toFloat()))
    }

    /**
     * define you're own behaviour, note: if you need to do complex stuff may write you're won entity
     * @sample sampleBeh
     */
    override fun behaviour(behavior: BehaviourEntity.() -> Unit) {
        unsafe.entity.behaviour(behavior)
    }

    /**
     * define you're own resource, note: if you need to do complex stuff may write you're won entity
     * @sample sampleRes
     */
    override fun resource(resource: ResourceEntity.() -> Unit) {
        unsafe.entity.resource(resource)
    }

    /**
     * define the icon of the furniture,
     *
     * also possible to set over resource { }
     * @sample sampleIcon
     */
    override fun icon(data: ResEntitySpawnEgg.() -> Unit) {
        if (!spawnable) return
        unsafe.entity.resource {
            components {
                spawnEgg(data = data)
            }
        }
        customSpawnEggApplied = true
    }

    internal open fun build() {
        //overwrite data from user if necessary
        with(unsafe.entity) {
            resource {
                textureLayer(texture)
                geometryLayer(geometry)

                components {
                    if (spawnable && !customSpawnEggApplied) spawnEgg { eggByColor(Color.ORANGE, Color.BLACK) }
                }
            }
            behaviour {
                componentGroup("${getIdentifier()}.despawn") {
                    equipment {
                        table = "loot_tables/entities/$name.json"
                    }
                    transformation {
                        into = "minecraft:area_effect_cloud"
                        dropEquipment = true
                    }
                }
                components {
                    if (dropBehaviour == FurnitureDropBehaviour.CAN_PICKUP)
                        equipment {
                            table(name) {
                                pool {
                                    entry{
                                        type = "item"
                                        name = "minecraft:spawn_egg"
                                        weight = 1
                                        functions {
                                            functionSetActorId(getIdentifier())
                                        }

                                    }
                                }
                            }
                        }
                    if (steeringType == SteeringType.STANDARD_WASD) {
                        inputGroundControlled {
                            variableMaxAutoStep {
                                baseValue = 1
                                controlledValue = 1
                                jumpPreventedValue = 1.1
                            }
                        }
                    }

                    damageSensor {
                        trigger {
                            cause = DamageType.ALL
                            dealsDamage = false
                            if (
                                (this@VehicleImpl.dropBehaviour == FurnitureDropBehaviour.CAN_PICKUP
                                        || this@VehicleImpl.dropBehaviour == FurnitureDropBehaviour.CUSTOM_LOOT)
                                && !this@VehicleImpl.unsafe.canBreak
                            ) {
                                onDamage {
                                    event = "${getIdentifier()}.despawn" //getIdentifier()
                                    filters {
                                        isFamily(subject = Subject.OTHER, value = "player")
                                    }
                                }
                            }
                            if (this@VehicleImpl.unsafe.canBreak) {
                                onDamage {
                                    event = "${getIdentifier()}.trigger_break"
                                    filters {
                                        isFamily(subject = Subject.OTHER, value = "player")
                                    }
                                }
                            }
                        }
                    }

                    collisionBox {
                        this.height = this@VehicleImpl.height
                        this.width = this@VehicleImpl.width
                    }
                    scale {
                        value = this@VehicleImpl.scale
                    }

                    rideable {
                        seatCount = 1
                        crouchingSkipInteract = true
                        familyTypes("player")
                        interactText = "action.interact.ride.horse"
                        exitText("Sneak to dismount", "action.hint.exit.${addon.config.namespace}:$name")

                        for (seat in seats) {
                            seat {
                                maxRiderCount = 1
                                position(seat.first, seat.second, seat.third)
                            }
                        }

                    }
                    movement = speed
                    movementBasic {}
                    navigationWalk {}
                    jumpStatic {}
                    isStackable {}
                    physics {}
                    pushable {
                        isPushable = false
                        isPushableByPiston = false
                    }
                }
                event("${getIdentifier()}.despawn") {
                    add { componentGroups ("${getIdentifier()}.despawn") }
                }
            }
        }
        unsafe.entity.build()
    }

    //------------------------------------------------------------------------------------------------------------------
    //  EXAMPLES
    //------------------------------------------------------------------------------------------------------------------

    private fun sampleBeh() {
        behaviour {
            components {

            }
            componentGroup("test_comp_group") {}
            event("test_event") {}
        }
    }

    private fun sampleRes() {
        resource {
            animation(getResource("my_anim.json"))
            components {

            }
        }
    }

    private fun sampleIcon() {
        icon {
            eggByFile(getResource("egg.png"), addon)
            //or
            eggByColor(Color.ORANGE, Color.BLACK)
        }
    }
}