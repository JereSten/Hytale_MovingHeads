package de.jerst.plugin.movingheads.utils

import com.hypixel.hytale.server.core.Message
import com.hypixel.hytale.server.core.command.system.CommandContext
import com.hypixel.hytale.server.core.universe.world.World
import de.jerst.plugin.movingheads.model.AnimationNode
import de.jerst.plugin.movingheads.model.AnimationTrack
import de.jerst.plugin.movingheads.model.MovingHeadConfig
import kotlinx.coroutines.*
import java.util.*

// Manager-Klasse für player-spezifische Jobs (Singleton oder in Plugin-Context)
object AnimationManager {
    private val playerJobs = mutableMapOf<UUID, Job>()

    fun startAnimation(
        config: MovingHeadConfig,
        commandContext: CommandContext,
        animation: AnimationTrack,
        world: World,
        player: UUID
    ) {
        if (playerJobs.contains(player)) {
            commandContext.sendMessage(Message.translation("Du kannst nur eine Animation ausführen!").withErrorPrefix())
            return
        }

        commandContext.sendMessage(
            Message.translation("Playing animation").param("animnation", animation.name).withPrefix()
        )

        val job = CoroutineScope(Dispatchers.Default + SupervisorJob()).launch {
            try {
                for (animationNodeName in animation.animationNodes) {
                    val animationNode = config.getAnimationNode(player, animationNodeName) ?: continue

                    playAnimationNode(animationNode, config, player, world)

                    delay(getAnimationNodeDuration(animationNode, config, player))

                    if (animationNode.wait != null) {
                        delay(animationNode.wait * 1000L)
                    }
                }
                commandContext.sendMessage(Message.translation("Animation finished").withPrefix())
            } finally {
                // Cleanup falls nötig
            }

            playerJobs.remove(player)
        }
        // Vorherigen stoppen falls vorhanden
        playerJobs[player]?.cancel()
        playerJobs[player] = job
    }

    suspend fun playAnimationNode(animationNode: AnimationNode, config: MovingHeadConfig, player: UUID, world: World) {
        coroutineScope {
            for (stateFrameName in animationNode.stateFrames) {
                val stateFrame = config.getStateFrame(player, stateFrameName) ?: continue
                val sceneGroup = config.getOrCreateSceneGroup(player, stateFrame.sceneGroupName) ?: continue

                launch {
                    repeat(stateFrame.iterations) {
                        SceneGroupUtil.play(sceneGroup, stateFrame.state, world)
                        delay(getStateDuration(stateFrame.state))
                    }
                }
            }
        }
    }

    private fun getAnimationNodeDuration(animationNode: AnimationNode, config: MovingHeadConfig, player: UUID): Long {
        if (animationNode.wait != null) {
            return animationNode.wait
        }

        val stateFrames = config.stateFrames.filter { animationNode.stateFrames.contains(it.name) }
        val durations = stateFrames.map { getStateDuration(it.name) * it.iterations }

        return durations.max()
    }

    fun stopAnimation(player: UUID) {
        playerJobs[player]?.cancel()
        playerJobs.remove(player)
    }

    fun stopAll() {
        playerJobs.values.forEach { it.cancel() }
        playerJobs.clear()
    }

    fun getStateDuration(stage: String): Long {
        when (stage) {
            "On" -> return 4000L
            else -> {}
        }
        return 0
    }
}
