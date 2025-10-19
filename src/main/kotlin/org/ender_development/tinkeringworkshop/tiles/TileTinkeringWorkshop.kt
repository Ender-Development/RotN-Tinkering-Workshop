package org.ender_development.tinkeringworkshop.tiles

import net.minecraft.block.state.IBlockState
import net.minecraft.init.Blocks
import net.minecraft.item.ItemEnchantedBook
import net.minecraft.item.ItemStack
import net.minecraft.util.ITickable
import net.minecraft.util.math.BlockPos
import org.ender_development.catalyx.tiles.BaseTile
import org.ender_development.catalyx.tiles.helper.IGuiTile
import org.ender_development.catalyx.tiles.helper.TileStackHandler
import org.ender_development.catalyx.utils.Delegates
import org.ender_development.catalyx.utils.extensions.getAllInBox
import org.ender_development.catalyx.utils.math.BlockPosUtils
import org.ender_development.tinkeringworkshop.TinkeringWorkshop
import org.ender_development.tinkeringworkshop.config.BookshelfParser
import org.ender_development.tinkeringworkshop.config.ConfigHandler

typealias Limit = Int
typealias Current = Int

data class BlockCount(val limit: Limit, var current: Current)

class TileTinkeringWorkshop :
    BaseTile(TinkeringWorkshop),
    IGuiTile,
    ITickable {

    override val enableItemCapability = false

    val blockLimits = mutableMapOf<IBlockState, BlockCount>()
    val blockPositions: Array<BlockPos> by Delegates.lazyProperty { // 1088
        val height = ConfigHandler.maxHeight - 1 // exclude the block the tile is in
        val radius = (ConfigHandler.maxDiameter - 1) shr 1
        val list = mutableListOf<BlockPos>()
        BlockPosUtils.hollowCuboid(pos, radius, height, 0).forEach { list.addAll(it.getAllInBox()) }
        return@lazyProperty list.toTypedArray()
    }

    init {
        initInventoryCapability(2, 0)
    }

    override fun initInventoryInputCapability() {
        input = object : TileStackHandler(inputSlots, this) {
            override fun isItemValid(slot: Int, stack: ItemStack): Boolean = when (slot) {
                0 -> stack.item !is ItemEnchantedBook && stack.item.isEnchantable(stack)
                1 -> stack.item is ItemEnchantedBook
                else -> error("wtf is slot $slot out of $inputSlots")
            }
        }
    }

    override val guiWidth = 176
    override val guiHeight = 222

    var enchantingPower = 0.0

    fun updateEnchantingPower() {
        if (ConfigHandler.debugMode) {
            blockPositions.forEach {
                @Suppress("DEPRECATION")
                if (world.isAirBlock(it))
                    world.setBlockState(it, Blocks.CONCRETE.getStateFromMeta(14))
            }
        }
        val oldEnchantingPower = enchantingPower
        enchantingPower = 0.0
        blockLimits.clear()
        blockPositions.forEach {
            BookshelfParser[world.getBlockState(it)]?.let { state ->
                val count = blockLimits.getOrPut(state.blockState) { BlockCount(0, 0) }
                if(count.current < count.limit) {
                    enchantingPower += state.power
                    ++count.current
                }
            }
        }
        if (ConfigHandler.debugMode && oldEnchantingPower != enchantingPower) {
            TinkeringWorkshop.logger.info("Enchanting power updated: $oldEnchantingPower -> $enchantingPower")
        }
    }

    var timer = 2

    override fun update() {
        if (world.isRemote) {
            return
        }

        if (timer-- == 0) {
            updateEnchantingPower()
            timer = 30
        }
    }
}
