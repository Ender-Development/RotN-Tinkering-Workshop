package org.ender_development.tinkeringworkshop.client.gui

import net.minecraft.client.gui.GuiTextField
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.inventory.IInventory
import net.minecraft.util.ResourceLocation
import org.ender_development.tinkeringworkshop.Reference
import org.ender_development.tinkeringworkshop.client.container.ContainerTinkeringWorkshop
import org.ender_development.tinkeringworkshop.tiles.TileTinkeringWorkshop

class GuiTinkeringWorkshop(playerInv: IInventory, val tile: TileTinkeringWorkshop) : GuiContainer(ContainerTinkeringWorkshop(playerInv, tile)) {
    val textureLocation = ResourceLocation(Reference.MODID, "textures/gui/container/tinkering_workshop.png")

    lateinit var nameField: GuiTextField

    init {
        xSize = tile.guiWidth
        ySize = tile.guiHeight
    }

    override fun initGui() {
        super.initGui()
        val i = (width - xSize) / 2
        val j = (height - ySize) / 2
        nameField = GuiTextField(0, fontRenderer, i + 62, j + 24, 103, 12)
        nameField.setTextColor(-1)
        nameField.setDisabledTextColour(-1)
        nameField.enableBackgroundDrawing = false
        nameField.setMaxStringLength(35)
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        drawDefaultBackground()
        super.drawScreen(mouseX, mouseY, partialTicks)
        drawText()
        renderHoveredToolTip(mouseX, mouseY)
        GlStateManager.disableLighting()
        GlStateManager.disableBlend()
        nameField.drawTextBox()
    }

    override fun drawGuiContainerBackgroundLayer(partialTicks: Float, mouseX: Int, mouseY: Int) {
        GlStateManager.color(1f, 1f, 1f, 1f)
        mc.textureManager.bindTexture(textureLocation)

        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize)
    }

    private fun drawText() {
        // TODO: properly align text
        fontRenderer.drawString("${tile.enchantingPower}", guiLeft + 80, guiTop + 16, 0x404040)
    }
}
