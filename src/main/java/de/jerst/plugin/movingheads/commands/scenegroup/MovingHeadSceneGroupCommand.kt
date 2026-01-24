package de.jerst.plugin.movingheads.commands.scenegroup

import com.hypixel.hytale.server.core.command.system.basecommands.AbstractCommandCollection

class MovingHeadSceneGroupCommand :
    AbstractCommandCollection("scenegroup", "server.movingheads.scenegroup.manage") {

    init {
        addAliases("sc")

        addSubCommand(MovingHeadSceneGroupCreateCommand())
        addSubCommand(MovingHeadSceneGroupAddCommand())
        addSubCommand(MovingHeadSceneGroupShowCommand())
        addSubCommand(MovingHeadSceneGroupRemoveCommand())
        addSubCommand(MovingHeadSceneGroupListCommand())
        addSubCommand(MovingHeadSceneGroupDeleteCommand())
        addSubCommand(MovingHeadSceneGroupPlayCommand())
    }
}