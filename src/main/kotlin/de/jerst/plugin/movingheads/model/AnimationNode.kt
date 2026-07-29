package de.jerst.plugin.movingheads.model

import org.joml.Vector3i
import java.util.UUID

data class AnimationNode (
    val playerUUID: UUID,
    var name: String,
    var wait: Long?,
    var stateFrames: MutableList<String> = mutableListOf(),
    var soundEvent: SoundEvent?
)

data class SoundEvent(
    val soundName: String,
    var blocks: Vector3i
)