package com.lop.devtools.stdlib.furnitures.furniturepresets.seat

import com.lop.devtools.stdlib.furnitures.Furniture

interface SeatingFurniture: Furniture {
    fun addSeat(coordinate: Triple<Number, Number, Number>)
}