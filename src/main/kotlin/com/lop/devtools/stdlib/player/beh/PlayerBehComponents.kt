package com.lop.devtools.stdlib.player.beh

import com.lop.devtools.monstera.addon.Addon
import com.lop.devtools.monstera.files.beh.entitiy.BehEntity
import com.lop.devtools.monstera.files.beh.entitiy.components.Components
import com.lop.devtools.monstera.files.beh.entitiy.data.DamageType
import com.lop.devtools.monstera.files.beh.entitiy.data.Subject
import com.lop.devtools.monstera.files.beh.entitiy.events.BehEntityEvent
import com.lop.devtools.monstera.files.properties.EntityProperties

@Suppress("MemberVisibilityCanBePrivate", "unused")
class PlayerBehComponents(
    val addon: Addon,
    val behEntity: BehEntity
) {
    var clean: Boolean = false

    //components
    var skipExpRew: Boolean = false
    var skipTypeFam: Boolean = false
    var skipIsHidden: Boolean = false
    var skipLoot: Boolean = false
    var skipCollBox: Boolean = false
    var skipCanClimb: Boolean = false
    var skipMovement: Boolean = false
    var skipHurtCond: Boolean = false
    var skipAttack: Boolean = false
    var skipSaturation: Boolean = false
    var skipExhaustion: Boolean = false
    var skipLevel: Boolean = false
    var skipExperience: Boolean = false
    var skipBreathAble: Boolean = false
    var skipNameAble: Boolean = false
    var skipPhysics: Boolean = false
    var skipInsomnia: Boolean = false
    var skipRideable: Boolean = false
    var skipBandWidthOpt: Boolean = false
    var skipScaffold: Boolean = false
    var skipEnvSensor: Boolean = false

    //groups
    var skipGroupAddBadOmen: Boolean = false
    var skipGroupClearBadOmen: Boolean = false
    var skipGroupRaidTrigger: Boolean = false

    //events
    var skipEventGainBadOmen: Boolean = false
    var skipEventClearBadOmen: Boolean = false
    var skipEventRaidTrigger: Boolean = false
    var skipEventRemoveRaidTriggers: Boolean = false

    var removeAllVanillaComponents = false
    var removeAllVanillaGroups = false
    var removeAllVanillaEvents = false


    fun properties(data: EntityProperties.() -> Unit) {
        behEntity.description {
            properties(data)
        }
    }

    fun components(data: Components.() -> Unit) {
        behEntity.components(data)
    }

    fun componentGroup(name: String, data: Components.() -> Unit) {
        behEntity.componentGroup(name, data)
    }

    fun event(name: String, data: BehEntityEvent.() -> Unit) {
        behEntity.event(name, data)
    }

    fun buildComponents() {
        if (!clean && !removeAllVanillaComponents) {
            behEntity.components {
                if (!skipExpRew)
                    experienceReward {
                        onDeath = "Math.Min(query.player_level * 7, 100)"
                    }
                if (!skipTypeFam)
                    typeFamily {
                        family("player")
                    }
                if (!skipIsHidden)
                    isHiddenWhenInvisible { }
                if (!skipLoot)
                    loot {
                        table = "loot_tables/empty.json"
                    }

                if (!skipCollBox)
                    collisionBox {
                        width = 0.6f
                        height = 1.8f
                    }
                if (!skipCanClimb)
                    canClimb { }
                if (!skipMovement)
                    movement {
                        value = 0.1f
                    }
                if (!skipHurtCond)
                    hurtOnCondition {
                        damageConditions {
                            cause = DamageType.LAVA
                            damagePerTick = 4
                            filters {
                                inLava()
                            }
                        }
                    }
                if (!skipAttack)
                    attack {
                        damage = 1
                    }
                if (!skipSaturation)
                    playerSaturation {
                        value = 20
                    }
                if (!skipExhaustion)
                    playerExhaustion {
                        value = 0
                        max = 4
                    }
                if (!skipLevel)
                    playerLevel {
                        value = 0
                        max = 24791
                    }
                if (!skipExperience)
                    playerExperience {
                        value = 0
                        max = 1
                    }
                if (!skipBreathAble)
                    breathable {
                        totalSupply = 15
                        suffocateTime = -1
                        inhaleTime = 3.75f
                        generatesBubbles = false
                    }
                if (!skipNameAble)
                    nameable {
                        alwaysShow = true
                        allowNameTagRenaming = false
                    }
                if (!skipPhysics)
                    physics { }
                if (!skipInsomnia)
                    insomnia {
                        daysUntilInsomnia = 3.0f
                    }
                if (!skipRideable)
                    rideable {
                        seatCount = 2
                        familyTypes("parrot_tame")
                        pullInEntities = true
                        seat {
                            position(0.4f, -0.2f, -0.1f)
                            maxRiderCount = 0
                            minRiderCount = 0
                            lockRiderRotation = 0
                        }
                        seat {
                            position(-0.4f, -0.2f, -0.1f)
                            minRiderCount = 1
                            maxRiderCount = 2
                            lockRiderRotation = 0
                        }
                    }
                if (!skipBandWidthOpt)
                    conditionalBandwidthOptimization { }
                if (!skipScaffold)
                    blockClimber { }
                if (!skipEnvSensor)
                    environmentSensor {
                        trigger {
                            event = "minecraft:trigger_raid"
                            filters {
                                allOf {
                                    hasMobEffect(value = "bad_omen")
                                    isInVillage()
                                }
                            }
                        }
                    }
            }
        }
    }

    fun buildComponentGroups() {
        if (!clean && !removeAllVanillaGroups) {

            if (!skipGroupAddBadOmen)
                componentGroup("minecraft:add_bad_omen") {
                    spellEffects {
                        addEffects {
                            effect = "bad_omen"
                            duration = 6000
                            displayOnScreenAnimation = true
                        }
                    }
                    timer {
                        looping = false
                        time = 0
                        timeDownEvent {
                            event = "minecraft:clear_add_bad_omen"
                            target = Subject.SELF
                        }
                    }
                }

            if (!skipGroupClearBadOmen)
                componentGroup("minecraft:clear_bad_omen_spell_effect") {
                    spellEffects { }
                }

            if (!skipGroupRaidTrigger)
                componentGroup("minecraft:raid_trigger") {
                    raidTrigger {
                        triggeredEvent {
                            event = "minecraft:remove_raid_trigger"
                            target = Subject.SELF
                        }
                    }
                    spellEffects {
                        removeEffects = arrayListOf("bad_omen")
                    }
                }
        }
    }

    fun buildEvents() {
        if (!clean && !removeAllVanillaEvents) {
            if (!skipEventGainBadOmen)
                behEntity.event("minecraft:gain_bad_omen") {
                    add {
                        componentGroups = arrayListOf("minecraft:add_bad_omen")
                    }
                }
            if (!skipEventClearBadOmen)
                behEntity.event("minecraft:clear_add_bad_omen") {
                    remove {
                        componentGroups = arrayListOf("minecraft:add_bad_omen")
                    }
                    add {
                        componentGroups = arrayListOf("minecraft:clear_bad_omen_spell_effect")
                    }
                }
            if (!skipEventRaidTrigger)
                behEntity.event("minecraft:trigger_raid") {
                    add {
                        componentGroups = arrayListOf("minecraft:raid_trigger")
                    }
                }
            if (!skipEventRemoveRaidTriggers)
                behEntity.event("minecraft:remove_raid_trigger") {
                    remove {
                        componentGroups = arrayListOf("minecraft:raid_trigger")
                    }
                }
        }
    }
}