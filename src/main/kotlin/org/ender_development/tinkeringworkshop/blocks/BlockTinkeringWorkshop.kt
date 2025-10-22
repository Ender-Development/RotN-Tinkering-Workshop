package org.ender_development.tinkeringworkshop.blocks

import net.minecraft.block.state.IBlockState
import net.minecraft.util.EnumBlockRenderType
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import org.ender_development.catalyx.blocks.multiblock.BaseMiddleBlock
import org.ender_development.tinkeringworkshop.TinkeringWorkshop
import org.ender_development.tinkeringworkshop.client.container.ContainerTinkeringWorkshop
import org.ender_development.tinkeringworkshop.client.gui.GuiTinkeringWorkshop
import org.ender_development.tinkeringworkshop.tiles.TileTinkeringWorkshop

class BlockTinkeringWorkshop :
    BaseMiddleBlock<TileTinkeringWorkshop>(
        TinkeringWorkshop,
        "tinkering_workshop",
        TileTinkeringWorkshop::class.java,
        TinkeringWorkshop.guiHandler.registerId(TileTinkeringWorkshop::class.java, ContainerTinkeringWorkshop::class.java) { GuiTinkeringWorkshop::class.java },
        ModBlocks.tinkeringWorkshopEdge,
    ) {
    private val pixelRatio = 1.0 / 16.0
    val aabb = AxisAlignedBB(-13 * pixelRatio, .0, -13 * pixelRatio, 29 * pixelRatio, 1.0, 29 * pixelRatio)

    @Deprecated("")
    override fun getRenderType(state: IBlockState): EnumBlockRenderType = EnumBlockRenderType.MODEL

    @Deprecated("")
    override fun isOpaqueCube(state: IBlockState) = false

    @Deprecated("")
    override fun isFullCube(state: IBlockState) = false

    @Deprecated("")
    override fun getBoundingBox(state: IBlockState, source: IBlockAccess, pos: BlockPos) = aabb

    override fun getLightValue(state: IBlockState, world: IBlockAccess, pos: BlockPos): Int = 1
}
