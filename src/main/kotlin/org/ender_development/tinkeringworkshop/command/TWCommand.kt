package org.ender_development.tinkeringworkshop.command

import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender
import net.minecraftforge.server.command.CommandTreeBase
import org.ender_development.tinkeringworkshop.Reference
import org.ender_development.tinkeringworkshop.config.EnchantmentParser

object TWCommand : CommandTreeBase() {
    override fun getName() = Reference.MODID

    override fun getUsage(sender: ICommandSender) = Reference.MOD_NAME

    init {
        addSubcommand(Reload)
        addSubcommand(CopyBlock)
        addSubcommand(CopyEnchantment)
    }

    object Reload : CommandBase() {
        override fun getName() = "reload"

        override fun getUsage(sender: ICommandSender) = "Reloads the Tinkering Workshop configuration files."

        override fun execute(server: net.minecraft.server.MinecraftServer, sender: ICommandSender, args: Array<out String>) {
            EnchantmentParser.loadFromJson()
            notifyCommandListener(sender, this, "Tinkering Workshop configuration reloaded.")
        }
    }

    object CopyBlock : CommandBase() {
        override fun getName() = "copyblock"

        override fun getUsage(sender: ICommandSender) = "Copies a default json for the hold or looked at block to the clipboard."

        override fun execute(server: net.minecraft.server.MinecraftServer, sender: ICommandSender, args: Array<out String>) {
            notifyCommandListener(sender, this, "Not implemented yet.")
        }
    }

    object CopyEnchantment : CommandBase() {
        override fun getName() = "copyenchantment"

        override fun getUsage(sender: ICommandSender) = "Copies a default json for all enchantments on the hold item to the clipboard."

        override fun execute(server: net.minecraft.server.MinecraftServer, sender: ICommandSender, args: Array<out String>) {
            notifyCommandListener(sender, this, "Not implemented yet.")
        }
    }
}
