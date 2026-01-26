package de.jerst.plugin.movingheads.utils

import java.awt.Color
import com.hypixel.hytale.server.core.Message

class MessageUtil {
    companion object {
        /**
         * Formatted Plugin Message
         */
        fun pluginMessage(message: String): Message {
            return Message.join(
                Message.raw("[MovingHeads] ").color(Color.CYAN),
                Message.raw(message).color(Color.WHITE)
            );
        }

        /**
         * Formatted Plugin Message
         */
        fun pluginTMessage(message: Message): Message {
            return Message.join(
                Message.raw("[MovingHeads] ").color(Color.CYAN),
                message
            );
        }
    }
}

/**
 * Add plugin prefix
 */
fun Message.withPrefix(): Message = Message.join(
    Message.raw("[MovingHeads] ").color(Color.CYAN),
    this.color(Color.WHITE)
)

/**
 * Add plugin prefix
 */
fun Message.withErrorPrefix(): Message = Message.join(
    Message.raw("[MovingHeads] ").color(Color.CYAN),
    this.color(Color.RED)
)