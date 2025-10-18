package org.ender_development.tinkeringworkshop.config

import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.common.Loader
import org.ender_development.catalyx.config.ConfigParser
import org.ender_development.tinkeringworkshop.Reference
import org.ender_development.tinkeringworkshop.TinkeringWorkshop
import java.io.File

object BookshelfParser : IParser<TWRawBookshelf, TWBookshelf> {
    override val dataRaw = mutableListOf<TWRawBookshelf>()
    override val dataSanitized = mutableListOf<TWBookshelf>()
    override val json = File(Loader.instance().configDir, "/${Reference.MODID}/bookshelfs.json")

    override fun loadFromJson() {
        dataRaw.clear()
        dataSanitized.clear()
        if (!json.exists()) {
            TinkeringWorkshop.logger.warn("Bookshelf configuration file not found: ${json.absolutePath}")
            generateDefaultConfig(json)
        }
        try {
            val fileContent = json.readText()
            dataRaw.addAll(gson.fromJson(fileContent, Array<TWRawBookshelf>::class.java))
            TinkeringWorkshop.logger.info("Loaded ${dataRaw.size} bookshelf configurations.")
        } catch (e: Exception) {
            TinkeringWorkshop.logger.error("Failed to load bookshelf configuration from ${json.absolutePath}", e)
        }
        sanitizeData()
        if (ConfigHandler.debugMode) TinkeringWorkshop.logger.info(dataSanitized.toString())
    }

    override fun sanitizeData() {
        dataSanitized.addAll(
            dataRaw.map {
                it.block ?: TinkeringWorkshop.logger.error("Bookshelf block is null in config: $it").let { return@map null }
                val blockState = ConfigParser.ConfigBlockState(it.block).state
                blockState?.let { bs ->
                    TWBookshelf(
                        blockState = bs,
                        power = it.power?.coerceAtLeast(0.0) ?: 0.0,
                        simultaneousEnchantment = it.simultaneousEnchantment?.coerceAtLeast(1) ?: 1,
                        maxConsidered = it.maxConsidered?.coerceAtLeast(1) ?: Int.MAX_VALUE,
                    )
                } ?: TinkeringWorkshop.logger.error("Block not found for bookshelf: ${it.block}").let { null }
            }.filterNotNull(),
        )
    }

    override fun generateDefaultConfig(file: File) {
        val cfg = mutableListOf<TWRawBookshelf>()
        cfg.add(
            TWRawBookshelf(
                _comment = "Example bookshelf configuration",
                block = "minecraft:bookshelf",
                power = 1.0,
                simultaneousEnchantment = 1,
                maxConsidered = 10,
            ),
        )
        try {
            val jsonContent = gson.toJson(cfg)
            file.parentFile?.mkdirs()
            file.writeText(jsonContent)
            TinkeringWorkshop.logger.info("Generated default bookshelf configuration at: ${file.absolutePath}")
        } catch (e: Exception) {
            TinkeringWorkshop.logger.error("Failed to generate default bookshelf configuration at: ${file.absolutePath}", e)
        }
    }

    override operator fun get(name: ResourceLocation): TWBookshelf? = dataSanitized.find { it.blockState.block.registryName == name }
}

fun String.toBookshelf(): TWBookshelf? = ResourceLocation(this).toBookshelf()

fun ResourceLocation.toBookshelf(): TWBookshelf? = BookshelfParser.get(this)

fun ItemStack.toBookshelf(): TWBookshelf? = this.item.registryName?.toBookshelf() ?: TinkeringWorkshop.logger.info("No bookshelf found for item: ${this.item.registryName}").let { null }
