package com.lop.devtools.stdlib.commands.item

enum class ReplaceMode(private val s: String) {
    DESTROY("destroy"),
    KEEP("keep"),
    REPLACE("replace");

    override fun toString(): String {
        return s
    }
}