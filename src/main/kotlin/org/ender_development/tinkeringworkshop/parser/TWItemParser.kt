package org.ender_development.tinkeringworkshop.parser

import com.google.gson.reflect.TypeToken
import net.minecraft.enchantment.Enchantment
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.common.Loader
import org.ender_development.catalyx.config.ConfigParser
import org.ender_development.catalyx.utils.extensions.validate
import org.ender_development.catalyx.utils.parser.AbstractJsonParser
import org.ender_development.catalyx.utils.validation.CommonValidators
import org.ender_development.catalyx.utils.validation.ValidationResult
import org.ender_development.tinkeringworkshop.Reference
import org.ender_development.tinkeringworkshop.config.ConfigHandler
import org.ender_development.tinkeringworkshop.config.TWItem
import org.ender_development.tinkeringworkshop.config.TWRawItem
import java.io.File

class TWItemParser : AbstractJsonParser<TWRawItem, TWItem>() {
    override val defaultRawData: List<TWRawItem> = listOf(
        TWRawItem(
            _comment = "Example item configuration",
            item = "minecraft:diamond_sword",
            slots = null,
            costMultiplier = null,
            whitelist = null,
            blacklist = null,
        ),
    )

    override val rawTypeToken: TypeToken<List<TWRawItem>> = object : TypeToken<List<TWRawItem>>() {}
    override val filePath: String = File(Loader.instance().configDir, "/${Reference.MODID}/item.json").path

    override fun sanitize(rawData: TWRawItem): ValidationResult<TWItem> = validate {
        val item = field(rawData.item, "item", CommonValidators.isItemStack()).get()
        val slots = rawData.slots?.coerceIn(0, ConfigHandler.maxEnchantmentsPerItem) ?: ConfigHandler.maxEnchantmentsPerItem
        val modifier = rawData.costMultiplier?.coerceAtLeast(0.0) ?: 1.0
        val whitelist = rawData.whitelist?.listEnchantment() ?: emptySet()
        val blacklist = rawData.blacklist?.listEnchantment() ?: emptySet()

        rule(
            whitelist.intersect(blacklist).isEmpty(),
            "<whitelist> and <blacklist> cannot contain the same enchantments.",
        )

        if (!hasErrors()) {
            TWItem(
                item = ConfigParser.ConfigItemStack(item!!).toItemStack(),
                slots = slots,
                costMultiplier = modifier,
                whitelist = whitelist,
                blacklist = blacklist
            )
        } else {
            null
        }
    }

    private fun List<String>.listEnchantment(): Set<Enchantment> = mapNotNull { Enchantment.REGISTRY.getObject(ResourceLocation(it)) }.toSet()
}
