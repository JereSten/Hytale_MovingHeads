package de.jerst.plugin.movingheads.commands.stateframe

import com.hypixel.hytale.server.core.command.system.basecommands.AbstractCommandCollection

class MovingHeadStateFrameCommand: AbstractCommandCollection("stateframe", "server.movingheads.scenegroup.manage") {
    init {
        addAliases("sf")

        addSubCommand(MovingHeadStateFrameCreateCommand())
        addSubCommand(MovingHeadStateFrameDeleteCommand())
    }
}