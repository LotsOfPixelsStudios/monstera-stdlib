package com.lop.devtools.stdlib.furnitures.furniturepresets.storage

import com.lop.devtools.monstera.files.beh.entitiy.components.scraped.Inventory
import com.lop.devtools.stdlib.furnitures.Furniture
import com.lop.devtools.stdlib.furnitures.shared.FurnitureDropBehaviour

interface StorageFurniture: Furniture {
    override var dropBehaviour: FurnitureDropBehaviour

    /**
     * define a generic inventory of the furniture
     */
    fun container(data: Inventory.() -> Unit)

    /**
     * add a normal-sized inventory (27 slots) to the furniture
     */
    fun largeContainer(displayName: String)

    /**
     * add a small hopper inventory (5 slots) to the furniture
     */
    fun smallContainer(displayName: String)
}