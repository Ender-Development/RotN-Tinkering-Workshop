package org.ender_development.tinkeringworkshop.command

import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender
import net.minecraft.server.MinecraftServer
import net.minecraftforge.server.command.CommandTreeBase
import org.ender_development.tinkeringworkshop.Reference
import org.ender_development.tinkeringworkshop.TinkeringWorkshop
import org.ender_development.tinkeringworkshop.parser.toBookshelf

object TWCommand : CommandTreeBase() {
    override fun getName() = Reference.MODID

    override fun getUsage(sender: ICommandSender) = Reference.MOD_NAME

    init {
        addSubcommand(Reload)
        addSubcommand(CopyBlock)
        addSubcommand(CopyEnchantment)
        addSubcommand(Info)
    }

    object Reload : CommandBase() {
        override fun getName() = "reload"

        override fun getUsage(sender: ICommandSender) = "Reloads the Tinkering Workshop configuration files."

        override fun execute(server: MinecraftServer, sender: ICommandSender, args: Array<out String>) {
            TinkeringWorkshop.parserRegistry.refreshAll()
            notifyCommandListener(sender, this, "Tinkering Workshop configuration reloaded.")
        }
    }

    object CopyBlock : CommandBase() {
        override fun getName() = "copyblock"

        override fun getUsage(sender: ICommandSender) = "Copies a default json for the hold or looked at block to the clipboard."

        override fun execute(server: MinecraftServer, sender: ICommandSender, args: Array<out String>) {
            notifyCommandListener(sender, this, "Not implemented yet.")
        }
    }

    object CopyEnchantment : CommandBase() {
        override fun getName() = "copyenchantment"

        override fun getUsage(sender: ICommandSender) = "Copies a default json for all enchantments on the hold item to the clipboard."

        override fun execute(server: MinecraftServer, sender: ICommandSender, args: Array<out String>) {
            notifyCommandListener(sender, this, "Not implemented yet.")
        }
    }

    object Info : CommandBase() {
        override fun getName() = "info"

        override fun getUsage(sender: ICommandSender) = "Displays informations about the currently hold bookshelf."

        override fun execute(server: MinecraftServer, sender: ICommandSender, args: Array<out String>) {
            try {
                val player = getCommandSenderAsPlayer(sender)
                val itemStack = player.heldItemMainhand
                val bookshelf = itemStack.toBookshelf()
                bookshelf?.let {
                    notifyCommandListener(sender, this, "Bookshelf Info:")
                    notifyCommandListener(sender, this, "- Block State: ${it.blockState}")
                    notifyCommandListener(sender, this, "- Power: ${it.power}")
                    notifyCommandListener(sender, this, "- Simultaneous Enchantments: ${it.simultaneousEnchantment}")
                    notifyCommandListener(sender, this, "- Max Considered: ${it.maxConsidered}")
                } ?: notifyCommandListener(sender, this, "The held item is not configured as a bookshelf.")
            } catch (_: Exception) {
                notifyCommandListener(sender, this, "An error occurred while retrieving bookshelf info.")
            }
        }
    }
}
