package org.ender_development.tinkeringworkshop.blocks

import net.minecraft.block.state.IBlockState
import net.minecraft.entity.Entity
import net.minecraft.util.EnumBlockRenderType
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import org.ender_development.catalyx.blocks.BaseRotatableTileBlock
import org.ender_development.tinkeringworkshop.TinkeringWorkshop
import org.ender_development.tinkeringworkshop.client.container.ContainerTinkeringWorkshop
import org.ender_development.tinkeringworkshop.client.gui.GuiTinkeringWorkshop
import org.ender_development.tinkeringworkshop.tiles.TileTinkeringWorkshop

class BlockTinkeringWorkshop :
    BaseRotatableTileBlock(
        TinkeringWorkshop,
        "tinkering_workshop",
        TinkeringWorkshop.guiHandler.registerId(
            TileTinkeringWorkshop::class.java,
            ContainerTinkeringWorkshop::class.java,
        ) { GuiTinkeringWorkshop::class.java },
    ) {
    val aabb = AxisAlignedBB(-1.0, .0, -1.0, 2.0, 1.0, 2.0)

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
