package com.lop.devtools.stdlib.vehicles

import com.lop.devtools.monstera.addon.entity.behaviour.BehaviourEntity
import com.lop.devtools.monstera.addon.entity.resource.ResourceEntity
import com.lop.devtools.monstera.files.res.entities.comp.ResEntitySpawnEgg
import com.lop.devtools.stdlib.furnitures.shared.FurnitureDropBehaviour
import com.lop.devtools.stdlib.vehicles.shared.SteeringType
import java.io.File

interface Vehicle {
    var name: String
    var displayName: String

    var texture: File
    var geometry: File

    var height : Float
    var width: Float
    var scale : Float

    var steeringType : SteeringType
    var speed : Float

    var spawnable : Boolean
    var dropBehaviour: FurnitureDropBehaviour

    fun addSeat(coordinate: Triple<Number, Number, Number>)
    fun behaviour(behavior: BehaviourEntity.() -> Unit)

    fun resource(resource: ResourceEntity.() -> Unit)

    fun icon(data: ResEntitySpawnEgg.() -> Unit)
}