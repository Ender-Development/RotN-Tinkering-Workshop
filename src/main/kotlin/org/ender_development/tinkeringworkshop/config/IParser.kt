package org.ender_development.tinkeringworkshop.config

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import net.minecraft.util.ResourceLocation
import java.io.File

interface IParser<R : ISerializable, S : ISanitized> {
    /**
     * The raw data loaded from the JSON file, before any sanitization or validation.
     */
    val dataRaw: MutableList<R>

    /**
     * The sanitized and validated data ready for use in the mod.
     */
    val dataSanitized: MutableList<S>

    /**
     * The JSON configuration file location.
     */
    val json: File

    /**
     * Gson instance configured for pretty printing.
     */
    val gson: Gson
        get() = GsonBuilder().setPrettyPrinting().create()

    /**
     * Loads the configuration data from the JSON file into [dataRaw].
     */
    fun loadFromJson()

    /**
     * Sanitizes and validates the raw data in [dataRaw], populating [dataSanitized].
     */
    fun sanitizeData()

    /**
     * Generates a default configuration file at the specified [file] location.
     */
    fun generateDefaultConfig(file: File)

    /**
     * Retrieves a configuration entry by its [name].
     */
    operator fun get(name: ResourceLocation): S?
}
