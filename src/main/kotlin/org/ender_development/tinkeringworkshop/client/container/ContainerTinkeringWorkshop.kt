package org.ender_development.tinkeringworkshop.client.container

import net.minecraft.inventory.IInventory
import net.minecraftforge.items.SlotItemHandler
import org.ender_development.catalyx.client.container.BaseContainer
import org.ender_development.catalyx.utils.extensions.get
import org.ender_development.tinkeringworkshop.tiles.TileTinkeringWorkshop

class ContainerTinkeringWorkshop(playerInv: IInventory, val tile: TileTinkeringWorkshop) : BaseContainer(playerInv, tile) {
    init {
        addSlotToContainer(object : SlotItemHandler(tile.input, 0, 27, 46) {
            override fun onSlotChanged() {
                super.onSlotChanged()
                detectAndSendChanges()
            }
        })
        addSlotToContainer(SlotItemHandler(tile.input, 1, 107, 46))
    }

    fun updateItemName(name: String) {
        var name = name
        if (name.length > 35) {
            // vanilla name character limit
            name = name.substring(0, 35)
        }
        val stack = this[0].stack
        if (name.isBlank()) {
            stack.clearCustomName()
        } else {
            stack.setStackDisplayName(name)
        }
    }
}
