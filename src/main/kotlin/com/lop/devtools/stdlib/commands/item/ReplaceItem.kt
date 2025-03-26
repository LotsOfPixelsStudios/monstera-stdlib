package com.lop.devtools.stdlib.commands.item

import com.lop.devtools.monstera.files.beh.item.comp.ItemWearable

class ReplaceItem {
    val entity = Entity()
    val block = Block()

    inner class Block {
        fun get(
            position: String = "~ ~ ~",
            slotId: Int = 0,
            itemName: String,
            amount: Int = 1,
            data: Int = 0,
            components: ItemLockComponents = ItemLockComponents.NONE
        ): String {
            return "replaceitem block $position slot.container $slotId $itemName $amount $data $components"
                .removeSuffix(" ")
        }

        fun get(
            position: String = "~ ~ ~",
            slotId: Int = 0,
            oldItemHandling: ReplaceMode,
            itemName: String,
            amount: Int = 1,
            data: Int = 0,
            components: ItemLockComponents = ItemLockComponents.NONE
        ): String {
            return "replaceitem block $position slot.container $slotId $oldItemHandling $itemName $amount $data $components"
                .removeSuffix(" ")
        }
    }

    inner class Entity {
        fun get(
            target: String,
            slot: ItemWearable.Slot,
            slotId: Int,
            itemName: String,
            amount: Int = 1,
            data: Int = 0,
            components: ItemLockComponents = ItemLockComponents.NONE
        ): String {
            return "replaceitem entity $target $slot $slotId $itemName $amount $data $components"
                .removeSuffix(" ")
        }

        fun get(
            target: String,
            slot: ItemWearable.Slot,
            slotId: Int,
            oldItemHandling: ReplaceMode,
            itemName: String,
            amount: Int = 1,
            data: Int = 0,
            components: ItemLockComponents = ItemLockComponents.NONE
        ): String {
            return "replaceitem entity $target $slot $slotId $oldItemHandling $itemName $amount $data $components"
                .removeSuffix(" ")
        }
    }
}