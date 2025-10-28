package org.ender_development.tinkeringworkshop.tiles

import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemBook
import net.minecraft.item.ItemEnchantedBook
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.ITickable
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import org.ender_development.catalyx.blocks.multiblock.IMultiblockCenter
import org.ender_development.catalyx.blocks.multiblock.IMultiblockEdge
import org.ender_development.catalyx.client.AreaHighlighter
import org.ender_development.catalyx.tiles.CenterTile
import org.ender_development.catalyx.tiles.helper.IGuiTile
import org.ender_development.catalyx.tiles.helper.TileStackHandler
import org.ender_development.catalyx.utils.Delegates
import org.ender_development.catalyx.utils.extensions.getAllInBox
import org.ender_development.catalyx.utils.extensions.getHorizontalSurroundings
import org.ender_development.catalyx.utils.math.BlockPosUtils
import org.ender_development.tinkeringworkshop.TinkeringWorkshop
import org.ender_development.tinkeringworkshop.config.ConfigHandler
import org.ender_development.tinkeringworkshop.parser.isTWItem
import org.ender_development.tinkeringworkshop.parser.toBookshelf

typealias Limit = Int
typealias Current = Int

data class BlockCount(val limit: Limit, var current: Current)

class TileTinkeringWorkshop :
    CenterTile(TinkeringWorkshop),
    IGuiTile,
    ITickable {

    override val enableItemCapability = false

    val blockLimits = mutableMapOf<IBlockState, BlockCount>()
    val blockPositions: Array<BlockPos> by Delegates.lazyProperty {
        // 1088
        val height = ConfigHandler.maxHeight - 1 // exclude the block the tile is in
        val radius = (ConfigHandler.maxDiameter - 1) shr 1
        val list = mutableListOf<BlockPos>()
        BlockPosUtils.hollowCuboid(pos, radius, height, 0).forEach { list.addAll(it.getAllInBox()) }
        return@lazyProperty list.toTypedArray()
    }

    val highlighterBookshelf = AreaHighlighter()
    val highlighterWorkshop = listOf(AreaHighlighter(), AreaHighlighter(), AreaHighlighter(), AreaHighlighter(), AreaHighlighter(), AreaHighlighter(), AreaHighlighter(), AreaHighlighter())

    init {
        initInventoryCapability(2, 0)
    }

    override fun initInventoryInputCapability() {
        input = object : TileStackHandler(inputSlots, this) {
            override fun isItemValid(slot: Int, stack: ItemStack): Boolean = when (slot) {
                0 -> stack.item !is ItemEnchantedBook && stack.item !is ItemBook && stack.item.isEnchantable(stack) && !ConfigHandler.configuredItemsOnly || stack.isTWItem()
                1 -> stack.item is ItemEnchantedBook
                else -> error("wtf is slot $slot out of $inputSlots")
            }
        }
    }

    override val guiWidth = 176
    override val guiHeight = 199

    var enchantingPower = 0.0

    fun updateEnchantingPower() {
        if (ConfigHandler.debugMode) {
            highlighterBookshelf.highlightBlocks(blockPositions, 0.5F, 0.5F, 1.0F, 500)
            this.pos.getHorizontalSurroundings().forEachIndexed { idx, it ->
                val state = world.getBlockState(it)
                val block = state.block as IMultiblockEdge
                val aabb = block.getAABB(state)
                highlighterWorkshop[idx].highlightArea(it.x + aabb.minX, it.y + aabb.minY, it.z + aabb.minZ, it.x + aabb.maxX, it.y + aabb.maxY, it.z + aabb.maxZ, 1.0F, 0.0F, 0.0F, 500)
            }
        }
        val oldEnchantingPower = enchantingPower
        enchantingPower = 0.0
        blockLimits.clear()
        blockPositions.forEach {
            world.getBlockState(it).toBookshelf()?.let { state ->
                val count = blockLimits.getOrPut(state.blockState) { BlockCount(state.maxConsidered, 0) }
                if (count.current < count.limit) {
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

    override fun activate(world: World, pos: BlockPos, state: IBlockState, player: EntityPlayer, hand: EnumHand, side: EnumFacing, hitX: Double, hitY: Double, hitZ: Double): Boolean {
        val controllerPos = (state.block as? IMultiblockEdge)?.getCenter(pos, state) ?: return false
        val controllerBlock = world.getBlockState(controllerPos)
        return if (controllerBlock.block !is IMultiblockCenter || player.isSneaking) {
            false
        } else {
            controllerBlock.block.onBlockActivated(world, controllerPos, controllerBlock, player, hand, side, hitX.toFloat(), hitY.toFloat(), hitZ.toFloat())
        }
    }
}
