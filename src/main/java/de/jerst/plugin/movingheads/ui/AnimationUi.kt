package de.jerst.plugin.movingheads.ui

import au.ellie.hyui.builders.DropdownBoxBuilder
import au.ellie.hyui.builders.GroupBuilder
import au.ellie.hyui.builders.PageBuilder
import au.ellie.hyui.events.UIContext
import au.ellie.hyui.html.TemplateProcessor
import com.hypixel.hytale.component.Store
import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType
import com.hypixel.hytale.server.core.Message
import com.hypixel.hytale.server.core.universe.PlayerRef
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore
import de.jerst.plugin.movingheads.MovingHeadsPlugin
import de.jerst.plugin.movingheads.model.*
import de.jerst.plugin.movingheads.utils.AnimationManager
import de.jerst.plugin.movingheads.utils.CollectionHelpers.moveDownCircular
import de.jerst.plugin.movingheads.utils.CollectionHelpers.moveUpCircular
import de.jerst.plugin.movingheads.utils.ConfigurationUtil
import de.jerst.plugin.movingheads.utils.withErrorPrefix
import de.jerst.plugin.movingheads.utils.withPrefix


class AnimationUi(val store: Store<EntityStore?>, val playerRef: PlayerRef) {

    var configManager: ConfigurationUtil = MovingHeadsPlugin.INSTANCE.config

    fun addEditAnimation(animation: AnimationTrack?, onSave: ((UIContext) -> Unit)? = null) {
        var animationEdit = animation;

        if (animationEdit == null) {
            animationEdit = AnimationTrack(playerRef.uuid, "", mutableListOf())
        }

        val config = configManager.load<MovingHeadConfig>()
        val allAnimationNodes = config.getAnimationNode(playerRef.uuid)
        val usedAnimationNodes = animationEdit.animationNodes.mapIndexed { index, name ->
            IndexedAnimationNode(index, name)
        }

        val templateProcessor = TemplateProcessor()
            .setVariable("animationNodes", allAnimationNodes)
            .setVariable("animation", animationEdit)
            .setVariable("usedAnimationNodes", usedAnimationNodes)

        val page = PageBuilder.pageForPlayer(playerRef)
            .loadHtml("Pages/AddEditAnimation.html", templateProcessor)

        page.getById("addAnimationNode", DropdownBoxBuilder::class.java).ifPresent { dropdown ->
            for (animationNode in allAnimationNodes) {
                dropdown.addEntry(animationNode.name, animationNode.name)
            }

            dropdown.addEventListener(CustomUIEventBindingType.ValueChanged) { selectedValue ->
                animationEdit.animationNodes.add(selectedValue)

                addEditAnimation(animationEdit)
            }
        }

        page.addEventListener("name", CustomUIEventBindingType.FocusLost) { _, ctx ->
            val name = ctx.getValue("name", String::class.java).get()
            animationEdit.name = name
        }

        for ((index, name) in animationEdit!!.animationNodes.withIndex()) {
            page.addEventListener("up-animation-node-${index}", CustomUIEventBindingType.Activating) { _, ctx ->
                animationEdit.animationNodes.moveUpCircular(index)
                addEditAnimation(animationEdit)
            }

            page.addEventListener("down-animation-node-${index}", CustomUIEventBindingType.Activating) {
                animationEdit.animationNodes.moveDownCircular(index)
                addEditAnimation(animationEdit)
            }

            page.addEventListener("delete-animation-node-${index}", CustomUIEventBindingType.Activating) {
                animationEdit.animationNodes.removeAt(index)
                addEditAnimation(animationEdit)
            }
        }

        page.addEventListener("submit", CustomUIEventBindingType.Activating) { _, ctx ->
            config.setAnimation(animationEdit)
            configManager.save(config)

            playerRef.sendMessage(
                Message.translation("server.movingheads.animation.saved")
                    .param("animationName", animationEdit.name).withPrefix()
            )

            onSave?.invoke(ctx)
        }

        page.open(store)
    }

    fun addEditAnimationNode(animationNode: AnimationNode?, onSave: ((UIContext) -> Unit)? = null) {
        var animationNodeEdit = animationNode;

        if (animationNodeEdit == null) {
            animationNodeEdit = AnimationNode(playerRef.uuid, "", null, mutableListOf(), null)
        }

        val config = configManager.load<MovingHeadConfig>()
        val stateFrames = config.getStateFrames(playerRef.uuid)

        val templateProcessor = TemplateProcessor()
            .setVariable("stateFrames", stateFrames)
            .setVariable("animationNode", animationNode)

        val page = PageBuilder.pageForPlayer(playerRef)
            .loadHtml("Pages/AddEditAnimationNode.html", templateProcessor)

        page.getById("addStateFrame", DropdownBoxBuilder::class.java).ifPresent { dropdown ->
            for (stateFrame in stateFrames) {
                if (animationNodeEdit.stateFrames.contains(stateFrame.name)) {
                    continue
                }

                dropdown.addEntry(stateFrame.name, stateFrame.name)
            }

            dropdown.addEventListener(CustomUIEventBindingType.ValueChanged) { selectedValue ->
                animationNodeEdit.stateFrames.add(selectedValue)

                addEditAnimationNode(animationNodeEdit)
            }
        }

        page.addEventListener("wait", CustomUIEventBindingType.FocusLost) { _, ctx ->
            val wait = ctx.getValue("wait", String::class.java).get().toLongOrNull()
            animationNodeEdit.wait = wait
        }

        page.addEventListener("name", CustomUIEventBindingType.FocusLost) { _, ctx ->
            val name = ctx.getValue("name", String::class.java).get()
            animationNodeEdit.name = name
        }

        for (stateFrame in animationNodeEdit.stateFrames) {
            page.addEventListener("delete-state-frame-${stateFrame}", CustomUIEventBindingType.Activating) {
                animationNodeEdit.stateFrames.remove(stateFrame)

                addEditAnimationNode(animationNodeEdit)
            }
        }

        page.addEventListener("submit", CustomUIEventBindingType.Activating) { _, ctx ->
            config.setAnimationNode(animationNodeEdit)
            configManager.save(config)

            playerRef.sendMessage(
                Message.translation("server.movingheads.animationnode.saved")
                    .param("animationNode", animationNodeEdit.name).withPrefix()
            )

            onSave?.invoke(ctx)
        }

        page.open(store)
    }

    fun buildStateFramePage(onSave: ((UIContext) -> Unit)? = null) {
        val config = configManager.load<MovingHeadConfig>()
        val sceneGroups = config.getSceneGroups(playerRef.uuid)
        val states = AnimationManager.getStates()

        val page = PageBuilder.pageForPlayer(playerRef)
            .loadHtml("Pages/CreateStateFrame.html")

        page.getById("sceneGroup", DropdownBoxBuilder::class.java).ifPresent { dropdown ->
            for (sceneGroup in sceneGroups) {
                dropdown.addEntry(sceneGroup.name, sceneGroup.name)
            }
        }

        page.getById("state", DropdownBoxBuilder::class.java).ifPresent { dropdown ->
            for (state in states) {
                dropdown.addEntry(state, state)
            }
        }

        page.addEventListener("submit", CustomUIEventBindingType.Activating) { data, ctx ->
            val name = ctx.getValue("name", String::class.java)
            val sceneGroupName = ctx.getValue("sceneGroup", String::class.java)
            val state = ctx.getValue("state", String::class.java)
            val iterations = ctx.getValue("iterations", String::class.java)

            if (name.isEmpty || sceneGroupName.isEmpty || state.isEmpty || iterations.isEmpty || iterations.get()
                    .toIntOrNull() == null
            ) {
                playerRef.sendMessage(Message.translation("server.movingheads.stateframe.incomplete").withErrorPrefix())
            }

            val stateFrame =
                StateFrame(playerRef.uuid, name.get(), sceneGroupName.get(), state.get(), iterations.get().toInt())
            config.stateFrames.add(stateFrame)
            configManager.save(config)

            playerRef.sendMessage(
                Message.translation("server.movingheads.stateframe.created")
                    .param("name", name.get()).withPrefix()
            )

            onSave?.invoke(ctx)
        }

        page.open(store)
    }

    fun buildMainPage(activeTab: String? = "tab1") {
        val config = configManager.load<MovingHeadConfig>()
        val sceneGroups = config.getSceneGroups(playerRef.uuid)
        val stateFrames = config.getStateFrames(playerRef.uuid)
        val animationNodes = config.getAnimationNode(playerRef.uuid)
        val animations = config.getAnimations(playerRef.uuid)

        val templateProcessor = TemplateProcessor()
            .setVariable("username", playerRef.username)
            .setVariable("sceneGroups", sceneGroups)
            .setVariable("stateFrames", stateFrames)
            .setVariable("animationNodes", animationNodes)
            .setVariable("animations", animations)
            .setVariable("activeTab", activeTab)

        // -- BUILD Page

        val page = PageBuilder.pageForPlayer(playerRef)
            .loadHtml("Pages/Main.html", templateProcessor)

        // --- 1. SCENEGROUPS --
        for (sceneGroup in sceneGroups) {
            page.addEventListener(
                "delete-scene-group-${sceneGroup.name}",
                CustomUIEventBindingType.Activating
            ) { _, ctx ->
                val sceneGroupToDelete = config.getOrCreateSceneGroup(playerRef.uuid, sceneGroup.name)
                config.sceneGroups.remove(sceneGroupToDelete)
                configManager.save(config)

                playerRef.sendMessage(
                    Message.translation("server.movingheads.scenegroup.deleted")
                        .param("name", sceneGroup.name)
                        .withPrefix()
                )

                ctx.getById("scenegroup-card-${sceneGroup.name}", GroupBuilder::class.java).ifPresent { group ->
                    group.withVisible(false)
                    ctx.updatePage(true)
                }
            }
        }

        // -- 2. StateFrames --
        for (stateFrame in stateFrames) {
            page.addEventListener("btn-add-stateframe", CustomUIEventBindingType.Activating) {
                buildStateFramePage {
                    buildMainPage("tab2")
                }
            }

            page.addEventListener(
                "delete-state-frame-${stateFrame.name}",
                CustomUIEventBindingType.Activating
            ) { _, ctx ->
                val stateFrameToDelete = config.getStateFrame(playerRef.uuid, stateFrame.name)
                config.stateFrames.remove(stateFrameToDelete)
                configManager.save(config)

                playerRef.sendMessage(
                    Message.translation("server.movingheads.stateframe.deleted")
                        .param("name", stateFrame.name)
                        .withPrefix()
                )

                ctx.getById("stateframe-card-${stateFrame.name}", GroupBuilder::class.java).ifPresent { group ->
                    group.withVisible(false)
                    ctx.updatePage(true)
                }
            }
        }

        // -- 3. AnimationNodes --
        for (animationNode in animationNodes) {
            page.addEventListener("btn-add-animationnode", CustomUIEventBindingType.Activating) { data, ctx ->
                addEditAnimationNode(null) {
                    buildMainPage("tab3")
                }
            }

            // delete
            page.addEventListener(
                "animation-node-delete-${animationNode.name}",
                CustomUIEventBindingType.Activating
            ) { _, ctx ->
                val animationNodeToDelete = config.getAnimationNode(playerRef.uuid, animationNode.name)
                config.animationNodes.remove(animationNodeToDelete)
                configManager.save(config)

                playerRef.sendMessage(
                    Message.translation("server.movingheads.animationnode.deleted")
                        .param("animationNodeName", animationNode.name)
                        .withPrefix()
                )

                ctx.getById("animation-node-card-${animationNode.name}", GroupBuilder::class.java).ifPresent { group ->
                    group.withVisible(false)
                    ctx.updatePage(true)
                }
            }

            // edit
            page.addEventListener(
                "animation-node-edit-${animationNode.name}",
                CustomUIEventBindingType.Activating
            ) { _, ctx ->
                addEditAnimationNode(animationNode)
            }
        }

        // -- 4. Animation --
        for (animation in animations) {
            page.addEventListener("btn-add-animation", CustomUIEventBindingType.Activating) { data, ctx ->
                addEditAnimation(null) {
                    buildMainPage("tab4")
                }
            }

            // delete
            page.addEventListener("animation-delete-${animation.name}", CustomUIEventBindingType.Activating) { _, ctx ->
                val animationNodeToDelete = config.getAnimation(playerRef.uuid, animation.name)
                config.animationTracks.remove(animationNodeToDelete)
                configManager.save(config)

                playerRef.sendMessage(
                    Message.translation("server.movingheads.animation.deleted")
                        .param("name", animation.name)
                        .withPrefix()
                )

                ctx.getById("animation-card-${animation.name}", GroupBuilder::class.java).ifPresent { group ->
                    group.withVisible(false)
                    ctx.updatePage(true)
                }
            }

            // edit
            page.addEventListener("animation-edit-${animation.name}", CustomUIEventBindingType.Activating) { _, ctx ->
                addEditAnimation(animation)
            }
        }

        page.open(store)
    }

}