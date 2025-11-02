package org.ender_development.tinkeringworkshop.parser

import com.google.gson.reflect.TypeToken
import net.minecraft.enchantment.Enchantment
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.common.Loader
import org.ender_development.catalyx.config.ConfigParser
import org.ender_development.catalyx.utils.extensions.colorValue
import org.ender_development.catalyx.utils.extensions.enumRarity
import org.ender_development.catalyx.utils.extensions.validate
import org.ender_development.catalyx.utils.parser.AbstractJsonParser
import org.ender_development.catalyx.utils.validation.CommonValidators
import org.ender_development.catalyx.utils.validation.ValidationResult
import org.ender_development.tinkeringworkshop.Reference
import org.ender_development.tinkeringworkshop.config.ConfigHandler
import org.ender_development.tinkeringworkshop.config.TWEnchantment
import org.ender_development.tinkeringworkshop.config.TWRawEnchantment
import java.awt.Color
import java.io.File

class TWEnchantmentParser : AbstractJsonParser<TWRawEnchantment, TWEnchantment>() {
    override val defaultRawData: List<TWRawEnchantment> = Enchantment.REGISTRY.map { enchantment ->
        val rarity = (10 - enchantment.rarity.weight).coerceAtLeast(1)
        val color = enchantment.rarity.enumRarity.colorValue
        TWRawEnchantment(
            _comment = "This configuration is auto-generated. Modify as needed.",
            enchantment = enchantment.registryName.toString(),
            blocks = null,
            blockLogic = null,
            costMultiplier = null,
            sound = null,
            color = color.toString(),
            mapLevelCost = (enchantment.minLevel..enchantment.maxLevel).associateWith { it * rarity },
            mapBookshelfPower = (enchantment.minLevel..enchantment.maxLevel).associateWith { it * rarity * (enchantment.rarity.ordinal + 1.0) },
        )
    }.toList()

    override val rawTypeToken: TypeToken<List<TWRawEnchantment>> = object : TypeToken<List<TWRawEnchantment>>() {}
    override val filePath: String = File(Loader.instance().configDir, "/${Reference.MODID}/enchantment.json").path

    override fun sanitize(rawData: TWRawEnchantment): ValidationResult<TWEnchantment> = validate {
        val enchantment = field(rawData.enchantment, "enchantment", CommonValidators.notEmpty(), { Enchantment.REGISTRY.getObject(ResourceLocation(it!!)) != null }).get()
        val blocks = rawData.blocks ?: emptyList()
        val blockLogic = rawData.blockLogic ?: "any"
        val costMultiplier = rawData.costMultiplier?.coerceAtLeast(.0) ?: 1.0
        val sound = rawData.sound ?: ConfigHandler.defaultCraftingSound
        val color = rawData.color ?: "#FFFFFF"
        val mapLevelCost = rawData.mapLevelCost ?: emptyMap()
        val mapBookshelfPower = rawData.mapBookshelfPower ?: emptyMap()

        val ench = Enchantment.REGISTRY.getObject(ResourceLocation(enchantment!!))!!
        rule(
            blocks.isEmpty() || CommonValidators.listAll(CommonValidators.isBlockState()).validate(blocks),
            "<blocks> must be a list of valid block states.",
        )

        rule(
            mapLevelCost.isEmpty() || CommonValidators.mapAll(CommonValidators.range(ench.minLevel, ench.maxLevel), CommonValidators.positive()).validate(mapLevelCost),
            "<mapLevelCost> must have keys in the range of ${ench.minLevel} to ${ench.maxLevel} with values of at least 1.",
        )

        rule(
            mapBookshelfPower.isEmpty() || CommonValidators.mapAll(CommonValidators.range(ench.minLevel, ench.maxLevel), CommonValidators.atLeast(0.0)).validate(mapBookshelfPower),
            "<mapBookshelfPower> must have keys in the range of ${ench.minLevel} to ${ench.maxLevel} with values of at least 0.0.",
        )

        rule(
            blockLogic in BlockCheckLogic.identifiers,
            "<blockLogic> must be one of the following: ${BlockCheckLogic.identifiers.joinToString(", ")}",
        )

        if (!hasErrors()) {
            TWEnchantment(
                enchantment = ench,
                blocks = blocks.mapNotNull { ConfigParser.ConfigBlockState(it).state },
                blockLogic = BlockCheckLogic.get(blockLogic),
                costMultiplier = costMultiplier,
                sound = ResourceLocation(sound),
                color = Color(if (color.startsWith("#")) Integer.parseInt(color.substring(1, (color.length - 2).coerceAtMost(6)), 16) else color.toInt()).rgb,
                mapLevelCost = mapLevelCost,
                mapBookshelfPower = mapBookshelfPower,
            )
        } else {
            null
        }
    }
}

enum class BlockCheckLogic(val identifier: String) {
    ANY("any"),
    ALL("all"),
    SINGLE("single"),
    ;

    override fun toString(): String = identifier

    companion object {
        val identifiers = entries.map { it.identifier }

        fun get(identifier: String): BlockCheckLogic = entries.first { it.identifier == identifier }
    }
}
