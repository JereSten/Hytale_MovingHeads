package de.jerst.plugin.movingheads.utils

import com.hypixel.hytale.server.core.universe.Universe

object ServerUtil {
    /*
     * Executes a task on the default world.
     */
    @JvmStatic
    fun executeWorld(task: Runnable) {
        Universe.get().defaultWorld!!.execute(task)
    }
}
