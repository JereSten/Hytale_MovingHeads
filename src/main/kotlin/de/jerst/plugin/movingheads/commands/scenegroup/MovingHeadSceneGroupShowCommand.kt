package de.jerst.plugin.movingheads.commands.scenegroup

import com.hypixel.hytale.component.Ref
import com.hypixel.hytale.component.Store
import com.hypixel.hytale.server.core.Message
import com.hypixel.hytale.server.core.command.system.CommandContext
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractTargetPlayerCommand
import com.hypixel.hytale.server.core.universe.PlayerRef
import com.hypixel.hytale.server.core.universe.world.ParticleUtil
import com.hypixel.hytale.server.core.universe.world.World
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore
import de.jerst.plugin.movingheads.MovingHeadsPlugin
import de.jerst.plugin.movingheads.utils.ConfigurationUtil
import de.jerst.plugin.movingheads.model.MovingHeadConfig
import de.jerst.plugin.movingheads.utils.withErrorPrefix
import de.jerst.plugin.movingheads.utils.withPrefix
import org.joml.Vector3d
import javax.annotation.Nonnull


/**
 * Show selected blocks of scene group using particles
 */
class MovingHeadSceneGroupShowCommand:
    AbstractTargetPlayerCommand("show", "server.movingheads.scenegroup.show") {

    @Nonnull
    private val nameArg: RequiredArg<String?> =
        withRequiredArg<String?>("name", "server.movingheads.scenegroup.name", ArgTypes.STRING)

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
            commandContext.sendMessage(
                Message.translation("server.movingheads.scenegroup.warning.noname").withErrorPrefix()
            )
            return
        }

        val config = configManager.load<MovingHeadConfig>()
        val sceneGroup = config.getOrCreateSceneGroup(playerRef.uuid, name)
        if (sceneGroup == null) {
            commandContext.sendMessage(
                Message.translation("server.movingheads.scenegroup.nonexistent").withErrorPrefix()
            )
            return
        }

        for (block in sceneGroup.blocks!!) {
            val particlePos = Vector3d(
                block.x().toDouble() + 0.5,
                block.y().toDouble() + 0.5,
                block.z().toDouble() + 0.5
            )

            ParticleUtil.spawnParticleEffect("MovingHead_Block_Indicator", particlePos, store)
        }

        commandContext.sendMessage(
            Message.translation("server.movingheads.scenegroup.show").withPrefix()
        )
    }
}