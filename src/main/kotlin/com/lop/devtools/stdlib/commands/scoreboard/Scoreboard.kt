package com.lop.devtools.stdlib.commands.scoreboard

class Scoreboard {
    /**
     * @sample sampleObj
     */
    val objectives = Objectives()

    /**
     * @sample samplePlayer
     */
    val players = Players()

    inner class Objectives {
        fun add(objective: String, displayName: String): String {
            return "scoreboard objectives add $objective dummy $displayName"
        }

        fun list(): String {
            return "scoreboard objectives list"
        }

        fun remove(objective: String): String {
            return "scoreboard objectives remove $objective"
        }

        fun setDisplay(
            objective: String,
            displayType: ScoreboardDisplayType,
            order: ScoreboardDisplayOrder = ScoreboardDisplayOrder.NONE
        ): String {
            return "scoreboard objectives setdisplay $displayType $objective" + if (order == ScoreboardDisplayOrder.NONE) "" else " $order"
        }
    }

    inner class Players {
        fun add(value: Int, target: String, objective: String): String {
            return "scoreboard players add $target $objective $value"
        }

        fun set(value: Int, target: String, objective: String): String {
            return "scoreboard players set $target $objective $value"
        }

        fun remove(value: Int, target: String, objective: String): String {
            return "scoreboard players remove $target $objective $value"
        }

        fun list(target: String): String {
            return "scoreboard players list $target"
        }

        /**
         * @param target the target selector
         * @param targetObjective the objective the target is on
         * @param operation =, %=, *=, -=, /=, <, >, ><
         * @param selector the other subject to compare the target to
         * @param objective the objective the selector is on
         */
        fun operation(
            target: String,
            targetObjective: String,
            operation: String,
            selector: String,
            objective: String
        ): String {
            return "scoreboard players operation $target $targetObjective $operation $selector $objective"
        }

        fun reset(target: String, objective: String): String {
            return "scoreboard players reset $target $objective"
        }

        fun test(target: String, objective: String, min: Int, max: Int): String {
            return "scoreboard players test $target $objective $min $max"
        }
    }

    private fun sampleObj() {
        objectives.add(objective = "obj", displayName = "Obj")
        objectives.remove(objective = "obj")
        objectives.list()
        objectives.setDisplay(
            objective = "obj",
            displayType = ScoreboardDisplayType.SIDEBAR,
            order = ScoreboardDisplayOrder.ASC
        )
    }

    private fun samplePlayer() {
        players.add(value = 2, target = "@p", objective = "obj")
        players.remove(value = 2, target = "@p", objective = "obj")
        players.set(value = 2, target = "@p", objective = "obj")
        players.list(target = "@p")
        players.operation(
            target = "@p[c=1]",
            targetObjective = "obj",
            operation = "=",
            selector = "@p[c=-1]",
            objective = "obj"
        )
        players.reset(target = "@p", objective = "obj")
        players.test(target = "@p", objective = "obj", min = 1, max = 2)
    }
}

enum class ScoreboardDisplayType(private val s: String) {
    SIDEBAR("sidebar"),
    BELOW_NAME("belowname"),
    LIST("list");

    override fun toString(): String {
        return s
    }
}

enum class ScoreboardDisplayOrder(private val s: String) {
    ASC("ascending"),
    DSC("descending"),
    NONE("");

    override fun toString(): String {
        return s
    }
}