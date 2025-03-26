package com.lop.devtools.stdlib.packaging

import com.lop.devtools.monstera.Config
import com.lop.devtools.monstera.addon.dev.zipper.Zipper
import com.lop.devtools.monstera.files.File
import com.lop.devtools.stdlib.packaging.behres.BasicBehResPacker
import com.lop.devtools.stdlib.packaging.behres.BehResPacker
import com.lop.devtools.stdlib.packaging.marketing.BasicMarketingPacker
import com.lop.devtools.stdlib.packaging.marketing.PackMarketing
import com.lop.devtools.stdlib.packaging.skin.PackSkinImpl
import com.lop.devtools.stdlib.packaging.skin.PackSkins
import com.lop.devtools.stdlib.packaging.store.BasicStoreArt
import com.lop.devtools.stdlib.packaging.store.PackStoreArt
import com.lop.devtools.stdlib.packaging.world.WorldPacker
import com.lop.devtools.stdlib.packaging.world.WorldPackerImpl
import org.slf4j.LoggerFactory
import java.io.File
import java.util.logging.Level

open class PackerImpl(final override val properties: Config) : Packer {
    override val buildDir: File
        get() = File("build", "package").also { it.mkdirs() }
    override val targetFile: File = File("zipVersions/package.zip")
    override var unsafeWorldPacker: WorldPacker = WorldPackerImpl(properties)
    override var packageName: String = properties.projectName
    override var world: File = File("")
    override var baseGameVersion: ArrayList<Int> = arrayListOf(1, 19, 0)
    override var lockTemplateOptions: Boolean = true
    override var unsafeBehPack: BehResPacker = BasicBehResPacker(properties, properties.behPath.toFile())
    override var unsafeResPack: BehResPacker = BasicBehResPacker(properties, properties.resPath.toFile())

    private val logger = LoggerFactory.getLogger("Packager")

    override fun addBehaviorPack(data: BehResPacker.() -> Unit) {
        unsafeBehPack.apply(data)
    }

    override fun addResourcePack(data: BehResPacker.() -> Unit) {
        unsafeResPack.apply(data)
    }

    override var unsafePackSkins: PackSkins = PackSkinImpl(properties)
    override var unsafePackMarketing: PackMarketing = BasicMarketingPacker(properties)
    override var unsafePackStore: PackStoreArt = BasicStoreArt(properties)

    override fun addSkinPack(data: PackSkins.() -> Unit) {
        unsafePackSkins.apply(data)
    }

    override fun addMarketing(data: PackMarketing.() -> Unit) {
        unsafePackMarketing.apply(data)
    }

    override fun addStoreArt(data: PackStoreArt.() -> Unit) {
        unsafePackStore.apply(data)
    }

    override fun build(targetZip: File) {
        //clean
        File(buildDir, "final").deleteRecursively()
        //skins
        if (unsafePackSkins.unsafeSkins.size > 0) {
            unsafePackSkins.unsafeSkins.forEach {
                it.copyTo(File(buildDir.absolutePath, "final", "Content", "skin_pack", it.name), true)
            }
            if (unsafePackSkins.unsafeManifestFile.exists())
                unsafePackSkins.unsafeManifestFile
                    .copyTo(
                        File(
                            buildDir.absolutePath,
                            "final",
                            "Content",
                            "skin_pack",
                            unsafePackSkins.unsafeManifestFile.name
                        ), true
                    )
            else
                unsafePackSkins.generateManifest()
                    .copyTo(
                        File(
                            buildDir.absolutePath,
                            "final",
                            "Content",
                            "skin_pack",
                            unsafePackSkins.unsafeManifestFile.name
                        ), true
                    )
            if (unsafePackSkins.unsafeSkinDefFile.exists())
                unsafePackSkins.unsafeSkinDefFile
                    .copyTo(
                        File(
                            buildDir.absolutePath,
                            "final",
                            "Content",
                            "skin_pack",
                            unsafePackSkins.unsafeSkinDefFile.name
                        ), true
                    )
            else
                unsafePackSkins.generateSkinsJSON()
                    .copyTo(
                        File(
                            buildDir.absolutePath,
                            "final",
                            "Content",
                            "skin_pack",
                            unsafePackSkins.unsafeSkinDefFile.name
                        ), true
                    )
            if (unsafePackSkins.unsafeTextDir.exists())
                unsafePackSkins.unsafeTextDir
                    .copyRecursively(
                        File(
                            buildDir.absolutePath,
                            "final",
                            "Content",
                            "skin_pack",
                            unsafePackSkins.unsafeTextDir.name
                        ), true
                    )
            else
                logger.warn("Invalid state, can't find text directory (${unsafePackSkins.unsafeTextDir})!")
        }
        //marketing
        unsafePackMarketing.buildTo(File(buildDir.absolutePath, "final"))
        //store
        unsafePackStore.buildTo(File(buildDir.absolutePath, "final"))
        //world
        if (world.exists() && world.isDirectory) {
            val db = File(world, "db")
            val levelDat = File(world, "level.dat")
            val worldIcon = File(world, "world_icon.jpeg")

            if (db.exists() && db.isDirectory) {
                db.copyRecursively(File(buildDir.absolutePath, "final", "Content", "world_template", "db"))
            }
            if (levelDat.exists() && levelDat.isFile) {
                levelDat.copyTo(File(buildDir.absolutePath, "final", "Content", "world_template", "level.dat"))
            }
            if (worldIcon.exists() && worldIcon.isFile) {
                levelDat.copyTo(File(buildDir.absolutePath, "final", "Content", "world_template", "world_icon.jpeg"))
            }
            unsafeBehPack.buildTo(
                File(
                    buildDir.absolutePath,
                    "final",
                    "Content",
                    "world_template",
                    "behavior_packs",
                    "BP"
                )
            )
            unsafeBehPack.buildTo(
                File(
                    buildDir.absolutePath,
                    "final",
                    "Content",
                    "world_template",
                    "resource_packs",
                    "RP"
                )
            )

            unsafeWorldPacker.generateWorldDependencies(
                File(
                    buildDir.absolutePath,
                    "final",
                    "Content",
                    "world_template"
                )
            )
            unsafeWorldPacker.generateWorldTexts(
                File(
                    buildDir.absolutePath,
                    "final",
                    "Content",
                    "world_template",
                    "texts"
                )
            )
            unsafeWorldPacker.generateWorldManifest(
                File(
                    buildDir.absolutePath,
                    "final",
                    "Content",
                    "world_template"
                )
            )
        }
        targetZip.let {
            Zipper.zipDirectory(File(buildDir, "final"), it)
        }
    }
}