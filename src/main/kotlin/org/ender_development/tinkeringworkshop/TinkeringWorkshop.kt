package org.ender_development.tinkeringworkshop

import net.minecraft.creativetab.CreativeTabs
import net.minecraft.util.text.TextFormatting
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.player.ItemTooltipEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.event.FMLServerStartingEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.network.NetworkRegistry
import org.apache.logging.log4j.Logger
import org.ender_development.catalyx.client.gui.CatalyxGuiHandler
import org.ender_development.catalyx.core.ICatalyxMod
import org.ender_development.tinkeringworkshop.blocks.ModBlocks
import org.ender_development.tinkeringworkshop.command.TWCommand
import org.ender_development.tinkeringworkshop.config.BookshelfParser
import org.ender_development.tinkeringworkshop.config.ConfigHandler
import org.ender_development.tinkeringworkshop.config.EnchantmentParser
import org.ender_development.tinkeringworkshop.config.toBookshelf

@Mod(
    modid = Reference.MODID,
    name = Reference.MOD_NAME,
    version = Reference.VERSION,
    dependencies = ICatalyxMod.CATALYX_ADDON,
    modLanguageAdapter = ICatalyxMod.MOD_LANGUAGE_ADAPTER,
)
@Mod.EventBusSubscriber
object TinkeringWorkshop : ICatalyxMod {
    override val creativeTab: CreativeTabs = CreativeTabs.DECORATIONS

    val guiHandler = CatalyxGuiHandler(this)

    lateinit var logger: Logger

    @Mod.EventHandler
    fun preInit(ev: FMLPreInitializationEvent) {
        logger = ev.modLog
        NetworkRegistry.INSTANCE.registerGuiHandler(TinkeringWorkshop, guiHandler)
        MinecraftForge.EVENT_BUS.register(this)
    }

    @Mod.EventHandler
    fun postInit(ev: FMLPostInitializationEvent) {
        BookshelfParser.loadFromJson()
        EnchantmentParser.loadFromJson()
    }

    @Mod.EventHandler
    fun starting(ev: FMLServerStartingEvent) {
        ev.registerServerCommand(TWCommand)
    }

    @SubscribeEvent
    fun tooltip(ev: ItemTooltipEvent) {
        if (!ConfigHandler.debugMode) return
        ev.itemStack.toBookshelf()?.let {
            ev.toolTip.addAll(
                listOf(
                    "${TextFormatting.DARK_PURPLE}${TextFormatting.BOLD}Bookshelf Power: ${TextFormatting.RESET}${TextFormatting.LIGHT_PURPLE}${it.power}",
                    "${TextFormatting.DARK_PURPLE}${TextFormatting.BOLD}Simultaneous Enchantments: ${TextFormatting.RESET}${TextFormatting.LIGHT_PURPLE}${it.simultaneousEnchantment}",
                    "${TextFormatting.DARK_PURPLE}${TextFormatting.BOLD}Max Considered: ${TextFormatting.RESET}${TextFormatting.LIGHT_PURPLE}${if (it.maxConsidered == Int.MAX_VALUE) "Unlimited" else it.maxConsidered}",
                ),
            )
        }
    }

    init {
        ModBlocks.jvmLoadClass()
    }
}
