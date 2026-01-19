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

class MovingHeadAnimationNodeAddCommand : AbstractTargetPlayerCommand("add", "server.movingheads.scenegroup.manage") {

    private val animationNameArg: RequiredArg<String> =
        withRequiredArg<String>("animationname", "server.movingheads.scenegroup.name", ArgTypes.STRING)

    private val nameArg: RequiredArg<String> =
        withRequiredArg<String>("name", "server.movingheads.scenegroup.name", ArgTypes.STRING)

    private val sceneGroupNameArg: RequiredArg<String> =
        withRequiredArg<String>("sceneGroupName", "server.movingheads.scenegroup.name", ArgTypes.STRING)

    private val stateNameArg: RequiredArg<String> =
        withRequiredArg<String>("stateName", "server.movingheads.scenegroup.name", ArgTypes.STRING)

    private val waitArg: OptionalArg<Int> =
        withOptionalArg<Int>("wait", "server.movingheads.scenegroup.name", ArgTypes.INTEGER)

    private val iterationsArg: OptionalArg<Int> =
        withOptionalArg<Int>("iteration", "server.movingheads.scenegroup.name", ArgTypes.INTEGER)

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
        val name = commandContext.get<String>(nameArg)
        val sceneGroupName = commandContext.get<String>(sceneGroupNameArg)
        val stateName = commandContext.get<String>(stateNameArg)
        val wait = commandContext.get<Int?>(waitArg)
        val iterations = commandContext.get<Int?>(iterationsArg)

        if (name == null) {
            commandContext.sendMessage(MessageUtil.pluginMessage("Name missing"))
            return
        }
        val config = configManager.load<MovingHeadConfig>()

        val animation = config.getAnimation(playerRef.uuid, animationName)
        if (animation != null) {
            val newAnimationNode = AnimationNode(name, sceneGroupName, stateName, wait, iterations ?: 1)
            animation.animationNodes.add(newAnimationNode)
        }

        configManager.save(config)

        commandContext.sendMessage(
            MessageUtil.pluginTMessage(
                Message.translation("server.movingheads.scenegroup.created").param("name", name)
            )
        )
    }
}