package org.ender_development.tinkeringworkshop.parser

import net.minecraft.block.state.IBlockState
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import org.ender_development.catalyx.config.ConfigParser
import org.ender_development.tinkeringworkshop.TinkeringWorkshop
import org.ender_development.tinkeringworkshop.config.TWBookshelf
import org.ender_development.tinkeringworkshop.config.TWEnchantment
import org.ender_development.tinkeringworkshop.config.TWItem

// Enchantment extensions
fun String.asEnchantment(): TWEnchantment? = TinkeringWorkshop.parserRegistry.getData<TWEnchantment>("enchantment")?.firstOrNull { it.enchantment.registryName.toString() == this }

fun ResourceLocation.asEnchantment(): TWEnchantment? = this.toString().asEnchantment()

// Bookshelf extensions
fun String.toBookshelf(): TWBookshelf? = TinkeringWorkshop.parserRegistry.getData<TWBookshelf>("bookshelf")?.firstOrNull { it.blockState == ConfigParser.ConfigBlockState(this).state }

fun ResourceLocation.toBookshelf(): TWBookshelf? = this.toString().toBookshelf()

fun IBlockState.toBookshelf(): TWBookshelf? = TinkeringWorkshop.parserRegistry.getData<TWBookshelf>("bookshelf")?.firstOrNull { it.blockState == this }

@Suppress("DEPRECATION")
fun ItemStack.toBookshelf(): TWBookshelf? = (this.item as? ItemBlock)?.block?.getStateFromMeta(this.metadata)?.toBookshelf()

// Item extensions
fun ItemStack.isTWItem(): Boolean = TinkeringWorkshop.parserRegistry.getData<TWItem>("item")?.any { it.item.item == this.item && it.item.metadata == this.metadata } == true

fun ItemStack.toTWItem(): TWItem? = TinkeringWorkshop.parserRegistry.getData<TWItem>("item")?.firstOrNull { it.item.item == this.item && it.item.metadata == this.metadata }
