package org.ender_development.tinkeringworkshop.tiles

import net.minecraft.block.BlockBookshelf
import net.minecraft.init.Blocks
import net.minecraft.util.ITickable
import net.minecraft.util.math.BlockPos
import org.ender_development.catalyx.tiles.BaseTile
import org.ender_development.catalyx.tiles.helper.IGuiTile
import org.ender_development.catalyx.utils.extensions.getAllInBox
import org.ender_development.catalyx.utils.math.BlockPosUtils
import org.ender_development.tinkeringworkshop.TinkeringWorkshop
import org.ender_development.tinkeringworkshop.config.ConfigHandler

class TileTinkeringWorkshop :
    BaseTile(TinkeringWorkshop),
    IGuiTile,
    ITickable {

    init {
        initInventoryCapability(1, 0)
    }

    override fun initInventoryInputCapability() {
        // TODO
        super.initInventoryInputCapability()
    }

    var enchantingPower = 0f

    fun updateEnchantingPower() {
        val height = ConfigHandler.maxHeight - 1 // exclude the block the tile is in
        val radius = (ConfigHandler.maxDiameter - 1) shr 1

        val cuboid = BlockPosUtils.hollowCuboid(pos, radius, height, 0).map(Pair<BlockPos, BlockPos>::getAllInBox)

        if (ConfigHandler.debugMode) {
            val (wallPX, wallNX, wallPZ, wallNZ) = cuboid
            when (timesRan) {
                1 -> wallPX.forEach { world.setBlockState(it, Blocks.GOLD_BLOCK.defaultState) }
                2 -> wallNX.forEach { world.setBlockState(it, Blocks.DIAMOND_BLOCK.defaultState) }
                3 -> wallPZ.forEach { world.setBlockState(it, Blocks.EMERALD_BLOCK.defaultState) }
                else -> wallNZ.forEach { world.setBlockState(it, Blocks.IRON_BLOCK.defaultState) }
            }
        }
        enchantingPower = 0f
        cuboid.forEach { wall ->
            wall.forEach {
                val block = world.getBlockState(it).block
                enchantingPower += when (block) {
                    // TODO: read from config here
                    is BlockBookshelf -> block.getEnchantPowerBonus(world, it)
                    else -> 0f
                }
            }
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
