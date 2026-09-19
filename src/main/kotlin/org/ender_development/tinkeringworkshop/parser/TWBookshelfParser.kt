package org.ender_development.tinkeringworkshop.parser

import com.google.gson.reflect.TypeToken
import net.minecraftforge.fml.common.Loader
import org.ender_development.catalyx.api.v1.common.extensions.toBlockState
import org.ender_development.catalyx.api.v1.common.extensions.validate
import org.ender_development.catalyx.api.v1.validation.CommonValidators
import org.ender_development.catalyx.core.utils.parser.AbstractJsonParser
import org.ender_development.catalyx.core.validation.ValidationResult
import org.ender_development.tinkeringworkshop.Reference
import org.ender_development.tinkeringworkshop.config.TWBookshelf
import org.ender_development.tinkeringworkshop.config.TWRawBookshelf
import java.io.File

class TWBookshelfParser : AbstractJsonParser<TWRawBookshelf, TWBookshelf>() {
    override val defaultRawData: List<TWRawBookshelf> = listOf(
        TWRawBookshelf(
            _comment = "Example bookshelf configuration",
            block = "minecraft:bookshelf",
            power = 1.0,
            simultaneousEnchantment = 1,
            maxConsidered = 10,
        ),
    )

    override val rawTypeToken: TypeToken<List<TWRawBookshelf>> = object : TypeToken<List<TWRawBookshelf>>() {}
    override val input: String = File(Loader.instance().configDir, "/${Reference.MODID}/bookshelf.json").path

    override fun sanitize(rawData: TWRawBookshelf): ValidationResult<TWBookshelf> = validate {
        val blockState = field(rawData.block, "block", CommonValidators.isBlockState()).get()
        val power = field(rawData.power, "power", CommonValidators.positive()).get()?.toDouble()
        val simultaneousEnchantment = rawData.simultaneousEnchantment ?: 1
        val maxConsidered = rawData.maxConsidered ?: Int.MAX_VALUE

        if (!hasErrors()) {
            TWBookshelf(
                blockState = blockState!!.toBlockState()!!,
                power = power!!,
                simultaneousEnchantment = simultaneousEnchantment,
                maxConsidered = maxConsidered,
            )
        } else {
            null
        }
    }
}
