package de.jerst.plugin.movingheads.commands.animationnode

import com.hypixel.hytale.server.core.command.system.basecommands.AbstractCommandCollection

class MovingHeadAnimationNodeCommand: AbstractCommandCollection("animationnode", "server.movingheads.animationnode.manage") {
    init {
        addAliases("annode")

        addSubCommand(MovingHeadAnimationNodeCreateCommand())
        addSubCommand(MovingHeadAnimationNodeDeleteCommand())
        addSubCommand(MovingHeadAnimationNodeAddCommand())
        addSubCommand(MovingHeadAnimationNodeRemoveCommand())
        addSubCommand(MovingHeadAnimationNodePlayCommand())
        addSubCommand(MovingHeadAnimationNodeListCommand())
    }
}