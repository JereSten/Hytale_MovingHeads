package de.jerst.plugin.movingheads.commands

import com.hypixel.hytale.server.core.command.system.basecommands.AbstractCommandCollection
import de.jerst.plugin.movingheads.commands.animation.MovingHeadAnimationCommand
import de.jerst.plugin.movingheads.commands.animationnode.MovingHeadAnimationNodeCommand
import de.jerst.plugin.movingheads.commands.scenegroup.MovingHeadSceneGroupCommand
import de.jerst.plugin.movingheads.commands.stateframe.MovingHeadStateFrameCommand


class MovingHeadCommand : AbstractCommandCollection("movinghead", "server.mh.commands") {
    init {
        setPermissionGroups("mh.command.scenery")
        addAliases("mh")

        addSubCommand(MovingHeadSceneGroupCommand())
        addSubCommand(MovingHeadAnimationCommand())
        addSubCommand(MovingHeadAnimationNodeCommand())
        addSubCommand(MovingHeadStateFrameCommand())
        addSubCommand(MovingHeadHelpCommand())
        addSubCommand(MovingHeadGuiCommand())
    }
}