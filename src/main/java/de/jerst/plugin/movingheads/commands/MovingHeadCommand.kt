package de.jerst.plugin.movingheads.commands

import com.hypixel.hytale.server.core.command.system.AbstractCommand
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractCommandCollection
import de.jerst.plugin.movingheads.commands.scenegroup.MovingHeadSceneGroupCommand


class MovingHeadCommand : AbstractCommandCollection("movinghead", "mh.commands") {
    init {
        setPermissionGroups("mh.command.scenery")
        addAliases("mh")

        addSubCommand(MovingHeadSceneGroupCommand() as AbstractCommand)
        addSubCommand(MovingHeadPlayCommand() as AbstractCommand)
    }
}