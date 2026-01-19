package de.jerst.plugin.movingheads.commands

import com.hypixel.hytale.component.Ref
import com.hypixel.hytale.component.Store
import com.hypixel.hytale.math.util.ChunkUtil
import com.hypixel.hytale.math.vector.Vector3i
import com.hypixel.hytale.server.core.Message
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType
import com.hypixel.hytale.server.core.command.system.CommandContext
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractTargetPlayerCommand
import com.hypixel.hytale.server.core.universe.PlayerRef
import com.hypixel.hytale.server.core.universe.world.World
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore
import de.jerst.plugin.movingheads.MovingHeadsPlugin
import de.jerst.plugin.movingheads.utils.ConfigurationUtil
import de.jerst.plugin.movingheads.utils.config.MovingHeadConfig
import de.jerst.plugin.movingheads.utils.withErrorPrefix
import de.jerst.plugin.movingheads.utils.withPrefix
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.annotation.Nonnull

class MovingHeadPlayCommand : AbstractTargetPlayerCommand("play", "server.movingheads.scenegroup.state.set") {
    @Nonnull
    private val nameArg: RequiredArg<String?> =
        withRequiredArg<String?>("name", "server.movingheads.scenegroup.name", ArgTypes.STRING)
    private val stateArg: RequiredArg<String?> = withRequiredArg<String?>("state", "", ArgTypes.STRING)

    var configManager: ConfigurationUtil = MovingHeadsPlugin.INSTANCE.config

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
            commandContext.sendMessage(Message.translation("server.movingheads.scenegroup.warning.noname").withErrorPrefix())
            return
        }

        val config = configManager.load<MovingHeadConfig>()
        val sceneGroup = config.getOrCreateSceneGroup(playerRef.uuid, name)
        if (sceneGroup == null) {
            commandContext.sendMessage(Message.translation("server.movingheads.scenegroup.warning").withErrorPrefix())
            return
        }

        CoroutineScope(Dispatchers.Default).launch {
                for (block in sceneGroup.blocks!!) {
                    delay(1000L)
                    setState(block, world, state)
                }
        }

        commandContext.sendMessage(Message.translation("server.movingheads.scenegroup.state.set.success").withPrefix())
    }

    /**
     * Set state of block at position
     * @param position Block position
     */
    private fun setState(position: Vector3i, world: World, stateName: String) {
        val chunckIndex = ChunkUtil.indexChunkFromBlock(position.x, position.z)

        world.getChunkAsync(chunckIndex).thenAccept { chunk ->
            val blockId: Int = chunk.getBlock(position)

            val blockType = BlockType.getAssetMap().getAsset(blockId)
            if (blockType != null) {
                chunk.setBlockInteractionState(position, blockType, stateName)
            }
         }
    }
}