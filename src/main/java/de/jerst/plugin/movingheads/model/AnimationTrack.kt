package de.jerst.plugin.movingheads.model

import java.util.*

data class AnimationTrack(
    val playerUUID: UUID,
    val name: String,
    val animationNodes: MutableList<AnimationNode> = mutableListOf()
)
