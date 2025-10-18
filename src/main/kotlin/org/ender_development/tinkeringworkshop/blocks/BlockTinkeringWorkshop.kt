package org.ender_development.tinkeringworkshop.blocks

import net.minecraft.block.BlockHorizontal
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.util.EnumBlockRenderType
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import org.ender_development.catalyx.blocks.BaseTileBlock
import org.ender_development.tinkeringworkshop.TinkeringWorkshop
import org.ender_development.tinkeringworkshop.client.container.ContainerTinkeringWorkshop
import org.ender_development.tinkeringworkshop.client.gui.GuiTinkeringWorkshop
import org.ender_development.tinkeringworkshop.tiles.TileTinkeringWorkshop

class BlockTinkeringWorkshop :
    BaseTileBlock(
        TinkeringWorkshop,
        "tinkering_workshop",
        TinkeringWorkshop.guiHandler.registerId(
            TileTinkeringWorkshop::class.java,
            ContainerTinkeringWorkshop::class.java,
        ) { GuiTinkeringWorkshop::class.java },
    ) {
    val aabb = AxisAlignedBB(-1.0, .0, -1.0, 2.0, 1.0, 2.0)

    init {
        defaultState = blockState.baseState.withProperty(BlockHorizontal.FACING, EnumFacing.NORTH)
    }

    override fun createBlockState() = BlockStateContainer(this, BlockHorizontal.FACING)

    override fun getStateForPlacement(world: World, pos: BlockPos, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float, meta: Int, placer: EntityLivingBase, hand: EnumHand): IBlockState =
        super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand).withProperty(BlockHorizontal.FACING, placer.horizontalFacing.opposite)

    @Deprecated("")
    override fun getStateFromMeta(meta: Int): IBlockState = defaultState.withProperty(BlockHorizontal.FACING, EnumFacing.HORIZONTALS[meta])

    override fun getMetaFromState(state: IBlockState): Int = EnumFacing.HORIZONTALS.indexOf(state.getValue(BlockHorizontal.FACING))

    @Deprecated("")
    override fun getRenderType(state: IBlockState): EnumBlockRenderType = EnumBlockRenderType.MODEL

    @Deprecated("")
    override fun isOpaqueCube(state: IBlockState) = false

    @Deprecated("")
    override fun isFullCube(state: IBlockState) = false

    @Deprecated("")
    override fun getBoundingBox(state: IBlockState, source: IBlockAccess, pos: BlockPos) = aabb

    @Deprecated("")
    override fun addCollisionBoxToList(state: IBlockState, worldIn: World, pos: BlockPos, entityBox: AxisAlignedBB, collidingBoxes: List<AxisAlignedBB>, entityIn: Entity?, mysteryboolean: Boolean) {
        @Suppress("DEPRECATION")
        addCollisionBoxToList(pos, entityBox, collidingBoxes, aabb)
    }
}
