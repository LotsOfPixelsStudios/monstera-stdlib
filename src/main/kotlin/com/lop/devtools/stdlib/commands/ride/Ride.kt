package com.lop.devtools.stdlib.commands.ride

import com.lop.devtools.stdlib.commands.Selector

class Ride {
    fun selector(data: Selector): RideWithSelect {
        return RideWithSelect(data.toString())
    }

    fun selector(data: String): RideWithSelect {
        return RideWithSelect(data)
    }

    inner class RideWithSelect(selector: String = "@s") {
        val startRiding = RideStart("ride $selector start_riding")
        val stopRiding = "ride $selector stop_riding"
        val summonRide = RideSummon("ride $selector summon_ride")
        val summonRider = RiderSummon("ride $selector summon_rider")
        val evictRider = "ride $selector evict_riders"
    }
}

class RideStart(private val pre: String) {
    /**
     * ```
     * target(Selector.a) {
     *      teleportRules = TeleportRules.TELEPORT_RIDE
     *      howToFill = FillType.IF_GROUP_FITS
     * }
     * ```
     *
     * @param target the target to start riding
     * @param data other data
     */
    fun target(target: String, data: RideDataStart.() -> Unit = {}): String {
        RideDataStart().apply(data)
        return "$pre $target${RideDataStart().apply(data).getData()}"
    }
}

class RideDataStart {
    var teleportRules: TeleportRules? = null
    var howToFill: FillType? = null

    enum class TeleportRules(private val qualifier: String) {
        TELEPORT_RIDE("teleport_ride"),
        TELEPORT_RIDER("teleport_rider");

        override fun toString(): String {
            return qualifier
        }
    }

    enum class FillType(private val qualifier: String) {
        IF_GROUP_FITS("if_group_fits"),
        UNTIL_FULL("until_full");

        override fun toString(): String {
            return qualifier
        }
    }

    fun getData(): String {
        var retStr = ""
        if (teleportRules != null) {
            retStr += " $teleportRules"
        }
        if (howToFill != null) {
            if (teleportRules == null) {
                teleportRules = TeleportRules.TELEPORT_RIDER
                retStr += " $teleportRules $howToFill"
            } else {
                retStr += " $howToFill"
            }
        }

        return retStr
    }
}

class RideSummon(private val pre: String) {
    fun target(target: String, data: RideDataSummon.() -> Unit = {}): String {
        RideDataSummon().apply(data)
        return "$pre $target ${RideDataSummon().apply(data).getData()}"
    }
}

class RiderSummon(private val pre: String) {
    fun target(target: String, data: RiderDataSummon.() -> Unit = {}): String {
        RiderDataSummon().apply(data)
        return "$pre $target${RiderDataSummon().apply(data).getData()}"
    }
}

class RideDataSummon {
    var rideRules: RideRules = RideRules.REASSIGN_RIDES
    var spawnEvent: String = "minecraft:entity_spawned"
    var nameTag: String = ""

    enum class RideRules(private val qualifier: String) {
        NO_RIDE_CHANGE("no_ride_change"),
        REASSIGN_RIDES("reassign_rides"),
        SKIP_RIDERS("skip_riders");

        override fun toString(): String {
            return qualifier
        }
    }

    fun getData(): String {
        var subStr = "$rideRules $spawnEvent"
        if (nameTag.isNotEmpty()) {
            subStr += " $nameTag"
        }
        return subStr
    }
}

class RiderDataSummon {
    var spawnEvent: String = "minecraft:entity_spawned"
    var nameTag: String = ""

    fun getData(): String {
        var subStr = spawnEvent
        if (nameTag.isNotEmpty()) {
            subStr += " $nameTag"
        }
        return subStr
    }
}