package de.jerst.plugin.movingheads.model


import com.hypixel.hytale.math.vector.Vector3i
import java.util.*

data class SceneGroup(
    val name: String,
    val playerUUID: UUID,
    var blocks: MutableList<Vector3i>? = mutableListOf()
)
