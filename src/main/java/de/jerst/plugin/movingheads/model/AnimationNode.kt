package de.jerst.plugin.movingheads.model

data class AnimationNode (
    val name: String,
    val sceneGroupName: String?,
    val state: String?,
    val wait: Int?,
    val iterations: Int = 1
)