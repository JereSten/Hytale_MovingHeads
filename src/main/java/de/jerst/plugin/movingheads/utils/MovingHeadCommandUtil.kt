package de.jerst.plugin.movingheads.utils

import com.hypixel.hytale.component.Store
import com.hypixel.hytale.math.vector.Vector3i
import com.hypixel.hytale.server.core.Message
import com.hypixel.hytale.server.core.command.system.CommandContext
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore
import com.hypixel.hytale.server.core.util.TargetUtil

class MovingHeadCommandUtil {
    companion object {
        /**
         * Validate user input and get target block looking at
         */
        fun validateAndGetTarget(commandContext: CommandContext, name: String?, store: Store<EntityStore?>): Vector3i? {
            if (name == null) {
                commandContext.sendMessage(
                    Message.translation("server.movingheads.scenegroup.warning.noname").withErrorPrefix()
                )
                return null
            }

            if (!commandContext.isPlayer) {
                commandContext.sendMessage(Message.translation("server.movingheads.warning.player").withErrorPrefix())
                return null
            }

            val targetBlock = commandContext.senderAsPlayerRef()?.let { TargetUtil.getTargetBlock(it, 10.0, store) }
            if (targetBlock == null) {
                commandContext.sendMessage(Message.translation("server.movingheads.warning.noblock").withErrorPrefix())
                return null
            }
            return targetBlock
        }
    }
}