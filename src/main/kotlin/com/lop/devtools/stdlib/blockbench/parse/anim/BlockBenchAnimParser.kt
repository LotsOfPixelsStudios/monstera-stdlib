package com.lop.devtools.stdlib.blockbench.parse.anim

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.lop.devtools.monstera.files.File
import com.lop.devtools.monstera.files.res.animations.ResAnimKeyFrames
import com.lop.devtools.monstera.files.res.animations.resAnimation
import com.lop.devtools.stdlib.blockbench.filestruc.BlBase
import com.lop.devtools.stdlib.blockbench.filestruc.data.anim.BlAnimators
import com.lop.devtools.stdlib.blockbench.filestruc.data.anim.BlKeyFrame
import org.slf4j.LoggerFactory
import java.io.File

class BlockBenchAnimParser(private val base: BlBase) {
    companion object {
        val tmpDir = File("build", "tmp", "blockbench", "anim")

        fun getAnimation(base: BlBase): File {
            val tmpDirID = File(tmpDir, base.modelIdentifier.replace("\\s".toRegex(), ""))
            tmpDirID.mkdirs()
            val target = File(tmpDirID, "${base.modelIdentifier}.animation.json")
            val gson = GsonBuilder().setPrettyPrinting().create()

            val parser = BlockBenchAnimParser(base)
            val data = gson.toJson(parser.map())
            target.writeText(data)
            return target
        }
    }

    private val gson = Gson()
    private val logger = LoggerFactory.getLogger("bbmodel parser")

    private fun map(): MutableMap<String, Any> {
        return resAnimation {
            formatVersion = "1.8.0"
            animations {
                base.animations?.forEach { animation ->
                    animation(animation.name) {
                        animationLength = animation.length
                        loop = when (animation.loop) {
                            "hold" -> "hold_on_last_frame"
                            else -> true
                        }
                        bones {
                            animation.animators.entrySet().forEach { animators ->
                                val animator = gson.fromJson(animators.value, BlAnimators::class.java)

                                bone(animator.name) {
                                    animator.keyframes.forEach { frame ->
                                        if(frame.time.toFloat() > animationLength.toFloat())
                                            animationLength = frame.time.toFloat()

                                        when (frame.channel) {
                                            "rotation" -> rotation {
                                                fillAnim(this, frame)
                                            }

                                            "position" -> position {
                                                fillAnim(this, frame)
                                            }

                                            "scale" -> scale {
                                                fillAnim(this, frame)
                                            }
                                            else -> logger.warn(
                                                "Unknown Animation Chanel '${frame.channel}'"
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun fillAnim(bedrock: ResAnimKeyFrames, blockbench: BlKeyFrame) {
        with(bedrock) {
            if (blockbench.interpolation == "step" && blockbench.dataPoints.size == 1) {
                keyFrame(
                    blockbench.time.toString(), arrayListOf(
                        blockbench.dataPoints[0].x,
                        blockbench.dataPoints[0].y,
                        blockbench.dataPoints[0].z
                    )
                )
            } else {
                keyFrame(blockbench.time.toString()) {
                    if (blockbench.interpolation != "step")
                        lerpMode = blockbench.interpolation
                    if (blockbench.dataPoints.size > 1) {
                        pre = arrayListOf(
                            blockbench.dataPoints[0].x,
                            blockbench.dataPoints[0].y,
                            blockbench.dataPoints[0].z
                        )
                    }
                    post = arrayListOf(
                        blockbench.dataPoints[0].x,
                        blockbench.dataPoints[0].y,
                        blockbench.dataPoints[0].z
                    )
                }
            }
        }
    }
}