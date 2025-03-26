package com.lop.devtools.stdlib.player.res

import com.lop.devtools.monstera.addon.Addon
import com.lop.devtools.monstera.addon.api.MonsteraBuildSetter
import com.lop.devtools.monstera.addon.entity.extractAnimationIdsFromFile
import com.lop.devtools.monstera.files.res.entities.ResEntity
import java.io.File

@Suppress("MemberVisibilityCanBePrivate", "unused")
class PlayerResAnimations(private val addon: Addon, private val entity: ResEntity) {
    var clean: Boolean = false

    val defaultKeyAnimations = mutableMapOf(
        "root" to "controller.animation.player.root",
        "base_controller" to "controller.animation.player.base",
        "hudplayer" to "controller.animation.player.hudplayer",
        "humanoid_base_pose" to "animation.humanoid.base_pose",
        "look_at_target" to "controller.animation.humanoid.look_at_target",
        "look_at_target_ui" to "animation.player.look_at_target.ui",
        "look_at_target_default" to "animation.humanoid.look_at_target.default",
        "look_at_target_gliding" to "animation.humanoid.look_at_target.gliding",
        "look_at_target_swimming" to "animation.humanoid.look_at_target.swimming",
        "look_at_target_inverted" to "animation.player.look_at_target.inverted",
        "cape" to "animation.player.cape",
        "move.arms" to "animation.player.move.arms",
        "move.legs" to "animation.player.move.legs",
        "swimming" to "animation.player.swim",
        "swimming.legs" to "animation.player.swim.legs",
        "riding.arms" to "animation.player.riding.arms",
        "riding.legs" to "animation.player.riding.legs",
        "holding" to "animation.player.holding",
        "brandish_spear" to "animation.humanoid.brandish_spear",
        "holding_spyglass" to "animation.humanoid.holding_spyglass",
        "charging" to "animation.humanoid.charging",
        "attack.positions" to "animation.player.attack.positions",
        "attack.rotations" to "animation.player.attack.rotations",
        "sneaking" to "animation.player.sneaking",
        "bob" to "animation.player.bob",
        "damage_nearby_mobs" to "animation.humanoid.damage_nearby_mobs",
        "bow_and_arrow" to "animation.humanoid.bow_and_arrow",
        "use_item_progress" to "animation.humanoid.use_item_progress",
        "skeleton_attack" to "animation.skeleton.attack",
        "sleeping" to "animation.player.sleeping",
        "first_person_base_pose" to "animation.player.first_person.base_pose",
        "first_person_empty_hand" to "animation.player.first_person.empty_hand",
        "first_person_swap_item" to "animation.player.first_person.swap_item",
        "first_person_attack_controller" to "controller.animation.player.first_person_attack",
        "first_person_attack_rotation" to "animation.player.first_person.attack_rotation",
        "first_person_vr_attack_rotation" to "animation.player.first_person.vr_attack_rotation",
        "first_person_walk" to "animation.player.first_person.walk",
        "first_person_map_controller" to "controller.animation.player.first_person_map",
        "first_person_map_hold" to "animation.player.first_person.map_hold",
        "first_person_map_hold_attack" to "animation.player.first_person.map_hold_attack",
        "first_person_map_hold_off_hand" to "animation.player.first_person.map_hold_off_hand",
        "first_person_map_hold_main_hand" to "animation.player.first_person.map_hold_main_hand",
        "first_person_crossbow_equipped" to "animation.player.first_person.crossbow_equipped",
        "third_person_crossbow_equipped" to "animation.player.crossbow_equipped",
        "third_person_bow_equipped" to "animation.player.bow_equipped",
        "crossbow_hold" to "animation.player.crossbow_hold",
        "crossbow_controller" to "controller.animation.player.crossbow",
        "shield_block_main_hand" to "animation.player.shield_block_main_hand",
        "shield_block_off_hand" to "animation.player.shield_block_off_hand",
        "blink" to "controller.animation.persona.blink"
    )
    val finalAnimations = mutableMapOf<String, String>()

    var skipHumanoidBasePose: Boolean = false
    var skipLookAtTargetUi: Boolean = false
    var skipLookAtTargetDefault: Boolean = false
    var skipLookAtTargetGliding: Boolean = false
    var skipLookAtTargetSwimming: Boolean = false
    var skipLookAtTargetInverted: Boolean = false
    var skipCape: Boolean = false
    var skipMoveArms: Boolean = false
    var skipMoveLegs: Boolean = false
    var skipSwimming: Boolean = false
    var skipSwimmingLegs: Boolean = false
    var skipRidingArms: Boolean = false
    var skipRidingLegs: Boolean = false
    var skipHolding: Boolean = false
    var skipBrandishSpear: Boolean = false
    var skipHoldingSpyglass: Boolean = false
    var skipCharging: Boolean = false
    var skipAttackPositions: Boolean = false
    var skipAttackRotations: Boolean = false
    var skipSneaking: Boolean = false
    var skipBob: Boolean = false
    var skipDamageNearbyMobs: Boolean = false
    var skipBowAndArrow: Boolean = false
    var skipUseItemProgress: Boolean = false
    var skipSkeletonAttack: Boolean = false
    var skipSleeping: Boolean = false
    var skipFirstPersonBasePose: Boolean = false
    var skipFirstPersonEmptyHand: Boolean = false
    var skipFirstPersonSwapItem: Boolean = false
    var skipFirstPersonAttackRotation: Boolean = false
    var skipFirstPersonVrAttackRotation: Boolean = false
    var skipFirstPersonWalk: Boolean = false
    var skipFirstPersonMapHold: Boolean = false
    var skipFirstPersonMapHoldAttack: Boolean = false
    var skipFirstPersonMapHoldOffHand: Boolean = false
    var skipFirstPersonMapHoldMainHand: Boolean = false
    var skipFirstPersonCrossbowEquipped: Boolean = false
    var skipThirdPersonCrossbowEquipped: Boolean = false
    var skipThirdPersonBowEquipped: Boolean = false
    var skipCrossbowHold: Boolean = false
    var skipShieldBlockMainHand: Boolean = false
    var skipShieldBlockOffHand: Boolean = false

    /**
     * @param name like "bob"
     * @param identifier like "animation.player.bob"
     */
    fun addAnimation(name: String, identifier: String) {
        finalAnimations[name] = identifier
    }

    /**
     * @param value first is the name, second is the anim id
     */
    fun addAnimation(value: Pair<String, String>) {
        addAnimation(value.first, value.second)
    }

    /**
     * @param value the file with animations to give to the player
     * @param preventOverwrite a suffix for the file that prevents overwriting, default is player.json
     */
    fun addAnimations(value: File, preventOverwrite: String = "") {
        val pFile = addon.config.paths.resAnim.resolve("player${preventOverwrite}.json").toFile()
        value.copyTo(pFile, true)
        val animIds = extractAnimationIdsFromFile(value)
        animIds.forEach {
            addAnimation(it.split(".").last(), it)
        }
    }

    @OptIn(MonsteraBuildSetter::class)
    fun build(): MutableMap<String, String> {
        if(skipHumanoidBasePose)
            defaultKeyAnimations.remove("animation.humanoid.base_pose")
        if(skipLookAtTargetUi)
            defaultKeyAnimations.remove("animation.player.look_at_target.ui")
        if(skipLookAtTargetDefault)
            defaultKeyAnimations.remove("animation.humanoid.look_at_target.default")
        if(skipLookAtTargetGliding)
            defaultKeyAnimations.remove("animation.humanoid.look_at_target.gliding")
        if(skipLookAtTargetSwimming)
            defaultKeyAnimations.remove("animation.humanoid.look_at_target.swimming")
        if(skipLookAtTargetInverted)
            defaultKeyAnimations.remove("animation.player.look_at_target.inverted")
        if(skipCape)
            defaultKeyAnimations.remove("animation.player.cape")
        if(skipMoveArms)
            defaultKeyAnimations.remove("animation.player.move.arms")
        if(skipMoveLegs)
            defaultKeyAnimations.remove("animation.player.move.legs")
        if(skipSwimming)
            defaultKeyAnimations.remove("animation.player.swim")
        if(skipSwimmingLegs)
            defaultKeyAnimations.remove("animation.player.swim.legs")
        if(skipRidingArms)
            defaultKeyAnimations.remove("animation.player.riding.arms")
        if(skipRidingLegs)
            defaultKeyAnimations.remove("animation.player.riding.legs")
        if(skipHolding)
            defaultKeyAnimations.remove("animation.player.holding")
        if(skipBrandishSpear)
            defaultKeyAnimations.remove("animation.humanoid.brandish_spear")
        if(skipHoldingSpyglass)
            defaultKeyAnimations.remove("animation.humanoid.holding_spyglass")
        if(skipCharging)
            defaultKeyAnimations.remove("animation.humanoid.charging")
        if(skipAttackPositions)
            defaultKeyAnimations.remove("animation.player.attack.positions")
        if(skipAttackRotations)
            defaultKeyAnimations.remove("animation.player.attack.rotations")
        if(skipSneaking)
            defaultKeyAnimations.remove("animation.player.sneaking")
        if(skipBob)
            defaultKeyAnimations.remove("animation.player.bob")
        if(skipDamageNearbyMobs)
            defaultKeyAnimations.remove("animation.humanoid.damage_nearby_mobs")
        if(skipBowAndArrow)
            defaultKeyAnimations.remove("animation.humanoid.bow_and_arrow")
        if(skipUseItemProgress)
            defaultKeyAnimations.remove("animation.humanoid.use_item_progress")
        if(skipSkeletonAttack)
            defaultKeyAnimations.remove("animation.skeleton.attack")
        if(skipSleeping)
            defaultKeyAnimations.remove("animation.player.sleeping")
        if(skipFirstPersonBasePose)
            defaultKeyAnimations.remove("animation.player.first_person.base_pose")
        if(skipFirstPersonEmptyHand)
            defaultKeyAnimations.remove("animation.player.first_person.empty_hand")
        if(skipFirstPersonSwapItem)
            defaultKeyAnimations.remove("animation.player.first_person.swap_item")
        if(skipFirstPersonAttackRotation)
            defaultKeyAnimations.remove("animation.player.first_person.attack_rotation")
        if(skipFirstPersonVrAttackRotation)
            defaultKeyAnimations.remove("animation.player.first_person.vr_attack_rotation")
        if(skipFirstPersonWalk)
            defaultKeyAnimations.remove("animation.player.first_person.walk")
        if(skipFirstPersonMapHold)
            defaultKeyAnimations.remove("animation.player.first_person.map_hold")
        if(skipFirstPersonMapHoldAttack)
            defaultKeyAnimations.remove("animation.player.first_person.map_hold_attack")
        if(skipFirstPersonMapHoldOffHand)
            defaultKeyAnimations.remove("animation.player.first_person.map_hold_off_hand")
        if(skipFirstPersonMapHoldMainHand)
            defaultKeyAnimations.remove("animation.player.first_person.map_hold_main_hand")
        if(skipFirstPersonCrossbowEquipped)
            defaultKeyAnimations.remove("animation.player.first_person.crossbow_equipped")
        if(skipThirdPersonCrossbowEquipped)
            defaultKeyAnimations.remove("animation.player.crossbow_equipped")
        if(skipThirdPersonBowEquipped)
            defaultKeyAnimations.remove("animation.player.bow_equipped")
        if(skipCrossbowHold)
            defaultKeyAnimations.remove("animation.player.crossbow_hold")
        if(skipShieldBlockMainHand)
            defaultKeyAnimations.remove("animation.player.shield_block_main_hand")
        if(skipShieldBlockOffHand)
            defaultKeyAnimations.remove("animation.player.shield_block_off_hand")

        if(!clean)
            finalAnimations.putAll(defaultKeyAnimations)

        entity.description {
            animationsData = (animationsData ?: mutableMapOf()).apply {
                putAll(finalAnimations)
            }
        }

        return finalAnimations
    }
}