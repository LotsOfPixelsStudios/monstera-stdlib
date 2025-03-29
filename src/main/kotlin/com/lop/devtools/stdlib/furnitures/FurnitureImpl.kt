package com.lop.devtools.stdlib.furnitures

import com.lop.devtools.monstera.addon.Addon
import com.lop.devtools.monstera.addon.dev.ResourceLoader
import com.lop.devtools.monstera.addon.entity.Entity
import com.lop.devtools.monstera.addon.entity.behaviour.BehaviourEntity
import com.lop.devtools.monstera.addon.entity.resource.ResourceEntity
import com.lop.devtools.monstera.addon.molang.Query
import com.lop.devtools.monstera.files.beh.entitiy.data.DamageType
import com.lop.devtools.monstera.files.beh.entitiy.data.Subject
import com.lop.devtools.monstera.files.getResource
import com.lop.devtools.monstera.files.res.entities.comp.ResEntitySpawnEgg
import com.lop.devtools.stdlib.furnitures.breakable.BreakableFurniture
import com.lop.devtools.stdlib.furnitures.shared.FurnitureDropBehaviour
import com.lop.devtools.stdlib.furnitures.shared.loadRotationAdjustment
import java.awt.Color
import java.io.File

abstract class FurnitureImpl(
    override var name: String,
    override var displayName: String,
    val addon: Addon
) : Furniture {
    override var texture: File = ResourceLoader.getDefaultTexture()
    override var geometry: File = ResourceLoader.getDefaultModel()
    override var height: Float = 1f
    override var width: Float = 1f
    override var scale: Float = 1f
    override var autoRotationAdjustment: Boolean = false
    override var spawnable: Boolean = true
    override var dropBehaviour: FurnitureDropBehaviour = FurnitureDropBehaviour.INVINCIBLE

    private var customSpawnEggApplied = false

    val unsafe = Unsafe()

    inner class Unsafe {
        var entity: Entity = Entity(addon, name, displayName)
        var breakComponentData: ArrayList<BreakableFurniture.() -> Unit> = arrayListOf()
        var canBreak: Boolean = false
    }

    init {
        //overridable data
        with(unsafe.entity) {
            behaviour {
                components {
                    typeFamily {
                        familyData = arrayListOf("furniture")
                    }
                    physics {}
                    isStackable { }
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

    /**
     * add a breakable component, if the furniture gets hit, it will play a defined animation
     *
     * the loot will be dropped after a set delay
     *
     * ```
     * breakable {
     *      animation("break", brokeAnimName = "broke")
     *      //or
     *      animation("break", despawnDelay = 2f)
     *
     *      loot(1f) {
     *          //loot table
     *          pool {  }
     *      }
     *      onBreak = arrayListOf("/say breaking")
     * }
     * ```
     */
    override fun breakable(data: BreakableFurniture.() -> Unit) {
        BreakableFurniture(unsafe.entity, addon).apply(data).applyToEntity()
        this.dropBehaviour = FurnitureDropBehaviour.CUSTOM_LOOT
        this.unsafe.canBreak = true
    }

    internal fun build() {
        //overwrite data from user if necessary
        if (autoRotationAdjustment)
            loadRotationAdjustment(addon)

        with(unsafe.entity) {
            resource {
                textureLayer(texture)
                geometryLayer(geometry)

                components {
                    if (spawnable && !customSpawnEggApplied) spawnEgg { eggByColor(Color.ORANGE, Color.BLACK) }
                }
            }
            behaviour {
                properties {
                    bool("despawn") {
                        default(false)
                        clientSync = true
                    }
                }
                if (autoRotationAdjustment) {
                    addSharedController(
                        "controller.animation.furniture.rotation_adjustment",
                        query = Query.True,
                        name = "auto_rotation_adjustment"
                    )
                }
                componentGroup("${getIdentifier()}.on_despawn") {
                    timer {
                        time = 0.1f
                        timeDownEvent {
                            event = "${getIdentifier()}.despawn"
                        }
                    }

                }
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
                    if (dropBehaviour == FurnitureDropBehaviour.CAN_PICKUP) {
                        equipment {
                            table(name) {
                                pool {
                                    rolls(1)
                                    entry {
                                        type = "item"
                                        identifier = "minecraft:spawn_egg"
                                        weight = 1
                                        functions {
                                            functionSetActorId(getIdentifier())
                                        }
                                    }
                                }
                            }
                        }
                    }
                    damageSensor {
                        trigger {
                            cause = DamageType.ALL
                            dealsNoDamage()
                            if (
                                (this@FurnitureImpl.dropBehaviour == FurnitureDropBehaviour.CAN_PICKUP
                                        || this@FurnitureImpl.dropBehaviour == FurnitureDropBehaviour.CUSTOM_LOOT)
                                && !this@FurnitureImpl.unsafe.canBreak
                            ) {
                                onDamage {
                                    event = "${getIdentifier()}.on_despawn" //getIdentifier()
                                    filters {
                                        isFamily(subject = Subject.OTHER, value = "player")
                                    }
                                }
                            }
                            if (this@FurnitureImpl.unsafe.canBreak) {
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
                        this.height = this@FurnitureImpl.height
                        this.width = this@FurnitureImpl.width
                    }
                    scale {
                        value = this@FurnitureImpl.scale
                    }
                }
                event("${getIdentifier()}.on_despawn") {
                    add { componentGroups = arrayListOf("${getIdentifier()}.on_despawn") }
                    setProperty("despawn", true)
                }
                event("${getIdentifier()}.despawn") {
                    add { componentGroups = arrayListOf("${getIdentifier()}.despawn") }
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
            componentGroup("name") {

            }
            event("name") {

            }
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

    private fun sampleBreakAble() {
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