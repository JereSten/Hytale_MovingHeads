package de.jerst.plugin.movingheads.model

import java.util.UUID

data class MovingHeadConfig(
    val sceneGroups: MutableList<SceneGroup> = mutableListOf(),
    val animationTracks: MutableList<AnimationTrack> = mutableListOf(),
    val stateFrames: MutableList<StateFrame> = mutableListOf(),
    val animationNodes: MutableList<AnimationNode> = mutableListOf()
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
        return animationTracks.find { it.playerUUID == playerUid && it.name == name}
    }

    fun getAnimations(playerUid: UUID): List<AnimationTrack> {
        return animationTracks.filter { it.playerUUID == playerUid}
    }

    fun getAnimationNode(playerUid: UUID, name: String): AnimationNode? {
        return animationNodes.find { it.playerUUID == playerUid && it.name == name}
    }

    fun getAnimationNode(playerUid: UUID): List<AnimationNode> {
        return animationNodes.filter { it.playerUUID == playerUid}
    }

    fun getStateFrame(playerUid: UUID, name: String): StateFrame? {
        return stateFrames.find { it.playerUUID == playerUid && it.name == name}
    }

    fun getStateFrames(playerUid: UUID): List<StateFrame> {
        return stateFrames.filter { it.playerUUID == playerUid}
    }
}