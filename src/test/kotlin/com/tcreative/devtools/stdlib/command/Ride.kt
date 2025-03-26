package com.tcreative.devtools.stdlib.command

import com.lop.devtools.stdlib.commands.Command
import org.junit.jupiter.api.Test

internal class Ride {
    @Test
    fun rideTest() {
        val command = Command.ride().selector("@s").startRiding.target("donkey") {

        }
        println(command)
    }
}