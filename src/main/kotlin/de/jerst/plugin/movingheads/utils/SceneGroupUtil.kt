package de.jerst.plugin.movingheads.utils

import com.hypixel.hytale.math.util.ChunkUtil
import com.hypixel.hytale.math.vector.Vector3i
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType
import com.hypixel.hytale.server.core.universe.world.World
import de.jerst.plugin.movingheads.model.SceneGroup

class SceneGroupUtil {
    companion object {
        fun play(sceneGroup: SceneGroup, state: String, world: World) {
            for (block in sceneGroup.blocks!!) {
                setState(block, world, state)
            }
        }

        /**
         * Set state of block at position
         * @param position Block position
         */
        private fun setState(position: Vector3i, world: World, stateName: String) {
            val chunckIndex = ChunkUtil.indexChunkFromBlock(position.x, position.z)

            world.getChunkAsync(chunckIndex).thenAccept { chunk ->
                val blockId: Int = chunk.getBlock(position)

                val blockType = BlockType.getAssetMap().getAsset(blockId)
                if (blockType != null) {
                    chunk.setBlockInteractionState(position.x, position.y, position.z, blockType, stateName, true)
                }
            }
        }
    }
}