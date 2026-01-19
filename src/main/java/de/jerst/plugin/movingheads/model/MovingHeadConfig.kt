package de.jerst.plugin.movingheads.model

import java.util.UUID

data class MovingHeadConfig(
    val sceneGroups: MutableList<SceneGroup> = mutableListOf(),
    val animationTracks: MutableList<AnimationTrack> = mutableListOf()
) {
    fun getOrCreateSceneGroup(playerUid: UUID, name: String): SceneGroup? {
        return sceneGroups.find { it.name == name && it.playerUUID == playerUid}?.apply {
            if (blocks == null) blocks = mutableListOf()
        }
    }

    fun getSceneGroups(playerUid: UUID): List<SceneGroup> {
        return sceneGroups.filter { it.playerUUID == playerUid}
    }

    fun getAnimation(playerUid: UUID, name: String): AnimationTrack? {
        return animationTracks.find { it.playerUUID == playerUid}
    }

    fun getAnimations(playerUid: UUID): List<AnimationTrack> {
        return animationTracks.filter { it.playerUUID == playerUid}
    }
}