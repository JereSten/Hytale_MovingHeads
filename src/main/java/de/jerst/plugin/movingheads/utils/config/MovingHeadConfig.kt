package de.jerst.plugin.movingheads.utils.config

import java.util.UUID

data class MovingHeadConfig(
    val sceneGroup: MutableList<SceneGroup> = mutableListOf()
) {
    fun getOrCreateSceneGroup(playerUid: UUID, name: String): SceneGroup? {
        return sceneGroup.find { it.name == name && it.playerUUID == playerUid}?.apply {
            if (blocks == null) blocks = mutableListOf()
        }
    }

    fun getSceneGroups(playerUid: UUID): List<SceneGroup> {
        return sceneGroup.filter { it.playerUUID == playerUid}
    }
}