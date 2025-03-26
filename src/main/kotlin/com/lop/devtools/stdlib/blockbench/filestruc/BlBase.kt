package com.lop.devtools.stdlib.blockbench.filestruc

import com.google.gson.JsonArray
import com.google.gson.annotations.SerializedName
import com.lop.devtools.stdlib.blockbench.filestruc.data.BlMeta
import com.lop.devtools.stdlib.blockbench.filestruc.data.BlResolution
import com.lop.devtools.stdlib.blockbench.filestruc.data.BlTexture
import com.lop.devtools.stdlib.blockbench.filestruc.data.anim.BlAnimation
import com.lop.devtools.stdlib.blockbench.filestruc.data.element.BlElement
import java.util.UUID

data class BlBase (
    val meta: BlMeta,
    val name: String,
    @SerializedName("model_identifier")
    val modelIdentifier: String,
    @SerializedName("visible_box")
    val visibleBox: ArrayList<Float>? = null,
    val variable_placeholders: String,
    val variable_placeholder_buttons: ArrayList<Any>,
    val timeline_setups: ArrayList<Any>,
    val resolution: BlResolution,
    val elements: ArrayList<BlElement>,
    val outliner: JsonArray,
    val textures: ArrayList<BlTexture>,
    val animations: ArrayList<BlAnimation>?,
    val type: String,
    val uuid: UUID
)