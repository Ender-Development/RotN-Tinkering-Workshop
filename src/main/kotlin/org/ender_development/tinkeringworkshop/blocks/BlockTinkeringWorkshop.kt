package org.ender_development.tinkeringworkshop.blocks

import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.Entity
import net.minecraft.util.BlockRenderLayer
import net.minecraft.util.EnumBlockRenderType
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import org.ender_development.catalyx.blocks.multiblock.CenterBlock
import org.ender_development.tinkeringworkshop.TinkeringWorkshop
import org.ender_development.tinkeringworkshop.client.container.ContainerTinkeringWorkshop
import org.ender_development.tinkeringworkshop.client.gui.GuiTinkeringWorkshop
import org.ender_development.tinkeringworkshop.tiles.TileTinkeringWorkshop

class BlockTinkeringWorkshop :
    CenterBlock<TileTinkeringWorkshop>(
        TinkeringWorkshop,
        "tinkering_workshop",
        TileTinkeringWorkshop::class.java,
        TinkeringWorkshop.guiHandler.registerId(TileTinkeringWorkshop::class.java, ContainerTinkeringWorkshop::class.java) { GuiTinkeringWorkshop::class.java },
        ModBlocks.cornerBlock,
        ModBlocks.sideBlock,
    ) {
    @Deprecated("Implementation is fine.")
    override fun getMaterial(state: IBlockState): Material = Material.WOOD

    override fun getSoundType(state: IBlockState, world: World, pos: BlockPos, entity: Entity?): SoundType = SoundType.WOOD

    override fun isFlammable(world: net.minecraft.world.IBlockAccess, pos: net.minecraft.util.math.BlockPos, face: net.minecraft.util.EnumFacing): Boolean = false

    @Deprecated("")
    override fun getRenderType(state: IBlockState): EnumBlockRenderType = EnumBlockRenderType.MODEL

    @SideOnly(Side.CLIENT)
    override fun getRenderLayer(): BlockRenderLayer = BlockRenderLayer.CUTOUT

    @Deprecated("")
    override fun isOpaqueCube(state: IBlockState) = false

    @Deprecated("")
    override fun isFullCube(state: IBlockState) = false
}
