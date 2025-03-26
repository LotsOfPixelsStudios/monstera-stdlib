package com.lop.devtools.stdlib.commands.title

enum class TitlePosition(private val s: String) {
    TITLE("title"),
    SUBTITLE("subtitle"),
    ACTIONBAR("actionbar");

    override fun toString(): String {
        return s
    }
}