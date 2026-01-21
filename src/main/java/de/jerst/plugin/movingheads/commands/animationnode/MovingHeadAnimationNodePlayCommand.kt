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
import de.jerst.plugin.movingheads.utils.AnimationManager
import de.jerst.plugin.movingheads.utils.ConfigurationUtil
import de.jerst.plugin.movingheads.utils.MessageUtil
import de.jerst.plugin.movingheads.utils.SceneGroupUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MovingHeadAnimationNodePlayCommand: AbstractTargetPlayerCommand("play", "server.movingheads.scenegroup.manage") {
    private val animationNodeNameArg: RequiredArg<String> =
        withRequiredArg<String>("animationnodename", "server.movingheads.scenegroup.name", ArgTypes.STRING)

    var configManager: ConfigurationUtil = MovingHeadsPlugin.INSTANCE.config

    override fun execute(
        commandContext: CommandContext,
        sourceRef: Ref<EntityStore?>?,
        ref: Ref<EntityStore?>,
        playerRef: PlayerRef,
        world: World,
        store: Store<EntityStore?>
    )  {
        val animationNodeName = commandContext.get<String>(animationNodeNameArg)

        val config = configManager.load<MovingHeadConfig>()
        val animationNode = config.getAnimationNode(playerRef.uuid, animationNodeName)
        if (animationNode == null) {
            // TODO Error Message
            return
        }

        CoroutineScope(Dispatchers.Default).launch {
            AnimationManager.playAnimationNode(animationNode, config, playerRef.uuid, world)
        }

        // TODO message
    }
}