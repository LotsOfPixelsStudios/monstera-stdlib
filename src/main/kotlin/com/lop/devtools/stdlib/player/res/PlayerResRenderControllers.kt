package com.lop.devtools.stdlib.player.res

import com.lop.devtools.monstera.addon.Addon
import com.lop.devtools.monstera.addon.molang.Query
import com.lop.devtools.monstera.files.res.entities.ResEntity
import com.lop.devtools.monstera.files.res.rendercontrollers.ResRenderController
import com.lop.devtools.monstera.files.res.rendercontrollers.ResRenderControllers

@Suppress("unused", "MemberVisibilityCanBePrivate")
class PlayerResRenderControllers(private val addon: Addon, private val entity: ResEntity) {
    var clean: Boolean = false

    var skipFirstPerson: Boolean = false
    var skipThirdPerson: Boolean = false
    var skipMap: Boolean = false

    private val renderControllers: ArrayList<Pair<String, String>> = arrayListOf()
    private val playerController: ResRenderControllers = ResRenderControllers()

    fun addCustomRenderController(name: String, query: String) {
        renderControllers.add("controller.render.player.$name" to query)
    }

    fun addCustomRenderController(name: String, query: String, data: ResRenderController.() -> Unit) {
        addCustomRenderController(name, query)
        playerController.apply {
            controllers("controller.render.player.${name}", data)
        }
    }

    fun build(): ArrayList<Pair<String, String>> {
        if (!playerController.renderControllers.isNullOrEmpty()) {
            playerController.build(
                "monstera_player", addon.config.paths.resRenderControllers
            )
        }
        if (clean)
            return renderControllers
        if (!skipFirstPerson)
            addCustomRenderController("first_person", "variable.is_first_person")
        if (!skipThirdPerson)
            addCustomRenderController("third_person", "!variable.is_first_person && !variable.map_face_icon")
        if (!skipMap)
            addCustomRenderController("map", "variable.map_face_icon")
        entity.description {
            renderControllers.forEach {
                renderController(it.first, Query(it.second))
            }
        }
        return renderControllers
    }
}