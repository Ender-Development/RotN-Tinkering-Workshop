package org.ender_development.tinkeringworkshop.blocks

import net.minecraft.block.state.IBlockState
import net.minecraft.entity.Entity
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import org.ender_development.catalyx.blocks.multiblock.InvisibleEdge
import org.ender_development.tinkeringworkshop.TinkeringWorkshop

class BlockEdge : InvisibleEdge(TinkeringWorkshop, "tinkering_workshop_edge") {
    private fun getAABB(state: IBlockState): AxisAlignedBB {
        val pixelRatio = 1.0 / 16.0
        val (facing, type) = unshiftMeta(getMetaFromState(state))
        // TODO: the corner values are all wrong, proof of concept only
        return when (type) {
            Type.CORNER -> when (facing) {
                BinaryFacing.NORTH -> AxisAlignedBB(0.0, 0.0, 0.0, 0.5, 1.0, 0.5)
                BinaryFacing.EAST -> AxisAlignedBB(0.5, 0.0, 0.0, 1.0, 1.0, 0.5)
                BinaryFacing.SOUTH -> AxisAlignedBB(0.5, 0.0, 0.5, 1.0, 1.0, 1.0)
                BinaryFacing.WEST -> AxisAlignedBB(0.0, 0.0, 0.5, 0.5, 1.0, 1.0)
            }
            else -> when (facing) {
                BinaryFacing.NORTH -> AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 13 * pixelRatio)
                BinaryFacing.EAST -> AxisAlignedBB(13 * pixelRatio, 0.0, 0.0, 1.0, 1.0, 1.0)
                BinaryFacing.SOUTH -> AxisAlignedBB(0.0, 0.0, 13 * pixelRatio, 1.0, 1.0, 1.0)
                BinaryFacing.WEST -> AxisAlignedBB(0.0, 0.0, 0.0, 13 * pixelRatio, 1.0, 1.0)
            }
        }
    }

    @Deprecated("Implementation is fine")
    override fun getBoundingBox(state: IBlockState, source: IBlockAccess, pos: BlockPos): AxisAlignedBB = getAABB(state)

    @Deprecated("Implementation is fine")
    override fun getCollisionBoundingBox(blockState: IBlockState, worldIn: IBlockAccess, pos: BlockPos): AxisAlignedBB? = getAABB(blockState)

    override fun onEntityCollision(worldIn: World, pos: BlockPos, state: IBlockState, entityIn: Entity) {
        super.onEntityCollision(worldIn, pos, state, entityIn)
    }
}
