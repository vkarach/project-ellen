# Project Ellen

An Alien-themed top-down 2D game, built on `sk.tuke.kpi.gamelib`, the teaching
game engine of the Technical University of Kosice's object-oriented
programming course. It is a course project, written between September and
December 2025 as the semester assignment - the gamelib package, the scene
graph, the actor/animation system, input handling and the Tiled map loader are
all supplied by the university; everything under `sk.tuke.kpi.oop.game` is
mine: the object model, the five scenarios, the characters, items, weapons and
the story that ties them together.

## What gamelib provides, what this project adds

`gamelib` gives you `Actor`, `Scene`, `Game`, an action scheduler
(`Action`, `ActionSequence`, `Loop`, `When`, `Wait`, `Invoke`, ...), animation
playback, keyboard input, a message bus (`Topic`/`MessageBus`) and a loader
that turns a Tiled `.tmx` map plus an `ActorFactory` into a populated scene.
None of that is mine.

What's mine sits in `src/main/java/sk/tuke/kpi/oop/game`: the characters,
items, weapons, openables and level logic, and the design decisions about how
they're wired together, described below.

## Object model

The framework hands you one base type, `Actor`. Everything an actor can *do*
beyond existing and rendering is expressed as a small interface it opts into,
instead of a growing inheritance tree:

- `Movable` - has a speed, reacts to `startedMoving`/`stoppedMoving`/
  `collidedWithWall`.
- `Alive` (`characters/Alive.java`) - exposes a `Health`.
- `Armed` - carries a `Firearm` and can fire it.
- `Keeper` - has a `Backpack` and can hold `Collectible` items.
- `Usable<A>` (`items/Usable.java`) - can be used by an actor of type `A`;
  the type parameter is what lets a `Door` be `Usable<Actor>` while a
  `PowerSwitch` is `Usable<Ripley>` only, checked by the compiler rather than
  by an `instanceof` at the call site.
- `Collectible` - can be picked up and dropped, with optional hooks.
- `Openable` - `open()`/`close()`/`isOpen()`, shared by every door variant.
- `Fireable` (`weapons/Fireable.java`) - a `Movable` that a firearm can
  launch, i.e. a bullet.

`Ripley` (the player character, `characters/Ripley.java`) implements
`Movable`, `Alive`, `Armed` and `Keeper` at once. An `Alien` implements
`Movable`, `Alive` and `Enemy`. A `Door` implements `Openable` and
`Usable<Actor>`. None of them share a common concrete superclass beyond
`AbstractActor`; what they have in common is which interfaces they
implement, and every piece of game logic (a controller, an action, a check
like `intersectsWithWall`) is written against the interface it actually
needs, not against a specific class. That's the point: a `Cooler` and a
`Ripley` have nothing to do with each other, but both can be `Usable`, and
code that reacts to "being used" doesn't need to know which.

### Behaviours and actions are objects, not methods

Two more small interfaces carry the same idea further:

- `Action<A>` (from gamelib) and this project's own actions in `actions/`
  (`Move`, `Fire`, `Use`, `Take`, `Drop`, `Shift`, `Speak`, `MoveToPlace`,
  `PerpetualReactorHeating`) are scheduled onto an actor and run over time -
  `execute(deltaTime)` each frame until `isDone()`. A `Move` doesn't know who
  is moving beyond the `Movable` interface, so the same action drives Ripley
  under keyboard control and an alien under AI control.
- `Behaviour<A>` (`behaviours/Behaviour.java`) is a strategy an actor is
  configured with at creation time rather than a subclass it belongs to.
  `RandomlyMoving` makes any `Movable` wander and avoid walls. `Observing`
  wraps another behaviour so it only starts once a chosen event fires on the
  message bus - e.g. an alien that stays put until a specific door opens,
  built as `new Observing<>(Door.DOOR_OPENED, doorPredicate, new
  RandomlyMoving())` rather than as its own class. New alien AI is composed
  from existing pieces instead of written as a new subclass every time.

### The message bus decouples level logic from actors

An actor doesn't call back into scenario code directly, and a scenario
doesn't poll actors every frame to see what happened. Instead an actor
publishes a `Topic` when something happens - `Ripley.RIPLEY_DIED`,
`Door.DOOR_OPENED`, `Ventilator.VENTILATOR_REPAIRED`, `Computer.KILL137` - and
whoever cares subscribes. `MissionImpossible`, for example, starts draining
Ripley's health on `Door.DOOR_OPENED` and stops on
`Ventilator.VENTILATOR_REPAIRED`, without the door or the ventilator knowing
that a leak exists. This is what makes the scenario classes (below) readable
as a sequence of "when X happens, do Y" rules instead of a tangle of direct
references between every actor on the map.

### Controllers translate input into actions

`controllers/` (`MovableController`, `KeeperController`, `ShooterController`,
`PauseController`) implement gamelib's `KeyboardListener` and turn key events
into the same `Action` objects described above - a controller doesn't move an
actor itself, it schedules a `Move`. Swapping controls, or driving the same
actor from AI instead of the keyboard, doesn't require touching `Ripley` or
the actions at all.

## Scenarios

`Main.java` currently launches `FinalMission`; the other four are earlier or
smaller scenarios kept in the project, each an independent `SceneListener` (or
`Scenario`) with its own Tiled map and its own `ActorFactory` that turns map
object names/types into actor instances:

- **FirstSteps** - the smallest scene: Ripley plus a scatter of pickups
  (energy, ammo, hammer, wrench, fire extinguisher), used to check that
  movement, the backpack and item pickup work before anything else was built.
- **TrainingGameplay** - a sandbox for the powered-device system: a reactor,
  coolers, lights, switches, teleports, bombs and a helicopter, wired up by
  hand to exercise `Switchable`/`PowerSwitch`/`Reactor` without any win
  condition.
- **EscapeRoom** - aliens with `Observing`-gated `RandomlyMoving` behaviour
  guard two doors; getting Ripley to the exit door despawns them and ends the
  scene. The first scenario with real AI and a win state.
- **MissionImpossible** - a stealth/timer level: opening the door starts a
  ventilator leak that drains Ripley's health until she repairs it, using a
  locked door and an access card as the gate.
- **FinalMission** - the full campaign: dialogue-driven cutscenes
  (`story/DialogueLoader` reads `dialogues/dialogues.json`), a reactor that
  can overheat and force an evacuation, a countdown after a computer is
  sabotaged, a rocket launch sequence, camera zoom for cutscenes, and pause
  handling. This is what `./gradlew run` starts.

## Building and running

```
./gradlew run
```

`sk.tuke.kpi.gamelib` is not on Maven Central; `build.gradle.kts` adds
`https://repo.kpi.fei.tuke.sk/repository/maven-public` as a repository, which
is publicly reachable, so the project builds without any TUKE-specific access
or credentials.

The windowing backend is chosen at build time: `lwjgl` everywhere, `lwjgl2` on
macOS, because the LWJGL 3 backend gamelib normally uses doesn't run on macOS
without extra JVM flags gradle's `run` task doesn't set. This switch is
already handled in `build.gradle.kts` and needs no manual step.

Target is Java 11, compiled with `-Xlint:all -Werror`.

## Tests

There are none. The project has no automated test suite; the framework
(`gamelib`) isn't designed around unit-testable game logic, and everything
here was verified by playing the scenarios.
