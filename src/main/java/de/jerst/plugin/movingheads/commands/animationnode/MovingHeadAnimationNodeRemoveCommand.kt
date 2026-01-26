package de.jerst.plugin.movingheads.commands.animationnode

import com.hypixel.hytale.component.Ref
import com.hypixel.hytale.component.Store
import com.hypixel.hytale.server.core.Message
import com.hypixel.hytale.server.core.command.system.CommandContext
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractTargetPlayerCommand
import com.hypixel.hytale.server.core.universe.PlayerRef
import com.hypixel.hytale.server.core.universe.world.World
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore
import de.jerst.plugin.movingheads.MovingHeadsPlugin
import de.jerst.plugin.movingheads.model.MovingHeadConfig
import de.jerst.plugin.movingheads.utils.ConfigurationUtil
import de.jerst.plugin.movingheads.utils.MessageUtil
import de.jerst.plugin.movingheads.utils.withPrefix

class MovingHeadAnimationNodeRemoveCommand :
    AbstractTargetPlayerCommand("remove", "server.movingheads.animationnode.remove") {
    private val animationNodeNameArg: RequiredArg<String> =
        withRequiredArg<String>("animationNodeName", "server.movingheads.arg.animationnode.name", ArgTypes.STRING)

    private val stateFrameNameArg: RequiredArg<String> =
        withRequiredArg<String>("stateFrameName", "server.movingheads.arg.stateframe.name", ArgTypes.STRING)

    var configManager: ConfigurationUtil = MovingHeadsPlugin.INSTANCE.config

    override fun execute(
        commandContext: CommandContext,
        sourceRef: Ref<EntityStore?>?,
        ref: Ref<EntityStore?>,
        playerRef: PlayerRef,
        world: World,
        store: Store<EntityStore?>
    ) {
        val animationNodeName = commandContext.get<String>(animationNodeNameArg)
        val stateFrameName = commandContext.get<String>(stateFrameNameArg)

        val config = configManager.load<MovingHeadConfig>()
        val animationNode = config.getAnimationNode(playerRef.uuid, animationNodeName)
        if (animationNode == null) {
            // TODO Error Message
            return
        }

        animationNode.stateFrames.remove(stateFrameName)
        configManager.save(config)

        commandContext.sendMessage(
            Message.translation("server.movingheads.animationnode.removed")
                .param("stateFrameName", stateFrameName)
                .param("animationNodeName", animationNodeName)
                .withPrefix()
        )
    }
}