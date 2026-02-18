package de.jerst.plugin.movingheads.commands.scenegroup

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
import de.jerst.plugin.movingheads.utils.ConfigurationUtil
import de.jerst.plugin.movingheads.utils.MovingHeadCommandUtil.Companion.validateAndGetTarget
import de.jerst.plugin.movingheads.model.MovingHeadConfig
import de.jerst.plugin.movingheads.utils.withErrorPrefix
import de.jerst.plugin.movingheads.utils.withPrefix
import javax.annotation.Nonnull

/**
 * Remove Block from scenery group
 */
class MovingHeadSceneGroupRemoveCommand:
    AbstractTargetPlayerCommand("remove", "server.movingheads.scenegroup.remove") {

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
        val targetBlock = validateAndGetTarget(commandContext, name, store) ?: return

        val config = configManager.load<MovingHeadConfig>()
        val sceneGroup = config.getOrCreateSceneGroup( playerRef.uuid, name!!)
        if (sceneGroup == null) {
            commandContext.sendMessage(
                Message.translation("server.movingheads.scenegroup.nonexistent").withErrorPrefix()
            )
            return
        }

        if (sceneGroup.blocks != null) {
            val elementToRemove =
                sceneGroup.blocks!!.find { it.x == targetBlock.x && it.y == targetBlock.y && it.z == targetBlock.z }
            sceneGroup.blocks!!.remove(elementToRemove)
        }
        configManager.save(config)
        commandContext.sendMessage(
            Message.translation("server.movingheads.scenegroup.block.remove")
                .param("x", targetBlock.x)
                .param("y", targetBlock.y)
                .param("z", targetBlock.z)
                .withPrefix()
        )
    }
}