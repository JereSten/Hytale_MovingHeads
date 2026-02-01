package de.jerst.plugin.movingheads.utils

import com.hypixel.hytale.component.ComponentAccessor
import com.hypixel.hytale.protocol.SoundCategory
import com.hypixel.hytale.server.core.Message
import com.hypixel.hytale.server.core.asset.type.soundevent.config.SoundEvent
import com.hypixel.hytale.server.core.command.system.CommandContext
import com.hypixel.hytale.server.core.universe.world.SoundUtil
import com.hypixel.hytale.server.core.universe.world.World
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore
import de.jerst.plugin.movingheads.model.AnimationNode
import de.jerst.plugin.movingheads.model.AnimationTrack
import de.jerst.plugin.movingheads.model.MovingHeadConfig
import kotlinx.coroutines.*
import java.util.*

object AnimationManager {
    private val playerJobs = mutableMapOf<UUID, Job>()

    fun startAnimation(
        config: MovingHeadConfig,
        commandContext: CommandContext,
        animation: AnimationTrack,
        world: World,
        player: UUID
    ): Boolean {
        if (playerJobs.contains(player)) {
            commandContext.sendMessage(Message.translation("server.movingheads.animation.onlyone").withErrorPrefix())
            return false
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
            }

            playerJobs.remove(player)
        }
        playerJobs[player]?.cancel()
        playerJobs[player] = job
        return true
    }

    suspend fun playAnimationNode(animationNode: AnimationNode, config: MovingHeadConfig, player: UUID, world: World) {
        coroutineScope {
            for (stateFrameName in animationNode.stateFrames) {
                val stateFrame = config.getStateFrame(player, stateFrameName) ?: continue
                val sceneGroup = config.getOrCreateSceneGroup(player, stateFrame.sceneGroupName) ?: continue

                if (animationNode.soundEvent != null) {
                    // Play sound if soundevent is set
                    val soundEvent = SoundEvent.getAssetMap().getIndex(animationNode.soundEvent!!.soundName)
                    val pos = animationNode.soundEvent!!.blocks

                    world.execute {
                        SoundUtil.playSoundEvent3d(
                            soundEvent,
                            SoundCategory.Music,
                            pos.x.toDouble(),
                            pos.y.toDouble(),
                            pos.z.toDouble(),
                            world.entityStore.store as ComponentAccessor<EntityStore>
                        )
                    }
                }

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

    /**
     * Stateduration in ms
     */
    fun getStateDuration(stage: String): Long {
        when (stage) {
            "Off" -> return 0L
            "On" -> return 0L
            "IntroSide" -> return 20000L
            "IntroStage" -> return 4000L
            "OutroStage" -> return 4000L
            "PartyOne" -> return 35000L
            "PartyTwo" -> return 33000L
            "Down" -> return 3500L
            "Up" -> return 3000L
            "UpDownOne" -> return 12000L
            else -> {}
        }
        return 0
    }
}
