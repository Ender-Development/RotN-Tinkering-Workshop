package org.ender_development.tinkeringworkshop.blocks

import org.ender_development.catalyx.blocks.BaseTileBlock
import org.ender_development.tinkeringworkshop.TinkeringWorkshop
import org.ender_development.tinkeringworkshop.client.container.ContainerTinkeringWorkshop
import org.ender_development.tinkeringworkshop.client.gui.GuiTinkeringWorkshop
import org.ender_development.tinkeringworkshop.tiles.TileTinkeringWorkshop

class BlockTinkeringWorkshop :
    BaseTileBlock(
        TinkeringWorkshop,
        "tinkering_workshop",
        TinkeringWorkshop.guiHandler.registerId(
            TileTinkeringWorkshop::class.java,
            ContainerTinkeringWorkshop::class.java,
        ) { GuiTinkeringWorkshop::class.java },
    )
