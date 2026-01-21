package de.jerst.plugin.movingheads.model

import java.util.UUID

data class StateFrame (
    val playerUUID: UUID,
    val name: String,
    val sceneGroupName: String,
    val state: String,
    val iterations: Int = 1
)