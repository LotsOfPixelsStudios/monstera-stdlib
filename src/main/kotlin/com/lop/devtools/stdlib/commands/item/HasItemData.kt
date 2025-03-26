package com.lop.devtools.stdlib.commands.item

import com.lop.devtools.monstera.files.beh.item.comp.ItemWearable

/**
 * @param item the item name
 * @param location the location to look for (Slot)
 * @param slot the specific Slot, -1 if ignoring
 * @param quantity how many to look for, -1 if ignoring
 * @param data the data modifier on the item, -1 if ignoring
 */
data class HasItemData(
    val item: String,
    val location: ItemWearable.Slot = ItemWearable.Slot.INVENTORY,
    val slot: Int = -1,
    val quantity: Int = -1,
    val data: Int = -1
) {
    override fun toString(): String {
        var retStr = "item=$item,location=$location"
        if(slot >= 0)
            retStr += ",slot=$slot"
        if(quantity >= 0)
            retStr += ",quantity=$quantity"
        if(data >= 0)
            retStr += ",data=$data"

        return "{$retStr}"
    }
}

