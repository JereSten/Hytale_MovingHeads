package de.jerst.plugin.movingheads.commands.animation

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
import de.jerst.plugin.movingheads.utils.withPrefix

class MovingHeadAnimationRemoveCommand : AbstractTargetPlayerCommand("remove", "server.movingheads.animation.remove") {

    private val animationNameArg: RequiredArg<String> =
        withRequiredArg<String>("animationName", "server.movingheads.arg.animation.name", ArgTypes.STRING)

    private val animationNodeNameArg: RequiredArg<String> =
        withRequiredArg<String>("animationNodeName", "server.movingheads.arg.animationnode.name", ArgTypes.STRING)

    var configManager: ConfigurationUtil = MovingHeadsPlugin.INSTANCE.config

    override fun execute(
        commandContext: CommandContext,
        sourceRef: Ref<EntityStore?>?,
        ref: Ref<EntityStore?>,
        playerRef: PlayerRef,
        world: World,
        store: Store<EntityStore?>
    ) {
        val animationName = commandContext.get<String>(animationNameArg)
        val animationNodeName = commandContext.get<String>(animationNodeNameArg)

        val config = configManager.load<MovingHeadConfig>()
        val animation = config.getAnimation(playerRef.uuid, animationName)
        if (animation == null) {
            // TODO message
            return
        }

        animation.animationNodes.remove(animationNodeName)
        configManager.save(config)

        commandContext.sendMessage(
                Message.translation("server.movingheads.scenegroup.created").param("animationName", animationName)
                    .param("animationNodeName", animationNodeName)
                    .withPrefix()
        )
    }
}