package com.lop.devtools.stdlib.blockbench.filestruc.data.anim

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import java.util.UUID

data class BlAnimation(
    val uuid: UUID,
    val name: String,
    val loop: String,
    val override: Boolean = false,
    val length: Float = 0f,
    val snapping: Number = 20,
    val selected: Boolean = false,
    val saved: Boolean = true,
    val path: String? = null,
    @SerializedName("anim_time_update")
    val animTimeUpdate: String = "",
    @SerializedName("blend_weight")
    val blendWeight: String = "",
    @SerializedName("start_delay")
    val startDelay: String = "",
    @SerializedName("loop_delay")
    val loopDelay: String = "",
    val animators: JsonObject
)