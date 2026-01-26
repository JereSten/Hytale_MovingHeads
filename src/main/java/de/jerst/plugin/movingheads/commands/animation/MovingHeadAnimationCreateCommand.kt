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
import de.jerst.plugin.movingheads.model.AnimationTrack
import de.jerst.plugin.movingheads.model.MovingHeadConfig
import de.jerst.plugin.movingheads.utils.ConfigurationUtil
import de.jerst.plugin.movingheads.utils.MessageUtil
import de.jerst.plugin.movingheads.utils.withPrefix
import javax.annotation.Nonnull

class MovingHeadAnimationCreateCommand : AbstractTargetPlayerCommand("create", "server.movingheads.animation.create") {

    @Nonnull
    private val nameArg: RequiredArg<String> =
        withRequiredArg<String>("name", "server.movingheads.arg.animation.name", ArgTypes.STRING)

    var configManager: ConfigurationUtil = MovingHeadsPlugin.INSTANCE.config

    override fun execute(
        commandContext: CommandContext,
        sourceRef: Ref<EntityStore?>?,
        ref: Ref<EntityStore?>,
        playerRef: PlayerRef,
        world: World,
        store: Store<EntityStore?>
    ) {
        val name = commandContext.get<String?>(nameArg)

        if (name == null) {
            commandContext.sendMessage(MessageUtil.pluginMessage("Name missing"))
            return
        }

        val newSceneryGroup = AnimationTrack(playerRef.uuid, name)

        val config = configManager.load<MovingHeadConfig>().apply {
            animationTracks.add(newSceneryGroup)
        }
        configManager.save(config)

        commandContext.sendMessage(
                Message.translation("server.movingheads.animation.created")
                    .param("name", name)
                    .withPrefix()
        )
    }
}