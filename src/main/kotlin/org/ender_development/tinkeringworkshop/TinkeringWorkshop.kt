package org.ender_development.tinkeringworkshop

import net.minecraft.creativetab.CreativeTabs
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import org.apache.logging.log4j.Logger
import org.ender_development.catalyx.client.gui.CatalyxGuiHandler
import org.ender_development.catalyx.core.ICatalyxMod
import org.ender_development.tinkeringworkshop.blocks.ModBlocks

@Mod(
    modid = Reference.MODID,
    name = Reference.MOD_NAME,
    version = Reference.VERSION,
    dependencies = ICatalyxMod.CATALYX_ADDON,
    modLanguageAdapter = ICatalyxMod.MOD_LANGUAGE_ADAPTER,
)
@Mod.EventBusSubscriber
object TinkeringWorkshop : ICatalyxMod {
    override val creativeTab: CreativeTabs = CreativeTabs.MISC

    val guiHandler = CatalyxGuiHandler(this)

    lateinit var logger: Logger

    @Mod.EventHandler
    fun preInit(ev: FMLPreInitializationEvent) {
        logger = ev.modLog
    }

    init {
        ModBlocks.jvmLoadClass()
    }
}
