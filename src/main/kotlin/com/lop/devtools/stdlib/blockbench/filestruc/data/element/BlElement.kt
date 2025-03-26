package com.lop.devtools.stdlib.blockbench.filestruc.data.element

import com.google.gson.annotations.SerializedName
import com.lop.devtools.stdlib.blockbench.filestruc.data.element.faces.BlFaces
import java.util.*

data class BlElement(
    val name: String? = null,
    val rescale: Boolean? = null,
    val locked: Boolean? = null,
    val from: ArrayList<Float>? = null,
    val to: ArrayList<Float>? = null,
    val autouv: Number? = null,
    val color: Number? = null,
    val visibility: Boolean? = null,
    val shade: Boolean? = null,
    val rotation: ArrayList<Float>? = null,
    val position: ArrayList<Float>? = null,
    val origin: ArrayList<Float>? = null,
    @SerializedName("uv_offset")
    val uvOffset: ArrayList<Float>? = null,
    @SerializedName("mirror_uv")
    val mirrorUv: Boolean? = null,
    val faces: BlFaces? = null,
    val type: String? = null,
    val uuid: UUID? = null,
    val inflate: Float? = null
)
