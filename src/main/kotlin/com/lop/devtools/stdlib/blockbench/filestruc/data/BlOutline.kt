package com.lop.devtools.stdlib.blockbench.filestruc.data

import com.google.gson.JsonArray
import java.util.UUID

data class BlOutline(
    val name: String,
    val origin: ArrayList<Float>,
    val bedrock_binding: String,
    val color: Number,
    val uuid: UUID,
    val export: Boolean,
    val isOpen: Boolean,
    val locked: Boolean,
    val visibility: Boolean,
    val autouv: Number,
    val rotation: ArrayList<Float>? = null,
    /**
     * either uuid as string or this object itself again
     */
    val children: JsonArray? = null
)
