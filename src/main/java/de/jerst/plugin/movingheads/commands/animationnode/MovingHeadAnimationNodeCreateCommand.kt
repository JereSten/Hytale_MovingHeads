package de.jerst.plugin.movingheads.commands.animationnode

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
import de.jerst.plugin.movingheads.utils.ConfigurationUtil
import de.jerst.plugin.movingheads.utils.MessageUtil

class MovingHeadAnimationNodeCreateCommand : AbstractTargetPlayerCommand("create", "server.movingheads.scenegroup.manage") {

    private val animationNodeNameArg: RequiredArg<String> =
        withRequiredArg<String>("animationnodename", "server.movingheads.scenegroup.name", ArgTypes.STRING)

    private val waitArg: OptionalArg<Int?> =
        withOptionalArg<Int?>("wait", "server.movingheads.scenegroup.name", ArgTypes.INTEGER)


    var configManager: ConfigurationUtil = MovingHeadsPlugin.INSTANCE.config

    override fun execute(
        commandContext: CommandContext,
        sourceRef: Ref<EntityStore?>?,
        ref: Ref<EntityStore?>,
        playerRef: PlayerRef,
        world: World,
        store: Store<EntityStore?>
    ) {
        val name = commandContext.get<String>(animationNodeNameArg)
        val wait = commandContext.get<Int?>(waitArg)?.toLong()

        val config = configManager.load<MovingHeadConfig>()

        val animationNode = config.getAnimationNode(playerRef.uuid, name)
        if (animationNode != null) {
            // TODO Error Message
            return
        }

        val newAnimationNode = AnimationNode(playerRef.uuid, name, wait)
        config.animationNodes.add(newAnimationNode)

        configManager.save(config)

        commandContext.sendMessage(
            MessageUtil.pluginTMessage(
                Message.translation("server.movingheads.scenegroup.created").param("name", name)
            )
        )
    }
}