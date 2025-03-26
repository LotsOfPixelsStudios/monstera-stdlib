package com.lop.devtools.stdlib.furnitures

import com.lop.devtools.monstera.addon.entity.behaviour.BehaviourEntity
import com.lop.devtools.monstera.addon.entity.resource.ResourceEntity
import com.lop.devtools.monstera.files.res.entities.comp.ResEntitySpawnEgg
import com.lop.devtools.stdlib.furnitures.breakable.BreakableFurniture
import com.lop.devtools.stdlib.furnitures.shared.FurnitureDropBehaviour
import java.io.File

interface Furniture {
    var name: String
    var displayName: String

    var texture: File
    var geometry: File

    var height : Float
    var width: Float
    var scale : Float

    var autoRotationAdjustment: Boolean
    var spawnable : Boolean
    var dropBehaviour: FurnitureDropBehaviour

    fun behaviour(behavior: BehaviourEntity.() -> Unit)

    fun resource(resource: ResourceEntity.() -> Unit)

    fun icon(data: ResEntitySpawnEgg.() -> Unit)

    fun breakable(data: BreakableFurniture.() -> Unit)
}