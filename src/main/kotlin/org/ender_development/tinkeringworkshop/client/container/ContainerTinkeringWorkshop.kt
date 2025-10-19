package org.ender_development.tinkeringworkshop.client.container

import net.minecraft.inventory.IInventory
import net.minecraftforge.items.SlotItemHandler
import org.ender_development.catalyx.client.container.BaseContainer
import org.ender_development.tinkeringworkshop.tiles.TileTinkeringWorkshop

class ContainerTinkeringWorkshop(playerInv: IInventory, tile: TileTinkeringWorkshop) : BaseContainer(playerInv, tile) {
    init {
        addSlotToContainer(SlotItemHandler(tile.input, 0, 14, 14))
        addSlotToContainer(SlotItemHandler(tile.input, 1, 14, 39))
    }
}
