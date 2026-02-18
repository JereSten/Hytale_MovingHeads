package de.jerst.plugin.movingheads.model

import java.util.UUID

data class StateFrame (
    val playerUUID: UUID,
    var name: String,
    var sceneGroupName: String,
    var state: String,
    var iterations: Int = 1
)