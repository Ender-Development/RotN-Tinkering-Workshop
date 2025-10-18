package org.ender_development.tinkeringworkshop.config

/**
 * Representation of an enchantment configuration for the Tinkering Workshop.
 * Intended to be deserialized from JSON.
 */
data class Enchantment(
    /**
     * The registry name of the [net.minecraft.enchantment.Enchantment]
     * Format: "modid:enchantmentname"
     */
    val enchantment: String,
    /**
     * The list of blocks that has to be present around the Workshop to enable this enchantment.
     * How the blocks are handled depends on the [blockLogic].
     * Format: "modid:blockname[:metadata]"
     */
    val blocks: List<String>,
    /**
     * The logic to use when checking for the presence of the required blocks.
     * Possible values:
     * - "any": At least one of the specified blocks must be present.
     * - "all": All specified blocks must be present.
     * - if empty or miss formated, defaults to "any".
     */
    val blockLogic: String,
    /**
     * The sound that plays when this enchantment is successfully applied.
     * Format: "namespace:sound_event"
     */
    val sound: String,
    /**
     * A map of enchantment levels to their corresponding costs in experience levels.
     * For example, a mapping of 1 to 5 means that level 1 of the enchantment costs 5 experience levels.
     */
    val levelCost: Map<Int, Int>,
    /**
     * A map of enchantment levels to the total bookshelf power required to apply that level.
     * For example, a mapping of 2 to 15 means that to apply level 2 of the enchantment,
     * the Workshop must have at least 15 total bookshelf power from the surrounding [Bookshelf]`s`.
     */
    val bookshelfPower: Map<Int, Int>,
)

/**
 * Representation of a bookshelf block configuration for the Tinkering Workshop.
 * Intended to be deserialized from JSON.
 */
data class Bookshelf(
    /**
     * The [net.minecraft.block.state.IBlockState] of the bookshelf
     * Format: "modid:blockname[:metadata]"
     */
    val block: String,
    /**
     * The power provided by this block similar to vanilla bookshelves,
     * but with our own implementation.
     * @see net.minecraft.block.BlockBookshelf.getEnchantPowerBonus
     */
    val power: Int,
    /**
     * The amount of enchantments this bookshelf allows to be applied simultaneously.
     * It is enough for one of these to be present around the Workshop to enable this effect.
     */
    val simultaneousEnchantment: Int,
    /**
     * The maximum number of bookshelves of this type to consider when calculating
     * total enchantment power.
     */
    val maxConsidered: Int,
)
