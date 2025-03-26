package com.lop.devtools.stdlib.blockbench.filestruc.data.anim

import com.google.gson.annotations.SerializedName
import java.util.*

data class BlKeyFrame(
    val channel: String,
    @SerializedName("data_points")
    val dataPoints: ArrayList<BlDataPoint>,
    val uuid: UUID,
    val time: Number,
    val color: Int,
    val interpolation: String
)
