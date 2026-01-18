package de.jerst.plugin.movingheads

import com.hypixel.hytale.logger.HytaleLogger
import com.hypixel.hytale.server.core.plugin.JavaPlugin
import com.hypixel.hytale.server.core.plugin.JavaPluginInit
import de.jerst.plugin.movingheads.commands.CameraCommand
import javax.annotation.Nonnull

class MovingHeadsPlugin(@Nonnull init: JavaPluginInit) : JavaPlugin(init) {
    companion object {
        val LOGGER = HytaleLogger.forEnclosingClass()
    }

    override fun setup() {
        LOGGER.atInfo().log(name + " Plugin loaded!")

        // Commands
        commandRegistry.registerCommand(CameraCommand())
//        commandRegistry.registerCommand(ExampleCommand())
//        commandRegistry.registerCommand(TitleCommand())
//
//        // Events
//        eventRegistry.registerGlobal<String?, PlayerChatEvent?>(
//            PlayerChatEvent::class.java
//        ) { event: PlayerChatEvent? -> PlayerChatListener.onPlayerChat(event!!) }
//        eventRegistry.registerGlobal<String?, PlayerReadyEvent?>(
//            PlayerReadyEvent::class.java
//        ) { event: PlayerReadyEvent? -> PlayerReadyListener.onPlayerReady(event!!) }
    }

    override fun start() {
        LOGGER.atInfo().log("$name started!")
    }

    override fun shutdown() {
        LOGGER.atInfo().log("$name shutting down!")
        super.shutdown()
    }
}