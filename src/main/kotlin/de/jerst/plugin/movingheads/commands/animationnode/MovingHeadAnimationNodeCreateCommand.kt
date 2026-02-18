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
import de.jerst.plugin.movingheads.utils.withPrefix

class MovingHeadAnimationNodeCreateCommand :
    AbstractTargetPlayerCommand("create", "server.movingheads.animationnode.create") {

    private val animationNodeNameArg: RequiredArg<String> =
        withRequiredArg<String>("animationNodeName", "server.movingheads.arg.animationnode.name", ArgTypes.STRING)

    private val waitArg: OptionalArg<Int?> =
        withOptionalArg<Int?>("wait", "server.movingheads.arg.animationnode.wait", ArgTypes.INTEGER)

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
            commandContext.sendMessage(
                Message.translation("server.movingheads.animationnode.notfound")
                    .param("animationNodeName", name).withPrefix()
            )
            return
        }

        val newAnimationNode = AnimationNode(playerRef.uuid, name, wait, mutableListOf(), null)
        config.animationNodes.add(newAnimationNode)

        configManager.save(config)

        commandContext.sendMessage(
            Message.translation("server.movingheads.animationnode.created")
                .param("animationNodeName", name).withPrefix()
        )
    }
}