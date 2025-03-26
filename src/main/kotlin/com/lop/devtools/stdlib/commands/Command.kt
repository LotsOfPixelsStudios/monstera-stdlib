package com.lop.devtools.stdlib.commands

import com.lop.devtools.monstera.files.lang.langKey
import com.lop.devtools.stdlib.commands.item.ItemLockComponents
import com.lop.devtools.stdlib.commands.item.ReplaceItem
import com.lop.devtools.stdlib.commands.ride.Ride
import com.lop.devtools.stdlib.commands.scoreboard.Scoreboard
import com.lop.devtools.stdlib.commands.structure.Structure
import com.lop.devtools.stdlib.commands.title.TitleRaw

/**
 * a command can only be executed from a timeline, onEntry, onExit or a function
 */
object Command {

    /**
     * modify a text in a way the normal /say command can't do it.
     * Note the command has no visible executor.
     *
     * @param target the selector like, [Selector]
     * @param output the text to show when executing the command in the chat
     * @param key the key is only necessary for texts, the developer has to provide the key as he has to make sure it's unique
     * @param addon necessary for the added lang keys
     *
     * @return a command/function command
     */
    fun tellRaw(target: String = "@a", output: String, key: String): String {
        langKey(key, output)
        return "tellraw $target {\"rawtext\":[{\"translate\":\"$key\"}]}"
    }

    /**
     * ```
     * titleRaw(Selector.a) {
     *      clear
     *      //or
     *      reset
     *      //or
     *      text(TitlePosition.TITLE, "output", "key.text", addon)
     *      //or
     *      times(0.2f, 3f, 0.2f)
     * }
     * ```
     */
    fun titleRaw(target: String = "@a", data: TitleRaw.() -> Unit): String {
        val title = TitleRaw().apply(data)
        if(title.data.isEmpty())
            throw IllegalArgumentException("not enough arguments")
        return "titleraw $target ${title.data}"
    }

    fun scoreboard(): Scoreboard {
        return Scoreboard()
    }

    fun give(
        target: String,
        item: String,
        amount: Int = 1,
        data: Int = 0,
        components: ItemLockComponents = ItemLockComponents.NONE
    ): String {
        return "give $target $item $amount $data $components".removeSuffix(" ") //remove trailing space
    }

    fun replaceItem() = ReplaceItem()

    fun ride() = Ride()

    /**
     * ```
     * structure().load() {}
     * structure().delete()
     * structure().save() {}
     * ```
     */
    fun structure() = Structure()
}