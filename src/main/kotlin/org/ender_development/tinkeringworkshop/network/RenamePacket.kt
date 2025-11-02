package org.ender_development.tinkeringworkshop.network

import io.netty.buffer.ByteBuf
import net.minecraftforge.fml.common.FMLCommonHandler
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext
import org.ender_development.catalyx.utils.extensions.readString
import org.ender_development.catalyx.utils.extensions.writeString
import org.ender_development.tinkeringworkshop.client.container.ContainerTinkeringWorkshop

class RenamePacket() : IMessage {
    lateinit var name: String

    override fun fromBytes(buf: ByteBuf) {
        name = buf.readString()
    }

    override fun toBytes(buf: ByteBuf) {
        buf.writeString(name)
    }

    constructor(name: String) : this() {
        this.name = name
    }

    class Handler : IMessageHandler<RenamePacket, IMessage> {
        override fun onMessage(message: RenamePacket, ctx: MessageContext): IMessage? {
            FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask {
                handle(message, ctx)
            }
            return null
        }

        private fun handle(message: RenamePacket, ctx: MessageContext) {
            val player = ctx.serverHandler.player
            val container = player.openContainer

            if (container is ContainerTinkeringWorkshop) {
                container.updateItemName(message.name)
            }
        }
    }
}
