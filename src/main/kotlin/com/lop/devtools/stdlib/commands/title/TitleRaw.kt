package com.lop.devtools.stdlib.commands.title

import com.lop.devtools.monstera.files.lang.langKey

class TitleRaw {
    var data: String = ""

    val clear: Unit
        get() {
            data = "clear"
            return
        }

    val reset: Unit
        get() {
            data = "reset"
            return
        }

    fun text(position: TitlePosition, output: String, key: String) {
        langKey(key, output)
        data = "$position {\"rawtext\":[{\"translate\":\"$key\"}]}"
    }

    fun times(fadeIn: Float, stay: Float, fadeOut: Float) {
        data = "$fadeIn $stay $fadeOut"
    }
}