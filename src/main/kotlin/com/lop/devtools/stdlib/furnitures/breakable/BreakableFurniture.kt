package com.lop.devtools.stdlib.furnitures.breakable

import com.lop.devtools.monstera.addon.Addon
import com.lop.devtools.monstera.addon.entity.Entity
import com.lop.devtools.monstera.addon.molang.Query
import com.lop.devtools.monstera.addon.molang.eq
import com.lop.devtools.monstera.files.beh.tables.loot.BehLootTables

@Suppress("MemberVisibilityCanBePrivate", "unused")
class BreakableFurniture(private val entity: Entity, private val addon: Addon) {
    private var delay: Float = 1f
    private var tableName: String? = null
    private var breakAnimId: String? = null
    private var brokeAnimId: String? = null
    private var despawnDelay: Float = -1f

    var onBreak: ArrayList<String> = arrayListOf()


    fun animation(breakAnimName: String, brokeAnimName: String?, despawnDelay: Float = -1f) {
        breakAnimId = breakAnimName
        brokeAnimId = brokeAnimName
        this.despawnDelay = despawnDelay
    }

    fun animation(breakAnimationName: String, despawnDelay: Float) {
        animation(breakAnimationName, null, despawnDelay)
    }

    fun loot(dropDelay: Float = 1f, table: BehLootTables.() -> Unit) {
        this.delay = dropDelay
        val tableName = entity.name
        this.tableName = tableName

        BehLootTables.Entity(BehLootTables().apply(table)).build(tableName)
    }

    fun loot(delay: Float = 1f, table: String) {
        this.delay = delay
        this.tableName = table
    }

    fun applyToEntity() {
        entity.apply {
            behaviour {
                animation("break") {
                    onBreak.add("@s ${this@BreakableFurniture.entity.getIdentifier()}.break")
                    timeline {
                        keyFrame(delay, onBreak)
                    }
                    animLength = delay + 1f
                }
                if (despawnDelay >= 0)
                    animation("despawn") {
                        timeline {
                            keyFrame(despawnDelay, "@s ${this@BreakableFurniture.entity.getIdentifier()}.despawn")
                        }
                        animLength = despawnDelay + 1f
                    }
                animController("break_controller") {

                    state("default") {
                        transition("broke", Query.skinId eq 1)
                        transition("break", Query.skinId eq 2)
                    }
                    state("break") {
                        animations("break")
                        transition("broke", Query.allAnimationsFinished)
                    }
                    state("broke") {
                        if (despawnDelay >= 0)
                            animations("despawn")
                    }

                }
                components {
                    skinId {
                        value = 0
                    }
                }

                componentGroup("${this@BreakableFurniture.entity.getIdentifier()}.broke") {
                    skinId {
                        value = 1
                    }
                }
                componentGroup("${this@BreakableFurniture.entity.getIdentifier()}.trigger_break") {
                    skinId {
                        value = 2
                    }
                }

                event("${this@BreakableFurniture.entity.getIdentifier()}.trigger_break") {
                    add {
                        componentGroups = arrayListOf("${this@BreakableFurniture.entity.getIdentifier()}.trigger_break")
                    }
                }
                event("${this@BreakableFurniture.entity.getIdentifier()}.break") {
                    remove {
                        componentGroups = arrayListOf("${this@BreakableFurniture.entity.getIdentifier()}.trigger_break")
                    }
                    add { componentGroups = arrayListOf("${this@BreakableFurniture.entity.getIdentifier()}.broke") }
                }

            }
            resource {
                breakAnimId?.let { breakAnim ->
                    var brokeAnimName: String? = null
                    brokeAnimId?.let {
                        brokeAnimName = it
                    }

                    animationController("break_furniture") {
                        initialState = "default"
                        state("default") {
                            transition("break", Query.skinId eq 2)
                            brokeAnimName?.let { transition("broke", Query.skinId eq 1) }
                        }
                        state("break") {
                            animations(breakAnim)
                            brokeAnimName?.let {
                                transition("broke", Query.allAnimationsFinished)
                            }
                        }
                        brokeAnimName?.let {
                            state("broke") {
                                animations(it)
                            }
                        }
                    }
                }
            }
        }
    }
}