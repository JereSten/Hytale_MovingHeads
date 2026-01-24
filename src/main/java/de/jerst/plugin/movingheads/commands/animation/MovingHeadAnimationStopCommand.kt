package de.jerst.plugin.movingheads.commands.animation

import com.hypixel.hytale.component.Ref
import com.hypixel.hytale.component.Store
import com.hypixel.hytale.server.core.Message
import com.hypixel.hytale.server.core.command.system.CommandContext
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractTargetPlayerCommand
import com.hypixel.hytale.server.core.universe.PlayerRef
import com.hypixel.hytale.server.core.universe.world.World
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore
import de.jerst.plugin.movingheads.MovingHeadsPlugin
import de.jerst.plugin.movingheads.utils.AnimationManager
import de.jerst.plugin.movingheads.utils.ConfigurationUtil
import de.jerst.plugin.movingheads.utils.withPrefix

class MovingHeadAnimationStopCommand : AbstractTargetPlayerCommand("stop", "server.movingheads.animation.stop") {

    var configManager: ConfigurationUtil = MovingHeadsPlugin.INSTANCE.config

    override fun execute(
        commandContext: CommandContext,
        sourceRef: Ref<EntityStore?>?,
        ref: Ref<EntityStore?>,
        playerRef: PlayerRef,
        world: World,
        store: Store<EntityStore?>
    ) {
        AnimationManager.stopAnimation(playerRef.uuid)

        commandContext.sendMessage(
            Message.translation("server.movingheads.animation.stopped").withPrefix()
        )
    }
}