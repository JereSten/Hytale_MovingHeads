package de.jerst.plugin.movingheads.commands.animationnode

import com.hypixel.hytale.component.Ref
import com.hypixel.hytale.component.Store
import com.hypixel.hytale.math.vector.Vector3i
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
import de.jerst.plugin.movingheads.model.SoundEvent
import de.jerst.plugin.movingheads.utils.ConfigurationUtil
import de.jerst.plugin.movingheads.utils.MessageUtil
import de.jerst.plugin.movingheads.utils.withErrorPrefix
import de.jerst.plugin.movingheads.utils.withPrefix

class MovingHeadAnimationNodeAddSoundCommand :
    AbstractTargetPlayerCommand("addsound", "server.movingheads.animationnode.addsound") {
    private val animationNodeNameArg: RequiredArg<String> =
        withRequiredArg<String>("animationNodeName", "server.movingheads.arg.animationnode.soundevent", ArgTypes.STRING)

    private val soundNameArg: RequiredArg<String> =
        withRequiredArg<String>("soundName", "server.movingheads.arg.stateframe.add.name", ArgTypes.STRING)

    private val positionArg = withRequiredArg<Vector3i>(
        "position",
        "server.commands.sound.play3d.position.desc",
        ArgTypes.VECTOR3I
    )

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
        val soundName = commandContext.get<String>(soundNameArg)
        val soundPosition = commandContext.get<Vector3i>(positionArg);

        val config = configManager.load<MovingHeadConfig>()

        val animationNode = config.getAnimationNode(playerRef.uuid, animationNodeName)
        if (animationNode == null) {
            commandContext.sendMessage(
                Message.translation("server.movingheads.animationnode.notfound")
                    .param("animationNodeName", animationNodeName)
                    .withErrorPrefix()
            )
            return
        }

        animationNode.soundEvent = SoundEvent(soundName, soundPosition)
        configManager.save(config)

        commandContext.sendMessage(
            Message.translation("server.movingheads.animationnode.soundadded")
                .param("animationNodeName", animationNodeName)
                .withPrefix()
        )
    }
}