package org.ender_development.tinkeringworkshop.blocks

import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.Entity
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import org.ender_development.catalyx.blocks.multiblock.parts.InvisibleCorner
import org.ender_development.catalyx.blocks.multiblock.parts.InvisibleSide
import org.ender_development.catalyx.utils.extensions.rotateY
import org.ender_development.tinkeringworkshop.TinkeringWorkshop

open class TWBlockCorner : InvisibleCorner(TinkeringWorkshop, "corner") {
    @Deprecated("Implementation is fine.")
    override fun getMaterial(state: IBlockState): Material = Material.WOOD

    override fun getSoundType(state: IBlockState, world: World, pos: BlockPos, entity: Entity?): SoundType = SoundType.WOOD

    override fun getAABB(state: IBlockState): AxisAlignedBB = AxisAlignedBB(3 * PIXEL_RATIO, .0, .0, 1.0, 1.0, 13 * PIXEL_RATIO).rotateY(normalizeRotation(state))

    override fun isFlammable(world: IBlockAccess, pos: BlockPos, face: EnumFacing): Boolean = false
}

class TWBlockSide : InvisibleSide(TinkeringWorkshop, "side") {
    @Deprecated("Implementation is fine.")
    override fun getMaterial(state: IBlockState): Material = Material.WOOD

    override fun getSoundType(state: IBlockState, world: World, pos: BlockPos, entity: Entity?): SoundType = SoundType.WOOD

    override fun getAABB(state: IBlockState): AxisAlignedBB = AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 13 * PIXEL_RATIO).rotateY(normalizeRotation(state))

    override fun isFlammable(world: IBlockAccess, pos: BlockPos, face: EnumFacing): Boolean = false
}
