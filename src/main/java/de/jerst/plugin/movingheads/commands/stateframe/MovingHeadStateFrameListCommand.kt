package de.jerst.plugin.movingheads.commands.stateframe

import com.hypixel.hytale.component.Ref
import com.hypixel.hytale.component.Store
import com.hypixel.hytale.server.core.Message
import com.hypixel.hytale.server.core.command.system.CommandContext
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractTargetPlayerCommand
import com.hypixel.hytale.server.core.universe.PlayerRef
import com.hypixel.hytale.server.core.universe.world.World
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore
import de.jerst.plugin.movingheads.MovingHeadsPlugin
import de.jerst.plugin.movingheads.model.MovingHeadConfig
import de.jerst.plugin.movingheads.utils.ConfigurationUtil
import de.jerst.plugin.movingheads.utils.withPrefix

class MovingHeadStateFrameListCommand : AbstractTargetPlayerCommand("list", "server.movingheads.stateframe.list") {
    var configManager: ConfigurationUtil = MovingHeadsPlugin.INSTANCE.config

    override fun execute(
        commandContext: CommandContext,
        sourceRef: Ref<EntityStore?>?,
        ref: Ref<EntityStore?>,
        playerRef: PlayerRef,
        world: World,
        store: Store<EntityStore?>
    ) {
        val config = configManager.load<MovingHeadConfig>()

        commandContext.sendMessage(
            Message.translation("server.movingheads.stateframe.yours").withPrefix()
        )

        for (sceneGroup in config.getStateFrames(playerRef.uuid)) {
            commandContext.sendMessage(
                Message.raw("\t- ${sceneGroup.name}")
            )
        }
    }
}