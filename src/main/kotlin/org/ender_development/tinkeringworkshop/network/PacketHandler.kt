package org.ender_development.tinkeringworkshop.network

import net.minecraftforge.fml.common.network.NetworkRegistry
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper
import net.minecraftforge.fml.relauncher.Side
import org.ender_development.tinkeringworkshop.Reference

object PacketHandler {
    internal val channel: SimpleNetworkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MODID)

    internal fun init() {
        channel.registerMessage(RenamePacket.Handler::class.java, RenamePacket::class.java, 0, Side.SERVER)
    }
}
