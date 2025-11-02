package org.ender_development.tinkeringworkshop

import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.ItemEnchantedBook
import net.minecraft.util.text.TextFormatting
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.player.ItemTooltipEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.event.FMLServerStartingEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.network.NetworkRegistry
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import org.apache.logging.log4j.Logger
import org.ender_development.catalyx.client.gui.CatalyxGuiHandler
import org.ender_development.catalyx.core.ICatalyxMod
import org.ender_development.catalyx.utils.extensions.translate
import org.ender_development.catalyx.utils.parser.ParserRegistry
import org.ender_development.tinkeringworkshop.blocks.ModBlocks
import org.ender_development.tinkeringworkshop.command.TWCommand
import org.ender_development.tinkeringworkshop.config.ConfigHandler
import org.ender_development.tinkeringworkshop.network.PacketHandler
import org.ender_development.tinkeringworkshop.parser.TWBookshelfParser
import org.ender_development.tinkeringworkshop.parser.TWEnchantmentParser
import org.ender_development.tinkeringworkshop.parser.TWItemParser
import org.ender_development.tinkeringworkshop.parser.getEnchantments
import org.ender_development.tinkeringworkshop.parser.toBookshelf
import org.ender_development.tinkeringworkshop.parser.toTWItem

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
    val parserRegistry = ParserRegistry {
        parser("bookshelf", TWBookshelfParser())
        parser("enchantment", TWEnchantmentParser())
        parser("item", TWItemParser())
    }

    lateinit var logger: Logger

    @Mod.EventHandler
    fun preInit(ev: FMLPreInitializationEvent) {
        logger = ev.modLog
        NetworkRegistry.INSTANCE.registerGuiHandler(TinkeringWorkshop, guiHandler)
        MinecraftForge.EVENT_BUS.register(this)
        PacketHandler.init()
    }

    @Mod.EventHandler
    fun postInit(ev: FMLPostInitializationEvent) {
        parserRegistry.refreshAll()
    }

    @Mod.EventHandler
    fun starting(ev: FMLServerStartingEvent) {
        ev.registerServerCommand(TWCommand)
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
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
        ev.itemStack.toTWItem()?.let {
            ev.toolTip.addAll(
                listOf(
                    "${TextFormatting.DARK_PURPLE}${TextFormatting.BOLD}Tinkering Workshop Config:${TextFormatting.RESET}",
                    "${TextFormatting.DARK_PURPLE}  Max Enchantment Slots: ${TextFormatting.RESET}${TextFormatting.LIGHT_PURPLE}${it.slots}",
                    "${TextFormatting.DARK_PURPLE}  Cost Multiplier: ${TextFormatting.RESET}${TextFormatting.LIGHT_PURPLE}${it.costMultiplier}",
                ),
            )
            if (it.whitelist.isNotEmpty()) {
                ev.toolTip.add("${TextFormatting.DARK_PURPLE}  Whitelist:")
                ev.toolTip.addAll(it.whitelist.map { enchantment -> "${TextFormatting.GREEN}    ${enchantment.name.translate()}" })
            }
            if (it.blacklist.isNotEmpty()) {
                ev.toolTip.add("${TextFormatting.DARK_PURPLE}  Blacklist:")
                ev.toolTip.addAll(it.blacklist.map { enchantment -> "${TextFormatting.RED}    ${enchantment.name.translate()}" })
            }
        }
        (ev.itemStack.item as? ItemEnchantedBook).let {
            val enchantments = ev.itemStack.getEnchantments()
            enchantments.forEach {
                ev.toolTip.add(
                    "${TextFormatting.DARK_PURPLE}${TextFormatting.BOLD}Enchantment Config for ${it.enchantment.name.translate()}:${TextFormatting.RESET}",
                )
                ev.toolTip.addAll(
                    listOf(
                        "${TextFormatting.DARK_PURPLE}  Color: ${TextFormatting.RESET}${TextFormatting.LIGHT_PURPLE}${it.color}",
                        "${TextFormatting.DARK_PURPLE}  Bookshelf Logic: ${TextFormatting.RESET}${TextFormatting.LIGHT_PURPLE}${it.blockLogic}",
                        "${TextFormatting.DARK_PURPLE}  Cost Multiplier: ${TextFormatting.RESET}${TextFormatting.LIGHT_PURPLE}${it.costMultiplier}",
                    ),
                )
            }
        }
    }

    init {
        ModBlocks.jvmLoadClass()
    }
}
