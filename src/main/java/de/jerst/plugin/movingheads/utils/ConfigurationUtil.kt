package de.jerst.plugin.movingheads.utils

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import de.jerst.plugin.movingheads.utils.config.MovingHeadConfig
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.exists

class ConfigurationUtil(dataDirectory: Path) {

    val gson: Gson = GsonBuilder().setPrettyPrinting().create()
    val configPath: Path = dataDirectory.resolve("moving_head.json")

    init {
        dataDirectory.createDirectories()
        if (!configPath.exists()) {
            save(MovingHeadConfig())
        }
    }

    inline fun <reified T> load(): T = try {
        if (!Files.exists(configPath)) throw IllegalStateException("Config existiert nicht: $configPath")
        gson.fromJson(Files.readString(configPath), object : TypeToken<T>() {}.type)
    } catch (e: Exception) {
        throw IllegalStateException("Load Fehler: $configPath", e)
    }

    inline fun <reified T> save(config: T) {
        configPath.parent?.createDirectories()
        Files.writeString(configPath, gson.toJson(config))
    }
}
