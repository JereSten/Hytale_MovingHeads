package de.jerst.plugin.movingheads.commands

import com.hypixel.hytale.component.Ref
import com.hypixel.hytale.component.Store
import com.hypixel.hytale.server.core.Message
import com.hypixel.hytale.server.core.command.system.CommandContext
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractTargetPlayerCommand
import com.hypixel.hytale.server.core.universe.PlayerRef
import com.hypixel.hytale.server.core.universe.world.World
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore
import de.jerst.plugin.movingheads.utils.withPrefix

class MovingHeadHelpCommand : AbstractTargetPlayerCommand("help", "server.mh.commands.help") {
    override fun execute(
        commandContext: CommandContext,
        sourceRef: Ref<EntityStore?>?,
        ref: Ref<EntityStore?>,
        playerRef: PlayerRef,
        world: World,
        store: Store<EntityStore?>
    ) {
        commandContext.sendMessage(
            Message.translation("server.mh.commands.help.text")
                .withPrefix()
        )
    }
}