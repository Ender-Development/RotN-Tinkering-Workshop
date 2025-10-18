package org.ender_development.tinkeringworkshop.client.gui

import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.inventory.IInventory
import net.minecraft.util.ResourceLocation
import org.ender_development.tinkeringworkshop.Reference
import org.ender_development.tinkeringworkshop.client.container.ContainerTinkeringWorkshop
import org.ender_development.tinkeringworkshop.tiles.TileTinkeringWorkshop

class GuiTinkeringWorkshop(playerInv: IInventory, val tile: TileTinkeringWorkshop) : GuiContainer(ContainerTinkeringWorkshop(playerInv, tile)) {
    val textureLocation = ResourceLocation(Reference.MODID, "textures/gui/container/tinkering_workshop.png")

    init {
        xSize = tile.guiWidth
        ySize = tile.guiHeight
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        drawDefaultBackground()
        super.drawScreen(mouseX, mouseY, partialTicks)
        renderHoveredToolTip(mouseX, mouseY)
    }

    override fun drawGuiContainerBackgroundLayer(partialTicks: Float, mouseX: Int, mouseY: Int) {
        GlStateManager.color(1f, 1f, 1f, 1f)
        mc.textureManager.bindTexture(textureLocation)

        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize)
    }
}
