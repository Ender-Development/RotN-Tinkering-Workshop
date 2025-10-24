package org.ender_development.tinkeringworkshop.blocks

import net.minecraft.block.state.IBlockState
import net.minecraft.util.math.AxisAlignedBB
import org.ender_development.catalyx.blocks.multiblock.parts.InvisibleCorner
import org.ender_development.catalyx.blocks.multiblock.parts.InvisibleSide
import org.ender_development.catalyx.utils.extensions.rotateY
import org.ender_development.tinkeringworkshop.TinkeringWorkshop

class TWBlockCorner : InvisibleCorner(TinkeringWorkshop, "corner") {
    override fun getAABB(state: IBlockState): AxisAlignedBB =
        AxisAlignedBB(3 * PIXEL_RATIO, .0, .0, 1.0, 1.0, 13 * PIXEL_RATIO).rotateY(getMetaFromState(state) % 4)
}

class TWBlockSide : InvisibleSide(TinkeringWorkshop, "side") {
    override fun getAABB(state: IBlockState): AxisAlignedBB =
        AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 13 * PIXEL_RATIO).rotateY(getMetaFromState(state) % 4)
}
