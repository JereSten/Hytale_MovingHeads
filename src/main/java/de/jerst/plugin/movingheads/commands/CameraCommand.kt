package de.jerst.plugin.movingheads.commands

import com.hypixel.hytale.component.Ref
import com.hypixel.hytale.component.Store
import com.hypixel.hytale.logger.HytaleLogger
import com.hypixel.hytale.protocol.ClientCameraView
import com.hypixel.hytale.protocol.ServerCameraSettings
import com.hypixel.hytale.protocol.packets.camera.SetServerCamera
import com.hypixel.hytale.server.core.command.system.CommandContext
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractTargetPlayerCommand
import com.hypixel.hytale.server.core.universe.PlayerRef
import com.hypixel.hytale.server.core.universe.world.World
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore
import java.util.logging.Level

class CameraCommand : AbstractTargetPlayerCommand("example_camera", "A camera command") {
    companion object {
        val LOGGER = HytaleLogger.forEnclosingClass()
    }

    override fun execute(
        context: CommandContext, sourceRef: Ref<EntityStore?>?,
        ref: Ref<EntityStore?>, playerRef: PlayerRef, world: World,
        store: Store<EntityStore?>
    ) {
        LOGGER.at(Level.INFO).log(name + " Run camera!")

        val settings = ServerCameraSettings()
        settings.distance = 10.0f // Zoom distance from player
        settings.isFirstPerson = false // Third-person mode
        settings.positionLerpSpeed = 0.2f // Smooth camera follow

        val playerRefComponent = store.getComponent<PlayerRef?>(ref, PlayerRef.getComponentType())
        playerRefComponent!!.packetHandler
            .writeNoCache(SetServerCamera(ClientCameraView.Custom, true, settings))
    }
}
