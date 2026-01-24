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
import de.jerst.plugin.movingheads.model.MovingHeadConfig
import de.jerst.plugin.movingheads.utils.ConfigurationUtil
import de.jerst.plugin.movingheads.utils.SceneGroupUtil
import de.jerst.plugin.movingheads.utils.withErrorPrefix
import de.jerst.plugin.movingheads.utils.withPrefix
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.annotation.Nonnull

class MovingHeadSceneGroupPlayCommand : AbstractTargetPlayerCommand("play", "server.movingheads.scenegroup.play") {
    @Nonnull
    private val nameArg: RequiredArg<String?> =
        withRequiredArg<String?>("name", "server.movingheads.scenegroup.name", ArgTypes.STRING)
    private val stateArg: RequiredArg<String?> = withRequiredArg<String?>("state", "", ArgTypes.STRING)

    var configManager: ConfigurationUtil = MovingHeadsPlugin.Companion.INSTANCE.config

    /**
     * Set state to scenery group
     */
    override fun execute(
        commandContext: CommandContext,
        sourceRef: Ref<EntityStore?>?,
        ref: Ref<EntityStore?>,
        playerRef: PlayerRef,
        world: World,
        store: Store<EntityStore?>
    ) {
        val name = commandContext.get<String?>(nameArg)
        val state = commandContext.get<String?>(stateArg)

        if (name == null || state == null) {
            commandContext.sendMessage(
                Message.translation("server.movingheads.scenegroup.warning.noname").withErrorPrefix()
            )
            return
        }

        val config = configManager.load<MovingHeadConfig>()
        val sceneGroup = config.getOrCreateSceneGroup(playerRef.uuid, name)
        if (sceneGroup == null) {
            commandContext.sendMessage(Message.translation("server.movingheads.scenegroup.warning").withErrorPrefix())
            return
        }

        CoroutineScope(Dispatchers.Default).launch {
            SceneGroupUtil.Companion.play(sceneGroup, state, world)
        }

        commandContext.sendMessage(
            Message.translation("server.movingheads.scenegroup.playing")
                .param("name", name)
                .withPrefix()
        )
    }
}