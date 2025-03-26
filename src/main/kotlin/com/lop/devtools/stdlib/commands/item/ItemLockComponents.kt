package com.lop.devtools.stdlib.commands.item

enum class ItemLockComponents(private val s: String) {
    LOCK_IN_SLOT("{\"item_lock\":{\"mode\":\"lock_in_slot\"}}"),
    LOCK_IN_INVENTORY("{\"item_lock\":{\"mode\":\"lock_in_inventory\"}}"),
    NONE("");

    override fun toString(): String {
        return s
    }
}