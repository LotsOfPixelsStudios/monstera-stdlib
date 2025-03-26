package com.lop.devtools.stdlib.furnitures.furniturepresets.storage

import com.lop.devtools.monstera.addon.Addon
import com.lop.devtools.monstera.addon.addon
import com.lop.devtools.monstera.files.beh.entitiy.components.scraped.Inventory
import com.lop.devtools.stdlib.furnitures.FurnitureImpl

abstract class StorageFurnitureImpl(name: String, displayName: String, addon: Addon) : StorageFurniture,
    FurnitureImpl(name, displayName, addon) {
    private var container: Inventory.() -> Unit = {}

    override fun container(data: Inventory.() -> Unit) {
        this.container = data
    }

    override fun largeContainer(displayName: String) {
        container = {
            containerType = "inventory"
            inventorySize = 27
            addon.config.langFileBuilder.addonRes.addOrReplace("entity.$name.name", displayName)
        }
    }

    override fun smallContainer(displayName: String) {
        container = {
            containerType = "minecart_hopper"
            inventorySize = 5
            addon.config.langFileBuilder.addonRes.addOrReplace("entity.$name.name", displayName)
        }
    }

    internal fun buildStorageFurniture() {
        super.behaviour {
            components {
                inventory {
                    this.apply(container)
                }
            }
        }
        super.build()
    }
}