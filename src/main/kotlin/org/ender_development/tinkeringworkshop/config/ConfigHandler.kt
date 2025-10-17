package org.ender_development.tinkeringworkshop.config

import net.minecraftforge.common.config.Config
import net.minecraftforge.common.config.ConfigManager
import net.minecraftforge.fml.client.event.ConfigChangedEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import org.ender_development.tinkeringworkshop.Reference

@Config(modid = Reference.MODID, name = "${Reference.MODID}/general")
object ConfigHandler {
    @JvmField
    @Config.Name("Default Crafting Sound")
    @Config.Comment("The sound that plays when crafting is completed. Use the format 'namespace:sound_event'.")
    var defaultCraftingSound = "entity.item.pickup"

    @JvmField
    @Config.Name("Max Enchantments per Item")
    @Config.Comment("The maximum number of enchantments that can be applied to a single item.")
    @Config.RangeInt(min = 1)
    var maxEnchantmentsPerItem = 5

    @JvmField
    @Config.Name("Max Diameter")
    @Config.Comment("The maximum diameter of the Tinkering Workshop will check for enchantment blocks.")
    @Config.RangeInt(min = 1, max = 17)
    var maxDiameter = 17

    @JvmField
    @Config.Name("Max Height")
    @Config.Comment("The maximum height above the Tinkering Workshop will check for enchantment blocks.")
    @Config.RangeInt(min = 1, max = 17)
    var maxHeight = 17

    @Mod.EventBusSubscriber(modid = Reference.MODID)
    object ConfigEventHandler {
        @JvmStatic
        @SubscribeEvent
        fun onConfigChanged(event: ConfigChangedEvent.OnConfigChangedEvent) {
            if (event.modID == Reference.MODID) {
                ConfigManager.sync(Reference.MODID, Config.Type.INSTANCE)
            }
        }
    }
}
