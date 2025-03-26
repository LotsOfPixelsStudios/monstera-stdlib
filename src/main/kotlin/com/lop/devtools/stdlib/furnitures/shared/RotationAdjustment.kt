package com.lop.devtools.stdlib.furnitures.shared

import com.lop.devtools.monstera.addon.Addon
import com.lop.devtools.monstera.addon.molang.Query
import com.lop.devtools.monstera.files.animcontroller.AnimationControllers

fun loadRotationAdjustment(addon: Addon) {
    rotationAdjustmentFunction(addon)
    rotationAdjustmentAnimController(addon)
}

fun rotationAdjustmentFunction(addon: Addon) {
    with(addon) {
        mcFunction("furniture_rotation_adjustment") {
            entries = arrayListOf(
                "execute as @s[rym=-45,ry=44] run tp @s ~ ~ ~ 0 ~",
                "execute as @s[rym=45,ry=134] run tp @s ~ ~ ~ 90 ~",
                "execute as @s[rym=135,ry=-134] run tp @s ~ ~ ~ 180 ~",
                "execute as @s[rym=-135,ry=-46] run tp @s ~ ~ ~ 270 ~"
            )
        }
    }
}

fun rotationAdjustmentAnimController(addon: Addon) {
    val con = AnimationControllers()
    con.animController("controller.animation.furniture.rotation_adjustment") { // TODO remove temporary fix
        state("init") {
            transition("default", Query.True)
        }
        state("default") {
            onEntry = arrayListOf("/function furniture_rotation_adjustment")
        }

    }
    con.build("furniture_rotation_adjustment", addon.config.paths.behAnimController)
}