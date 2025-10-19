package org.ender_development.tinkeringworkshop.config

import net.minecraft.enchantment.Enchantment
import net.minecraft.item.EnumDyeColor
import net.minecraft.item.EnumRarity
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.common.Loader
import org.ender_development.catalyx.config.ConfigParser
import org.ender_development.tinkeringworkshop.Reference
import org.ender_development.tinkeringworkshop.TinkeringWorkshop
import java.io.File
import java.util.Locale

enum class BlockCheckLogic {
    ANY,
    ALL,
    SINGLE,
}

object EnchantmentParser : IParser<TWRawEnchantment, TWEnchantment> {
    override val dataRaw = mutableListOf<TWRawEnchantment>()
    override val dataSanitized = mutableListOf<TWEnchantment>()

    override val json = File(Loader.instance().configDir, "/${Reference.MODID}/enchantments.json")

    override fun loadFromJson() {
        dataRaw.clear()
        dataSanitized.clear()
        if (!json.exists()) {
            TinkeringWorkshop.logger.warn("Enchantment configuration file not found: ${json.absolutePath}")
            generateDefaultConfig(json)
        }
        try {
            val fileContent = json.readText()
            dataRaw.addAll(gson.fromJson(fileContent, Array<TWRawEnchantment>::class.java))
            TinkeringWorkshop.logger.info("Loaded ${dataRaw.size} enchantment configurations.")
        } catch (e: Exception) {
            TinkeringWorkshop.logger.error("Failed to load enchantment configuration from ${json.absolutePath}", e)
        }
        sanitizeData()
        if (ConfigHandler.debugMode) TinkeringWorkshop.logger.info(dataSanitized.toString())
    }

    override fun sanitizeData() {
        dataSanitized.addAll(
            dataRaw.map {
                val enchResLoc = ResourceLocation(it.enchantment ?: "")
                val enchantment = Enchantment.REGISTRY.getObject(enchResLoc)
                enchantment?.let { ench ->
                    val blocks = it.blocks?.mapNotNull { block -> ConfigParser.ConfigBlockState(block).state ?: TinkeringWorkshop.logger.error("Block not found: $block").let { null } } ?: emptyList()
                    val blockLogic = when (it.blockLogic?.lowercase(Locale.ROOT)) {
                        "all" -> BlockCheckLogic.ALL
                        "single" -> BlockCheckLogic.SINGLE
                        else -> BlockCheckLogic.ANY
                    }
                    val color = it.color?.let { color ->
                        if (color.startsWith("#")) {
                            color.substring(1).toIntOrNull(16)
                        } else {
                            color.toIntOrNull()
                        } ?: TinkeringWorkshop.logger.error("Invalid color format for enchantment: ${it.enchantment}").let { 0xFFFFFF }
                    } ?: 0xFFFFFF
                    val levelCost = it.mapLevelCost?.map { (level, cost) -> level.coerceAtLeast(ench.minLevel).coerceAtMost(ench.maxLevel) to cost.coerceAtLeast(1) }?.toMap()
                        ?: TinkeringWorkshop.logger.error("Found an error parsing the level costs for: ${it.enchantment}").let { emptyMap<EnchantmentLevel, ExperienceLevel>() }
                    val bookshelfPower = it.mapBookshelfPower?.map { (level, power) -> level.coerceAtLeast(ench.minLevel).coerceAtMost(ench.maxLevel) to power.toDouble().coerceAtLeast(0.0) }?.toMap()
                        ?: TinkeringWorkshop.logger.error("Found an error parsing the bookshelf power for: ${it.enchantment}").let { emptyMap<EnchantmentLevel, BookshelfPower>() }

                    return@map TWEnchantment(
                        enchantment = enchantment,
                        blocks = blocks,
                        blockLogic = blockLogic,
                        costMultiplier = it.costMultiplier?.coerceAtLeast(0.0) ?: 1.0,
                        sound = it.sound?.let { ResourceLocation(it) } ?: ResourceLocation(ConfigHandler.defaultCraftingSound),
                        color = color,
                        mapLevelCost = levelCost,
                        mapBookshelfPower = bookshelfPower,
                    )
                } ?: TinkeringWorkshop.logger.warn("Enchantment not found: ${it.enchantment}").let { null }
            }.filterNotNull(),
        )
    }

    override fun generateDefaultConfig(file: File) {
        val cfg = mutableListOf<TWRawEnchantment>()
        Enchantment.REGISTRY.forEach { ench ->
            val rarity = 10 - ench.rarity.weight
            val color = EnumDyeColor.entries.first { c -> c.chatColor == EnumRarity.entries[ench.rarity.ordinal].color }.colorValue
            cfg.add(
                TWRawEnchantment(
                    _comment = "This configuration is auto-generated. Modify as needed.",
                    enchantment = ench.registryName.toString(),
                    blocks = null,
                    blockLogic = null,
                    costMultiplier = null,
                    sound = null,
                    color = color.toString(),
                    mapLevelCost = (ench.minLevel..ench.maxLevel).associate { it to (it * rarity).coerceAtLeast(1) },
                    mapBookshelfPower = (ench.minLevel..ench.maxLevel).associate { it to (it * rarity * ench.rarity.ordinal).toDouble() },
                ),
            )
        }
        try {
            file.parentFile?.mkdirs()
            file.writeText(gson.toJson(cfg))
            TinkeringWorkshop.logger.info("Generated default enchantment configuration at ${file.absolutePath}")
        } catch (e: Exception) {
            TinkeringWorkshop.logger.error("Failed to generate default enchantment configuration at ${file.absolutePath}", e)
        }
    }

    override operator fun get(name: Any): TWEnchantment? = dataSanitized.find { it.enchantment.registryName == name }
}

fun String.asEnchantment(): TWEnchantment? = ResourceLocation(this).asEnchantment()

fun ResourceLocation.asEnchantment(): TWEnchantment? = EnchantmentParser.get(this)
