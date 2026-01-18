package de.jerst.plugin.movingheads.commands

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
import com.hypixel.hytale.server.core.util.TargetUtil
import de.jerst.plugin.movingheads.MovingHeadsPlugin
import de.jerst.plugin.movingheads.utils.ConfigurationUtil
import de.jerst.plugin.movingheads.utils.MessageUtil
import de.jerst.plugin.movingheads.utils.config.MovingHeadConfig
import de.jerst.plugin.movingheads.utils.config.SceneGroup
import de.jerst.plugin.movingheads.utils.withErrorPrefix
import de.jerst.plugin.movingheads.utils.withPrefix
import javax.annotation.Nonnull

class MovingHeadSceneGroupCommand :
    AbstractTargetPlayerCommand("scenegroup", "server.movingheads.scenegroup.manage") {
    @Nonnull
    private val actionArg: RequiredArg<String?> =
        withRequiredArg<String?>("action", "server.movingheads.action", ArgTypes.STRING)

    @Nonnull
    private val nameArg: RequiredArg<String?> =
        withRequiredArg<String?>("name", "server.movingheads.scenegroup.name", ArgTypes.STRING)
    var configManager: ConfigurationUtil = MovingHeadsPlugin.INSTANCE.config

    init {
        addAliases("sc")

    }

    override fun execute(
        commandContext: CommandContext,
        sourceRef: Ref<EntityStore?>?,
        ref: Ref<EntityStore?>,
        playerRef: PlayerRef,
        world: World,
        store: Store<EntityStore?>
    ) {
        val action = commandContext.get<String?>(actionArg)

        when (action) {
            "create" -> createSceneGroup(commandContext, playerRef)
            "add" -> addBlock(commandContext, playerRef, store)
        }
    }

    fun addBlock(commandContext: CommandContext, playerRef: PlayerRef, store: Store<EntityStore?>) {
        val name = commandContext.get<String?>(nameArg)

        if (!commandContext.isPlayer) {
            commandContext.sendMessage(Message.translation("server.movingheads.warning.player").withErrorPrefix())
            return
        }

        val targetBlock = commandContext.senderAsPlayerRef()?.let { TargetUtil.getTargetBlock(it, 10.0, store) }
        if (targetBlock == null) {
            commandContext.sendMessage(Message.translation("server.movingheads.warning.noblock").withErrorPrefix())
            return
        }

        val config = configManager.load<MovingHeadConfig>()
        var scenegroup = config.sceneGroup.find { it.name == name }
        if (scenegroup?.blocks == null) {
            scenegroup!!.blocks = mutableListOf()
        }

        scenegroup.blocks?.add(targetBlock)
        configManager.save(config)

        commandContext.sendMessage(
            Message.translation("server.movingheads.scenegroup.block.add")
                .param("x", targetBlock.x)
                .param("y", targetBlock.y)
                .param("z", targetBlock.z)
                .withPrefix()
        )
    }

    /**
     * Create new scene group
     */
    fun createSceneGroup(commandContext: CommandContext, playerRef: PlayerRef) {
        val name = commandContext.get<String?>(nameArg)

        if (name == null) {
            commandContext.sendMessage(MessageUtil.pluginMessage("Name missing"))
            return
        }

        val newSceneryGroup = SceneGroup(name, playerRef.uuid)

        val config = configManager.load<MovingHeadConfig>().apply {
            sceneGroup.add(newSceneryGroup)
        }
        configManager.save(config)

        commandContext.sendMessage(
            MessageUtil.pluginTMessage(
                Message.translation("server.movingheads.scenegroup.created").param("name", name)
            )
        )
    }
}