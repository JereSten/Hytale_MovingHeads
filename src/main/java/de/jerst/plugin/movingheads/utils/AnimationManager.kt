package de.jerst.plugin.movingheads.utils

import com.hypixel.hytale.server.core.Message
import com.hypixel.hytale.server.core.command.system.CommandContext
import com.hypixel.hytale.server.core.universe.world.World
import de.jerst.plugin.movingheads.model.AnimationTrack
import de.jerst.plugin.movingheads.model.MovingHeadConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import java.util.UUID

// Manager-Klasse für player-spezifische Jobs (Singleton oder in Plugin-Context)
object AnimationManager {
    private val playerJobs = mutableMapOf<UUID, Job>()

    fun startAnimation(config: MovingHeadConfig, commandContext: CommandContext, animation: AnimationTrack, world: World, player: UUID) {
        if (playerJobs.contains(player)) {
            commandContext.sendMessage(Message.translation("Du kannst nur eine Animation ausführen!").withErrorPrefix())
            return
        }


        commandContext.sendMessage(
            Message.translation("Playing animation").param("animnation", animation.name).withPrefix()
        )

        val job = CoroutineScope(Dispatchers.Default + SupervisorJob()).launch {
            try {
                for (animationNode in animation.animationNodes) {
                    ensureActive() // Cancellation check vor blocking calls
                    if (animationNode.sceneGroupName != null && animationNode.state != null && animationNode.wait == null) {
                        val sceneGroup = config.getOrCreateSceneGroup(player, animationNode.sceneGroupName)
                        if (sceneGroup != null) {
                            repeat(animationNode.iterations) {
                                ensureActive()
                                SceneGroupUtil.play(sceneGroup, animationNode.state, world)
                                delay(getStateDuration(animationNode.state))
                            }
                        }
                    }
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
