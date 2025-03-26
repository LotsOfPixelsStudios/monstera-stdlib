package com.lop.devtools.stdlib.entitiy

import com.lop.devtools.monstera.addon.entity.behaviour.BehaviourEntity

/**
 * creates an empty animation on the entity
 */
fun BehaviourEntity.timerAnimation(length: Number): String {
    val animName = "timer_${length.toString().replace(".", "_")}"

    animation(animName) {
        timeline {  }
        animLength = length
    }

    return animName
}