package com.lop.devtools.stdlib.packaging.store

import com.lop.devtools.monstera.Config
import java.io.File

class BasicStoreArt(properties: Config) : StoreArtImpl(properties) {
    override val targetDir = File("build", "store_art_tmp")
}