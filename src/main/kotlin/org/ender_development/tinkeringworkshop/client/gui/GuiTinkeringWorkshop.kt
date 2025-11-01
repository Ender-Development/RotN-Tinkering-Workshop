package org.ender_development.tinkeringworkshop.client.gui

import net.minecraft.client.gui.GuiTextField
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.inventory.IInventory
import net.minecraft.util.ResourceLocation
import org.ender_development.catalyx.utils.RenderUtils.FONT_RENDERER
import org.ender_development.catalyx.utils.extensions.translate
import org.ender_development.tinkeringworkshop.Reference
import org.ender_development.tinkeringworkshop.client.container.ContainerTinkeringWorkshop
import org.ender_development.tinkeringworkshop.tiles.TileTinkeringWorkshop

class GuiTinkeringWorkshop(playerInv: IInventory, val tile: TileTinkeringWorkshop) : GuiContainer(ContainerTinkeringWorkshop(playerInv, tile)) {
    val textureLocation = ResourceLocation(Reference.MODID, "textures/gui/container/tinkering_workshop.png")

    val textField = GuiTextField(1, FONT_RENDERER, 21, 36, 108, 14).apply {
        maxStringLength = 35
        enableBackgroundDrawing = false
        setTextColor(0x350279f)
    }

    init {
        xSize = tile.guiWidth
        ySize = tile.guiHeight
    }

    override fun initGui() {
        super.initGui()
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        drawDefaultBackground()
        super.drawScreen(mouseX, mouseY, partialTicks)
        drawText()
        renderHoveredToolTip(mouseX, mouseY)
        // textField.drawTextBox()
    }

    override fun drawGuiContainerBackgroundLayer(partialTicks: Float, mouseX: Int, mouseY: Int) {
        GlStateManager.color(1f, 1f, 1f, 1f)
        mc.textureManager.bindTexture(textureLocation)
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize)
    }

    private fun drawText() {
        GlStateManager.pushMatrix()
        GlStateManager.color(1f, 1f, 1f, 1f)
        centerString("tile.${Reference.MODID}:tinkering_workshop.name".translate(), width / 2f, guiTop + 6f, 0x303030)

        val strPower = "%.2f".format(tile.enchantingPower)
        val lengthPower = fontRenderer.getStringWidth(strPower)
        val scale = 21f / lengthPower
        GlStateManager.scale(scale, scale, 1f)
        val adjustedX = (guiLeft + 66) * (1 / scale)
        val adjustedY = (guiTop + 60 - fontRenderer.FONT_HEIGHT) * (1 / scale)
        centerString(strPower, adjustedX, adjustedY, 0xF0F0F0)
        GlStateManager.scale(1 / scale, 1 / scale, 1f)
        GlStateManager.popMatrix()
    }

    private fun centerString(text: String, x: Float, y: Float, color: Int, shadow: Boolean = false) {
        fontRenderer.drawString(text, x - (fontRenderer.getStringWidth(text) shr 1), y, color, shadow)
    }
}
