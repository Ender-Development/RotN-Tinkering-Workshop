package org.ender_development.tinkeringworkshop.client.gui

import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.inventory.IInventory
import org.ender_development.tinkeringworkshop.client.container.ContainerTinkeringWorkshop
import org.ender_development.tinkeringworkshop.tiles.TileTinkeringWorkshop

class GuiTinkeringWorkshop(playerInv: IInventory, val tile: TileTinkeringWorkshop) :
    GuiContainer(ContainerTinkeringWorkshop(playerInv, tile)) {
    override fun drawGuiContainerBackgroundLayer(partialTicks: Float, mouseX: Int, mouseY: Int) {
        TODO("Not yet implemented")
    }
}
