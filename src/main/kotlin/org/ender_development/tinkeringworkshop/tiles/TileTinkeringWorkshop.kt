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
import org.ender_development.catalyx.utils.extensions.getAllInBox
import org.ender_development.catalyx.utils.math.BlockPosUtils
import org.ender_development.tinkeringworkshop.TinkeringWorkshop
import org.ender_development.tinkeringworkshop.config.BookshelfParser
import org.ender_development.tinkeringworkshop.config.ConfigHandler

typealias Limit = Int
typealias Current = Int

class TileTinkeringWorkshop :
    BaseTile(TinkeringWorkshop),
    IGuiTile,
    ITickable {

    override val enableItemCapability = false

    val blockLimits = mutableMapOf<IBlockState, Pair<Limit, Current>>()

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
        val height = ConfigHandler.maxHeight - 1 // exclude the block the tile is in
        val radius = (ConfigHandler.maxDiameter - 1) shr 1

        val cuboid = BlockPosUtils.hollowCuboid(pos, radius, height, 0).map(Pair<BlockPos, BlockPos>::getAllInBox)

        if (ConfigHandler.debugMode) {
            val (wallPX, wallNX, wallPZ, wallNZ) = cuboid
            when (timesRan) {
                1 -> wallPX.forEach { if (world.isAirBlock(it)) world.setBlockState(it, Blocks.GOLD_BLOCK.defaultState) }
                2 -> wallNX.forEach { if (world.isAirBlock(it)) world.setBlockState(it, Blocks.DIAMOND_BLOCK.defaultState) }
                3 -> wallPZ.forEach { if (world.isAirBlock(it)) world.setBlockState(it, Blocks.EMERALD_BLOCK.defaultState) }
                else -> wallNZ.forEach { if (world.isAirBlock(it)) world.setBlockState(it, Blocks.IRON_BLOCK.defaultState) }
            }
        }
        val oldEnchantingPower = enchantingPower
        enchantingPower = 0.0
        blockLimits.clear()
        cuboid.forEach { wall ->
            wall.forEach {
                BookshelfParser[world.getBlockState(it)]?.let { bs ->
                    blockLimits[bs.blockState]?.let { limitPair ->
                        if (limitPair.second < limitPair.first) {
                            enchantingPower += bs.power
                            blockLimits[bs.blockState] = Pair(limitPair.first, limitPair.second + 1)
                        }
                    } ?: run {
                        enchantingPower += bs.power
                        blockLimits[bs.blockState] = Pair(bs.maxConsidered, 1)
                    }
                }
            }
        }
        if (ConfigHandler.debugMode && oldEnchantingPower != enchantingPower) {
            TinkeringWorkshop.logger.info("Enchanting power updated: $oldEnchantingPower -> $enchantingPower")
        }
    }

    var timer = 2
    var timesRan = 0

    override fun update() {
        if (world.isRemote) {
            return
        }

        if (timer-- == 0) {
            if (ConfigHandler.debugMode && ++timesRan == 5) {
                timesRan = 1
            }
            updateEnchantingPower()
            timer = 20
        }
    }
}
