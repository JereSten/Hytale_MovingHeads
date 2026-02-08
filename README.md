<!-- PROJECT LOGO -->
<br />
<div align="center">
  <a href="https://github.com/JereSten/Hytale_MovingHeads">
    <img src="reporesources/images/Moving%20Heads%20Icon.png" alt="Logo" width="80" height="80">
  </a>

<h3 align="center">Moving Heads</h3>
  <p align="center">
    Working moving heads with configurable, customisable movement sequences
</div>

## Description

This mod adds real, working moving heads to the game. You can use commands to build your own custom moving sequences
based on eight preset animations (more animations are expected to be released soon)! You can also add your own 
custom music to the animation! 

> [!NOTE]  
> Currently, creating custom moving sequences is quite difficult. The following documentation and the help pages
> for the commands may help you. A quick how to can be accessed by typing `/mh help`. A custom GUI is planned for the
> future to improve usability and make animation crafting easier.

Demo-Video:

[![Demo Video](https://img.shields.io/badge/YouTube-%23FF0000.svg?style=for-the-badge&logo=YouTube&logoColor=white)](https://www.youtube.com/watch?v=oHK8FP-96g4)
## Provided Blocks

### Moving Heads

This mod provides 8 types of moving heads in two different variations:
* Different beam ranges: 2, 4, 6 or 12 blocks
* Left or right spot. The animations for the left and right spots are mirrored. Place them accordingly on the left 
  and right sides of your stage to create a mirrored scene like shown in the video above

### Misc

Non-functionable stage speaker.

## Usage

### Create new Animation

This hierarchy is used to create custom animations of movement scenes. Each component is stored separately, 
allowing you to reuse them in multiple animations.

```
AnimationTrack
│ 
├── name (String)
└── animationNodes
    │   
    └── AnimationNode
        ├── name (String)
        ├── wait (Long?)
        ├── soundEvent (SoundEvent?)
        │   └── SoundEvent -> play your Custom Sounds!
        │       ├── soundName (String)
        │       └── blocks (Vector3i)
        │
        └── stateFrames
            │
            └── StateFrame
                ├── name (String)
                ├── state (String)
                ├── iterations (Int)
                └── sceneGroupName
                    │
                    └── SceneGroup
                        ├── name (String)
                        └── blocks (MutableList<Vector3i>?)
```

To create a new animation let's start from the uʍop ǝpᴉsdn 🔦🚲:
Following guide shows only a small set of available commands for each step. Refer to the `/help` command to 
familiarise yourself with all the available commands.

#### 1. Create new SceneGroup

A SceneGroup is a 'container' that can hold multiple blocks. For example, you could put all the spots on the stage 
in one group and put the rest in another 'side' group. 

So let's create a new group with `/mh sc create stage`.

Next, we focus on each spot on the stage in turn and add them with the command `/mh sc add stage`. This command adds 
the block you are looking and to the group.

To view all added block type `/mh sc show stage`. All block will be shown up using blue particles.

Let's test! `/mh sc play stage Up`

#### 2. Create a new StateFrame

A Stateframe combines states and SceneGroups. It determines which animation is played for each Scenegroup. You can 
create multiple reusable StateFrames, such as 'stageUp', 'stageDown' and 'stageParty1':
`/mh sf create stageUp stage Up`

If you want the animation repeat for X times append X at the end of the command to define the amount of iterations 
the animation should play.

#### 3. Create AnimationNode

An AnimationNode plays all the added StateFrames simultaneously.
So let's create one and add the StateFrames that we have made.

1. `/mh annode create part1`
2. `/mh annode add stageUp`
3. `/mh annode add sideIntro`

The states `stageUp` and `sideIntro` will be set at the same time to their predefined SceneGroups.

Let's test! `/mh annode play part1`

#### 4. Create Animation

Almost done! Finally we have to define a new Animation. 
`/mh an create demo`

Now add all the AnimationNodes you want. Animations play each node sequentially, while state frames within an 
AnimationNode are played in parallel. This allows for highly flexible animation combinations.

1. `/mh an add part1`
2. `/mh an add part2`

Let the party begin! `/mh an play demo`

#### Additional

Let's add some nice music to the animation: `/mh annode addsound Custom_Music 10 10 10`

### Animations

The animations are initiated by block state changes. Currently we have 8 animations and 2 additional states without 
an animation.

#### 🌑 Static States

##### 🔘 Off
* **Description:** The silent state. No light, no movement.

##### 💡 On
* **Description:** Static light with beam visible.

#### Animations

##### IntroSide
* **Description:** Introductory movement sweeping into the field of view from the left side.

##### IntroStage
* **Description:** Standard entrance animation for stage-center alignment.

##### OutroStage
* **Description:** Conclusion sequence based on the IntroStage configuration.

##### PartyOne
* **Description:** Movement sequence designed for active show phases.

##### PartyTwo
* **Description:** Movement sequence designed for active show phases.

##### Down
* **Description:** Linear downward movement of the head.

##### Up
* **Description:** Linear upward movement of the head.

##### UpDownOne
* **Description:** Smooth Up and Down movement.

## Installation

Place the `release` jar file into the `mods` folder of your server.

## Contributing

Contributions are what make the open source community such an amazing place to learn, inspire, and create. Any
contributions you make are **greatly appreciated**.

If you have a suggestion that would make this better, please fork the repo and create a pull request. You can also
simply open an issue with the tag "enhancement".
Don't forget to give the project a star! Thanks again!

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## Build

1. Create `libs`-Folder
2. Add `HytaleServer.jar` to this directory
3. Build with `gradle build`

## License

Distributed under the MIT License. See `LICENSE` for more information.

## References

* HyUi: https://www.curseforge.com/hytale/mods/hyui
