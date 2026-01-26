package de.jerst.plugin.movingheads.model

import com.hypixel.hytale.math.vector.Vector3i
import java.util.UUID

data class AnimationNode (
    val playerUUID: UUID,
    val name: String,
    val wait: Long?,
    val stateFrames: MutableList<String> = mutableListOf(),
    var soundEvent: SoundEvent?
)

data class SoundEvent(
    val soundName: String,
    var blocks: Vector3i
)