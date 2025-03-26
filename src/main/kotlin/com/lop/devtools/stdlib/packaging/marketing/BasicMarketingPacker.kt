package com.lop.devtools.stdlib.packaging.marketing

import com.lop.devtools.monstera.Config
import java.io.File

class BasicMarketingPacker(override val properties: Config) : MarketingImpl(properties) {
    override val targetDir = File("build", "marketing_tmp")
}