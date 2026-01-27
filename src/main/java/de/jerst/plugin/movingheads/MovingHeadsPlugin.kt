package de.jerst.plugin.movingheads

import com.hypixel.hytale.logger.HytaleLogger
import com.hypixel.hytale.server.core.event.events.player.PlayerChatEvent
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent
import com.hypixel.hytale.server.core.plugin.JavaPlugin
import com.hypixel.hytale.server.core.plugin.JavaPluginInit
import de.jerst.plugin.movingheads.commands.MovingHeadCommand
import de.jerst.plugin.movingheads.utils.ConfigurationUtil
import javax.annotation.Nonnull

class MovingHeadsPlugin(@Nonnull init: JavaPluginInit) : JavaPlugin(init) {
    companion object {
        val LOGGER = HytaleLogger.forEnclosingClass()
        lateinit var INSTANCE: MovingHeadsPlugin
            private set
    }
    lateinit var config: ConfigurationUtil

    override fun setup() {
        // Commands
        INSTANCE = this
        config = ConfigurationUtil(dataDirectory)

        commandRegistry.registerCommand(MovingHeadCommand())
    }

    override fun start() {
        LOGGER.atInfo().log("$name started!")
    }

    override fun shutdown() {
        LOGGER.atInfo().log("$name shutting down!")
        super.shutdown()
    }
}