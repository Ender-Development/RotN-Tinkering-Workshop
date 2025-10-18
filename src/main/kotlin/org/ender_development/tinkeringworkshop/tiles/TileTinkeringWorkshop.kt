package org.ender_development.tinkeringworkshop.tiles

import net.minecraft.init.Blocks
import net.minecraft.util.ITickable
import org.ender_development.catalyx.client.AreaHighlighter
import org.ender_development.catalyx.tiles.BaseTile
import org.ender_development.catalyx.tiles.helper.IGuiTile
import org.ender_development.catalyx.utils.math.BlockPositions
import org.ender_development.catalyx.utils.math.Vec3
import org.ender_development.tinkeringworkshop.TinkeringWorkshop
import org.ender_development.tinkeringworkshop.config.ConfigHandler

class TileTinkeringWorkshop :
    BaseTile(TinkeringWorkshop),
    IGuiTile,
    ITickable {

    val ahNZ = AreaHighlighter()
    val ahPZ = AreaHighlighter()
    val ahNX = AreaHighlighter()
    val ahPX = AreaHighlighter()

    fun getEnchantingPower() {
        val height = ConfigHandler.maxHeight - 1 // exclude the block the tile is in
        val radius = (ConfigHandler.maxDiameter - 1) shr 1

        val cuboid = BlockPositions.hollowCuboid(Vec3(pos.x, pos.y, pos.z), radius, height, 0)
        val (wallPX, wallNX, wallPZ, wallNZ) = cuboid.map { BlockPositions.getAllInBox(it) }

        // TODO ender: I have no idea if this highlighting works :/
//        ahNZ.highlightArea(cuboid[0].first.toBlockPos(), cuboid[0].second.toBlockPos(), 0f, 1f, 0f, 2000)
//        ahPZ.highlightArea(cuboid[1].first.toBlockPos(), cuboid[1].second.toBlockPos(), 0f, 1f, 0f, 2000)
//        ahNX.highlightArea(cuboid[2].first.toBlockPos(), cuboid[2].second.toBlockPos(), 0f, 1f, 0f, 2000)
//        ahPX.highlightArea(cuboid[3].first.toBlockPos(), cuboid[3].second.toBlockPos(), 0f, 1f, 0f, 2000)

        wallPX.forEach { if (world.getBlockState(it).block == Blocks.AIR) world.setBlockState(it, Blocks.GOLD_BLOCK.defaultState) }
        wallNX.forEach { if (world.getBlockState(it).block == Blocks.AIR) world.setBlockState(it, Blocks.DIAMOND_BLOCK.defaultState) }
        wallPZ.forEach { if (world.getBlockState(it).block == Blocks.AIR) world.setBlockState(it, Blocks.EMERALD_BLOCK.defaultState) }
        wallNZ.forEach { if (world.getBlockState(it).block == Blocks.AIR) world.setBlockState(it, Blocks.IRON_BLOCK.defaultState) }
    }

    var timer = 20

    override fun update() {
        if (timer-- == 0) {
            getEnchantingPower()
            timer = 20
        }
    }
}
