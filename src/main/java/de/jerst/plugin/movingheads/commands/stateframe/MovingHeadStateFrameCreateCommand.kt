package de.jerst.plugin.movingheads.commands.stateframe

import com.hypixel.hytale.component.Ref
import com.hypixel.hytale.component.Store
import com.hypixel.hytale.server.core.Message
import com.hypixel.hytale.server.core.command.system.CommandContext
import com.hypixel.hytale.server.core.command.system.arguments.system.OptionalArg
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractTargetPlayerCommand
import com.hypixel.hytale.server.core.universe.PlayerRef
import com.hypixel.hytale.server.core.universe.world.World
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore
import de.jerst.plugin.movingheads.MovingHeadsPlugin
import de.jerst.plugin.movingheads.model.AnimationNode
import de.jerst.plugin.movingheads.model.MovingHeadConfig
import de.jerst.plugin.movingheads.model.StateFrame
import de.jerst.plugin.movingheads.utils.ConfigurationUtil
import de.jerst.plugin.movingheads.utils.MessageUtil

class MovingHeadStateFrameCreateCommand : AbstractTargetPlayerCommand("create", "server.movingheads.stateframe.create") {

    private val stateFrameNameArg: RequiredArg<String> =
        withRequiredArg<String>("stateframename", "server.movingheads.arg.stateframe.name", ArgTypes.STRING)

    private val sceneGroupNameArg: RequiredArg<String> =
        withRequiredArg<String>("sceneGroupName", "server.movingheads.arg.scenegroup.name", ArgTypes.STRING)

    private val stateNameArg: RequiredArg<String> =
        withRequiredArg<String>("stateName", "server.movingheads.arg.statename.name", ArgTypes.STRING)

    private val iterationsArg: OptionalArg<Int> =
        withOptionalArg<Int>("iterations", "server.movingheads.arg.iterations.name", ArgTypes.INTEGER)

    var configManager: ConfigurationUtil = MovingHeadsPlugin.INSTANCE.config

    override fun execute(
        commandContext: CommandContext,
        sourceRef: Ref<EntityStore?>?,
        ref: Ref<EntityStore?>,
        playerRef: PlayerRef,
        world: World,
        store: Store<EntityStore?>
    ) {
        val stateFrameName = commandContext.get<String>(stateFrameNameArg)
        val sceneGroupName = commandContext.get<String>(sceneGroupNameArg)
        val stateName = commandContext.get<String>(stateNameArg)
        val iterations = commandContext.get<Int?>(iterationsArg)


        val config = configManager.load<MovingHeadConfig>()
        if (config.getStateFrame(playerRef.uuid, stateFrameName) != null) {
            // TODO Message already stateframe already exists
            return
        }

        val newStateFrame = StateFrame(playerRef.uuid, stateFrameName, sceneGroupName, stateName, iterations ?: 1)
        config.stateFrames.add(newStateFrame)

        configManager.save(config)

        commandContext.sendMessage(
            MessageUtil.pluginTMessage(
                Message.translation("server.movingheads.scenegroup.created").param("name", stateFrameName)
            )
        )
    }
}