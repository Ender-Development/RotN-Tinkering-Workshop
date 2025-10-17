package org.ender_development.tinkeringworkshop.tiles

import net.minecraft.util.ITickable
import net.minecraft.util.math.BlockPos
import org.ender_development.catalyx.client.AreaHighlighter
import org.ender_development.catalyx.tiles.BaseTile
import org.ender_development.catalyx.tiles.helper.IGuiTile
import org.ender_development.tinkeringworkshop.TinkeringWorkshop
import org.ender_development.tinkeringworkshop.config.ConfigHandler

class TileTinkeringWorkshop :
    BaseTile(TinkeringWorkshop),
    IGuiTile, ITickable {

    val ahNZ = AreaHighlighter()
    val ahPZ = AreaHighlighter()
    val ahNX = AreaHighlighter()
    val ahPX = AreaHighlighter()

    fun getEnchantingPower() {
        val diameter = ConfigHandler.maxDiameter
        val height = ConfigHandler.maxHeight
        val radius = (diameter - 1) shr 1
        val x = pos.x
        val y = pos.y
        val z = pos.z
        val topY = y + height - 1

        /*

         */

        // todo roz: fix this mess because my brain is asleep
        val wallPX = BlockPos.getAllInBox(x + radius, y, z - radius + 1, x + radius - 1, topY, z + radius)
        val wallNX = BlockPos.getAllInBox(x - radius, y, z - radius, x - radius + 1, topY, z + radius - 1)
        val wallPZ = BlockPos.getAllInBox(x - radius, y, z + radius, x + radius - 1, topY, z + radius - 1)
        val wallNZ = BlockPos.getAllInBox(x - radius + 1, y, z - radius + 1, x + radius, topY, z - radius)
    }

    var timer = 20

    override fun update() {
        if(timer-- == 0)
            getEnchantingPower()
    }
}
