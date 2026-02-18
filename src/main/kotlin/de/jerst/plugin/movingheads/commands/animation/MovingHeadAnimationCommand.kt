package de.jerst.plugin.movingheads.commands.animation

import com.hypixel.hytale.server.core.command.system.basecommands.AbstractCommandCollection

class MovingHeadAnimationCommand: AbstractCommandCollection("animation", "server.movingheads.animation.name") {
    init {
        addAliases("an")

        addSubCommand(MovingHeadAnimationCreateCommand())
        addSubCommand(MovingHeadAnimationDeleteCommand())
        addSubCommand(MovingHeadAnimationPlayCommand())
        addSubCommand(MovingHeadAnimationStopCommand())
        addSubCommand(MovingHeadAnimationAddCommand())
        addSubCommand(MovingHeadAnimationRemoveCommand())
        addSubCommand(MovingHeadAnimationListCommand())
    }
}