package com.lop.devtools.stdlib.furnitures.furniturepresets.seat

import com.lop.devtools.monstera.addon.Addon
import com.lop.devtools.stdlib.furnitures.FurnitureImpl

abstract class SeatingFurnitureImpl(name: String, displayName: String, addon: Addon) : SeatingFurniture,
    FurnitureImpl(name, displayName, addon) {

    private val seats: ArrayList<Triple<Float, Float, Float>> = arrayListOf()

    override fun addSeat(coordinate: Triple<Number, Number, Number>) {
        seats.add(Triple(coordinate.first.toFloat(), coordinate.second.toFloat(), coordinate.third.toFloat()))
    }

    internal fun buildSeatingFurniture() {
        super.behaviour {
            components {
                rideable {
                    exitText("Sneak to Dismount", "action.hint.exit.${addon.config.namespace}:$name")
                    interactText("Sit Down", key = "action.hint.interact.${addon.config.namespace}:$name")
                    seatCount = seats.size
                    seats.forEach {
                        seat {
                            position(it.first, it.second, it.third)
                        }
                    }
                }
            }
        }
        super.build()
    }
}