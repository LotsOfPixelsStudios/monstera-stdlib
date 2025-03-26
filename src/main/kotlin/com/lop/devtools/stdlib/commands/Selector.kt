package com.lop.devtools.stdlib.commands

import com.lop.devtools.stdlib.commands.item.HasItemData

/**
 * sample usage: Selector.e[Selector.family("controller")]
 */
@Suppress("MemberVisibilityCanBePrivate", "unused") //usage & modify of fields by design
class Selector(var currentSelector: String = "@e") {
    companion object {
        var allPlayers = Selector("@a")
        var a = Selector("@a")
        var entity = Selector("@e")
        var e = Selector("@e")
        var self = Selector("@s")
        var s = Selector("@s")
        var nearestPlayer = Selector("@p")
        var p = Selector("@p")
        var randomPlayer = Selector("@r")
        var r = Selector("@r")

        /**
         * select a specific amount of entities
         * negatives means counting up to 0 but in reverse distance
         * -> /damage @e[type=skeleton,c=-2] 2 will damage the furthest two skeletons
         * @return c=value
         */
        fun c(value: Int): String {
            return "c=$value"
        }

        /**
         * Selects entities inside the given bounding box
         * @return dx=value
         */
        fun dx(value: Number): String {
            return "dx=$value"
        }

        /**
         * Selects entities inside the given bounding box
         * @return dy=value
         */
        fun dy(value: Number): String {
            return "dy=$value"
        }

        /**
         * Selects entities inside the given bounding box
         * @return dz=value
         */
        fun dz(value: Number): String {
            return "dz=$value"
        }

        /**
         * select entities with a certain family type, use ! to negate the family type
         * @return family=value
         */
        fun family(type: String): String {
            return "family=$type"
        }

        /**
         * items are checked with a logical AND, so the selector checks if all items are present, fails if at least one is missing
         *
         * @return hasitem=[{item=value}, {...}]
         */
        fun hasItem(vararg value: String): String {
            var currentCommand = "hasitem=["
            value.forEach {
                currentCommand = "$currentCommand{item=$it},"
            }
            currentCommand = currentCommand.removeSuffix(",")
            currentCommand = "$currentCommand]"
            return currentCommand
        }

        /**
         * items are checked with a logical AND, so the selector checks if all items are present, fails if at least one is missing
         *
         * @param value data options to look for a specific item/items
         * @return hasitem=[{item=value}, {...}]
         */
        fun hasItem(vararg value: HasItemData): String {
            var currentCommand = "hasitem=["
            value.forEach {
                currentCommand += "$it,"
            }
            currentCommand = currentCommand.removeSuffix(",")
            currentCommand += "]"
            return currentCommand
        }

        /**
         * select entities with a maximum of xp-level
         * @return l=value
         */
        fun l(value: Int): String {
            return "l=$value"
        }

        /**
         * select entities with a minimum of xp-level
         * @return lm=value
         */
        fun lm(value: Int): String {
            return "lm=$value"
        }

        /**
         * select by gamemode (only player)
         * @param value can be string like s,c,a or survival, creative, adventure or an int 0,1,2 or d for default
         * @return m=value
         */
        fun m(value: Any): String {
            return "m=$value"
        }

        /**
         * select an entity with a specific name
         * @return name=value
         */
        fun name(value: String): String {
            return "name=$value"
        }

        /**
         * select entities within a certain radius
         * @return r=value
         */
        fun r(value: Number): String {
            return "r=$value"
        }

        /**
         * select entities outside a certain radius
         * @return rm=value
         */
        fun rm(value: Number): String {
            return "rm=$value"
        }

        /**
         * select entities with a certain rotation (minimum)
         * @return rx=value
         */
        fun rx(value: Number): String {
            return "rx=$value"
        }

        /**
         * select entities with a certain rotation (maximum)
         * @return rxm=value
         */
        fun rxm(value: Number): String {
            return "rxm=$value"
        }

        /**
         * select entities with a certain rotation (minimum)
         * @return ry=value
         */
        fun ry(value: Number): String {
            return "ry=$value"
        }

        /**
         * select entities with a certain rotation (maximum)
         * @return rym=value
         */
        fun rym(value: Number): String {
            return "rym=$value"
        }

        /**
         * select entities with a specific score on a scoreboard
         *
         * value selecting:
         * - N.. is any number greater than or equal to N.
         * - ..N is any number less than or equal to N.
         * - N..M is any number between N and M, inclusive.
         * @param elements key-pairs with the first element being the objective and the second element the value to check (int or string)
         * @return scores{key=value, key=value, ...}
         */
        fun scores(vararg elements: Pair<String, String>): String {
            var strBuild = "scores={"
            for (i in elements.indices) {
                strBuild += if (i == elements.size - 1) {
                    "${elements[i].first}=${elements[i].second}"
                } else {
                    "${elements[i].first}=${elements[i].second},"
                }
            }
            return "$strBuild}"
        }

        /**
         * select entities with a specific tag
         * use ! to negate tag
         * @return tag=value
         */
        fun tag(value: String): String {
            return "tag=$value"
        }

        /**
         * select entities with type like "type=chicken"
         * @return type=value
         */
        fun type(value: String): String {
            return "type=$value"
        }

        /**
         * execute the command on this x position (entire length)
         * -> @p[x=140, y=64, z=-200] will select the nearest player to this coordinate
         * @return x=value
         */
        fun x(value: Number): String {
            return "x=$value"
        }

        /**
         * execute the command on this y position (entire length)
         * -> @p[x=140, y=64, z=-200] will select the nearest player to this coordinate
         * @return y=value
         */
        fun y(value: Number): String {
            return "y=$value"
        }

        /**
         * execute the command on this z position (entire length)
         * -> @p[x=140, y=64, z=-200] will select the nearest player to this coordinate
         * @return z=value
         */
        fun z(value: Number): String {
            return "z=$value"
        }
    }

    /**
     * sample usage: Selector.e[Selector.family("controller")]
     */
    operator fun get(vararg elements: String): String {
        if (elements.isNotEmpty()) {
            var inner = ""
            for (i in elements.indices) {
                inner += if (i == elements.size - 1) {
                    elements[i]
                } else {
                    "${elements[i]},"
                }
            }
            return "$currentSelector[$inner]"
        }
        return currentSelector
    }

    override fun toString(): String {
        return currentSelector
    }
}

