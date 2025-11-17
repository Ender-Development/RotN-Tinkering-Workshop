package org.ender_development.tinkeringworkshop.client.gui

import net.minecraft.client.gui.GuiTextField
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.inventory.Container
import net.minecraft.inventory.IContainerListener
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraft.util.NonNullList
import net.minecraft.util.ResourceLocation
import org.ender_development.catalyx.client.container.BaseContainer.Companion.PLAYER_INVENTORY_SIZE
import org.ender_development.catalyx.utils.RenderUtils
import org.ender_development.catalyx.utils.RenderUtils.FONT_RENDERER
import org.ender_development.catalyx.utils.extensions.get
import org.ender_development.catalyx.utils.extensions.translate
import org.ender_development.tinkeringworkshop.Reference
import org.ender_development.tinkeringworkshop.client.container.ContainerTinkeringWorkshop
import org.ender_development.tinkeringworkshop.network.PacketHandler
import org.ender_development.tinkeringworkshop.network.RenamePacket
import org.ender_development.tinkeringworkshop.tiles.TileTinkeringWorkshop
import org.lwjgl.input.Keyboard

class GuiTinkeringWorkshop(playerInv: IInventory, val tile: TileTinkeringWorkshop) :
    GuiContainer(ContainerTinkeringWorkshop(playerInv, tile)),
    IContainerListener {
    val textureLocation = ResourceLocation(Reference.MODID, "textures/gui/container/tinkering_workshop.png")
    val container = inventorySlots as ContainerTinkeringWorkshop

    val renameTextField = GuiTextField(1, FONT_RENDERER, 0, 0, 108, 14).apply {
        maxStringLength = 35
        enableBackgroundDrawing = false
        setTextColor(0xff80ba)
    }

    init {
        xSize = tile.guiWidth
        ySize = tile.guiHeight
        Keyboard.enableRepeatEvents(true)
    }

    override fun initGui() {
        super.initGui()
        renameTextField.x = guiLeft + 24
        renameTextField.y = guiTop + 26
        container.removeListener(this)
        container.addListener(this)
    }

    override fun onGuiClosed() {
        Keyboard.enableRepeatEvents(false)
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        drawDefaultBackground()
        super.drawScreen(mouseX, mouseY, partialTicks)
        renderHoveredToolTip(mouseX, mouseY)
        // note to Ender: do not draw anything here, use drawGuiContainer{Foreground/Background}Layer instead as those have the correct gl state; you can delete this comment after you read it ;p
    }

    override fun drawGuiContainerBackgroundLayer(partialTicks: Float, mouseX: Int, mouseY: Int) {
        GlStateManager.color(1f, 1f, 1f, 1f)
        RenderUtils.bindTexture(textureLocation)
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize)
        drawTexturedModalRect(renameTextField.x - 3, renameTextField.y - 3, 0, if (renameTextField.isEnabled) 199 else 213, 108, 14)
    }

    override fun drawGuiContainerForegroundLayer(mouseX: Int, mouseY: Int) {
        renameTextField.x -= guiLeft
        renameTextField.y -= guiTop
        renameTextField.drawTextBox()
        renameTextField.x += guiLeft
        renameTextField.y += guiTop
        drawText()
    }

    private fun drawText() {
        GlStateManager.pushMatrix()
        GlStateManager.color(1f, 1f, 1f, 1f)
        centerString("tile.${Reference.MODID}:tinkering_workshop.name".translate(), -guiLeft + width / 2f, 6f, 0x303030)

        val strPower = "%.2f".format(tile.enchantingPower)
        val lengthPower = fontRenderer.getStringWidth(strPower)
        val scale = 21f / lengthPower
        val inverseScale = 1 / scale
        GlStateManager.scale(scale, scale, 1f)
        centerString(strPower, 66 * inverseScale, (60 - fontRenderer.FONT_HEIGHT) * inverseScale, 0xaf80ba)
        GlStateManager.popMatrix()
    }

    private fun centerString(text: String, x: Float, y: Float, color: Int, shadow: Boolean = false) {
        fontRenderer.drawString(text, x - (fontRenderer.getStringWidth(text) shr 1), y, color, shadow)
    }

    override fun keyTyped(typedChar: Char, keyCode: Int) {
        if (!renameTextField.textboxKeyTyped(typedChar, keyCode)) {
            super.keyTyped(typedChar, keyCode)
        } else {
            renameItem()
        }
    }

    private fun renameItem() {
        // update client-side
        val stack = container[0].stack
        val name = renameTextField.text
        if (name.isBlank()) {
            stack.clearCustomName()
        } else {
            stack.setStackDisplayName(name)
        }

        // update server-side
        PacketHandler.channel.sendToServer(RenamePacket(name))
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
        super.mouseClicked(mouseX, mouseY, mouseButton)
        renameTextField.mouseClicked(mouseX, mouseY, mouseButton)
    }

    override fun sendAllContents(containerToSend: Container, itemsList: NonNullList<ItemStack?>) {
        sendSlotContents(containerToSend, PLAYER_INVENTORY_SIZE, containerToSend[0].stack)
    }

    override fun sendSlotContents(containerToSend: Container, slotInd: Int, stack: ItemStack) {
        if (slotInd != PLAYER_INVENTORY_SIZE) {
            return
        }

        if (renameTextField.text != stack.displayName) {
            renameTextField.text = if (stack.isEmpty) "" else stack.displayName
        }
        renameTextField.setEnabled(!stack.isEmpty)
    }

    // no-op
    override fun sendWindowProperty(containerIn: Container, varToUpdate: Int, newValue: Int) {}
    override fun sendAllWindowProperties(containerIn: Container, inventory: IInventory) {}
}
