package org.ender_development.tinkeringworkshop.config

import net.minecraft.block.state.IBlockState
import net.minecraft.enchantment.Enchantment
import net.minecraft.util.ResourceLocation

typealias EnchantmentLevel = Int
typealias ExperienceLevel = Int
typealias BookshelfPower = Double

interface ISerializable
interface ISanitized

/**
 * Representation of an enchantment configuration for the Tinkering Workshop.
 * Intended to be deserialized from JSON.
 */
data class TwRawEnchantment(
    internal val _comment: String? = null,
    /**
     * The registry name of the [net.minecraft.enchantment.Enchantment]
     * Format: "modid:enchantmentname"
     */
    internal val enchantment: String?,
    /**
     * The list of blocks that has to be present around the Workshop to enable this enchantment.
     * How the blocks are handled depends on the [blockLogic].
     * Format: "modid:blockname[:metadata]"
     */
    internal val blocks: List<String>?,
    /**
     * The logic to use when checking for the presence of the required blocks.
     * Possible values:
     * - "any": At least one of the specified blocks must be present.
     * - "all": All the specified blocks must be present.
     * - "single": Only one of the specified blocks must be present, and no others.
     * - if empty or invalid, defaults to "any".
     */
    internal val blockLogic: String?,
    /**
     * The sound that plays when this enchantment is successfully applied.
     * Format: "namespace:sound_event"
     */
    internal val sound: String?,
    /**
     * A map of enchantment levels to their corresponding costs in experience levels.
     * For example, a mapping of 1 to 5 means that level 1 of the enchantment costs 5 experience levels.
     */
    internal val mapLevelCost: Map<EnchantmentLevel, ExperienceLevel>?,
    /**
     * A map of enchantment levels to the total bookshelf power required to apply that level.
     * For example, a mapping of 2 to 15 means that to apply level 2 of the enchantment,
     * the Workshop must have at least 15 total bookshelf power from the surrounding [TwRawBookshelf]`s`.
     */
    internal val mapBookshelfPower: Map<EnchantmentLevel, BookshelfPower>?,
) : ISerializable

data class TwEnchantment(
    val enchantment: Enchantment,
    val blocks: List<IBlockState>,
    val blockLogic: BlockCheckLogic,
    val sound: ResourceLocation,
    val mapLevelCost: Map<EnchantmentLevel, ExperienceLevel>,
    val mapBookshelfPower: Map<EnchantmentLevel, BookshelfPower>,
) : ISanitized

/**
 * Representation of a bookshelf block configuration for the Tinkering Workshop.
 * Intended to be deserialized from JSON.
 */
data class TwRawBookshelf(
    internal val _comment: String? = null,
    /**
     * The [net.minecraft.block.state.IBlockState] of the bookshelf
     * Format: "modid:blockname[:metadata]"
     */
    internal val block: String?,
    /**
     * The power provided by this block similar to vanilla bookshelves,
     * but with our own implementation.
     * @see net.minecraft.block.BlockBookshelf.getEnchantPowerBonus
     */
    internal val power: BookshelfPower?,
    /**
     * The amount of enchantments this bookshelf allows to be applied simultaneously.
     * It is enough for one of these to be present around the Workshop to enable this effect.
     */
    internal val simultaneousEnchantment: Int?,
    /**
     * The maximum number of bookshelves of this type to consider when calculating
     * total enchantment power.
     */
    internal val maxConsidered: Int?,
) : ISerializable

data class TwBookshelf(val blockState: IBlockState, val power: BookshelfPower, val simultaneousEnchantment: Int, val maxConsidered: Int) : ISanitized
