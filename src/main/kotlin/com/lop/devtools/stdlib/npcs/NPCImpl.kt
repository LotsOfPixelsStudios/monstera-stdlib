package com.lop.devtools.stdlib.npcs

import com.lop.devtools.monstera.addon.Addon
import com.lop.devtools.monstera.addon.dev.ResourceLoader
import com.lop.devtools.monstera.addon.entity.Entity
import com.lop.devtools.monstera.addon.entity.behaviour.BehaviourEntity
import com.lop.devtools.monstera.addon.entity.resource.ResourceEntity
import com.lop.devtools.monstera.addon.molang.Query
import com.lop.devtools.monstera.files.beh.entitiy.data.DamageType
import com.lop.devtools.monstera.files.getResource
import com.lop.devtools.monstera.files.res.entities.comp.ResEntitySpawnEgg
import java.awt.Color
import java.io.File

abstract class NPCImpl(
    override var name: String,
    override var displayName: String,
    override val addon: Addon
) : NPCInterface {
    override var scale: Float = 1f
    override var walkingSpeed: Float = 0.3f

    override var invincible: Boolean = false

    override var avoidSun: Boolean = false
    override var avoidBlocks: ArrayList<String> = arrayListOf()

    val alexSkins: ArrayList<File> = arrayListOf()
    val steveSkins: ArrayList<File> = arrayListOf()

    private var customSpawnEggApplied = false

    val unsafe = Unsafe()

    inner class Unsafe {
        var entity: Entity = Entity(addon, name, displayName)
    }

    init {
        with(unsafe.entity) {
            resource {
                unsafeApplyDefaultGeometry = false
                geometryLayerByIds(
                    arrayListOf("geometry.humanoid.customSlim", "geometry.humanoid.custom.std-lib_npc"),
                    Query.variant,
                    "npc_geos"
                )
                val tmp = addon.config.paths.resModels.resolve("entity").resolve("humanoid.custom.std-lib_npc.geo.json")
                    .toFile()
                tmp.parentFile.mkdirs()  //create dir
                tmp.createNewFile()      //create file if it does not exist
                tmp.writeBytes(ResourceLoader.getResourceAsStream("npcs/humanoid.custom.custom.geo.json").readBytes())

                //default animation controllers
                animation("look_at_target", "controller.animation.humanoid.look_at_target")
                animation("base_pose", "controller.animation.humanoid.base_pose")
                animation("move", "controller.animation.humanoid.move")

                //default humanoid movement animations
                animation("look_at_target_default", "animation.humanoid.look_at_target.default")
                animation("look_at_target_gliding", "animation.humanoid.look_at_target.gliding")
                animation("look_at_target_swimming", "animation.humanoid.look_at_target.swimming")
                animation("look_at_target_inverted", "animation.humanoid.look_at_target.inverted")

                animation("move", "animation.humanoid.move")
                animation("riding.arms", "animation.humanoid.riding.arms")
                animation("riding.legs", "animation.humanoid.riding.legs")

                animation("humanoid_base_pose", "animation.humanoid.base_pose")

                //needed for default humanoid movement animations
                components {
                    scripts {
                        preAnimationEntry(
                            "variable.tcos0 = (Math.cos(query.modified_distance_moved * 38.17) * query.modified_move_speed / variable.gliding_speed_value) * 57.3;"
                        )
                        animate("look_at_target")
                        animate("base_pose")
                        animate("move")
                    }
                }
            }

            behaviour {
                components {
                    health {
                        max = 20
                        value = 20
                    }
                    physics { }
                    pushable { }
                    typeFamily {
                        familyData = arrayListOf("npc", "monstera_entity")
                    }
                    collisionBox {
                        width = 0.6f
                        height = 1.8f
                    }

                    behFloat {
                        priority = 0
                    }
                    movementBasic { }
                    jumpStatic { }
                    behLookAtPlayer {
                        priority = 3
                        targetDistance = 6f
                        probability = 0.02f
                        lookDistance = 6f
                    }
                    behRandomStroll {
                        priority = 2
                        interval = 10
                        speedMultiplier = 0.8f
                        xzDist = 6
                        yDist = 2
                    }
                }

                componentGroup("despawn") {
                    instantDespawn { }
                }

                event("despawn") {
                    add { componentGroups = arrayListOf("despawn") }
                }
            }
        }
    }

    /**
     * add a texture to the NPC. You need to define whether that texture is for the alex or the steve geometry.
     * @sample sampleTexture
     */
    override fun addTexture(file: File, geometry: GeoType) {
        when (geometry) {
            GeoType.STEVE -> steveSkins.add(file)
            GeoType.ALEX -> alexSkins.add(file)
        }
    }

    /**
     * add all textures of a folder to the NPC. The textures in the folder all must be of one type, so either steve OR alex. they can't be mixed.
     * note: If you want to use both types, you can add the other variant as another folder.
     * @sample sampleTextures
     */
    override fun addTextures(textureFolder: File, geometry: GeoType) {
        val textureFiles = textureFolder.listFiles()

        when (geometry) {
            GeoType.STEVE -> steveSkins.addAll(textureFiles!!)
            GeoType.ALEX -> alexSkins.addAll(textureFiles!!)
        }
    }

    /**
     * define you're own behaviour, note: if you need to do complex stuff may write you're own entity
     * @sample sampleBeh
     */
    override fun behaviour(behavior: BehaviourEntity.() -> Unit) {
        unsafe.entity.behaviour(behavior)
    }

    /**
     * define you're own resource, note: if you need to do complex stuff may write you're own entity
     * @sample sampleRes
     */
    override fun resource(resource: ResourceEntity.() -> Unit) {
        unsafe.entity.resource(resource)
    }

    /**
     * define the colors or the texture of the spawn egg
     * @sample sampleIcon
     */
    override fun icon(data: ResEntitySpawnEgg.() -> Unit) {
        unsafe.entity.resource {
            components {
                spawnEgg(data = data)
            }
        }
        customSpawnEggApplied = true
    }

    internal fun build() {
        with(unsafe.entity) {
            resource {
                val allSkins = arrayListOf<File>()
                allSkins.addAll(alexSkins)
                allSkins.addAll(steveSkins)
                textureLayer(allSkins, "query.skin_id", "default")

                if (!customSpawnEggApplied) {
                    components {
                        spawnEgg {
                            eggByColor(Color.ORANGE, Color.GRAY)
                        }
                    }
                }
            }
            behaviour {
                components {
                    movement = walkingSpeed
                    this@components.scale = this@NPCImpl.scale

                    if (invincible) {
                        damageSensor {
                            trigger {
                                dealsNoDamage()
                                cause = DamageType.ALL
                            }
                        }
                    }

                    navigationWalk {
                        avoidWater = true
                        canOpenDoors = false
                        canBreakDoors = false
                        canPassDoors = false
                        canOpenDoors = false
                        avoidSun = this@NPCImpl.avoidSun
                        if (!this@NPCImpl.avoidBlocks.isEmpty()) avoidBlocks = this@NPCImpl.avoidBlocks
                    }

                }

                var skinID = 0
                for (texture in alexSkins) {
                    componentGroup("skin_$skinID") {
                        skinId {
                            value = skinID
                        }
                        variant = 0
                    }
                    skinID++
                }
                for (texture in steveSkins) {
                    componentGroup("skin_$skinID") {
                        skinId {
                            value = skinID
                        }
                        variant = 1
                    }
                    skinID++
                }
                eventSpawned {
                    randomize {
                        var skinID = 0
                        for (texture in alexSkins) {
                            randomComp {
                                weight = 1
                                add { componentGroups("skin_$skinID") }
                            }
                            skinID++
                        }
                        for (texture in steveSkins) {
                            randomComp {
                                weight = 1
                                add { componentGroups("skin_$skinID") }
                            }
                            skinID++
                        }
                    }
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
            componentGroup("") {

            }
            event("") {

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
            //eggByFile(getResource("egg.png"))
            //or
            eggByColor(Color.ORANGE, Color.BLACK)
        }
    }

    private fun sampleTexture() {
        addTexture(getResource("alex_skin.png"), GeoType.ALEX)
    }

    private fun sampleTextures() {
        addTextures(getResource("general/skins/alex_skins"), GeoType.ALEX)

        //OR if you want to have both variants
        addTextures(getResource("general/skins/alex_skins"), GeoType.ALEX)
        addTextures(getResource("general/skins/steve_skins"), GeoType.STEVE)
    }
}