package com.lop.devtools.stdlib.player.res

import com.lop.devtools.monstera.addon.api.MonsteraBuildSetter
import com.lop.devtools.monstera.files.res.entities.ResEntity
import com.lop.devtools.monstera.files.res.entities.comp.ResEntityScripts

@Suppress("unused", "MemberVisibilityCanBePrivate")
class PlayerResScripts(private val entity: ResEntity) {
    var clean: Boolean = false

    var scriptScale: String? = "0.9375"
    var scriptInitialize: ArrayList<String>? = arrayListOf(
        "variable.is_holding_right = 0.0;",
        "variable.is_blinking = 0.0;",
        "variable.last_blink_time = 0.0;",
        "variable.hand_bob = 0.0;"
    )
    var scriptPreAnim: ArrayList<String>? = arrayListOf(
        "variable.helmet_layer_visible = 1.0;",
        "variable.leg_layer_visible = 1.0;",
        "variable.boot_layer_visible = 1.0;",
        "variable.chest_layer_visible = 1.0;",
        "variable.attack_body_rot_y = Math.sin(360*Math.sqrt(variable.attack_time)) * 5.0;",
        "variable.tcos0 = (math.cos(query.modified_distance_moved * 38.17) * query.modified_move_speed / variable.gliding_speed_value) * 57.3;",
        "variable.first_person_rotation_factor = math.sin((1 - variable.attack_time) * 180.0);",
        "variable.hand_bob = query.life_time < 0.01 ? 0.0 : variable.hand_bob + ((query.is_on_ground && query.is_alive ? math.clamp(math.sqrt(math.pow(query.position_delta(0), 2.0) + math.pow(query.position_delta(2), 2.0)), 0.0, 0.1) : 0.0) - variable.hand_bob) * 0.02;",

        "variable.map_angle = math.clamp(1 - variable.player_x_rotation / 45.1, 0.0, 1.0);",
        "variable.item_use_normalized = query.main_hand_item_use_duration / query.main_hand_item_max_duration;"
    )
    var scriptAnimate: ArrayList<String>? = arrayListOf("root")

    private val resEntityScripts = ResEntityScripts()

    fun addScript(data: ResEntityScripts.() -> Unit) {
        resEntityScripts.apply(data)
    }

    @OptIn(MonsteraBuildSetter::class)
    fun build(): ResEntityScripts {
        if(clean)
            return resEntityScripts

        resEntityScripts.scale = scriptScale
        scriptInitialize?.forEach {
            resEntityScripts.initializeEntry(it)
        }
        scriptPreAnim?.forEach {
            resEntityScripts.preAnimationEntry(it)
        }
        scriptAnimate?.forEach {
            resEntityScripts.animate(it)
        }


        entity.description {
            scriptsData = resEntityScripts
        }
        return resEntityScripts
    }
}